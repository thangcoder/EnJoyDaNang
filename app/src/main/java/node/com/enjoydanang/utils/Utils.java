package node.com.enjoydanang.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import node.com.enjoydanang.GlobalApplication;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.Constant;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.ui.activity.splash.ScreenSplashActivity;
import node.com.enjoydanang.utils.helper.LanguageHelper;

/**
 * Author: Tavv
 * Created on 09/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class Utils {


    public static int getColorRes(int colorId) {
        return ContextCompat.getColor(GlobalApplication.getGlobalApplicationContext(), colorId);
    }

    public static int getDimensRes(Context context, int dimens) {
        return context.getResources().getDimensionPixelSize(dimens);
    }

    public static String getString(int stringId) {
        return GlobalApplication.getGlobalApplicationContext().getString(stringId);
    }

    public static String getColorTransparent(int baseColor, int percent) {
        String colorPrimary = Integer.toHexString(ContextCompat.getColor(GlobalApplication.getGlobalApplicationContext(), baseColor));
        colorPrimary = colorPrimary.substring(2).toUpperCase();
        String hexTransparent;
        switch (percent) {
            case 0:
                hexTransparent = "#00";
                break;
            case 5:
                hexTransparent = "#0D";
                break;
            case 10:
                hexTransparent = "#1A";
                break;
            case 15:
                hexTransparent = "#26";
                break;
            case 20:
                hexTransparent = "#33";
                break;
            case 25:
                hexTransparent = "#40";
                break;
            case 30:
                hexTransparent = "#4D";
                break;
            case 35:
                hexTransparent = "#59";
                break;
            case 40:
                hexTransparent = "#66";
                break;
            case 45:
                hexTransparent = "#73";
                break;
            case 50:
                hexTransparent = "#80";
                break;
            case 55:
                hexTransparent = "#8C";
                break;
            case 60:
                hexTransparent = "#99";
                break;
            case 65:
                hexTransparent = "#A6";
                break;
            case 70:
                hexTransparent = "#B3";
                break;
            case 75:
                hexTransparent = "#BF";
                break;
            case 80:
                hexTransparent = "#CC";
                break;
            case 85:
                hexTransparent = "#D9";
                break;
            case 90:
                hexTransparent = "#E6";
                break;
            case 95:
                hexTransparent = "#F2";
                break;
            case 100:
                hexTransparent = "#FF";
                break;
            default:
                hexTransparent = "#FF";
                break;
        }
        return hexTransparent + colorPrimary;
    }

    public static <T> boolean isNotEmptyContent(Repository<T> lstModel) {
        return lstModel != null
                && StringUtils.endsWithIgnoreCase(lstModel.getStatus(), Constant.MSG_SUCCESS)
                && CollectionUtils.isNotEmpty(lstModel.getData());
    }

    public static <T> boolean isResponseError(Repository<T> repository) {
        return StringUtils.endsWithIgnoreCase(repository.getStatus(), Constant.MSG_FAILURE) ||
                StringUtils.endsWithIgnoreCase(repository.getStatus(), Constant.MSG_WARNING);
    }

    public static String reFormatYoutubeUrl(int width, int height, String url) {
        if (StringUtils.isNotBlank(url)) {
            return String.format(Locale.getDefault(), Constant.EMBEB_YOUTUBE_FORMAT, width, height, url);
        }
        return StringUtils.EMPTY;
    }

    public static String getIframeVideoPlay(String videoId, int height) {
        return "<iframe class=\"youtube-player\" " + "style=\"border: 0; width: 100%; height: " + height + ";"
                + "padding:0px; margin:0px\" " + "id=\"ytplayer\" type=\"text/html\" "
                + "src=\"http://www.youtube.com/embed/" + videoId
                + "?&theme=dark&autohide=2&modestbranding=1&showinfo=0&autoplay=1\fs=0\" frameborder=\"0\" "
                + "allowfullscreen </iframe>\n";
    }

    public static void clearForm(EditText... editTexts) {
        for (EditText editText : editTexts) {
            if (editText != null) {
                editText.setText(StringUtils.EMPTY);
            }
        }
    }

    public static int getStatusBarHeight() {
        Context context = GlobalApplication.getGlobalApplicationContext();
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static String getFullName(String firstName, String lastName, String fullName) {
        if (StringUtils.isNotEmpty(fullName)) {
            return fullName.trim();
        }
        return StringUtils.isNotEmpty(firstName) || StringUtils.isNotEmpty(lastName) ? (firstName.trim() + " " + lastName.trim()).trim() : StringUtils.EMPTY;
    }

    public static String getImageNormalOrSocial(String url) {
        if (StringUtils.isNotEmpty(url) && url.matches(Constant.REGEX_URL)) {
            return url;
        }
        return StringUtils.isNotEmpty(url) ? Constant.URL_HOST_IMAGE + url : url;
    }

    public static UserInfo getUserInfo() {
        return GlobalApplication.getUserInfo() != null ? GlobalApplication.getUserInfo() : new UserInfo();
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static boolean hasLogin() {
        return GlobalApplication.getUserInfo() != null;
    }

    public static String formatDate(String patternInput, String patternOutput, String dateTime) {
        if (StringUtils.isNotEmpty(dateTime)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(patternInput, Locale.getDefault());
            try {
                Date date = simpleDateFormat.parse(dateTime);
                simpleDateFormat.applyPattern(patternOutput);
                return simpleDateFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return StringUtils.EMPTY;
    }

    public static String getUriMapsDirection(double latitudeFirst, double longtitudeFirst, double latitudeSecond, double longtitudeSecond) {
        return "http://maps.google.com/maps?f=d&hl=en&saddr=" + latitudeFirst + "," + longtitudeFirst + "&daddr=" + latitudeSecond + "," + longtitudeSecond;
    }


    public static void startIntentMap(Context context, String url) {
        Intent navigationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(Intent.createChooser(navigationIntent, "Select an application"));
    }

    public static void startIntentMaps(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setComponent(new ComponentName("com.google.android.apps.maps",
                "com.google.android.maps.MapsActivity"));
        context.startActivity(intent);
    }

    public static String getLanguageByResId(@StringRes int strResId) {
        return LanguageHelper.getValueByKey(getString(strResId));
    }

    public static Map<String, String> getStartEndDateOfCurrentMonth(int dayCondition) {
        Map<String, String> dateMap = new HashMap<>();
        Calendar calander = Calendar.getInstance();
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        dateMap.put(Constant.TO_DATE, sdf.format(calander.getTime()));
        int date = calander.get(Calendar.DAY_OF_MONTH);
        int cMonth = calander.get(Calendar.MONTH) + 1;
        int cYear = calander.get(Calendar.YEAR);
        calander.set(Calendar.YEAR, cYear);
        calander.set(Calendar.MONTH, date < dayCondition ? cMonth - 2 : cMonth - 1);
        calander.set(Calendar.DAY_OF_MONTH, 1);
        dateMap.put(Constant.FROM_DATE, sdf.format(calander.getTime()));
        return dateMap;
    }

    public static Map<String, String> getStartEndDateOfCurrentMonth() {
        Map<String, String> dateMap = new HashMap<>();
        Calendar calander = Calendar.getInstance();
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        dateMap.put(Constant.TO_DATE, sdf.format(calander.getTime()));
        int cMonth = calander.get(Calendar.MONTH);
        int cYear = calander.get(Calendar.YEAR);
        calander.set(Calendar.YEAR, cYear);
        calander.set(Calendar.MONTH, cMonth);
        calander.set(Calendar.DAY_OF_MONTH, 1);
        dateMap.put(Constant.FROM_DATE, sdf.format(calander.getTime()));
        return dateMap;
    }

    public static String formatCurrency(@Nullable String pattern, double input) {
        DecimalFormat dfnd = new DecimalFormat(StringUtils.isBlank(pattern) ? "#,###" : pattern);
        return dfnd.format(input);
    }

    public static void redirectStore(Activity activity, String updateUrl) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public static void restartApp() {
        Intent i = GlobalApplication.getGlobalApplicationContext().getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(GlobalApplication.getGlobalApplicationContext().getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        GlobalApplication.getGlobalApplicationContext().startActivity(i);
    }

    public static void backToSplashScreen() {
        Intent mStartActivity = new Intent(GlobalApplication.getGlobalApplicationContext(), ScreenSplashActivity.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(GlobalApplication.getGlobalApplicationContext(), mPendingIntentId, mStartActivity,
                PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) GlobalApplication.getGlobalApplicationContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }

    public static boolean hasSessionLogin(){
        return SharedPrefsUtils.checkPrefs(Constant.SHARED_PREFS_NAME, Constant.KEY_EXTRAS_USER_INFO);
    }
}
