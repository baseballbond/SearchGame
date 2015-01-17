package com.searchgame.types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Round {
	
	private String searchTerm;
	private String winningSuggestion;
	private List<String> suggestions;
	
	public Round(final String searchTerm, final String[] searchSuggestions)
	{
		this.searchTerm = searchTerm;
		this.suggestions = new ArrayList<String>();
		
		int suggestionsCount = 0;
		for (int i = 0; i < searchSuggestions.length; i++)
		{
			if (!searchSuggestions[i].equalsIgnoreCase(searchTerm)
			    && searchSuggestions[i].length() <= (searchTerm.length() + 30)
			    && suggestionsCount < 6
			    && searchSuggestions[i].length() > searchTerm.length()
			    && searchSuggestions[i].charAt(searchTerm.length()) == ' ')
			{
				if (winningSuggestion == null)
				{
					winningSuggestion = searchSuggestions[i].substring(searchTerm.length());				
				}
				suggestions.add(searchSuggestions[i].substring(searchTerm.length()));
				suggestionsCount++;
			}
		}
		
		Collections.shuffle(suggestions);	
	}
	
	public String getSearchTerm() 
	{
		return searchTerm;
	}

	public String getWinningSuggestion() 
	{
		return winningSuggestion;
	}
	
	public List<String> getSuggestions() 
	{
		return suggestions;
	}
	

}
