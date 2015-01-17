package com.searchgame.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.searchgame.R;

public class ChooseCategoryFragment extends Fragment {

	private View rootView;
	private Set<String> categories;
	
	public ChooseCategoryFragment(final Set<String> categories)
	{
		this.categories = categories;
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	rootView = inflater.inflate(R.layout.fragment_choose_category, container, false);
    	populateButtonsText();
        
        return rootView;
    }
    
    public void populateButtonsText()
    {
		final List<String> categoryList = new ArrayList<String>();
		categoryList.addAll(categories);
    	Collections.shuffle(categoryList);
    	
		System.out.println(categoryList);
    	
    	final Button b1 = (Button) rootView.findViewById(R.id.Category1Button);
    	b1.setText(categoryList.get(0));
    	
    	final Button b2 = (Button) rootView.findViewById(R.id.Category2Button);
    	b2.setText(categoryList.get(1));
    	
    	final Button b3 = (Button) rootView.findViewById(R.id.Category3Button);
    	b3.setText(categoryList.get(2));
    	
    	final Button b4 = (Button) rootView.findViewById(R.id.Category4Button);
    	b4.setText(categoryList.get(3));
    	
    	final Button b5 = (Button) rootView.findViewById(R.id.Category5Button);
    	b5.setText(categoryList.get(4));
    	
    	final Button b6 = (Button) rootView.findViewById(R.id.Category6Button);
    	b6.setText(categoryList.get(5));
    	
    	final Button bRandom = (Button) rootView.findViewById(R.id.ChooseRandomButton);
    	bRandom.setText("Random!");
    }
    
}