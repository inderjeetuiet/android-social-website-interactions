package inderjeet.test.sociomantic.sociomantictest;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

/**
 * Created by isingh on 8/19/15.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity activity;
    private LoginButton fbLoginButton;
    private SignInButton gPlus;
    private TwitterLoginButton twitterLoginButton;

    private Intent fbIntent;
    private Intent twitterIntent;
    private Intent googleIntent;

    public MainActivityTest(){
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception{
        super.setUp();
        setActivityInitialTouchMode(true);
        activity = getActivity();
        fbLoginButton = (LoginButton) activity.findViewById(R.id.login_button);
        twitterLoginButton = (TwitterLoginButton) activity.findViewById(R.id.twitter_login_button);
        gPlus = (SignInButton) activity.findViewById(R.id.sign_in_button);
        fbIntent = new Intent(getInstrumentation().getTargetContext(), FaceBookDataActivity.class);
        twitterIntent = new Intent(getInstrumentation().getTargetContext(), TwitterDataActivity.class);
        googleIntent = new Intent(getInstrumentation().getTargetContext(), GooglePlusActivity.class);
    }

    public void testPreconditions() {
        assertNotNull("Activity is null", activity);
        assertNotNull("FB button is null", fbLoginButton);
        assertNotNull("Twitter button is null", twitterLoginButton);
        assertNotNull("Google Plus is null", gPlus);
    }

    @MediumTest
    public void testFbButton_layout() {
        final View decorView = activity.getWindow().getDecorView();
        ViewAsserts.assertOnScreen(decorView, fbLoginButton);

        final ViewGroup.LayoutParams layoutParams =fbLoginButton.getLayoutParams();
        assertNotNull(layoutParams);
        assertEquals(layoutParams.width, WindowManager.LayoutParams.MATCH_PARENT);
        assertEquals(layoutParams.height, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @MediumTest
    public void testGPButton_layout() {
        final View decorView = activity.getWindow().getDecorView();
        ViewAsserts.assertOnScreen(decorView, gPlus);

        final ViewGroup.LayoutParams layoutParams =gPlus.getLayoutParams();
        assertNotNull(layoutParams);
        assertEquals(layoutParams.width, WindowManager.LayoutParams.MATCH_PARENT);
        assertEquals(layoutParams.height, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @MediumTest
    public void testTwButton_layout() {
        final View decorView = activity.getWindow().getDecorView();
        ViewAsserts.assertOnScreen(decorView, twitterLoginButton);

        final ViewGroup.LayoutParams layoutParams =twitterLoginButton.getLayoutParams();
        assertNotNull(layoutParams);
        assertEquals(layoutParams.width, WindowManager.LayoutParams.MATCH_PARENT);
        assertEquals(layoutParams.height, WindowManager.LayoutParams.WRAP_CONTENT);
    }

}
