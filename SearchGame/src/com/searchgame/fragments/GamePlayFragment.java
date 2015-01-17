package com.searchgame.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.searchgame.R;
import com.searchgame.types.Game;
import com.searchgame.types.GameResult;
import com.searchgame.types.Round;

public class GamePlayFragment extends Fragment {

	private View rootView;
	private Game game;
	private Map<String, String[]> searchResponsesMap;
	private List<Button> answerButtons;
	
	public GamePlayFragment(final Map<String, String[]> searchResponsesMap)
	{
		this.searchResponsesMap = searchResponsesMap;
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) 
    {
    	rootView = inflater.inflate(R.layout.fragment_play_game, container, false);
        
    	for (Button b : getAnswerButtons())
    	{
    		b.setOnTouchListener(onClickListener);
    	}
    	
        beginGame();
        
        return rootView;
    }
    
    @Override
	public void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }
        
    public void beginGame()
    {
    	game = new Game(searchResponsesMap);
    	playRound(game.getNextRound());    	
    }
    
    
	public void playNextRound()
	{
		unlockAnswerButtons();
		if (game.hasNextRound())
		{
			playRound(game.getNextRound());
		}
	}
	
	public void resumeCurrentRound()
	{
		playRound(game.getCurrentRound());
	}
    
    private void playRound(final Round round)
    {
    	// Set Search Term
        TextView searchTerm = (TextView) rootView.findViewById(R.id.SearchTermText);
        searchTerm.setText(round.getSearchTerm() + "...");
        
        System.out.println("SUGGESTIONS for " + round.getSearchTerm() + ": " + round.getSuggestions());
        
        final List<Button> answerButtons = getAnswerButtons();
        for (int i = 0; i < answerButtons.size(); i++)
    	{
        	final Button currentButton = answerButtons.get(i);
        	currentButton.setText(round.getSuggestions().get(i));
        	currentButton.setTypeface(null, Typeface.NORMAL);
        	currentButton.setBackgroundColor(getResources().getColor(R.color.buttonBlue));
        	currentButton.setTextColor(getResources().getColor(R.color.textColor));
    	}
    }
    
    public boolean recordAnswer(final View view) 
    {
    	final Button answeredButton = (Button)view;
    	final String searchSuggestion = answeredButton.getText().toString();
    	
    	System.out.println("ROOTVIEW: " + rootView);
    	
    	TextView searchTerm = (TextView) rootView.findViewById(R.id.SearchTermText);
    	
    	markCorrectAnswer(game.getCurrentRound().getWinningSuggestion(), searchSuggestion);
    	
    	if (searchSuggestion.equalsIgnoreCase(game.getCurrentRound().getWinningSuggestion()))
    	{
            searchTerm.setText("CORRECT!");
            game.recordCorrectAnswer();
            return true;
    	}
    	else 
    	{
    		searchTerm.setText("WRONG!");
    		return false;
    	}
    }
    
    private void markCorrectAnswer(final String correctAnswer, final String answerGiven)
    {
    	for (Button answerButton : getAnswerButtons())
    	{
    		if (answerButton.getText().toString().equalsIgnoreCase(correctAnswer))
        	{
        		answerButton.setTypeface(null, Typeface.BOLD);
        		answerButton.setTextColor(getResources().getColor(R.color.buttonCorrectText));
        		if (correctAnswer.equals(answerGiven))
        		{
        			answerButton.setBackgroundColor(getResources().getColor(R.color.buttonCorrectGreen));
        		}
        		else
        		{
        			answerButton.setBackgroundColor(getResources().getColor(R.color.buttonWrongRed));
        		}
        	}
    	}
    }
    
    public void lockAnswerButtons()
    {
    	for (Button button : getAnswerButtons())
    	{
    		button.setEnabled(false);
    		button.setTextColor(getResources().getColor(R.color.buttonDisabledText));
    	}
    }

    public void unlockAnswerButtons()
    {
    	for (Button button : getAnswerButtons())
    	{
    		button.setEnabled(true);
    		button.setTextColor(getResources().getColor(R.color.textColor));
    	}
    }
    
    private List<Button> getAnswerButtons()
    {
    	if (answerButtons == null)
    	{
        	answerButtons = new ArrayList<Button>();
        	
        	answerButtons.add((Button) rootView.findViewById(R.id.Button01));        
        	answerButtons.add((Button) rootView.findViewById(R.id.Button02));
        	answerButtons.add((Button) rootView.findViewById(R.id.Button03));
        	answerButtons.add((Button) rootView.findViewById(R.id.Button04));
        	answerButtons.add((Button) rootView.findViewById(R.id.Button05));
        	answerButtons.add((Button) rootView.findViewById(R.id.Button06));
    	}
        
        return answerButtons;
    }
    
    public GameResult getGameResults()
    {
    	return game.getResults();
    }
    
    public boolean isGameOver()
    {
    	return !game.hasNextRound();
    }
    
    final OnTouchListener onClickListener = new OnTouchListener() {      
        @Override
        public boolean onTouch(View v, MotionEvent event) {
        	System.out.println("ACTION: " + event.getAction() + " for button: " + v.getId());
            switch (event.getAction()) 
            {
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_DOWN:
            	v.setBackgroundColor(getResources().getColor(R.color.buttonOnTouch));
            	break;
            case MotionEvent.ACTION_UP:
            default:
            	v.setBackgroundColor(getResources().getColor(R.color.buttonBlue));
            }
            
            return false;
        }
    };
    
}