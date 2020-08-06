package com.app.dynamicform.dynamicFields;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.dynamicform.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextViewClass {

    //create UI for textView
    public static TextView createTextView(Context mContext, String text, LinearLayout parent, int textColor, float textSize, String headerType, String tag, String customerName) {
        TextView textView = new TextView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        textView.setLayoutParams(params);
        textView.setTextColor(textColor);
        textView.setTextSize(textSize);
        textView.setTag(tag);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        params.setMargins(0, 0, 0, 0);
        if (text == null) {
            text = "";
        }
        if (headerType.equalsIgnoreCase("tabs")) {
            textView.setText("View Job Details");
            textView.setGravity(Gravity.CENTER);
            textView.setTypeface(null, Typeface.BOLD);
        } else if (headerType.equalsIgnoreCase("form")) {
            if (text.equalsIgnoreCase("Applicant Profile") || text.equalsIgnoreCase("Employment") || text.equalsIgnoreCase("Office") || text.equalsIgnoreCase("Business")) {
                textView.setText(text + " : " + customerName);
            } else if (text.equalsIgnoreCase("CoApplicant Profile") || text.equalsIgnoreCase("CoApplicant Office") || text.equalsIgnoreCase("CoApplicant Business")) {
            } else {
                textView.setText(text);
            }
            textView.setGravity(Gravity.LEFT);
            textView.setTypeface(null, Typeface.BOLD);
            textView.setTextColor(Color.WHITE);
            textView.setPadding(20, 0, 0, 0);
        } else if (headerType.equalsIgnoreCase("Job")) {
            textView.setText(text);
            textView.setGravity(Gravity.CENTER);
            textView.setTypeface(null, Typeface.BOLD);
        } else if (headerType.equalsIgnoreCase("fieldHeader")) {
            textView.setText(text);
            textView.setTypeface(null, Typeface.BOLD);
            textView.setGravity(Gravity.LEFT);
            textView.setTextColor(mContext.getResources().getColor(R.color.accent));
        } else if (headerType.equalsIgnoreCase("label")) {
            textView.setText(text);
            textView.setGravity(Gravity.LEFT);
        } else {
            textView.setText(text);
            textView.setGravity(Gravity.LEFT);
        }
        parent.addView(textView);
        return textView;
    }

    public static boolean validate(String expression, String text) {
        Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

}
