package com.searchgame.utilities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FileParsingUtility {

	public static Map<String, String> getSearchCategorys(final InputStream input)
	{
		final Map<String, String> results = new HashMap<String, String>();
		
		final JSONParser parser = new JSONParser();
		
		try 
		{	
	        final BufferedReader reader = new BufferedReader(new InputStreamReader(input));
	        final StringBuilder inputString = new StringBuilder();
	        String line;
	        while ((line = reader.readLine()) != null) 
	        {
	        	inputString.append(line);
	        }
	        reader.close();
	        
			final Object obj = parser.parse(inputString.toString());
	 
			final JSONArray jsonArray = (JSONArray) obj;
			
			@SuppressWarnings("unchecked")
			final Iterator<Object> iterator = jsonArray.iterator();
			while (iterator.hasNext()) 
			{
				@SuppressWarnings("unchecked")
				final Map<String, String> entryMap = (Map<String, String>)iterator.next();
				results.put(entryMap.get("Category"), entryMap.get("File"));
			}
	 
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		catch (ParseException e) 
		{
			e.printStackTrace();
		}
		
		return results;
	}
	
	public static List<String> getSearchTerms(final InputStream input)
	{
		final List<String> results = new ArrayList<String>();
		
		final JSONParser parser = new JSONParser();
		
		try 
		{	
	        final BufferedReader reader = new BufferedReader(new InputStreamReader(input));
	        final StringBuilder inputString = new StringBuilder();
	        String line;
	        while ((line = reader.readLine()) != null) 
	        {
	        	inputString.append(line);
	        }
	        reader.close();
	        
			final Object obj = parser.parse(inputString.toString());
	 
			final JSONArray jsonArray = (JSONArray) obj;

			@SuppressWarnings("unchecked")
			final Iterator<Object> iterator = jsonArray.iterator();
			while (iterator.hasNext()) 
			{
				results.add((String)iterator.next());
			}
	 
		}
		catch (FileNotFoundException e) 
		{
			System.out.println("FILENOTFOUND: " + e);
		} 
		catch (IOException e) 
		{
			System.out.println("IO: " + e);
		} 
		catch (ParseException e) 
		{
			System.out.println("PARSING: " + e);
		}
		
		return results;
	}
}
