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
import android.widget.Toast;

public class LoadRecipe extends ActionBarActivity {

	String recipeName;
	EditText recipeName_ET,prepTime_ET,ingredients_ET, procedure_ET;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_load_recipe);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Log.i("LoadRecipe-", "OnCreate called");
	
		recipeName_ET = (EditText)findViewById(R.id.recipeNameEditText);
		prepTime_ET = (EditText)findViewById(R.id.prepTimeEditText);
		ingredients_ET = (EditText)findViewById(R.id.recipeIngredientsEditText);
		procedure_ET = (EditText)findViewById(R.id.procedureEditText);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.load_recipe, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	   
		switch (item.getItemId()) {
	    	// Respond to the action bar's Up/Home button
	    	case android.R.id.home:
	    		NavUtils.navigateUpFromSameTask(this);
	    		return true;
	        
	    	case R.id.action_edit:
	    		EditRecipe();
	            return true;

	    	case R.id.action_save:
	    		SaveRecipe();
	    		return true;
	    		
	        case R.id.action_addtoFav:
	        	AddToFav();
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
		
		Log.i("LoadRecipe-", "OnStart called");

		recipeName = getIntent().getStringExtra("RecipeName");
		Log.i("LoadRecipe-onStart: recipeName", recipeName);
	
		setTitle(recipeName);
		
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Recipes");
		query.whereEqualTo("RecipeName", recipeName );
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
		    public void done(List<ParseObject> results, com.parse.ParseException e) {
		        if (e == null) {
		            	Log.d("LoadRecipe-onStart", "Result list size: " + results.size() );
		            	displaySelectedRecipe(results);
		        	}
		        	else {
		        		 Log.d("LoadRecipe-onStart", "Error: " + e.getMessage());
		        	}
			}
		});
	} 
	
	// Function to display the selected recipe details
	public void displaySelectedRecipe(List<ParseObject> inputResults)
	{
		ParseObject parseObj=null;
		Log.i("LoadRecipe-", "displaySelectedRecipe called");
		
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
			ingredients_ET.setSingleLine(false);
			procedure_ET.setText(procedure);
			procedure_ET.setSingleLine(false);
			
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
	
	// Function to support Edit option
	public void EditRecipe()
	{
		Log.i("LoadRecipe- Edit called, recipeName: ", recipeName);
		// enable all the edit field since this is only Recipe view page.
		recipeName_ET.setEnabled(true);
		prepTime_ET.setEnabled(true);
		ingredients_ET.setEnabled(true);
		procedure_ET.setEnabled(true);
	      
	    recipeName_ET.setBackgroundColor(getResources().getColor(android.R.color.white));
	    prepTime_ET.setBackgroundColor(getResources().getColor(android.R.color.white));
	    ingredients_ET.setBackgroundColor(getResources().getColor(android.R.color.white));
	    procedure_ET.setBackgroundColor(getResources().getColor(android.R.color.white));
	    
	}
	
	/* Check if the existing recipe entry is being modified. If so, update the present entry.
	 * If no, then check if the new recipename is a duplicate of list of entries. Then throw error.
	 * */
	public void SaveRecipe()
	{
		
		String name = recipeName_ET.getText().toString();
		Log.i("LoadRecipe- SaveRecipe called, recipeName: ", recipeName);
		Log.i("LoadRecipe- SaveRecipe called, new Recipename: ", name);

		if(name.compareTo(recipeName) == 0)
		{
			ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Recipes");
			query.whereEqualTo("RecipeName", recipeName ); // Retreive the recipe entry that is being modified
			query.findInBackground(new FindCallback<ParseObject>() {
				@Override
			    public void done(List<ParseObject> results, com.parse.ParseException e) {
			        if (e == null) {
			            	Log.d("SaveRecipe", "Result list size: " + results.size() );
			            	saveToRecipeandFavClass(results);
			        	}
			        	else {
			        		Log.d("LoadRecipe- SaveRecipe ", "Error: " + e.getMessage());
			        	}
				}
			});
			
		}
		else
		{
			ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Recipes");
			query.whereEqualTo("RecipeName", name ); // Check if the new name already exists in the Recipes list (Avoid duplicate)
			query.findInBackground(new FindCallback<ParseObject>() {
				@Override
			    public void done(List<ParseObject> results, com.parse.ParseException e) {
			        if (e == null) {
			            	Log.d("SaveRecipe", "Result list size: " + results.size() );
			            	if(results.size() > 0)
			    				Toast.makeText(getBaseContext(), "Recipe name already exists!!", Toast.LENGTH_LONG).show();
			            	else
			            		saveToRecipeandFavClass(results);
			        	}
			        	else {
			        		 Log.d("LoadRecipe- SaveRecipe ", "Error: " + e.getMessage());
			        	}
				}
			});
		}
	}	
	
	// Function to update the newly saved recipe in Recipes and FavRecipe class.
	public void saveToRecipeandFavClass(List<ParseObject> inputResults)
	{
		ParseObject parseObj=null;
		
		Log.i("LoadRecipe-", "saveToRecipeandFavClass called");
		String name = recipeName_ET.getText().toString();
		String prepTime = prepTime_ET.getText().toString();
		String ingredients = ingredients_ET.getText().toString();
		String procedure = procedure_ET.getText().toString();
		
		// get the first matching recipe, since there will be only one matching recipe
		if(inputResults.size()> 0)
		{
			parseObj = inputResults.get(0);
			parseObj.put("RecipeName", name);
			parseObj.put("PrepTime", prepTime);
			parseObj.put("Ingredients", ingredients);
			parseObj.put("Procedure", procedure);
			parseObj.saveInBackground();
			
			if(parseObj.getBoolean("isFav") == true)
        	{
        		saveToFavRecipe(recipeName);
        	}
			
		}
		// disable all the edit field since this is only Recipe view page.
		recipeName_ET.setEnabled(false);
		prepTime_ET.setEnabled(false);
		ingredients_ET.setEnabled(false);
		procedure_ET.setEnabled(false);
		
		// Change background color to the default view mode color
		recipeName_ET.setBackgroundColor(getResources().getColor(R.color.green));
	    prepTime_ET.setBackgroundColor(getResources().getColor(R.color.green));
	    ingredients_ET.setBackgroundColor(getResources().getColor(R.color.green));
	    procedure_ET.setBackgroundColor(getResources().getColor(R.color.green));
		
	}
	
	
	public void saveToFavRecipe(String recipename)
	{
		Log.i("LoadRecipe- saveToFavRecipe called, recipeName: ", recipeName);

		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("FavRecipes");
		
		query.whereEqualTo("RecipeName", recipeName );
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
		    public void done(List<ParseObject> results, com.parse.ParseException e) {
		        if (e == null) 
		        {
		            	Log.d("saveToFavRecipe", "Result list size: " + results.size() );
		        		if(results.size() > 0)
		        		{	
		        			String name = recipeName_ET.getText().toString();
		        			String prepTime = prepTime_ET.getText().toString();
		        			String ingredients = ingredients_ET.getText().toString();
		        			String procedure = procedure_ET.getText().toString();
		        		
		        			ParseObject parseObj=null;
		        			parseObj = results.get(0);
		        			parseObj.put("RecipeName", name);
		        			parseObj.put("PrepTime", prepTime);
		        			parseObj.put("Ingredients", ingredients);
		        			parseObj.put("Procedure", procedure);
		        			parseObj.saveInBackground();
		        			
		        	}
		        }
		      	else {
		        		 Log.d("saveToFavRecipe-saveToFavRecipe", "Error: " + e.getMessage());
		        	}
			}
		});
	}
	
	// Function to support AddToFav option.
	public void AddToFav()
	{
    	Log.d("AddToFav called: ", recipeName);

		final String name = recipeName_ET.getText().toString();
		final String prepTime = prepTime_ET.getText().toString();
		final String ingredients = ingredients_ET.getText().toString();
		final String procedure = procedure_ET.getText().toString();
		
		
    	if(name.length() != 0 && prepTime.length() != 0 && ingredients.length() != 0 && procedure.length() != 0)
		{
				// Enable isFav to true in Recipes class for a given recipe
				ParseQuery<ParseObject> recipeObject = new ParseQuery<ParseObject>("Recipes");
				recipeObject.whereEqualTo("RecipeName", name );
				recipeObject.findInBackground(new FindCallback<ParseObject>() {
					@Override
				    public void done(List<ParseObject> results, com.parse.ParseException e) {
				        if (e == null) {
				            	Log.d("AddToFav", "Result list size: " + results.size() );
				        		if(results.size() > 0)
				        		{
					            	ParseObject parseObj=null;
					            	parseObj = results.get(0);
					            	boolean isFav = parseObj.getBoolean("isFav");
					            	if(isFav == true)
					    				Toast.makeText(getBaseContext(), "Your recipe is already in My Fav!!", Toast.LENGTH_LONG).show();
					            	else
					            	{	
					            		parseObj.put("isFav", true);
					            		parseObj.saveInBackground();	// Set isFav to true and addto FavRecipe
					            		
					            		ParseObject favRecipeObject = new ParseObject("FavRecipes");
					    				favRecipeObject.put("RecipeName", name);
					    				favRecipeObject.put("PrepTime", prepTime);
					    				favRecipeObject.put("Ingredients", ingredients);
					    				favRecipeObject.put("Procedure", procedure);
					    				favRecipeObject.saveInBackground();	
					            	}
				        		}
				        	}
				        	else {
				        		 Log.d("LoadRecipes-AddToFav", "Error: " + e.getMessage());
				        	}
					}
				});
				
				Intent intent = new Intent(getBaseContext(),ListAllRecipes.class);
				startActivity(intent);	
		}
	}
	
	// Function invoked when a delete option is selected.
	// Selected recipe must be deleted from Recipes list and if isFav is set, then it must be deleted from MyFavourite list too.
	public void DeleteRecipe()
	{
		Log.i("LoadRecipe- Delete called, recipeName: ", recipeName);

		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Recipes");
		query.whereEqualTo("RecipeName", recipeName );
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
		    public void done(List<ParseObject> results, com.parse.ParseException e) {
		        if (e == null) {
		            	Log.d("DeleteRecipe", "Result list size: " + results.size() );
		        		if(results.size() > 0)
		        		{
			            	ParseObject parseObj=null;
			        		// Delete the selected recipe and return to home page
			            	parseObj = results.get(0);
			            	
			            	// If recipe is also in FavRecipes list, we need to delete there too
			            	if(parseObj.getBoolean("isFav") == true)
			            	{
			            		deleteFromFavRecipes(recipeName);
			            	}
			            	parseObj.deleteInBackground();

		        		}
		        		Intent intent = new Intent(getBaseContext(),ListAllRecipes.class);
	    				startActivity(intent);
		        	}
		        	else {
		        		 Log.d("LoadRecipes-DeleteRecipe", "Error: " + e.getMessage());
		        	}
			}
		});
		
		
	}
	
	public void deleteFromFavRecipes(String recipename)
	{
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("FavRecipes");
		query.whereEqualTo("RecipeName", recipeName );
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
		    public void done(List<ParseObject> results, com.parse.ParseException e) {
		        if (e == null) {
		            	Log.d("deleteFromFavRecipes", "Result list size: " + results.size() );
		        		if(results.size() > 0)
		        		{
			            	ParseObject parseObj=null;
			        		// Delete the selected recipe and return to home page
			            	parseObj = results.get(0);
			            	parseObj.deleteInBackground();
		        		}
		        	}
		        	else {
		        		 Log.d("LoadRecipe-deleteFromFavRecipes", "Error: " + e.getMessage());
		        	}
			}
		});
		
	}
	
}
