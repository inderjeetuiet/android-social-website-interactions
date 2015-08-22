package inderjeet.test.sociomantic.sociomantictest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


public class GooglePlusActivity extends ActionBarActivity {

    private static String TAG = GooglePlusActivity.class.getName();

    ImageView p_pic;
    TextView u_name, u_email, u_aboutMe, u_curr_location, u_dob, u_lang;
    Button btngetData;

    String name, url, email, location, aboutMe, language, dob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_google_plus);

        /**
         * accessing all the component defined in xml for the activity
         */

        p_pic = (ImageView) findViewById(R.id.userPic);
        u_name = (TextView) findViewById(R.id.txtName);
        u_email = (TextView) findViewById(R.id.txtEmail);
        u_aboutMe = (TextView) findViewById(R.id.txtAbout);
        u_curr_location = (TextView) findViewById(R.id.txtLoc);
        u_dob = (TextView) findViewById(R.id.txtDob);
        u_lang = (TextView) findViewById(R.id.txtLang);

        /**
         * Receiving intent to get data from MainAcitvity
         */

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");
        url = intent.getStringExtra("purl");
        location = intent.getStringExtra("location");
        aboutMe = intent.getStringExtra("about");
        dob = intent.getStringExtra("dob");
        language = intent.getStringExtra("language");
        btngetData = (Button) findViewById(R.id.btn_getData);

        btngetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                u_name.setText(name);
                u_email.setText(email);
                u_aboutMe.setText(aboutMe);
                u_curr_location.setText(location);
                u_dob.setText(dob);
                u_lang.setText(language);
                new LoadProfileImage(p_pic).execute(url);
            }
        });
    }

    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }
        protected Bitmap doInBackground(String... urls) {
            Bitmap mIcon11 = null;
            try {
                URL url = new URL(urls[0]);
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                mIcon11 =bmp;
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
