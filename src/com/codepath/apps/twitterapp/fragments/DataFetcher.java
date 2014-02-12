package com.codepath.apps.twitterapp.fragments;

import java.io.Serializable;

import com.loopj.android.http.JsonHttpResponseHandler;

public abstract interface DataFetcher extends Serializable {

	public abstract void fetch(long maxId, int count, 
			JsonHttpResponseHandler handler);

}
