package com.searchgame.types;

public final class GameResult {

	private int numCorrect;
	private int numAnswered;
	
	public GameResult(final int numCorrect, final int numAnswered) 
	{
		this.numCorrect = numCorrect;
		this.numAnswered = numAnswered;
	}

	public int getNumAnswered() {
		return numAnswered;
	}

	public int getNumCorrect() {
		return numCorrect;
	}
	
	public double getScore()
	{
		return numCorrect / (1.0 * numAnswered);
	}
}
