package inderjeet.test.sociomantic.sociomantictest;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by isingh on 8/18/15.
 */
public class TweetAdapter extends BaseAdapter {

    private TwitterDataActivity activity;
    private ArrayList data;
    private static LayoutInflater inflater=null;
    Tweets tweetsData=null;
    int i=0;

    /**
     * Construtor for list adpater and to get data
     * @param activity: activity context to populate the information
     * @param data: arraylist of data containing information of type object Tweets
     */

    public TweetAdapter(TwitterDataActivity activity, ArrayList data) {
        this.activity = activity;
        this.data=data;
        inflater = ( LayoutInflater )activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }
    public Object getItem(int position) {
        return position;
    }
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder{
        public TextView status;
        public TextView name;
        public TextView country;
        public TextView reTweet;
    }

    /**
     * populate the listview with custom layout
     * @param position: position of tweets in arraylist
     * @param convertView: view of list item
     * @param parent : list view object
     * @return: item view in list
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){
            vi = inflater.inflate(R.layout.twitter_layout, null);
            holder = new ViewHolder();
            holder.status = (TextView) vi.findViewById(R.id.txtTweets);
            holder.name = (TextView) vi.findViewById(R.id.txttname);
            holder.country = (TextView) vi.findViewById(R.id.txttCountry);
            holder.reTweet = (TextView) vi.findViewById(R.id.txtretweet);
            vi.setTag(holder);
        }
        else
            holder=(ViewHolder)vi.getTag();
            tweetsData=null;
            tweetsData = (Tweets) data.get(position);
            holder.status.setText(tweetsData.ttext);
            holder.name.setText(tweetsData.uname);
            holder.country.setText(tweetsData.country);
            holder.reTweet.setText(Integer.toString(tweetsData.reTweetCount));

        return vi;
    }
}
