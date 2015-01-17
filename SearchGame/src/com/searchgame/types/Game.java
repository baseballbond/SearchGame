package com.searchgame.types;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Game {
	
	private Iterator<Round> rounds;
	private Round currentRound;
	private int correctAnswers = 0;
	private int numRounds = 0;
	
	public Game (final Map<String, String[]> searchTermResults)
	{
		List<Round> rounds = new ArrayList<Round>();
		
		for (Entry<String, String[]> searchTerm : searchTermResults.entrySet())
		{
			if (rounds.size() >= 10)
			{
				break;
			}
			
			final Round round = new Round(searchTerm.getKey(), searchTerm.getValue());
			if (round.getSuggestions().size() == 6)
			{
				rounds.add(round);
			}
		}
		
		this.numRounds = rounds.size();
		this.rounds = rounds.iterator();
	}

	public Round getNextRound() 
	{
		this.currentRound = rounds.next();
		return currentRound;
	}
	
	public Round getCurrentRound()
	{
		return currentRound;
	}
	
	public boolean hasNextRound() 
	{
		return rounds.hasNext();
	}

	public void recordCorrectAnswer()
	{
		correctAnswers++;
	}
	
	public GameResult getResults() 
	{
		return new GameResult(correctAnswers, numRounds);
	}

}
