package node.com.enjoydanang.utils.helper;

import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import node.com.enjoydanang.GlobalApplication;
import node.com.enjoydanang.constant.AppLanguage;
import node.com.enjoydanang.constant.Constant;

import static android.R.attr.key;

/**
 * Author: Tavv
 * Created on 22/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class LanguageHelper {

    public static String getValueByKey(String key) {
        JSONObject js = GlobalApplication.getGlobalApplicationContext().getJsLanguage();
        if (js != null) {
            try {
                return js.getString(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return StringUtils.EMPTY;
    }


    public static void getValueByViewId(View... views) throws JSONException {
        JSONObject js = GlobalApplication.getGlobalApplicationContext().getJsLanguage();
        if (views != null) {
            for (int i = 0; i < views.length; i++) {
                if (views[i] instanceof TextView) {
                    ((TextView) views[i]).setText(js.getString(((TextView) views[i]).getText() + ""));
                } else if (views[i] instanceof EditText) {
                    ((EditText) views[i]).setHint(js.getString(((EditText) views[i]).getHint() + ""));
                } else if (views[i] instanceof AppCompatButton) {
                    ((AppCompatButton) views[i]).setText(js.getString(((AppCompatButton) views[i]).getText() + ""));
                }
            }
        }
    }

    public static String getId(View view) {
        if (view.getId() == 0xffffffff) return "no-id";
        else return view.getResources().getResourceName(view.getId());
    }

    public static String[] getTitleMenuNormal() {
        JSONObject js = GlobalApplication.getGlobalApplicationContext().getJsLanguage();
        if (js != null) {
            try {
                return new String[]{js.getString(AppLanguage.Key.Information), js.getString(AppLanguage.Key.Contact_Us),
                        js.getString(AppLanguage.Key.Favorite), js.getString(AppLanguage.Key.Home_Account_LogCheck_in),
                        js.getString(AppLanguage.Key.Persional), js.getString(AppLanguage.Key.Home_Account_UpdateProfile),
                        js.getString(AppLanguage.Key.Home_Account_ChangePassword),
                        js.getString(AppLanguage.Key.Home_Account_Logout)};
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ;
        }
        return null;
    }

    public static String[] getTitleMenuNoLogin() {
        JSONObject js = GlobalApplication.getGlobalApplicationContext().getJsLanguage();
        if (js != null) {
            try {
                return new String[]{js.getString(AppLanguage.Key.Information), js.getString(AppLanguage.Key.Introduction), js.getString(AppLanguage.Key.Contact_Us),
                        js.getString(AppLanguage.Key.Persional), js.getString(AppLanguage.Key.Home_Account_Logout)};
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ;
        }
        return null;
    }
}
