package com.cmpe277.myapp;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	
	String username,password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ParseAnalytics.trackAppOpened(getIntent());
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		return super.onOptionsItemSelected(item);
	}

	
	public void onLogin(View view)
	{
		EditText et;
		
		et = (EditText)findViewById(R.id.loginEditText);
		username=et.getText().toString();
		
		et = (EditText)findViewById(R.id.passwdEditText);
		password = et.getText().toString();
		
		Log.d("onLogin: username-", username);
		Log.d("onLogin: passwd-", password);
		
		/*
		ParseQuery<ParseUser> queryuserlist = ParseUser.getQuery();
        queryuserlist.whereEqualTo("username", username);
		   try {
		    		Log.d("onLogin: username-", username);

		    		Log.d("count=", Integer.toString(queryuserlist.count()));
		    		
		        	//attempt to find a user with the specified credentials.
		            boolean res = (queryuserlist.count() != 0) ? true : false;
		            if(res == true)
		            {
			    		Log.d("onLogin:", "username exist");
			    		success();
			    		
		            }		            
		       } catch (com.parse.ParseException e) {
					Log.i("exception-", "asd");
					e.printStackTrace();
		       } */
		
		ParseUser.logInInBackground(username, password, new LogInCallback() {
			@Override
			public void done(ParseUser user, com.parse.ParseException e)
			{
				if(e == null){
			    	Toast.makeText(getBaseContext(), "Login successfull",Toast.LENGTH_SHORT).show();
					success();
				}
				else
			    	Toast.makeText(getBaseContext(), "Login failed! Try again..",Toast.LENGTH_SHORT).show();
			}
		});
		
	}
	
	
	public void onSignUp(View view)
	{	
		ParseUser user = new ParseUser();
		EditText et;
		
		et = (EditText)findViewById(R.id.loginEditText);
		user.setUsername(et.getText().toString());
		
		et = (EditText)findViewById(R.id.passwdEditText);
		user.setPassword(et.getText().toString());
		
		user.signUpInBackground(new SignUpCallback() {
			
				@Override
				public void done(com.parse.ParseException e) {
					
				  if (e == null) {
				    	success();
				    	
				    } else {
				    	Toast.makeText(getBaseContext(), "SignUp failed, Try again!!!",Toast.LENGTH_SHORT).show();
				    }
				  }
				
				}); 
	}
	
	public void success()
	{
		Log.d("success called", "username");

		// Save the username in shared preference in internal storage
		SharedPreferences app_preferences = 
				 PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = app_preferences.edit();
		editor.putString("username", username);
		editor.commit();
		
		Intent theIntent = new Intent (getApplication(), HomePage.class);
		startActivity(theIntent);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		Log.d("destroy calle1d", "username");
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			ParseUser.logOut();
		} 		
	}

}
	
