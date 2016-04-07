package com.epicodus.restaurants;

import android.content.Intent;
import android.os.Build;
import android.view.Menu;
import android.widget.TextView;

import com.epicodus.restaurants.ui.MainActivity;
import com.epicodus.restaurants.ui.RestaurantsListActivity;
import com.epicodus.restaurants.ui.TestActivity;
import com.firebase.client.Firebase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)
public class MainActivityTest {
    private MainActivity activity;

    @Before
    public void setup() {
        activity = Robolectric.setupActivity(MainActivity.class);
    }

    @Test
    public void validateTextViewContent() {
        TextView appNameTextView = (TextView) activity.findViewById(R.id.appNameTextView);
        assertTrue("My Restaurants".equals(appNameTextView.getText().toString()));
    }

    @Test
    public void secondActivityStarted() {
        Firebase ref = MyRestaurantsApplication.getAppInstance().getFirebaseRef();
        activity.findViewById(R.id.findRestaurantsButton).performClick();
        Intent expectedIntent = new Intent(activity, RestaurantsListActivity.class);
        ShadowActivity shadowActivity = org.robolectric.Shadows.shadowOf(activity);
        Intent actualIntent = shadowActivity.getNextStartedActivity();
        if (ref.getAuth() != null) {
            assertTrue(actualIntent.filterEquals(expectedIntent));
        }
    }

    @Test
    public void loginActivityStarted() {
        Firebase ref = MyRestaurantsApplication.getAppInstance().getFirebaseRef();
        activity.findViewById(R.id.findRestaurantsButton).performClick();
        Intent expectedIntent = new Intent(activity, RestaurantsListActivity.class);
        ShadowActivity shadowActivity = org.robolectric.Shadows.shadowOf(activity);
        Intent actualIntent = shadowActivity.getNextStartedActivity();
        if (ref.getAuth() != null) {
            assertTrue(actualIntent.filterEquals(expectedIntent));
        }
    }

//    @Test
//    public void logoutActivity() {
//        Menu menu =(Menu) activity.findViewById(R.menu.menu_main);
//        activity.findViewById(R.id.action_logout).performClick();
//        Firebase ref = MyRestaurantsApplication.getAppInstance().getFirebaseRef();
//        assertNull(ref);
//    }

}