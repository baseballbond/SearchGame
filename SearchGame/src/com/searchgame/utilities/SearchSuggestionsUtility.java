package com.searchgame.utilities;
import android.annotation.SuppressLint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SearchSuggestionsUtility {

	public static String getSearchSuggestions(final String searchTerm) throws IOException
	{
		final String url = generateURL(searchTerm);
		return sendRequest(url);
	}
	
	private static String generateURL(final String searchTerm)
	{
		final String searchTerm_withoutSpaces = searchTerm.replaceAll(" ", "%20");
		return "http://suggestqueries.google.com/complete/search?q=" + searchTerm_withoutSpaces + "&client=firefox&";
	}
	
	private static String sendRequest(final String urlString) throws IOException
	{
		final URL url = new URL(urlString);
		
		final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		
		final InputStream input = connection.getInputStream();
	    final BufferedReader inputStream = new BufferedReader(new InputStreamReader(input));
	
		String inputLine;
		final StringBuffer response = new StringBuffer();
 
		while ((inputLine = inputStream.readLine()) != null) {
			response.append(inputLine);
		}
		
		inputStream.close();
		
		return response.toString();
	}
	
	@SuppressLint("NewApi")
	@SuppressWarnings("unchecked")
	public static String[] parseJSON(final String json) throws ParseException
	{
		System.out.println("Input JSON: " + json);
		if (json.isEmpty())
		{
			return new String[0];
		}
		
		final JSONParser parser = new JSONParser();
		final JSONArray jsonArray = (JSONArray) parser.parse(json);
		System.out.println("JSON Array: " + jsonArray);
		
		// 0 index is search term, 1st index is array of results
		final List<String> responseArrayList = (ArrayList<String>) jsonArray.get(1);
		System.out.println("ArrayList: " + responseArrayList);
		
		System.out.println("Array response: " + responseArrayList.toArray(new String[responseArrayList.size()]));
		return responseArrayList.toArray(new String[responseArrayList.size()]);
	}
}

