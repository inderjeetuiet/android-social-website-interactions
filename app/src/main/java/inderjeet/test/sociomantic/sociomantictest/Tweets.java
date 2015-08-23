package inderjeet.test.sociomantic.sociomantictest;

import com.twitter.sdk.android.Twitter;

/**
 * Created by isingh on 8/18/15.
 */
public class Tweets {
    public String ttext;
    public String country;
    public String uname;
    public int reTweetCount;
    private static String countTemp = "Not Found";
    public Tweets(){
        super();
    }

    private Tweets(String ttext, String country, String uname, int reTweetCount){
        this.ttext = ttext;
        this.country = country;
        this.uname = uname;
        this.reTweetCount= reTweetCount;

    }

    public static Tweets updateAllValues(String ttext, String country, String uname, int reTweetCount){
        return new Tweets(ttext,countTemp,uname, reTweetCount);
    }
}
