package inderjeet.test.sociomantic.sociomantictest;

import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import com.google.android.gms.plus.model.people.Person;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import io.fabric.sdk.android.Fabric;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class MainActivity extends ActionBarActivity implements View.OnClickListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    //Twitter keys to access developer settings
    private static final String TWITTER_KEY = "Your Twitter Key";
    private static final String TWITTER_SECRET = "Your Twitter Secret Key";




    private LoginButton fbLoginButton;
    private SignInButton gPlusLogin;
    private TwitterLoginButton twitterLoginButton;
    private Button gPlusLogOut;
    private Button btn_instagram;
    private Button btn_dropbox;

    private CallbackManager callbackManager;

    private boolean currentIntent;
    private ConnectionResult connectionResult;
    private boolean gSignedIn;

    private GoogleApiClient loginGoogleApiClient;

    private DropboxAPI<AndroidAuthSession> dropboxAPI;

    public static TwitterApiClient twitterApiClient;
    public static AccessToken accessToken;
    private static String PACKAGE_NAME = "inderjeet.test.sociomantic.sociomantictest";
    private static String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        callbackManager = CallbackManager.Factory.create();
        getFbKeyHash(PACKAGE_NAME);

        setContentView(R.layout.activity_main);

        /*
        accessing components defined in xml such as buttons, layouts
         */

        fbLoginButton = (LoginButton)findViewById(R.id.login_button);
        gPlusLogin = (SignInButton) findViewById(R.id.sign_in_button);
        gPlusLogOut = (Button) findViewById(R.id.btn_sign_out);
        twitterLoginButton = (TwitterLoginButton)findViewById(R.id.twitter_login_button);
        btn_instagram = (Button) findViewById(R.id.btn_instagram);
        btn_dropbox = (Button) findViewById(R.id.btn_dropdop);

        /*
        Dropbox authtication button listener
         */

        btn_dropbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppKeyPair appKeys = new AppKeyPair(MainApplication.DROPBOX_APP_KEY, MainApplication.DROPBOX_APP_SECRET);
                AndroidAuthSession session = new AndroidAuthSession(appKeys);
                dropboxAPI = new DropboxAPI<AndroidAuthSession>(session);
                dropboxAPI.getSession().startOAuth2Authentication(MainActivity.this);
            }
        });

        /*
        Instagram authentication button listner
         */

        btn_instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InstagramApp mApp = new InstagramApp(MainActivity.this, MainApplication.CLIENT_ID,
                        MainApplication.CLIENT_SECRET, MainApplication.CALLBACK_URL);
                mApp.setListener(new InstagramApp.OAuthAuthenticationListener() {
                    @Override
                    public void onSuccess() {
                        Intent intent = new Intent(MainActivity.this, InstagramActivityData.class);
                        startActivity(intent);
                    }
                    @Override
                    public void onFail(String error) {
                        Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        /*
            Google plus authtication button listener
         */

        gPlusLogin.setOnClickListener(this);
        gPlusLogOut.setOnClickListener(this);

        /*
        Twitter login authtication callback handler
         */

        twitterLoginButton.setCallback(new LoginHandler());
        loginGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();

        /*
        Facebook login authetication callback manager
         */

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();
                Toast.makeText(MainActivity.this, "You are logged in", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, FaceBookDataActivity.class);
                startActivity(intent);
            }
            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "You have canceled the login process", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onError(FacebookException e) {
                Toast.makeText(MainActivity.this, "Unable to login", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Sign-in into google
     * */

    private void signInWithGplus() {
        if (!loginGoogleApiClient.isConnecting()) {
            gSignedIn = true;
            resolveSignInError();
        }
    }
    /**
     * Method to resolve any signin errors related to google plus
     * */

    private void resolveSignInError() {
        if (connectionResult.hasResolution()) {
            try {
                currentIntent = true;
                connectionResult.startResolutionForResult(this, 0);
            } catch (IntentSender.SendIntentException e) {
                currentIntent = false;
                loginGoogleApiClient.connect();
            }
        }
    }
    /**
     * Hanlding result after Google, Tweitter, Facebook, Dropbox etc login button click
     */
    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent i) {
        callbackManager.onActivityResult(reqCode, resCode, i);
        twitterLoginButton.onActivityResult(reqCode, resCode, i);
        if (dropboxAPI.getSession().authenticationSuccessful()) {
            try {
                dropboxAPI.getSession().finishAuthentication();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
        if (reqCode == 0) {
            if (resCode != RESULT_OK) {
                gSignedIn = false;
            }
            currentIntent = false;
            if (!loginGoogleApiClient.isConnecting()) {
                loginGoogleApiClient.connect();
            }
        }
    }

    public void getFbKeyHash(String packageName) {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(packageName,PackageManager.GET_SIGNATURES);
            for (Signature signature : packageInfo.signatures) {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
            }
        } catch (PackageManager.NameNotFoundException exception) {
            exception.printStackTrace();
        } catch (NoSuchAlgorithmException exception) {
            exception.printStackTrace();
        }
    }

    protected void onStart() {
        super.onStart();
        loginGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        if (loginGoogleApiClient.isConnected()) {
            loginGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        gSignedIn = false;
        Toast.makeText(this, "You are connected with gmail id", Toast.LENGTH_LONG).show();
        try {
            if (Plus.PeopleApi.getCurrentPerson(loginGoogleApiClient) != null) {
                Person userProfile = Plus.PeopleApi.getCurrentPerson(loginGoogleApiClient);
                String userName = userProfile.getDisplayName();
                String userPhotoUrl = userProfile.getImage().getUrl();
                String userEmail = Plus.AccountApi.getAccountName(loginGoogleApiClient);
                userPhotoUrl = userPhotoUrl.substring(0,userPhotoUrl.length() - 2)+ 400;
                String userAboutMe = null;
                if(userProfile.hasAboutMe()){
                    userAboutMe = userProfile.getAboutMe();
                } else{
                    userAboutMe = "Not available";
                }
                String user_Dob = userProfile.getBirthday();
                String location = userProfile.getCurrentLocation();
                String langauge = userProfile.getLanguage();
                Intent intent = new Intent(MainActivity.this, GooglePlusActivity.class);
                intent.putExtra("name",userName);
                intent.putExtra("email",userEmail);
                intent.putExtra("purl",userPhotoUrl);
                intent.putExtra("about",userAboutMe);
                intent.putExtra("dob",user_Dob);
                intent.putExtra("location",location);
                intent.putExtra("language",langauge);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(),"User information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        updateUI(true);
    }

    @Override
    public void onConnectionSuspended(int i) {
        loginGoogleApiClient.connect();
        updateUI(false);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }
        if (!currentIntent) {
            connectionResult = result;
            if (gSignedIn) {
                resolveSignInError();
            }
        }
    }

    /**
     * Updating the UI, showing/hiding buttons and profile layout
     * */

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            gPlusLogin.setVisibility(View.GONE);
            fbLoginButton.setVisibility(View.GONE);
            twitterLoginButton.setVisibility(View.GONE);
            gPlusLogOut.setVisibility(View.VISIBLE);
        } else {
            gPlusLogin.setVisibility(View.VISIBLE);
            gPlusLogOut.setVisibility(View.GONE);
            fbLoginButton.setVisibility(View.VISIBLE);
            twitterLoginButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Sign-out only from google
     * */
    private void signOutFromGplus() {
        if (loginGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(loginGoogleApiClient);
            loginGoogleApiClient.disconnect();
            loginGoogleApiClient.connect();
            updateUI(false);
        }
    }

    private AndroidAuthSession createDropboxSession() {
        AppKeyPair appKeyPair = new AppKeyPair(MainApplication.DROPBOX_APP_KEY,
                MainApplication.DROPBOX_APP_SECRET);
        AndroidAuthSession session;

        String[] stored = new String[2];
        stored[0] = MainApplication.DROPBOX_APP_KEY;
        stored[1] = MainApplication.DROPBOX_APP_SECRET;
        if (stored != null) {
            AccessTokenPair accessToken = new AccessTokenPair(stored[0],
                    stored[1]);
            session = new AndroidAuthSession(appKeyPair, MainApplication.ACCESS_TYPE,
                    accessToken);
        } else {
            session = new AndroidAuthSession(appKeyPair, MainApplication.ACCESS_TYPE);
        }

        return session;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signInWithGplus();
                break;
            case R.id.login_button:
                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("user_posts", "public_profile", "user_friends", "user_likes","user_events","read_stream", "user_about_me", "user_friends" ));
                break;
            case R.id.btn_sign_out:
                LoginManager.getInstance().logOut();
                signOutFromGplus();
                break;
            default:
                break;
        }
    }

    /**
     * inner class for twitter login using Fabric plugin
     */

    private class LoginHandler extends Callback<TwitterSession> {
        @Override
        public void success(Result<TwitterSession> twitterSessionResult) {
            TwitterSession session = twitterSessionResult.data;
            twitterApiClient = TwitterCore.getInstance().getApiClient(session);
            Intent intent = new Intent(MainActivity.this, TwitterDataActivity.class);
            startActivity(intent);
            Toast.makeText(MainActivity.this, "You are connected with twitter account", Toast.LENGTH_LONG).show();
        }
        @Override
        public void failure(TwitterException e) {
        }
    }
}
