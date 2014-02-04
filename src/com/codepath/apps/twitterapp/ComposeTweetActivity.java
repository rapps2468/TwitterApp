package com.codepath.apps.twitterapp;

import java.util.ArrayList;

import org.json.JSONArray;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.codepath.apps.twitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class ComposeTweetActivity extends Activity {
	
	EditText etTweet;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose_tweet);
		// Show the Up button in the action bar.
		setupActionBar();

		setupViews();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose_tweet, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void cancelTweet(View button) {
        Intent i = new Intent();
        setResult(RESULT_OK, i);
        finish();
	}

	public void composeTweet(View button) {
		
		String tweetMessage = etTweet.getText().toString();
		
        Intent data = new Intent();
        
    	TwitterApp.getRestClient().postTweet(tweetMessage, 
    			new JsonHttpResponseHandler() {
    		@Override
    		public void onSuccess(int arg0, JSONArray jsonTweets) {
    			Log.d("DEBUG", "POSTRESULT: " + jsonTweets.toString());

    			ArrayList<Tweet> newTweets = Tweet.fromJson(jsonTweets);

    		}
    	});

        
        data.putExtra("tweet_message", tweetMessage);
        setResult(RESULT_OK, data);
        finish();
	}

    public void setupViews() {
    	etTweet = (EditText) findViewById(R.id.etMessage);
    }
    


}
