package inderjeet.test.sociomantic.sociomantictest;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class FaceBookDataActivity extends ActionBarActivity {

    private static String TAG = FaceBookDataActivity.class.getName();

    ArrayList<String> post_ids = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_book_data);
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/posts",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Log.d(TAG, " FB Data "+ response.toString());
                        try{
                            JSONObject main = new JSONObject();
                            JSONArray jArray = main.getJSONArray("data");
                        } catch (JSONException exception){
                            exception.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }


}
