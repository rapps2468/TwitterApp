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
public class User extends Model implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 30463455321242648L;

	// Define database columns and associated fields
	@Column(name = "name")
	String name;

	@Column(name = "user_id")
	long userId;

	@Column(name = "screen_name")
	String screen_name;

	@Column(name = "profile_image_url")
	String profile_image_url;

	@Column(name = "profile_background_image_url")
	String profile_background_image_url;

	@Column(name = "num_tweets")
	int num_tweets;

	@Column(name = "followers_count")
	int followers_count;

	@Column(name = "friends_count")
	int friends_count;

	public String getTagline() {
		return tagline;
	}



	public void setTagline(String tagline) {
		this.tagline = tagline;
	}




	@Column(name = "tagline")
	String tagline;

	@Column(name = "following_count")
	int following_count;



	public int getFollowingCount() {
		return following_count;
	}



	public void setFollowingCount(int following_count) {
		this.following_count = following_count;
	}

	public User() {
		super();
	}



	// Add a constructor that creates an object from the JSON response
	public User(JSONObject object){
		super();

		try {
			this.name = object.getString("name");
			this.userId = object.getLong("id");
			this.screen_name = object.getString("screen_name");
			this.profile_image_url = object.getString("profile_image_url");
			this.profile_background_image_url = object.getString("profile_background_image_url");
			this.num_tweets = object.getInt("statuses_count");
			this.followers_count = object.getInt("followers_count");
			this.friends_count = object.getInt("friends_count");
			this.following_count = object.getInt("friends_count");
			this.tagline = object.getString("description");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<User> fromJson(JSONArray jsonArray) {
		ArrayList<User> users = new ArrayList<User>(jsonArray.length());

		for (int i=0; i < jsonArray.length(); i++) {
			JSONObject userJson = null;
			try {
				userJson = jsonArray.getJSONObject(i);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}

			User user = new User(userJson);
			user.save();
			users.add(user);
		}

		return users;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public long getUserId() {
		return userId;
	}



	public void setUserId(long id) {
		this.userId = id;
	}



	public String getScreenName() {
		return screen_name;
	}



	public void setScreenName(String screen_name) {
		this.screen_name = screen_name;
	}



	public String getProfileImageUrl() {
		return profile_image_url;
	}



	public void setProfileImageUrl(String profile_image_url) {
		this.profile_image_url = profile_image_url;
	}



	public String getProfileBackgroundImageUrl() {
		return profile_background_image_url;
	}



	public void setProfileBackgroundImageUrl(String profile_background_image_url) {
		this.profile_background_image_url = profile_background_image_url;
	}



	public int getNumTweets() {
		return num_tweets;
	}



	public void setNumTweets(int num_tweets) {
		this.num_tweets = num_tweets;
	}



	public int getFollowersCount() {
		return followers_count;
	}



	public void setFollowersCount(int followers_count) {
		this.followers_count = followers_count;
	}



	public int getFriendsCount() {
		return friends_count;
	}



	public void setFriendsCount(int friends_count) {
		this.friends_count = friends_count;
	}



}