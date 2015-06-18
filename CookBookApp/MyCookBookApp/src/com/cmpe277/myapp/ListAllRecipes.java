package com.cmpe277.myapp;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;


public class ListAllRecipes extends ListActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
		String username = app_preferences.getString("username", "null");
		
		Log.i("ListAllRecipes-username", username);
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	        NavUtils.navigateUpFromSameTask(this);
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		Log.i("ListAllRecipes-", "OnStart called");
		
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Recipes");
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
		    public void done(List<ParseObject> results, com.parse.ParseException e) {
		        if (e == null) {
		        	updateListView(results);
		        }
		        	else {
		        		 Log.d("ListAllRecipes-OnStart", "Error: " + e.getMessage());
		        	}
			}
		}); 
	}
	
	public void updateListView(List<ParseObject> results)
	{	
    	ArrayList<String> recipeList = new ArrayList<String>();

        Log.d("ListAllRecipes:updateListView", "Result list size: " + results.size() );
        for(int i =0; i<results.size(); i++)
		{
			recipeList.add(results.get(i).getString("RecipeName"));
		}	
		
     	setListAdapter(new ArrayAdapter<String>(this, R.layout.activity_list_all_recipes,
					recipeList));
			
		ListView lv = getListView();
		
		// Enable filtering when the user types in the virtual keyboard
		lv.setTextFilterEnabled(true);
		
		// Set an setOnItemClickListener on the ListView
		lv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
			        Log.d("ListAllRecipes:updateListView", "Invoking LoadRecipe class");

			    	 Intent i = new Intent(ListAllRecipes.this, LoadRecipe.class);
			    	 i.putExtra("RecipeName", ((TextView) view).getText());
			    	 startActivity(i);
					
				}
			}); 
			
		} 
		
}