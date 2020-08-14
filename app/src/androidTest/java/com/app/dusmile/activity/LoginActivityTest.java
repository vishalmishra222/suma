package com.app.dusmile.activity;


import android.support.design.widget.TextInputLayout;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.app.dusmile.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void loginActivityTest() {


        ViewInteraction appCompatEditText12 = onView(
                allOf(withId(R.id.edtUsername), isDisplayed()));
        appCompatEditText12.perform(replaceText("FI001"), closeSoftKeyboard());

        ViewInteraction appCompatEditText13 = onView(
                allOf(withId(R.id.edtPassword), isDisplayed()));
        appCompatEditText13.perform(replaceText("sumasoft123"), closeSoftKeyboard());


        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.btnLogin), withText("Sign In"),
                        withParent(allOf(withId(R.id.activity_login),
                                withParent(withId(R.id.genericCardView))))));
        appCompatButton.perform(scrollTo(), click());

        ViewInteraction appCompatEditText14 = onView(
                allOf(withId(R.id.searchbox),
                        withParent(allOf(withId(R.id.searchBoxFrame),
                                withParent(withId(R.id.job_fragment)))),
                        isDisplayed()));
        appCompatEditText14.perform(replaceText("dtuidtyu"), closeSoftKeyboard());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.searchBoxCancel),
                        withParent(allOf(withId(R.id.searchBoxFrame),
                                withParent(withId(R.id.job_fragment)))),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatEditText15 = onView(
                allOf(withId(R.id.searchbox),
                        withParent(allOf(withId(R.id.searchBoxFrame),
                                withParent(withId(R.id.job_fragment)))),
                        isDisplayed()));
        appCompatEditText15.perform(click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("open drawer"),
                        withParent(withId(R.id.app_bar)),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        pressBack();

        ViewInteraction relativeLayout = onView(
                withId(R.id.linearFirst));
//        relativeLayout.perform(scrollTo(), click());

        ViewInteraction relativeLayout2 = onView(
                allOf(withId(R.id.linearSecond), isDisplayed()));
//        relativeLayout2.perform(click());

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.ImageView),
                        withParent(allOf(withId(R.id.jobIdLin),
                                withParent(withId(R.id.recordLin)))),
                        isDisplayed()));
//        appCompatImageView.perform(click());

        ViewInteraction editText3 = onView(
                withText("Select"));
//        editText3.perform(scrollTo(), click());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(android.R.id.text1), withText("Single"),
                        childAtPosition(
                                withId(R.id.dropdownList),
                                1),
                        isDisplayed()));
       // appCompatTextView.perform(click());

        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(android.R.id.text1), withText("Accept"),
                        childAtPosition(
                                withId(R.id.dropdownList),
                                1),
                        isDisplayed()));
//        appCompatTextView2.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.btnSave), withText("Save")));
    // appCompatButton3.perform( click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.confirm_button), withText("OK"), isDisplayed()));
      //  appCompatButton4.perform(click());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.btnSubmit), withText("Submit")));
      //  appCompatButton5.perform(scrollTo(), click());

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.btnUploadImage), withText("Upload Image")));
        //appCompatButton6.perform(scrollTo(), click());

        ViewInteraction appCompatEditText16 = onView(
                allOf(withId(R.id.dropdownSelectImage), withText("Select")));
       // appCompatEditText16.perform(scrollTo(), click());

        ViewInteraction appCompatTextView3 = onView(
                allOf(withId(android.R.id.text1), withText("HousePhoto2"),
                        childAtPosition(
                                withId(R.id.dropdownList),
                                1),
                        isDisplayed()));
       // appCompatTextView3.perform(click());

        ViewInteraction appCompatButton7 = onView(
                allOf(withId(R.id.btnBrowseImage), withText("Select Image")));
//        appCompatButton7.perform(scrollTo(), click());

        ViewInteraction appCompatTextView4 = onView(
                allOf(withId(android.R.id.text1), withText("Capture Photo"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                0),
                        isDisplayed()));
        //appCompatTextView4.perform(click());

        ViewInteraction appCompatButton8 = onView(
                allOf(withId(R.id.doneButton), withText("Done"), isDisplayed()));
      //  appCompatButton8.perform(click());

        ViewInteraction appCompatButton9 = onView(
                allOf(withId(R.id.btnBrowseImage), withText("Select Image")));
     //   appCompatButton9.perform(scrollTo(), click());

        ViewInteraction appCompatTextView5 = onView(
                allOf(withId(android.R.id.text1), withText("Capture Photo"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                0),
                        isDisplayed()));
       // appCompatTextView5.perform(click());

        ViewInteraction appCompatButton10 = onView(
                allOf(withId(R.id.doneButton), withText("Done"), isDisplayed()));
       // appCompatButton10.perform(click());

        ViewInteraction appCompatButton11 = onView(
                allOf(withId(R.id.btnUpload), withText("Upload")));
       // appCompatButton11.perform(scrollTo(), click());

        ViewInteraction relativeLayout3 = onView(
                withId(R.id.linearFirst));
       // relativeLayout3.perform(scrollTo(), click());

        ViewInteraction relativeLayout4 = onView(
                withId(R.id.linearFirst));
       // relativeLayout4.perform(scrollTo(), click());

        ViewInteraction relativeLayout5 = onView(
                allOf(withId(R.id.linearSecond), isDisplayed()));
       // relativeLayout5.perform(click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withContentDescription("open drawer"),
                        withParent(withId(R.id.app_bar)),
                        isDisplayed()));
        //appCompatImageButton2.perform(click());

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withContentDescription("open drawer"),
                        withParent(withId(R.id.app_bar)),
                        isDisplayed()));
       // appCompatImageButton3.perform(click());

        ViewInteraction appCompatImageButton4 = onView(
                allOf(withContentDescription("open drawer"),
                        withParent(withId(R.id.app_bar)),
                        isDisplayed()));
      //  appCompatImageButton4.perform(click());

        ViewInteraction relativeLayout6 = onView(
                withId(R.id.linearFirst));
      //  relativeLayout6.perform(scrollTo(), click());

        ViewInteraction relativeLayout7 = onView(
                allOf(withId(R.id.linearSecond), isDisplayed()));
      //  relativeLayout7.perform(click());

        ViewInteraction editText4 = onView(
                allOf(withText("20-02-2017"), isDisplayed()));
       // editText4.perform(click());

        ViewInteraction appCompatButton12 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        withParent(allOf(withClassName(is("com.android.internal.widget.ButtonBarLayout")),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
      //  appCompatButton12.perform(click());

        ViewInteraction button = onView(
                allOf(withText("Submit"),
                        withParent(withId(R.id.report_fragment)),
                        isDisplayed()));
       // button.perform(click());

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
