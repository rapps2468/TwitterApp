package com.codepath.apps.twitterapp;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.twitterapp.models.Tweet;
import com.codepath.apps.twitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;


public class TimelineActivity extends Activity {
	private static final int DEFAULT_LAST_NUM_TWEETS = 25;
	private static final int DEFAULT_MAX_NUM_TWEETS = 200;
	private static final String MAIN_USERNAME = "rapps2468";
	
	
	
	private ListView lvTweets;
	ArrayList<Tweet> tweets;
	TweetsAdapter adapter;
	User user;
	String username;
	
	private final int COMPOSE_TWEET_REQUEST = 1;
	
	long maxId = -1;
	
	private EndlessScrollListener scrollListener;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        setupViews();
        
        tweets = new ArrayList<Tweet>();
    	adapter = new TweetsAdapter(getBaseContext(), tweets);
    	lvTweets.setAdapter(adapter);
    	
		scrollListener = new EndlessScrollListener() {
	        @Override
	        public void onLoadMore(int page, int totalItemsCount) {
	        		int batchSize = DEFAULT_LAST_NUM_TWEETS;
	                // Triggered only when new data needs to be appended to the list
	                // Add whatever code is needed to append new items to your AdapterView
	        	
	        	if ((totalItemsCount + DEFAULT_LAST_NUM_TWEETS) >= DEFAULT_MAX_NUM_TWEETS) {
	        		batchSize = (DEFAULT_MAX_NUM_TWEETS - totalItemsCount);
	        	}
	        	else

	            customLoadMoreDataFromApi(maxId, batchSize); 
	        }
	    };
        
	    getUser();
	    lvTweets.setOnScrollListener(scrollListener);
	    refreshHomeTimeline();
    }
    
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
		if ((resultCode == RESULT_OK) && (requestCode == COMPOSE_TWEET_REQUEST)) {
			String newTweetMessage = data.getStringExtra("tweet_message");
			Tweet newTweet = new Tweet();
			newTweet.setUser(user);
			newTweet.setTweetId(-1);
			newTweet.setTimestamp("");
			newTweet.setBody(newTweetMessage);
			adapter.insert(newTweet, 0);
			adapter.notifyDataSetChanged();
		}
	}
    
    public void getUser() {
    	/*
       	TwitterApp.getRestClient().getUsername(
    			new JsonHttpResponseHandler() {
    		@Override
    		public void onSuccess(int arg0, JSONObject accountSettings) {
    			Log.d("DEBUG", "USERNAME RESULT: " + accountSettings.toString());
    				
    				try {
    					username = accountSettings.getString("screen_name");
    				} catch (Exception e) {
    					e.printStackTrace();
    				}

    			
    			
    				
		@Override
		public void onFailure(Throwable arg0, JSONArray arg1) {
			// TODO Auto-generated method stub
			super.onFailure(arg0, arg1);
			Log.e("ERROR", "Failed to get username.");
		}

	});
	*/
	
    		      	TwitterApp.getRestClient().getUser(MAIN_USERNAME,
    		    			new JsonHttpResponseHandler() {
    		    		@Override
    		    		public void onSuccess(int arg0, JSONObject jsonUser) {
    		    			Log.d("DEBUG", "USERRESULT: " + jsonUser.toString());
    		    				
    		    				try {
    		    					user = new User(jsonUser);
    		    				} catch (Exception e) {
    		    					e.printStackTrace();
    		    				}

    		    				
    		    			}
    		    			
    					@Override
    					public void onFailure(Throwable arg0, JSONArray arg1) {
    						// TODO Auto-generated method stub
    						super.onFailure(arg0, arg1);
    						Log.e("ERROR", "Failed to get user.");
    					}

    		    		
    		    		}
    		    	);
    		      	
    		}
    		

    

    public void composeTweet(MenuItem item) {
    	//Toast.makeText(getApplicationContext(), "composeTweet", Toast.LENGTH_SHORT).show();
    	Intent i = new Intent(this, ComposeTweetActivity.class);
    	startActivityForResult(i, COMPOSE_TWEET_REQUEST);
    }
  
    
    public void refreshTimeline(MenuItem item) {
    	refreshHomeTimeline();
    }

    
    public void refreshHomeTimeline() {
    		maxId = -1;
    		scrollListener.reset();
    		adapter.clear();
    		Toast.makeText(getApplicationContext(), "Reloading timeline...", Toast.LENGTH_SHORT).show();
    		getHomeTimeline(maxId, DEFAULT_LAST_NUM_TWEETS);
    }

	
        public void getHomeTimeline(long curMaxId, int batchSize) {
        	TwitterApp.getRestClient().getHomeTimeline(curMaxId,  // -1 means don't specify param
        			batchSize, 
        			new JsonHttpResponseHandler() {
        		@Override
        		public void onSuccess(int arg0, JSONArray jsonTweets) {
        			Log.d("DEBUG", "RESULT: " + jsonTweets.toString());

        			ArrayList<Tweet> newTweets = Tweet.fromJson(jsonTweets);

        			for (Tweet newTweet : newTweets) {
        				if ((maxId < 0) || (newTweet.getTweetId() < maxId) )
        				{
        					maxId = newTweet.getTweetId();
        				}
        			}

        			adapter.addAll(newTweets);
        			adapter.notifyDataSetChanged();

        		}

				@Override
				public void onFailure(Throwable arg0, JSONArray arg1) {
					// TODO Auto-generated method stub
					super.onFailure(arg0, arg1);
					Log.e("ERROR", "Failed to get home timeline.");
				}
        	});

        }
    // Append more data into the adapter
    public void customLoadMoreDataFromApi(long maxId, int batchSize) {
    	getHomeTimeline(maxId, batchSize);
    }
    
    
    
    public void setupViews() {
    	lvTweets = (ListView) findViewById(R.id.lvTweets);

    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.timeline, menu);
        return true;
    }
    
}
