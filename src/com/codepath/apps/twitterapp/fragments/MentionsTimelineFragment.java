package com.codepath.apps.twitterapp.fragments;

import com.codepath.apps.twitterapp.TwitterApp;
import com.loopj.android.http.JsonHttpResponseHandler;

public class MentionsTimelineFragment extends TimelineFragment {

	@Override
	public void fetch(long maxId, int count, JsonHttpResponseHandler handler) {
		TwitterApp.getRestClient().getMentionsTimeline(maxId,  // -1 means don't specify param
				count, 
				handler);
	}

}
