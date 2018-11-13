package com.goose.game.main;


import com.goose.game.constants.Constants;
import com.goose.game.inputs.Inputs;

public class Main {
	public static void main (String[] args){
		System.out.println(Constants.NO_PARTICIPANTS);
		Inputs inputs = new Inputs();
		inputs.userInput(Constants.ADD_PARTICIPANTS);
	}
}
