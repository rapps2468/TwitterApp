package com.codepath.apps.twitterapp.fragments;

import android.os.Bundle;

import com.codepath.apps.twitterapp.TwitterApp;
import com.loopj.android.http.JsonHttpResponseHandler;

public class UserTimelineFragment extends TimelineFragment {

	String curUsername;

	public static UserTimelineFragment newInstance(String username) {
		UserTimelineFragment fragment = new UserTimelineFragment();
		Bundle args = new Bundle();
		args.putString("username", username);
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		curUsername = getArguments().getString("username");
	}

	public String getCurUsername() {
		return curUsername;
	}


	public void setCurUsername(String curUsername) {
		this.curUsername = curUsername;
	}


	@Override
	public void fetch(long maxId, int count, JsonHttpResponseHandler handler) {
		TwitterApp.getRestClient().getUserTimeline(maxId,  // -1 means don't specify param
				count, 
				curUsername,
				handler);
	}

}
