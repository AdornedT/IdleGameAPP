package com.finalproject.idlegame;

import static androidx.core.content.ContextCompat.*;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

import android.app.ActivityManager;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivity = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void onStartup(){
        mActivity.getActivity();
    }

    @Test
    public void bottleWaterTest(){
        //Change screen
        onView(withId(R.id.button_start)).perform(click());

        //Reset game
        onView(withId(R.id.button_reset_game)).perform(click());

        //Click button
        onView(withId(R.id.button_bottleWater)).perform(click());

        onView(withId(R.id.textView_moneyValue)).check(matches(withText("1.62")));
    }
}