package node.com.enjoydanang.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: Tavv
 * Created on 25/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class ValidUtils {
    static String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    static String emailpattern2 = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+\\.+[a-z]+";
    public static String terminal_id;

    public boolean validateEditTexts(EditText... testObj) {
        for (EditText aTestObj : testObj) {
            if (aTestObj.getText().toString().trim().equals(""))
                return false;
        }
        return true;
    }

    public boolean validateMobileNumber(EditText... testObj) {
        for (EditText aTestObj : testObj) {
            if (aTestObj.getText().toString().length() != 10)
                return false;
        }
        return true;
    }


    public boolean validateEmail(EditText... testObj) {
        for (EditText aTestObj : testObj) {
            if (!aTestObj.getText().toString().trim().matches(emailpattern) && !aTestObj.getText().toString().trim().matches(emailpattern2))
                return false;
        }
        return true;
    }

    public boolean validateForDigits(EditText testObj, int noOfDigits) {
        return testObj.getText().toString().length() == noOfDigits;
    }

    public void showToast(Context c, String s) {
        Toast.makeText(c, s, Toast.LENGTH_LONG).show();
    }


    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return conMan.getActiveNetworkInfo() != null && conMan.getActiveNetworkInfo().isConnected();
    }

    public boolean isValidConfirmPasswrod(String confirmPassword, String password) {
        return confirmPassword.equals(password);
    }

    public boolean isValidAddress(String address) {
        return !(address == null || address.equals(""));
    }

    public boolean isValidPincode(String pincode) {
        if (pincode == null) {
            return false;
        } else {
            String PINCODE_PATTERN = "^[0-9]{6}$";

            Pattern pattern = Pattern.compile(PINCODE_PATTERN);
            Matcher matcher = pattern.matcher(pincode);
            return matcher.matches();
        }
    }


    public boolean isValidMobile(String mobile) {
        Pattern p = Pattern.compile("^[789]\\d{9,9}$");
        if (mobile == null) {
            return false;
        } else {
            Matcher m = p.matcher(mobile);
            return m.matches();
        }
    }

    public boolean isValidPassword(String password) {
        Pattern p = Pattern.compile("((?!\\s)\\A)(\\s|(?<!\\s)\\S){8,20}\\Z");
        if (password == null) {
            return false;
        } else {
            Matcher m = p.matcher(password);
            return m.matches();
        }
    }

    public boolean isValidEmail(String email) {
        return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean isValidLastName(String lastName) {
        Pattern p = Pattern.compile("^[a-zA-Z]{3,20}$");
        if (lastName == null) {
            return false;
        } else {
            Matcher m = p.matcher(lastName);
            return m.matches();
        }
    }

    public boolean isValidFirstName(String firstname) {
        Pattern p = Pattern.compile("^[a-zA-Z]{3,20}$");
        if (firstname == null) {
            return false;
        } else {
            Matcher m = p.matcher(firstname);
            return m.matches();
        }
    }

    public boolean isValidAge(String age) {
        Pattern p = Pattern.compile("^[1-9]{1,3}$");
        if (age == null || age.equals("")) {
            return false;
        } else {
            Matcher m = p.matcher(age);
            return m.matches();
        }
    }

    public boolean isEmptyEditText(String s) {
        return !(s == null || s.equals(""));
    }


}
