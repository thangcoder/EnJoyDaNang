package node.com.enjoydanang.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;

import node.com.enjoydanang.GlobalApplication;
import node.com.enjoydanang.utils.helper.LanguageHelper;

/**
 * Author: Tavv
 * Created on 29/11/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class LanguageReceiver extends BroadcastReceiver {
    private static final String TAG = LanguageReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String strLangFirstTime = GlobalApplication.getGlobalApplicationContext().getStrLanguage();
        String strLangChanged = LanguageHelper.getSystemLanguage();
        if(!StringUtils.equals(strLangChanged, strLangFirstTime)){
            EventBus.getDefault().post("hasChangeLanguage");
        }

    }
}
