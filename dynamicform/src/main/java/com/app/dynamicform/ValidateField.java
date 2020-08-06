package com.app.dynamicform;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Patterns;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateField extends Activity {
    String validationType;
    String validationPattern;

    public ValidateField(String validationType, String validationPattern) {
        this.validationType = validationType;
        this.validationPattern = validationPattern;
    }

    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    public static boolean ValidateEmailId(String email) {
        //String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        return emailPattern.matcher(email).matches();
    }

    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    public static boolean ValidateMobileNumber(String mobile) {
        Pattern emailPattern = Patterns.PHONE;
        return emailPattern.matcher(mobile).matches();
    }

    public static boolean ValidateName(String name) {
        String emailPattern = "[a-zA-Z ]+";
        return emailPattern.matches(name);
    }

    public static boolean Address(String address) {
        String addressPattern = "[A-Za-z0-9'\\.\\-\\s\\,]";
        return addressPattern.matches(address);
    }

    public static boolean validateDataField(String expression, String text) {
        Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    public static boolean validateNumberRange(int text, int minRange, int maxRange) {
        boolean validate = false;
        if (text > minRange && text < maxRange) {
           validate = true;
        }
        return validate;
    }
}
