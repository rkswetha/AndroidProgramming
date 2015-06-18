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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MyFavRecipe extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_my_fav_recipe);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
		String username = app_preferences.getString("username", "null");
		
		Log.i("MyFavRecipe-username", username);
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
		Log.i("MyFavRecipe-", "OnStart called");
		
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("FavRecipes");
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
		    public void done(List<ParseObject> results, com.parse.ParseException e) {
		        if (e == null) {
		        	updateListView(results);
		        }
		        	else {
		        		 Log.d("MyFavRecipe-OnStart", "Error: " + e.getMessage());
		        	}
			}
		}); 
	}
	
	public void updateListView(List<ParseObject> results)
	{
		Log.i("MyFavRecipe-", "updateListView called");
		
    	ArrayList<String> recipeList = new ArrayList<String>();

        for(int i =0; i<results.size(); i++)
		{
			recipeList.add(results.get(i).getString("RecipeName"));
		}	
		
     	setListAdapter(new ArrayAdapter<String>(this, R.layout.activity_my_fav_recipe,
					recipeList));
			
		ListView lv = getListView();

		// Enable filtering when the user types in the virtual keyboard
		lv.setTextFilterEnabled(true);

		// Set an setOnItemClickListener on the ListView
		lv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					
			    	 Intent i = new Intent(MyFavRecipe.this, LoadFavRecipe.class);
			    	 i.putExtra("RecipeName", ((TextView) view).getText());
			    	 startActivity(i);
				}
			}); 
			
		}

	}
