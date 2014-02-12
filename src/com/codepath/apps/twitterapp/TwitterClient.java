package com.codepath.apps.twitterapp;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.FlickrApi;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "DTxlndloeuavrglyaLACRg";       // Change this
	public static final String REST_CONSUMER_SECRET = "cXe4fcM5JD9GNcDO1iiuIRUeQqQ3bcmbnpxTij2HUs"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://codepathtweets"; // Change this (here and in manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	// CHANGE THIS
	public void getHomeTimeline(long maxId, int count, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		if (maxId >= 0)
		{
			params.put("max_id", String.valueOf(maxId-1));
		}

		params.put("count", String.valueOf(count));

		Log.d("DEBUG", "GET_HOME_TIMELINE QUERY: " + "count=" + count + "max_id=" + maxId);
		getClient().get(apiUrl, params, handler);
	}


	// CHANGE THIS
	public void getMentionsTimeline(long maxId, int count, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/mentions_timeline.json");
		RequestParams params = new RequestParams();
		if (maxId >= 0)
		{
			params.put("max_id", String.valueOf(maxId-1));
		}

		params.put("count", String.valueOf(count));

		Log.d("DEBUG", "GET_MENTIONS QUERY: " + "count=" + count + "max_id=" + maxId);
		getClient().get(apiUrl, params, handler);
	}

	// CHANGE THIS
	public void getUserTimeline(long maxId, int count, String username,
			AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/user_timeline.json");
		RequestParams params = new RequestParams();

		if (maxId >= 0)
		{
			params.put("max_id", String.valueOf(maxId-1));
		}

		params.put("count", String.valueOf(count));

		if ((username != null) && (!username.isEmpty()))
		{
			params.put("screen_name", username);
		}

		Log.d("DEBUG", "GET_USER_TIMELINE QUERY: " + "count=" + count + "max_id=" + maxId);
		getClient().get(apiUrl, params, handler);
	}





	// RestClient.java
	public void postTweet(String body, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", body);
		getClient().post(apiUrl, params, handler);
	}


	public void getUsername(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("account/settings.json");
		RequestParams params = new RequestParams();
		getClient().get(apiUrl, params, handler);
	}

	public void getUser(String username, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("users/show.json");
		RequestParams params = new RequestParams();
		params.put("screen_name", username);
		getClient().get(apiUrl, params, handler);
	}

	public void getUserInfo(String username, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("account/verify_credentials.json");
		RequestParams params = new RequestParams();
		getClient().get(apiUrl, params, handler);
	}

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}