package com.cmpe277.myapp;

import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class LoadFavRecipe extends ActionBarActivity {

	String recipeName;
	EditText recipeName_ET,prepTime_ET,ingredients_ET, procedure_ET;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_load_fav_recipe);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Log.i("LoadFavRecipe-", "OnCreate called");
	
		recipeName_ET = (EditText)findViewById(R.id.recipeNameEditText);
		prepTime_ET = (EditText)findViewById(R.id.prepTimeEditText);
		ingredients_ET = (EditText)findViewById(R.id.recipeIngredientsEditText);
		procedure_ET = (EditText)findViewById(R.id.procedureEditText);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.load_fav_recipe, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
    	// Respond to the action bar's Up/Home button
    	case android.R.id.home:
    		NavUtils.navigateUpFromSameTask(this);
    		return true;

    	case R.id.action_delete:
        	DeleteRecipe();
        	return true;
        	
        default:
            return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	protected void onStart() {
	
		super.onStart();
			
		Log.i("LoadFavRecipe-", "OnStart called");

		recipeName = getIntent().getStringExtra("RecipeName");
		Log.i("LoadRecipe-onStart: recipeName", recipeName);
			
		setTitle(recipeName);

		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("FavRecipes");
		query.whereEqualTo("RecipeName", recipeName );
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
		    public void done(List<ParseObject> results, com.parse.ParseException e) {
		        if (e == null) {
		            	Log.d("onStart", "Result list size: " + results.size() );
		            	displaySelectedRecipe(results);
			        }
			       	else {
			       		 Log.d("LoadFavRecipe-onStart", "Error: " + e.getMessage());
			      	}
		      }
		});
	}
	
	//Function to load the recipe name selected from Favourite list
	public void displaySelectedRecipe(List<ParseObject> inputResults)
	{
			ParseObject parseObj=null;
			Log.i("LoadFavRecipe-", "displaySelectedRecipe called");
			
			// get the first matching recipe, since there will be only one matching recipe
			if(inputResults.size() > 0)
			{
				parseObj = inputResults.get(0);
				String name = parseObj.getString("RecipeName");
				String prepTime = parseObj.getString("PrepTime");
				String ingredients = parseObj.getString("Ingredients");
				String procedure = parseObj.getString("Procedure");
				
				recipeName_ET.setText(name);
				prepTime_ET.setText(prepTime);
				ingredients_ET.setText(ingredients);
				procedure_ET.setText(procedure);
			}	
			else // This can hit only on: First Delete the selected Recipe and press Back. (Not an idle case)
			{
				recipeName_ET.setText("");
				prepTime_ET.setText("");
				ingredients_ET.setText("");
				procedure_ET.setText("");
			}
			
			// disable all the edit field since this is only Recipe view page.
			recipeName_ET.setEnabled(false);
			prepTime_ET.setEnabled(false);
			ingredients_ET.setEnabled(false);
			procedure_ET.setEnabled(false);
			
	}
		
	// Delete recipe from Favorite should be deleted only from FavRecipes class.
	public void DeleteRecipe()
	{
			Log.i("LoadFavRecipe- DeleteRecipe called, recipeName: ", recipeName);

			ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("FavRecipes");
			query.whereEqualTo("RecipeName", recipeName );
			query.findInBackground(new FindCallback<ParseObject>() {
				@Override
			    public void done(List<ParseObject> results, com.parse.ParseException e) {
			        if (e == null) {
			            	Log.d("DeleteRecipe", "Result list size: " + results.size() );
			        		if(results.size() > 0)
			        		{
			        			disableIsFav();
				            	ParseObject parseObj=null;
				        		parseObj = results.get(0);
				            	parseObj.deleteInBackground();
				            	
			        		}
			        		Intent intent = new Intent(getBaseContext(),MyFavRecipe.class);
		    				startActivity(intent);
			        	}
			        	else {
			        		 Log.d("LoadFavRecipe-DeleteRecipe", "Error: " + e.getMessage());
			        	}
				}
			});
			
	}
		
	// Function to reset back the isFav flag when the recipe is deleted from Favourite list
	public void disableIsFav()
	{
			Log.i("LoadFavRecipe- disableIsFav called, recipeName: ", recipeName);
			
			// Disable the isFav value for this recipeName in Recipes class.
			ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>("Recipes");
			query1.whereEqualTo("RecipeName", recipeName );
			query1.findInBackground(new FindCallback<ParseObject>() {
				@Override
			    public void done(List<ParseObject> results, com.parse.ParseException e) {
			        if (e == null) {
			        		if(results.size() > 0)
			        		{
			        			Log.i("LoadFavRecipe- disableIsFav called, results.size(): ", Integer.toString(results.size()));

				            	ParseObject parseObj=null;
				        		parseObj = results.get(0);
				        		//parseObj.put("isFav", false); // Doesnt seem to be working
				        		
				        		// Its an alternate to avoid to above problem
			        			// Recreating a new entry with isFav=false and delete the old entry
			        			ParseObject parseObj1 = new ParseObject("Recipes");
			        			parseObj1.put("RecipeName", parseObj.getString("RecipeName"));
			        			parseObj1.put("PrepTime", parseObj.getString("PrepTime"));
			        			parseObj1.put("Ingredients", parseObj.getString("Ingredients"));
			        			parseObj1.put("Procedure", parseObj.getString("Procedure"));
				            	parseObj1.put("isFav", false);
			        			parseObj1.saveInBackground();	
			        			
			        			parseObj.deleteInBackground();
			        			
			        		}	
			        	}
			        	else {
			        		 Log.d("LoadFavRecipe-DeleteRecipe", "Error: " + e.getMessage());
			        	}
				}
		});
			
	}
}
