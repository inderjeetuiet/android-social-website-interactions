package inderjeet.test.sociomantic.sociomantictest;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Base64;
import android.util.Log;

import com.dropbox.client2.session.Session;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;

/**
 * Created by isingh on 8/17/15.
 */
public class MainApplication extends Application {

    private static Context instance;
    public static Context get() {return MainApplication.instance;}

    public static final String CLIENT_ID = "Instagram key id";
    public static final String CLIENT_SECRET = "Instagram client secret";
    public static final String CALLBACK_URL = "http://www-sop.inria.fr/members/Inderjeet.Singh/";

    final static public String DROPBOX_APP_KEY = "dropBox key";
    final static public String DROPBOX_APP_SECRET = "dropbox app secrect";

    final static public Session.AccessType ACCESS_TYPE = Session.AccessType.DROPBOX;


    @Override public void onCreate()
    {
        super.onCreate();
        instance = getApplicationContext();
    }
}
