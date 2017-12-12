package node.com.enjoydanang.common;

import android.app.Activity;
import android.content.DialogInterface;
import android.net.http.SslError;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;

import org.greenrobot.eventbus.EventBus;

/**
 * Author: Tavv
 * Created on 31/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class Common {
    public static void onSslError(Activity activity, WebView view, final SslErrorHandler handler, SslError error) {
        if (activity == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("ssl The certificate is an incorrect page, can I open it?");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handler.proceed();
            }
        });
        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handler.cancel();
            }
        });
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    handler.cancel();
                    dialog.dismiss();
                    return true;
                }
                return false;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void registerEventBus(Object obj){
        if(!EventBus.getDefault().isRegistered(obj)){
            EventBus.getDefault().register(obj);
        }
    }

    public static void unregisterEventBus(Object obj){
        if(EventBus.getDefault().isRegistered(obj)){
            EventBus.getDefault().unregister(obj);
        }
    }
}