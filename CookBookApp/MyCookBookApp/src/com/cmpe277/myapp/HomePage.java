package com.cmpe277.myapp;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class HomePage extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_page);
		
		Log.i("HomePage-onCreate", "called");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_page, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		Log.i("HomePage-onOptionsItemSelected", "called");

		
		switch (item.getItemId()) {

        case R.id.add_recipe:
    		Log.i("HomePage-onOptionsItemSelected1", "");

        	Intent theIntent1 = new Intent (getApplication(), NewRecipe.class);
    		startActivity(theIntent1);
    		
            return true;
        case R.id.myfavList:
    		Log.i("HomePage-onOptionsItemSelected2", "");

        	Intent theIntent2 = new Intent (getApplication(), MyFavRecipe.class);
    		startActivity(theIntent2);
    		
            return true;
        case R.id.allRecipesList:
    		Log.i("HomePage-onOptionsItemSelected3", "");

        	Intent theIntent3 = new Intent (getApplication(), ListAllRecipes.class);
    		startActivity(theIntent3);
    		
            return true;
        default:
        	Log.i("HomePage-onOptionsItemSelected id", Integer.toString(item.getItemId()));

            return super.onOptionsItemSelected(item);
            
		}
	}
}
