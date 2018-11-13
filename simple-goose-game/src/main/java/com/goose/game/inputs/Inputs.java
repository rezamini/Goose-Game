package com.goose.game.inputs;

import java.util.Scanner;

import com.goose.game.constants.Constants;
import com.goose.game.movements.Game;

public class Inputs {
	
	
	public void userInput(String command){
		Game game = new Game();
		System.out.println(command.concat(Constants.COLON_PUNC));
		Scanner scanner = new Scanner(System.in);

		if(command.equals(Constants.ADD_PARTICIPANTS)){
			game.performPlayerRegistration(scanner.nextLine());
		}else{
			game.performPlayerMovement(scanner.nextLine());
		}	
		scanner.close();
	}
}
