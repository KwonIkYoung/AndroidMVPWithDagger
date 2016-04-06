package com.github.dubulee.samples.imagesearch.home.view;

import android.content.Intent;
import android.net.Uri;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;


import com.github.dubulee.samples.imagesearch.home.presenter.HomePresenter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Matchers.eq;

//AndroidJUnit - 안드로이드 특화된 JUnit Rule
@RunWith(AndroidJUnit4.class)
public class HomeActivityTest {

    @Rule
    public IntentsTestRule<HomeActivity> rule = new IntentsTestRule<>(HomeActivity.class);
    private HomePresenter.View view;

    @Before
    public void setUp() throws Exception {
        view = rule.getActivity();

    }

    @Test
    public void testOnMoveLink() throws Throwable {
        String text = "http://www.coupang.com";
        rule.runOnUiThread(() -> view.onMoveLink(text));

        Intents.intending(IntentMatchers.hasAction(eq(Intent.ACTION_VIEW)));
        Intents.intending(IntentMatchers.hasData(eq(Uri.parse(text))));
    }
}