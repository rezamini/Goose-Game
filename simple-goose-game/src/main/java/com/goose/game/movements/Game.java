package com.goose.game.movements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.goose.game.constants.Constants;
import com.goose.game.inputs.Inputs;
import com.goose.game.player.Player;

public class Game {
	public static List<Player> playerList = new ArrayList<Player>();
	public static Inputs inputs = new Inputs();
	
	public void performPlayerRegistration(String sentence){
		StringBuilder sb = new StringBuilder();
		
		try {
			if(!sentence.equals("") && sentence.toLowerCase().contains(Constants.ADD_PLAYER.toLowerCase())){
				String sentenceKeyword = sentence.substring(0, sentence.lastIndexOf(" "));
				String playerName = sentence.substring(sentence.lastIndexOf(" ") + 1);
				
				if(sentenceKeyword.equalsIgnoreCase(Constants.ADD_PLAYER) && !playerName.isEmpty()){
					Player player = new Player();
					player.setName(playerName);
					player.setCellNum(0);

					if(playerList.size() > 0){
						for(Player p: playerList){
							if(p.getName().equalsIgnoreCase(playerName)){
								sb.append(playerName).append(Constants.COLON_PUNC).append(Constants.PLAYER_EXISTS);
								printOutput(sb.toString());
								inputs.userInput(Constants.ADD_PARTICIPANTS);
								return;
							}
						}
					}
					
					playerList.add(player);
					
					sb.append(Constants.PLAYERS).append(Constants.COLON_PUNC).append(playerList.get(0).getName())
						.append(playerList.size() == 2 ? Constants.COMMA_PUNC.concat(playerList.get(1).getName()) : "");
					
					printOutput(sb.toString());
					
					if(playerList.size() < 2){
						inputs.userInput(Constants.ADD_PARTICIPANTS);
					}else{
						inputs.userInput(Constants.TYPE_COMMAND);
					}
				}else{
					printOutput(Constants.NO_KEYWORD);
					inputs.userInput(Constants.ADD_PARTICIPANTS);
				}
			}else{
				printOutput(Constants.NO_KEYWORD);
				inputs.userInput(Constants.ADD_PARTICIPANTS);
			}
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	
	public void performPlayerMovement(String sentence){
		boolean isfinish = false;
		int totalNewCells = 0;

 		try {
 			
 			String[] nameSplit = sentence.split(Constants.BLANK_SPACE_EXPR);
 			if(!sentence.equals("") && nameSplit.length > 1){
 	 	 		String playerName = nameSplit[1];
 	 	 		String numbers = null;
 	 	 		
 	 			if(sentence.contains(",")){
 	 	 			numbers = sentence.substring(sentence.replace(", ", ",").lastIndexOf(" ") + 1);
 	 	 			String[] numberSplit = numbers.split(",");
 	 	 			if(numberSplit.length == 2){
 	 	 				totalNewCells = Integer.valueOf(numberSplit[0].trim()) + Integer.valueOf(numberSplit[1].trim());
 	 	 			}else{
 	 	 				inputs.userInput(Constants.TYPE_COMMAND);
 	 	 			}
 	 	 		}else{
 	 	 			int firstDice = diceThrow();
 	 	 			int secondDice = diceThrow();
 	 	 			numbers = firstDice + ", " + secondDice;
 	 	 			totalNewCells = firstDice + secondDice;
 	 	 		}
 	 			
 	 			if(playerList != null && playerList.size() > 0){
 	 				for(Player p: playerList){
 	 					if(p.getName().equalsIgnoreCase(playerName)){
 	 						StringBuilder sb = new StringBuilder();
 	 						int playerNewCell = p.getCellNum() + totalNewCells;
 	 						
 	 						if(p.getCellNum() == 0){
 	 							boolean isPrank = isPrankCell(p, playerNewCell, numbers);
 	 							
 	 							if(!isPrank){
 	 								sb.append(p.getName()).append(Constants.PLAYER_DICE_ROLL).append(numbers).append(Constants.DOT_PUNC).append(p.getName()).append(Constants.PLAYER_START_MOVE)
 	 								.append(playerNewCell);
 	 								printOutput(sb.toString());
 	 							}
 	 						}else{
 	 							if(playerNewCell > Constants.NUMBER_OF_CELLS){
 	 								playerNewCell = Constants.NUMBER_OF_CELLS - (playerNewCell - Constants.NUMBER_OF_CELLS) ;
 	 								sb.append(p.getName()).append(Constants.PLAYER_DICE_ROLL).append(numbers).append(Constants.DOT_PUNC)
 	 								.append(p.getName()).append(Constants.PLAYER_MOVES_FROM).append(p.getCellNum()).append(Constants.PLAYER_TO)
 	 								.append(Constants.NUMBER_OF_CELLS).append(Constants.DOT_PUNC).append(p.getName()).append(Constants.PLAYER_BOUNCE)
 	 								.append(p.getName()).append(Constants.PLAYER_RETURNS_TO).append(playerNewCell);
 	 								
 	 								printOutput(sb.toString());
 	 								
 	 							
 	 							}else if(playerNewCell == Constants.NUMBER_OF_CELLS){
 	 								sb.append(p.getName()).append(Constants.PLAYER_DICE_ROLL).append(numbers).append(Constants.DOT_PUNC).append(p.getName())
 	 								.append(Constants.PLAYER_MOVES_FROM).append(p.getCellNum()).append(Constants.PLAYER_TO).append(playerNewCell)
 	 								.append(Constants.DOT_PUNC).append(p.getName()).append(Constants.PLAYER_WINS);
 	 								
 	 								printOutput(sb.toString());
 	 								isfinish = true;
 	 							
 	 							}else if(playerNewCell == Constants.BRIDGE_CELL){
 	 								playerNewCell = Constants.BRIDGE_CELL + Constants.BRIDGE_CELL;
 	 								sb.append(p.getName()).append(Constants.PLAYER_DICE_ROLL).append(numbers).append(Constants.DOT_PUNC).append(p.getName())
 	 								.append(Constants.PLAYER_MOVES_FROM).append(p.getCellNum()).append(Constants.PLAYER_TO).append(Constants.THE_BRIDGE)
 	 								.append(Constants.DOT_PUNC).append(p.getName()).append(Constants.PLAYER_JUMPS).append(playerNewCell);
 	 								
 	 								printOutput(sb.toString());
 	 							
 	 							}else if(isGooseCell(Constants.GOOSE_CELLS, playerNewCell)){
 	 								playerNewCell = playerNewCell + totalNewCells;
 	 								if(isGooseCell(Constants.GOOSE_CELLS, playerNewCell)){
 	 									playerNewCell += totalNewCells;
 	 									sb.append(p.getName()).append(Constants.PLAYER_DICE_ROLL).append(numbers).append(Constants.DOT_PUNC).append(p.getName())
 	 									.append(Constants.PLAYER_MOVES_FROM).append(p.getCellNum()).append(Constants.PLAYER_TO).append(p.getCellNum() + totalNewCells)
 	 									.append(Constants.COMMA_PUNC).append(Constants.PLAYER_GOOSE).append(Constants.DOT_PUNC).append(p.getName()).append(Constants.PLAYER_MOVES_AGAIN)
 	 									.append(p.getCellNum() + totalNewCells * 2).append(Constants.COMMA_PUNC).append(Constants.PLAYER_GOOSE).append(Constants.DOT_PUNC).append(p.getName())
 	 									.append(Constants.PLAYER_MOVES_AGAIN).append(playerNewCell);
 	 									
 	 									printOutput(sb.toString());
 	 								}else{
 	 									sb.append(p.getName()).append(Constants.PLAYER_DICE_ROLL).append(numbers).append(Constants.DOT_PUNC).append(p.getName())
 	 									.append(Constants.PLAYER_MOVES_FROM).append(p.getCellNum()).append(Constants.PLAYER_TO).append(p.getCellNum() + totalNewCells)
 	 									.append(Constants.COMMA_PUNC).append(Constants.PLAYER_GOOSE).append(Constants.DOT_PUNC).append(p.getName()).append(Constants.PLAYER_MOVES_AGAIN)
 	 									.append(playerNewCell);
 	 									
 	 									printOutput(sb.toString());
 	 								}
 	 								
 	 							}else{
 	 								boolean isPrank = isPrankCell(p, playerNewCell, numbers);

 	 								if(!isPrank){
 	 									sb.append(p.getName()).append(Constants.PLAYER_DICE_ROLL).append(numbers).append(Constants.DOT_PUNC).append(p.getName())
 	 									.append(Constants.PLAYER_MOVES_FROM).append(p.getCellNum()).append(Constants.PLAYER_TO).append(playerNewCell);
 	 									
 	 									printOutput(sb.toString());
 	 								}	
 	 							}
 	 						
 	 						}
 	 						
 	 						p.setCellNum(playerNewCell);
 	 						break;
 	 					}
 	 				}

 	 				if(!isfinish){
 	 					inputs.userInput(Constants.TYPE_COMMAND);
 	 				}else{
 	 					playerList.clear();
 	 					inputs.userInput(Constants.ADD_PARTICIPANTS);
 	 				}
 	 			}
 			}else{
				inputs.userInput(Constants.TYPE_COMMAND);
 			}
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	
	public static boolean isPrankCell(Player p, int playerCurrentCell, String numbers){
		
		StringBuilder sb = new StringBuilder();
		int currentPlayerIndex = playerList.indexOf(p);
		int otherPlayerIndex = 0;
		boolean isPrank = false;
		
		try {
			if(currentPlayerIndex > 0){
				otherPlayerIndex = playerList.indexOf(p) - 1;
				if(playerList.get(otherPlayerIndex).getCellNum() == playerCurrentCell){
					isPrank = true;
				}
			}else {
				otherPlayerIndex = playerList.indexOf(p) + 1;
				if(playerList.get(otherPlayerIndex).getCellNum() == playerCurrentCell){
					isPrank = true;
				}
			}
			
			if(isPrank){
				sb.append(p.getName()).append(Constants.PLAYER_DICE_ROLL).append(numbers).append(Constants.DOT_PUNC).append(p.getName()).append(Constants.PLAYER_MOVES_FROM)
				.append(p.getCellNum()).append(Constants.PLAYER_TO).append(playerCurrentCell).append(Constants.DOT_PUNC).append(Constants.PLAYER_ON).append(playerCurrentCell)
				.append(Constants.PLAYER_ON_CELL).append(playerList.get(otherPlayerIndex).getName()).append(Constants.PLAYER_PRANK_RETURN).append(p.getCellNum());
				
				printOutput(sb.toString());
				
				Player otherPlayer = playerList.get(otherPlayerIndex);
				otherPlayer.setCellNum(p.getCellNum());
				
				playerList.set(otherPlayerIndex, otherPlayer);
			}
		} catch (Exception e) {
			System.err.println(e);
		}

		return isPrank;
	}
	
	private static boolean isGooseCell(Integer[] arr, Integer item){
	    List<Integer> list = Arrays.asList(arr);
	    return list.contains(item);
    }
	
    private static int diceThrow() {	
        return ((int) (Math.random()*10000) % 6)+ 1; 
     }
    
    private static void printOutput(String output){
    	System.out.println(output);
    	System.out.println();
    }
}
