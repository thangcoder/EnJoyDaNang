package node.com.enjoydanang.utils.helper;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import node.com.enjoydanang.GlobalApplication;

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
}
