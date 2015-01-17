package com.searchgame.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.searchgame.R;
import com.searchgame.fragments.GamePlayFragment;
import com.searchgame.tasks.GetSearchSuggestionsTask;
import com.searchgame.types.GameResult;
import com.searchgame.utilities.FileParsingUtility;

import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class PlayGameActivity extends ActionBarActivity 
{

	private GamePlayFragment gameFragment;
	private List<GetSearchSuggestionsTask> searchSuggestionTasks;
	private List<String> searchTermsLeft;
	private Map<String, String[]> searchResponsesMap;
	private boolean gameStarted;
	private Handler delayHandler;
	private Runnable createGameRunnable;
	private Runnable nextRoundRunnable;
	private Runnable endGameRunnable;
	
    @SuppressLint("NewApi") @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_loading);
        
        searchSuggestionTasks = new ArrayList<GetSearchSuggestionsTask>();
        gameStarted = false;
        searchResponsesMap = new HashMap<String, String[]>();
        
        delayHandler = new Handler();
        createGameRunnable = new Runnable() {
            public void run() 
            {
            	gameStarted = true;
            	setContentView(R.layout.activity_play_game);
            	getSupportFragmentManager().beginTransaction()
            	                           .add(R.id.container, gameFragment)
            	                           .commit();
            }
        };
        
        nextRoundRunnable = new Runnable() {
            public void run() {
            	gameFragment.playNextRound();
            }
        };
        
        endGameRunnable = new Runnable() {
            public void run() {
        		getSupportFragmentManager().beginTransaction().detach(gameFragment).commit();
        		final GameResult result = gameFragment.getGameResults();
        		
        		setContentView(R.layout.results);
        		final TextView resultsText = (TextView) findViewById(R.id.GameResultsScoreText);
        		resultsText.setText(result.getNumCorrect() + "/" + result.getNumAnswered()  + " Correct!");
            }
        };
    }
    
    public void loadSearchTerms()
    {
    	final String fileName = getIntent().getStringExtra("fileName");
         
        searchTermsLeft = getSearchTermsFromFile(fileName);
        Collections.shuffle(searchTermsLeft);
        if (searchTermsLeft.size() > 20)
        {
        	searchTermsLeft = searchTermsLeft.subList(0, 20);
        }
         
         
     	for (String searchTerm : searchTermsLeft)
     	{
             GetSearchSuggestionsTask task = new GetSearchSuggestionsTask(this, searchTerm);
             searchSuggestionTasks.add(task);
             task.execute();	
     	}
    }
    
    @Override
    public void onPause()
    {
    	super.onPause();
    	delayHandler.removeCallbacks(createGameRunnable);
    	delayHandler.removeCallbacks(nextRoundRunnable);
    	delayHandler.removeCallbacks(endGameRunnable);
    	
    	for (GetSearchSuggestionsTask task : searchSuggestionTasks)
    	{
    		task.cancel(true);
    	}
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }
    
    @Override
    public void onResume() 
    {
        super.onResume();

        if (gameStarted)
        {
            if (gameFragment.isGameOver())
            {
            	gameFragment.beginGame();
            }
            else
            {
            	gameFragment.resumeCurrentRound();
            }
        }
        else
        {
            loadSearchTerms();
        }
    }
    
    public void insertParsedResponse(final String searchTerm, final String[] searchResponses)
    {
    	searchTermsLeft.remove(searchTerm);
    	searchResponsesMap.put(searchTerm, searchResponses);
    	
    	if (searchTermsLeft.isEmpty())
    	{
    		gameFragment = new GamePlayFragment(searchResponsesMap);
         
    		delayHandler.postDelayed(createGameRunnable, 1000);
    	}
    }


    private List<String> getSearchTermsFromFile(final String fileName) {
        try 
        {
			return FileParsingUtility.getSearchTerms(getAssets().open(fileName));
		} 
        catch (IOException e)
        {
        	System.out.print("IO exception: " + e);
		}
        return null;
	}


	@Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.play_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) 
//        {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
    	return true;
    }
	
    @SuppressLint("NewApi") 
    public void answerButtonOnClick(final View view) 
    {
    	gameFragment.lockAnswerButtons();
    	
    	final Boolean isAnswerCorrect = gameFragment.recordAnswer(view);
    	if (!isAnswerCorrect)
    	{
    		final Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    		v.vibrate(50);
    	}
    	
    	if (gameFragment.isGameOver())
    	{
    		delayHandler.postDelayed(endGameRunnable, 1500);
    	}
    	else
    	{
    		delayHandler.postDelayed(nextRoundRunnable, 1500);
    	}
    }  
    
    public void resultsScreenClick(final View view)
    {
    	PlayGameActivity.this.finish();
    }
    
}
