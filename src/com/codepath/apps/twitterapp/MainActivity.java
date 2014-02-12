package com.codepath.apps.twitterapp;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker.OnTimeChangedListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.codepath.apps.twitterapp.fragments.HomeTimelineFragment;
import com.codepath.apps.twitterapp.fragments.HomeTimelineFragment.OnHomeTimelineNotifyActivityListener;
import com.codepath.apps.twitterapp.fragments.MentionsTimelineFragment;
import com.codepath.apps.twitterapp.fragments.SherlockTabListener;
import com.codepath.apps.twitterapp.fragments.TimelineFragment;
import com.codepath.apps.twitterapp.fragments.TimelineFragment.OnTimelineNotifyActivityListener;
import com.codepath.apps.twitterapp.models.Tweet;
import com.codepath.apps.twitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;


public class MainActivity extends SherlockFragmentActivity implements OnHomeTimelineNotifyActivityListener, 
OnTimelineNotifyActivityListener {
	User user;
	JsonHttpResponseHandler jsonHandler;

	private final int COMPOSE_TWEET_REQUEST = 1;
	private final int GOTO_MY_USER_PROFILE_REQUEST = 2;
	private final int GOTO_USER_PROFILE_REQUEST = 3;

	private static final String MAIN_USERNAME = "rapps2468";

	public void setupViews(View v) {
		//timelineFragment = (TimelineFragment) getSupportFragmentManager().findFragmentById(R.id.) 
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setupTabs();

		jsonHandler = new JsonHttpResponseHandler() {
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
		};

		getUser();

	}

	private void setupTabs() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);

		Tab tabFirst = actionBar
				.newTab()
				.setText("HOME")
				.setIcon(R.drawable.ic_home)
				.setTabListener(
						new SherlockTabListener<HomeTimelineFragment>(R.id.flTab, this, "Home",
								HomeTimelineFragment.class));

		actionBar.addTab(tabFirst);
		actionBar.selectTab(tabFirst);

		Tab tabSecond = actionBar
				.newTab()
				.setText("MENTIONS")
				.setIcon(R.drawable.ic_mention)
				.setTabListener(
						new SherlockTabListener<MentionsTimelineFragment>(R.id.flTab, this, "Mentions",
								MentionsTimelineFragment.class));

		actionBar.addTab(tabSecond);
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

		TwitterApp.getRestClient().getUser(MAIN_USERNAME, jsonHandler);

	}



	@Override
	public void onTimelineNotifyActivity(Bundle bundle) { 
		int eventType = bundle.getInt("event_type");
		switch (eventType)
		{
		case TimelineFragment.IMAGE_VIEW_CLICKED:
		{
			String username = bundle.getString("username");
			Tweet tweet = (Tweet) bundle.getSerializable("tweet");
			Intent i = new Intent(this, UserProfileActivity.class);
			i.putExtra("calling_fragment", "home_fragment");
			i.putExtra("username", tweet.getUserHandle());
			startActivityForResult(i, GOTO_USER_PROFILE_REQUEST);
			break;
		}

		default:
			break;

		}
	}

	@Override
	public void onHomeTimelineNotifyActivity(Bundle bundle) {

		int eventType = bundle.getInt("event_type");
		switch (eventType)
		{
		case HomeTimelineFragment.COMPOSE_TWEET:
		{
			Intent i = new Intent(this, ComposeTweetActivity.class);
			i.putExtra("calling_fragment", "home_fragment");
			startActivityForResult(i, COMPOSE_TWEET_REQUEST);
			break;
		}
		case HomeTimelineFragment.MY_USER_PROFILE:
		{
			Intent i = new Intent(this, UserProfileActivity.class);
			i.putExtra("calling_fragment", "home_fragment");
			startActivityForResult(i, GOTO_MY_USER_PROFILE_REQUEST);
			break;
		}
		case HomeTimelineFragment.USER_PROFILE:
		{
			Intent i = new Intent(this, User.class);
			i.putExtra("calling_fragment", "home_fragment");
			startActivityForResult(i, GOTO_USER_PROFILE_REQUEST);
			break;
		}
		default:
			break;

		}
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

			String callingFragment = data.getStringExtra("calling_fragment");

			if ((callingFragment != null) && callingFragment.equals("home_fragment"));
			{
				HomeTimelineFragment homeFragment = (HomeTimelineFragment)
						getSupportFragmentManager().findFragmentByTag("Home");
				homeFragment.getAdapter().insert(newTweet, 0);
				homeFragment.getAdapter().notifyDataSetChanged();
			}
		}
		else if ((resultCode == RESULT_OK) && (requestCode == GOTO_MY_USER_PROFILE_REQUEST)) {

		}
		else
		{

		}
	}
}




