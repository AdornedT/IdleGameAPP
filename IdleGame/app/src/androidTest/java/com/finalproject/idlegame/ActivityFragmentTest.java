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
public class ActivityFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivity = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void onStartup(){
        mActivity.getActivity();
    }

    @Test
    public void testStartScreen(){
        //Has to be visible
        onView(withId(R.id.button_start)).check(matches(isDisplayed()));

        //Has to not be visible
        onView(withId(R.id.button_back)).check(matches(not(isDisplayed())));
        onView(withId(R.id.button_divineWater_upgrade)).check(matches(not(isDisplayed())));
        onView(withId(R.id.button_mountainWater_upgrade)).check(matches(not(isDisplayed())));
        onView(withId(R.id.button_spicyWater_upgrade)).check(matches(not(isDisplayed())));
        onView(withId(R.id.button_buyFactory_upgrade)).check(matches(not(isDisplayed())));
        onView(withId(R.id.textView_factoriesNumber_upgrade)).check(matches(not(isDisplayed())));
        onView(withId(R.id.textView_factories_upgrade)).check(matches(not(isDisplayed())));
        onView(withId(R.id.textView_money_upgrade)).check(matches(not(isDisplayed())));
        onView(withId(R.id.textView_moneyValue_upgrade)).check(matches(not(isDisplayed())));

        onView(withId(R.id.button_reset_game)).check(matches(not(isDisplayed())));
        onView(withId(R.id.button_save)).check(matches(not(isDisplayed())));
        onView(withId(R.id.button_upgrades)).check(matches(not(isDisplayed())));
        onView(withId(R.id.button_musicPlay)).check(matches(not(isDisplayed())));
        onView(withId(R.id.button_musicPause)).check(matches(not(isDisplayed())));
        onView(withId(R.id.button_bottleWater)).check(matches(not(isDisplayed())));
        onView(withId(R.id.button_buyFactory)).check(matches(not(isDisplayed())));
        onView(withId(R.id.textView_factoriesNumber)).check(matches(not(isDisplayed())));
        onView(withId(R.id.textView_factories)).check(matches(not(isDisplayed())));
        onView(withId(R.id.textView_money)).check(matches(not(isDisplayed())));
        onView(withId(R.id.textView_moneyValue)).check(matches(not(isDisplayed())));
        onView(withId(R.id.text_music)).check(matches(not(isDisplayed())));
    }

    @Test
    public void testGameScreen(){
        //Change screen
        onView(withId(R.id.button_start)).perform(click());

        //Has to be visible
        onView(withId(R.id.button_reset_game)).check(matches((isDisplayed())));
        onView(withId(R.id.button_save)).check(matches((isDisplayed())));
        onView(withId(R.id.button_upgrades)).check(matches((isDisplayed())));
        onView(withId(R.id.button_musicPlay)).check(matches((isDisplayed())));
        onView(withId(R.id.button_musicPause)).check(matches((isDisplayed())));
        onView(withId(R.id.button_bottleWater)).check(matches((isDisplayed())));
        onView(withId(R.id.button_buyFactory)).check(matches((isDisplayed())));
        onView(withId(R.id.textView_factoriesNumber)).check(matches((isDisplayed())));
        onView(withId(R.id.textView_factories)).check(matches((isDisplayed())));
        onView(withId(R.id.textView_money)).check(matches((isDisplayed())));
        onView(withId(R.id.textView_moneyValue)).check(matches((isDisplayed())));
        onView(withId(R.id.text_music)).check(matches((isDisplayed())));

        //Has to not be visible
        onView(withId(R.id.button_start)).check(matches(not(isDisplayed())));

        onView(withId(R.id.button_back)).check(matches(not(isDisplayed())));
        onView(withId(R.id.button_divineWater_upgrade)).check(matches(not(isDisplayed())));
        onView(withId(R.id.button_mountainWater_upgrade)).check(matches(not(isDisplayed())));
        onView(withId(R.id.button_spicyWater_upgrade)).check(matches(not(isDisplayed())));
        onView(withId(R.id.button_buyFactory_upgrade)).check(matches(not(isDisplayed())));
        onView(withId(R.id.textView_factoriesNumber_upgrade)).check(matches(not(isDisplayed())));
        onView(withId(R.id.textView_factories_upgrade)).check(matches(not(isDisplayed())));
        onView(withId(R.id.textView_money_upgrade)).check(matches(not(isDisplayed())));
        onView(withId(R.id.textView_moneyValue_upgrade)).check(matches(not(isDisplayed())));
    }

    @Test
    public void testUpgradesScreen(){
        //Change screen
        onView(withId(R.id.button_start)).perform(click());
        onView(withId(R.id.button_upgrades)).perform(click());

        //Has to be visible
        onView(withId(R.id.button_back)).check(matches(isDisplayed()));
        onView(withId(R.id.button_divineWater_upgrade)).check(matches(isDisplayed()));
        onView(withId(R.id.button_mountainWater_upgrade)).check(matches(isDisplayed()));
        onView(withId(R.id.button_spicyWater_upgrade)).check(matches(isDisplayed()));
        onView(withId(R.id.button_buyFactory_upgrade)).check(matches(isDisplayed()));
        onView(withId(R.id.textView_factoriesNumber_upgrade)).check(matches(isDisplayed()));
        onView(withId(R.id.textView_factories_upgrade)).check(matches(isDisplayed()));
        onView(withId(R.id.textView_money_upgrade)).check(matches(isDisplayed()));
        onView(withId(R.id.textView_moneyValue_upgrade)).check(matches(isDisplayed()));

        //Has to not be visible
        onView(withId(R.id.button_start)).check(matches(not(isDisplayed())));

        onView(withId(R.id.button_reset_game)).check(matches(not(isDisplayed())));
        onView(withId(R.id.button_save)).check(matches(not(isDisplayed())));
        onView(withId(R.id.button_upgrades)).check(matches(not(isDisplayed())));
        onView(withId(R.id.button_musicPlay)).check(matches(not(isDisplayed())));
        onView(withId(R.id.button_musicPause)).check(matches(not(isDisplayed())));
        onView(withId(R.id.button_bottleWater)).check(matches(not(isDisplayed())));
        onView(withId(R.id.button_buyFactory)).check(matches(not(isDisplayed())));
        onView(withId(R.id.textView_factoriesNumber)).check(matches(not(isDisplayed())));
        onView(withId(R.id.textView_factories)).check(matches(not(isDisplayed())));
        onView(withId(R.id.textView_money)).check(matches(not(isDisplayed())));
        onView(withId(R.id.textView_moneyValue)).check(matches(not(isDisplayed())));
        onView(withId(R.id.text_music)).check(matches(not(isDisplayed())));
    }
}