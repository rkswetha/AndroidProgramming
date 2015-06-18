package com.cmpe277.myapp;

import java.util.List;

import com.cmpe277.myapp.R;
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
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NewRecipe extends ActionBarActivity {

	EditText recipeName_ET,prepTime_ET,ingredients_ET, procedure_ET;
	boolean isDup = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_recipe);
	
		Log.d("NewRecipe", "onCreate called");

		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		recipeName_ET = (EditText)findViewById(R.id.recipeNameEditText);
		prepTime_ET = (EditText)findViewById(R.id.prepTimeEditText);
		ingredients_ET = (EditText)findViewById(R.id.recipeIngredientsEditText);
		procedure_ET = (EditText)findViewById(R.id.procedureEditText);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_recipe, menu);
		return true;
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
	
	// Function to support add new recipe
	public void onAddRecipe(View view)
	{
		Log.d("onAddRecipe", "called");

		String name = recipeName_ET.getText().toString();
		String prepTime = prepTime_ET.getText().toString();
		String ingredients = ingredients_ET.getText().toString();
		String procedure = procedure_ET.getText().toString();
		
		if(name.length() != 0 && prepTime.length() != 0 && ingredients.length() != 0 && procedure.length() != 0)
		{
			ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Recipes");
			query.whereEqualTo("RecipeName", name );
			query.findInBackground(new FindCallback<ParseObject>() {
				@Override
			    public void done(List<ParseObject> results, com.parse.ParseException e) {
			        if (e == null) {
			            	Log.d("onAddRecipe", "Result list size: " + results.size() );
			            	if(results.size() > 0)
			    				Toast.makeText(getBaseContext(), "Recipe already exists!!", Toast.LENGTH_LONG).show();
			            	else
			            		addNewRecipe();
			        	}
			        	else {
			        		 Log.d("LoadRecipe-onStart", "Error: " + e.getMessage());
			        	}
				}
			});
		}
		else
			Toast.makeText(getBaseContext(), "Enter all fields", Toast.LENGTH_LONG).show();
	}
	
	public void addNewRecipe()
	{
		Log.d("addNewRecipe", "called");

		String name = recipeName_ET.getText().toString();
		String prepTime = prepTime_ET.getText().toString();
		String ingredients = ingredients_ET.getText().toString();
		String procedure = procedure_ET.getText().toString();

		ParseObject recipeObject = new ParseObject("Recipes");
		recipeObject.put("RecipeName", name);
		recipeObject.put("PrepTime", prepTime);
		recipeObject.put("Ingredients", ingredients);
		recipeObject.put("Procedure", procedure);
		recipeObject.put("isFav", false); // This is only set to true from LoadRecipe view
		recipeObject.saveInBackground();	
		
		int i = 0; // Sleep till the parse DB is updated.
		while(i <= 99)
			i++;
		
		Intent intent = new Intent(getBaseContext(),ListAllRecipes.class);
		startActivity(intent);
			
	} 
	
}

