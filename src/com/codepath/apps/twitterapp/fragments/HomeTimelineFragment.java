package com.codepath.apps.twitterapp.fragments;

import java.io.Serializable;

import android.app.Activity;
import android.os.Bundle;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.codepath.apps.twitterapp.R;
import com.codepath.apps.twitterapp.TwitterApp;
import com.loopj.android.http.JsonHttpResponseHandler;

public class HomeTimelineFragment extends TimelineFragment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8901035109010590019L;

	public static final int COMPOSE_TWEET = 1;
	public static final int MY_USER_PROFILE = 2;
	public static final int USER_PROFILE = 3;


	// Define the events that the fragment will use to communicate
	public interface OnHomeTimelineNotifyActivityListener {
		public void onHomeTimelineNotifyActivity(Bundle bundle);
	}

	private OnHomeTimelineNotifyActivityListener notifyHomeTimelineActivityListener;

	public void fetch(long maxId, int count, JsonHttpResponseHandler handler) {
		TwitterApp.getRestClient().getHomeTimeline(maxId,  // -1 means don't specify param
				count, 
				handler);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}  


	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

		inflater.inflate(R.menu.home, menu);
	}

	// Store the listener (activity) that will have events fired once the fragment is attached
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof OnHomeTimelineNotifyActivityListener) {
			notifyHomeTimelineActivityListener = (OnHomeTimelineNotifyActivityListener) activity;
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implement HomeTimelineFragment.OnComposeTweetListener");
		}
	}

	public void composeTweet(MenuItem item) {
		//Toast.makeText(getApplicationContext(), "composeTweet", Toast.LENGTH_SHORT).show();
		Bundle newBundle = new Bundle();
		newBundle.putInt("event_type", 
				COMPOSE_TWEET);
		notifyHomeTimelineActivityListener.onHomeTimelineNotifyActivity(newBundle);
	}  

	public void gotoMyProfile(MenuItem item) {
		//Toast.makeText(getApplicationContext(), "composeTweet", Toast.LENGTH_SHORT).show();
		Bundle newBundle = new Bundle();
		newBundle.putInt("event_type", 
				MY_USER_PROFILE);
		notifyHomeTimelineActivityListener.onHomeTimelineNotifyActivity(newBundle);
	}  



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// handle item selection
		switch (item.getItemId()) {
		/*	      case R.id.miRefresh:
	    	  refreshTimeline(item);
	         return true;*/
		case R.id.miMyProfile:
			gotoMyProfile(item);
			return true;
		case R.id.miCompose:
			composeTweet(item);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}


}
