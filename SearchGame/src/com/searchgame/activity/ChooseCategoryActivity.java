package com.searchgame.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.searchgame.R;
import com.searchgame.fragments.ChooseCategoryFragment;
import com.searchgame.utilities.CheckConnectivityUtilitiy;
import com.searchgame.utilities.FileParsingUtility;

import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ChooseCategoryActivity extends ActionBarActivity 
{

	private ChooseCategoryFragment categoryFragment;
	private Map<String, String> categories;
	
    @SuppressLint("NewApi") @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);
		
        try 
        {
			this.categories = FileParsingUtility.getSearchCategorys(getAssets().open("Categories.json"));
		} 
        catch (IOException e)
        {
			System.out.print("IO exception: " + e);
		}
                
        this.categoryFragment = new ChooseCategoryFragment(categories.keySet());
        
        if (savedInstanceState == null) 
        {
				getSupportFragmentManager().beginTransaction()
				                           .add(R.id.container, categoryFragment)
				                           .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.play_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.Shuffle) 
        {
        	categoryFragment.populateButtonsText();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @SuppressLint("NewApi") 
    public void pickCategoryOnClick(final View view) 
    {
    	final CheckConnectivityUtilitiy connectionUtility = new CheckConnectivityUtilitiy();
        if(!connectionUtility.checkNow(this.getApplicationContext()))
        {
        	displayConnectivityMessage();
        	return;
        }
    	
    	final Button categoryButton = (Button)view;
    	
    	final String category = categoryButton.getText().toString();
    	
    	String fileName;
    	if (category.equalsIgnoreCase("Random!"))
    	{
    		final Random random = new Random();
    		final List<String> keys = new ArrayList<String>(categories.keySet());
    		final String randomKey = keys.get(random.nextInt(keys.size()));
    		fileName = categories.get(randomKey);
    	}
    	else
    	{
    		fileName = categories.get(category);
    	}
    	
    	final Intent myIntent = new Intent(ChooseCategoryActivity.this, PlayGameActivity.class);    	
    	myIntent.putExtra("fileName", "SearchTerms/" + fileName);
    	
    	ChooseCategoryActivity.this.startActivity(myIntent);
    }
    
    public void displayConnectivityMessage()
    {
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setText("Internet connection is needed to play");
        toast.show();
   }
    
}
