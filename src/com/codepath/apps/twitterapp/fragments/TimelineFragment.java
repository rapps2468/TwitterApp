package com.codepath.apps.twitterapp.fragments;

import java.util.ArrayList;

import org.json.JSONArray;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.MenuItem;
import com.codepath.apps.twitterapp.EndlessScrollListener;
import com.codepath.apps.twitterapp.R;
import com.codepath.apps.twitterapp.TweetsAdapter;
import com.codepath.apps.twitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public abstract class TimelineFragment extends SherlockFragment implements DataFetcher {
	private static final int DEFAULT_LAST_NUM_TWEETS = 25;
	private static final int DEFAULT_MAX_NUM_TWEETS = 200;

	public static final int IMAGE_VIEW_CLICKED = 100;
	View.OnClickListener onClickListener; 

	public TweetsAdapter getAdapter() {
		return adapter;
	}


	public void setAdapter(TweetsAdapter adapter) {
		this.adapter = adapter;
	}

	private ListView lvTweets;
	ArrayList<Tweet> tweets;
	TweetsAdapter adapter;
	String username;


	long maxId = -1;

	private EndlessScrollListener scrollListener;


	JsonHttpResponseHandler jsonHandler;




	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Defines the xml file for the fragment
		View view = inflater.inflate(R.layout.fragment_timeline, container, false);
		// Setup handles to view objects here
		// etFoo = (EditText) v.findViewById(R.id.etFoo);

		setupViews(view);

		tweets = new ArrayList<Tweet>();

		onClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ImageView iv = (ImageView) v;

				int position = (Integer) iv.getTag();
				Tweet tweet = (Tweet) adapter.getItem(position);

				//Toast.makeText(getApplicationContext(), "composeTweet", Toast.LENGTH_SHORT).show();
				Bundle newBundle = new Bundle();
				newBundle.putInt("event_type", 
						IMAGE_VIEW_CLICKED);
				newBundle.putSerializable("tweet", tweet);
				notifyTimelineActivityListener.onTimelineNotifyActivity(newBundle);

			}
		};


		adapter = new TweetsAdapter(getActivity(), tweets, onClickListener);

		lvTweets.setAdapter(adapter);

		scrollListener = new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				int batchSize = DEFAULT_LAST_NUM_TWEETS;
				// Triggered only when new data needs to be appended to the list
				// Add whatever code is needed to append new items to your AdapterView

				if ((totalItemsCount + DEFAULT_LAST_NUM_TWEETS) >= DEFAULT_MAX_NUM_TWEETS) {
					batchSize = (DEFAULT_MAX_NUM_TWEETS - totalItemsCount);
				}
				else

					customLoadMoreDataFromApi(maxId, batchSize); 
			}
		};

		lvTweets.setOnScrollListener(scrollListener);




		return view;


	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		jsonHandler = new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, JSONArray jsonTweets) {
				Log.d("DEBUG", "RESULT: " + jsonTweets.toString());

				ArrayList<Tweet> newTweets = Tweet.fromJson(jsonTweets);

				for (Tweet newTweet : newTweets) {
					if ((maxId < 0) || (newTweet.getTweetId() < maxId) )
					{
						maxId = newTweet.getTweetId();
					}
				}

				adapter.addAll(newTweets);
				adapter.notifyDataSetChanged();

			}

			@Override
			public void onFailure(Throwable arg0, JSONArray arg1) {
				// TODO Auto-generated method stub
				super.onFailure(arg0, arg1);
				Log.e("ERROR", "Failed to get home timeline.");
			}

			@Override
			public void handleFailureMessage(Throwable e, String responseBody)
			{
				// TODO Auto-generated method stub
				super.handleFailureMessage(e,  responseBody);
				Log.e("ERROR", "Failed to get home timeline.  RESPONSE: " + responseBody);
			}
		};





	}







	public void refreshTimeline(MenuItem item) {
		maxId = -1;
		scrollListener.reset();
		adapter.clear();
		//		Toast.makeText(getActivity(), "Reloading timeline...", Toast.LENGTH_SHORT).show();
		fetch(maxId, DEFAULT_LAST_NUM_TWEETS, jsonHandler);
	}

	// Append more data into the adapter
	public void customLoadMoreDataFromApi(long maxId, int batchSize) {
		fetch(maxId, batchSize, jsonHandler);
	}


	public void setupViews(View v) {
		lvTweets = (ListView) v.findViewById(R.id.lvTweets);

	}


	// Define the events that the fragment will use to communicate
	public interface OnTimelineNotifyActivityListener {
		public void onTimelineNotifyActivity(Bundle bundle);
	}

	private OnTimelineNotifyActivityListener notifyTimelineActivityListener;

	public void onTimelineNotifyActivity(Bundle bundle) 
	{

	};


	// This fires 4th, and this is the first time the Activity is fully created.
	// Accessing the view hierarchy of the parent activity must be done in the onActivityCreated
	// At this point, it is safe to search for activity View objects by their ID, for example.
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		refreshTimeline(null);
	}

	// Store the listener (activity) that will have events fired once the fragment is attached
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof OnTimelineNotifyActivityListener) {
			notifyTimelineActivityListener = (OnTimelineNotifyActivityListener) activity;
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implement HomeTimelineFragment.OnTimelineListener");
		}
	}


	public abstract void fetch(long maxId, int count, 
			JsonHttpResponseHandler handler);

}
