package com.codepath.apps.twitterapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.widget.FrameLayout;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.codepath.apps.twitterapp.fragments.TimelineFragment.OnTimelineNotifyActivityListener;
import com.codepath.apps.twitterapp.fragments.UserProfileFragment;
import com.codepath.apps.twitterapp.fragments.UserProfileFragment.OnUserProfileNotifyActivityListener;
import com.codepath.apps.twitterapp.fragments.UserTimelineFragment;
import com.loopj.android.http.JsonHttpResponseHandler;

public class UserProfileActivity extends SherlockFragmentActivity implements OnUserProfileNotifyActivityListener,
OnTimelineNotifyActivityListener {

	private static final String MAIN_USERNAME = "rapps2468";
	JsonHttpResponseHandler jsonHandler;
	FrameLayout flUserProfile;

	String username;

	public void setupViews() {
		flUserProfile = (FrameLayout) findViewById(R.id.flUserProfile);
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS); 
		setContentView(R.layout.activity_user_profile);

		Intent data = getIntent();
		username = data.getStringExtra("username");

		if (username == null)
		{
			username = MAIN_USERNAME;
		}

		getActionBar().setTitle(" @" + username);

		setupViews();

		if (savedInstanceState == null)
		{
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			UserProfileFragment fragment = UserProfileFragment.newInstance(username);
			ft.replace(R.id.flUserProfile, fragment);

			UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(username);
			ft.replace(R.id.flUserTimeline, userTimelineFragment);
			ft.commit();

		}
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onUserProfileNotifyActivity(Bundle bundle) {

		int eventType = bundle.getInt("event_type");
		switch (eventType)
		{
		case UserProfileFragment.DONE_WITH_USER_PROFILE:
		{
			Intent data = new Intent();
			setResult(RESULT_OK, data);
			finish();
			break;
		}
		default:
			break;

		}
	}

	public void onTimelineNotifyActivity(Bundle bundle) 
	{

	};

}
