// models/Tweet.java
package com.codepath.apps.twitterapp.models;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Tweets")
public class Tweet extends Model implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4398613498703183184L;



	// Define database columns and associated fields
	@Column(name = "tweet_id")
	long tweetId;



	@Column(name = "userHandle")
	String userHandle;
	@Column(name = "tweet_timestamp")
	String timestamp;

	@Column(name = "body")
	String body;

	@Column(name = "user")
	private User user;



	public long getTweetId() {
		return tweetId;
	}


	public void setTweetId(long tweetId) {
		this.tweetId = tweetId;
	}

	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public String getUserHandle() {
		return userHandle;
	}


	public void setUserHandle(String userHandle) {
		this.userHandle = userHandle;
	}


	public String getTimestamp() {
		return timestamp;
	}


	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}


	public String getBody() {
		return body;
	}


	public void setBody(String body) {
		this.body = body;
	}


	// Make sure to always define this constructor with no arguments
	public Tweet() {
		super();
	}


	// Add a constructor that creates an object from the JSON response
	public Tweet(JSONObject object){
		super();

		try {
			this.tweetId = object.getLong("id");
			this.timestamp = object.getString("created_at");
			this.body = object.getString("text");
			this.user = new User(object.getJSONObject("user"));
			this.userHandle = this.user.getScreenName();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<Tweet> fromJson(JSONArray jsonArray) {
		ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

		for (int i=0; i < jsonArray.length(); i++) {
			JSONObject tweetJson = null;
			try {
				tweetJson = jsonArray.getJSONObject(i);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}

			Tweet tweet = new Tweet(tweetJson);
			tweet.save();
			tweets.add(tweet);
		}

		return tweets;
	}
}