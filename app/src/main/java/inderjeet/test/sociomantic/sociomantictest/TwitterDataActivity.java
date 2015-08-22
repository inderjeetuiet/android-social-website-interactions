package inderjeet.test.sociomantic.sociomantictest;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.List;


public class TwitterDataActivity extends ActionBarActivity {

    EditText hashTag;
    ArrayList<Tweets> allTweets = new ArrayList<>();
    ListView listView;
    TextView no_tweet;

    private static String TAG = TwitterDataActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        /**
         * accessing all the component defined in xml for the activity
         */

        hashTag = (EditText) findViewById(R.id.twitter_edit_hash);
        listView = (ListView) findViewById(R.id.tweetList);
        no_tweet = (TextView) findViewById(R.id.txt_noData);

        hashTag.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            Log.d(TAG, hashTag.getText().toString().trim());
                            MainActivity.twitterApiClient.getSearchService().tweets(hashTag.getText().toString().trim(), null, null, null, null, 20, null, null, null, true, new Callback<Search>() {
                                @Override
                                public void success(Result<Search> searchResult) {
                                    allTweets.clear();
                                    for(int i = 0; i < searchResult.data.tweets.size(); i++) {
                                        Tweets t = new Tweets();
                                        t.ttext = searchResult.data.tweets.get(i).text;
                                        t.uname = searchResult.data.tweets.get(i).user.name;
                                        if (searchResult.data.tweets.get(i).place != null)
                                            t.country = searchResult.data.tweets.get(i).place.country;
                                        t.reTweetCount = searchResult.data.tweets.get(i).retweetCount;
                                        allTweets.add(t);
                                    }
                                    Log.d(TAG, " Size #1 "+ allTweets.size());
                                    if(allTweets.size() > 0) {
                                        no_tweet.setVisibility(View.GONE);
                                        TweetAdapter adapter = new TweetAdapter(TwitterDataActivity.this, allTweets);
                                        listView.setAdapter(adapter);
                                    } else {
                                        no_tweet.setVisibility(View.VISIBLE);
                                    }
                                }
                                @Override
                                public void failure(TwitterException e) {

                                }
                            } );

                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
    }
}
