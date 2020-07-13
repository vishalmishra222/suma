package com.example.sumaforms.view;

import android.app.Activity;
import android.util.Patterns;

import java.util.regex.Pattern;

public class ValidateField extends Activity {
    String validationType;
    String validationPattern;

   public ValidateField(String validationType, String validationPattern){
        this.validationType = validationType;
        this.validationPattern = validationPattern;
    }

    public static boolean ValidateEmailId(String email){
        //String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        return emailPattern.matcher(email).matches();
    }

    public static boolean ValidateMobileNumber(String mobile){
        Pattern emailPattern = Patterns.PHONE;
        return emailPattern.matcher(mobile).matches();
    }

    public static boolean ValidateName(String name){
        String emailPattern = "[a-zA-Z ]+";
        return emailPattern.matches(name);
    }

    public static boolean Address(String address){
       String addressPattern = "[A-Za-z0-9'\\.\\-\\s\\,]";
       return  addressPattern.matches(address);
    }

    public static boolean validateDataField(String validationPattern,String text){
       String pattern = validationPattern;
       return pattern.matches(text);
    }

}
