package com.epicodus.restaurants;

import android.support.test.rule.ActivityTestRule;

import com.epicodus.restaurants.ui.LoginActivity;
import com.epicodus.restaurants.ui.MainActivity;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.lang.Thread.sleep;

/**
 * Created by Guest on 4/7/16.
 */
public class LoginActivityInstrumentationTest {
    @Rule
    public ActivityTestRule<LoginActivity> activityTestRule = new ActivityTestRule<>(LoginActivity.class);


    @Test
    public void validateLoginForm() {
        onView(withId(R.id.emailEditText)).perform(typeText("hello@world.com"));
        onView(withId(R.id.passwordEditText)).perform(typeText("1234")).perform(closeSoftKeyboard());
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.passwordLoginButton)).perform(click());
        onView(withId(R.id.appNameTextView)).check(matches(withText("My Restaurants")));
    }

}
