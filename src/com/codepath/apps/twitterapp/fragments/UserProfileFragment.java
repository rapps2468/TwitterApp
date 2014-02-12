package com.codepath.apps.twitterapp.fragments;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.codepath.apps.twitterapp.R;
import com.codepath.apps.twitterapp.TwitterApp;
import com.codepath.apps.twitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UserProfileFragment extends SherlockFragment {
	public static final int DONE_WITH_USER_PROFILE = 1;


	private static final String MAIN_USERNAME = "rapps2468";
	JsonHttpResponseHandler jsonHandler;

	TextView tvName;
	TextView tvTagline;
	TextView tvFollowers;
	TextView tvFollowing;
	ImageView ivUser;

	User user;
	String username;


	public static UserProfileFragment newInstance(String username) {
		UserProfileFragment fragment = new UserProfileFragment();
		Bundle args = new Bundle();
		args.putString("username", username);
		fragment.setArguments(args);
		return fragment;
	}



	public void setupViews(View v) {
		tvName = (TextView) v.findViewById(R.id.tvName);
		tvTagline = (TextView) v.findViewById(R.id.tvTagline);
		tvFollowers = (TextView) v.findViewById(R.id.tvFollowers);
		tvFollowing = (TextView) v.findViewById(R.id.tvFollowing);
		ivUser = (ImageView) v.findViewById(R.id.ivUser);
	}




	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Defines the xml file for the fragment
		View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
		// Setup handles to view objects here
		// etFoo = (EditText) v.findViewById(R.id.etFoo);

		setupViews(view);

		if (username == null)
		{
			username = MAIN_USERNAME;
		}



		jsonHandler = new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, JSONObject jsonUser) {
				Log.d("DEBUG", "USERRESULT: " + jsonUser.toString());

				try {
					user = new User(jsonUser);
				} catch (Exception e) {
					e.printStackTrace();
				}


				tvName.setText(user.getName());
				tvTagline.setText(user.getTagline());
				tvFollowers.setText(user.getFollowersCount() + " Followers");
				tvFollowing.setText(user.getFollowingCount() + " Following");



				ImageLoader.getInstance().displayImage(user.getProfileImageUrl(), ivUser);

				//Toast.makeText(getApplicationContext(), "composeTweet", Toast.LENGTH_SHORT).show();




			}

			@Override
			public void onFailure(Throwable arg0, JSONArray arg1) {
				// TODO Auto-generated method stub
				super.onFailure(arg0, arg1);
				Log.e("ERROR", "Failed to get user.");

			}	

			@Override
			public void handleFailureMessage(Throwable e, String responseBody)
			{
				// TODO Auto-generated method stub
				super.handleFailureMessage(e,  responseBody);
				Log.e("ERROR", "Failed to get user profile.  RESPONSE: " + responseBody);

			}

		};

		getUser();

		return view;	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		username = getArguments().getString("username");
	}

	// Store the listener (activity) that will have events fired once the fragment is attached
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof OnUserProfileNotifyActivityListener) {
			notifyUserProfileActivityListener = (OnUserProfileNotifyActivityListener) activity;
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implement OnUserProfileNotifyActivityListener.onNotify...");
		}
	}



	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Bundle newBundle = new Bundle();
		newBundle.putInt("event_type", 
				DONE_WITH_USER_PROFILE);
		notifyUserProfileActivityListener.onUserProfileNotifyActivity(newBundle);
	}



	public void getUser() {

		TwitterApp.getRestClient().getUser(username, jsonHandler);

	}

	// Define the events that the fragment will use to communicate
	public interface OnUserProfileNotifyActivityListener {
		public void onUserProfileNotifyActivity(Bundle bundle);
	}

	private OnUserProfileNotifyActivityListener notifyUserProfileActivityListener;


}
