package node.com.enjoydanang.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import node.com.enjoydanang.GlobalApplication;
import node.com.enjoydanang.constant.Constant;

import static android.R.attr.type;

/**
 * Author: Tavv
 * Created on 09/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class JsonUtils {
    public static JSONObject getJsonResponse(String link) throws IOException, JSONException {
        StringBuilder sBuilder = new StringBuilder();
        String result = "";
        URL url = new URL(link);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(Constant.CONNECT_TIME_OUT);
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),
                "UTF-8"), 8);
        String line = null;
        while ((line = reader.readLine()) != null) {
            sBuilder.append(line + "\n");
        }
        result = sBuilder.toString();
        return new JSONObject(result);
    }

    public static <M> M convertJsonToObject(String json, Class<M> typeInput) {
        if (StringUtils.isBlank(json)) return null;
        Gson gson = new Gson();
        return gson.fromJson(json, typeInput);
    }
}
