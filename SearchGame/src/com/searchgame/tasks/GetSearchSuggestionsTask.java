package com.searchgame.tasks;
import android.os.AsyncTask;

import java.io.IOException;

import org.json.simple.parser.ParseException;

import com.searchgame.activity.PlayGameActivity;
import com.searchgame.utilities.SearchSuggestionsUtility;

public class GetSearchSuggestionsTask extends AsyncTask<Void, Void, String>{
	
	private static final String REQUEST_FAILURE_MESSAGE = "Request for search term failed";
	private static final String PARSING_FAILURE_MESSAGE = "Failure parsing json array";
	
    private PlayGameActivity activity;
    private String searchTerm;
    
    public GetSearchSuggestionsTask (final PlayGameActivity playGameActivity, final String searchTerm) 
    {
        this.activity = playGameActivity;
        this.searchTerm = searchTerm;
    }
	
	@Override
	protected String doInBackground(Void... params) 
	{
		String response;
		try 
		{
			response = SearchSuggestionsUtility.getSearchSuggestions(searchTerm);
		} 
			catch (IOException e) 
		{
			response = REQUEST_FAILURE_MESSAGE;
		}
		
		return response;
	}
	
    protected void onPostExecute(final String response) 
    {  	
	    System.out.println("TASK ACTIVITY: " + activity);
    	try 
	    {
			final String[] responseStringArray = SearchSuggestionsUtility.parseJSON(response);
			activity.insertParsedResponse(searchTerm, responseStringArray);
		} 
	    catch (ParseException e) 
	    {
	    	activity.insertParsedResponse(searchTerm, new String[] {PARSING_FAILURE_MESSAGE});
		}
    }
}
