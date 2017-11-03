package node.com.enjoydanang.utils.helper;

import android.content.Context;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Author: Tavv
 * Created on 03/11/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class SoftKeyboardManager {
    public static void showSoftKeyboard(Context context, View view) {
        showSoftKeyboard(context, view, InputMethodManager.SHOW_FORCED);
    }

    public static void showSoftKeyboard(Context context, View view, int flags) {
        showSoftKeyboard(context, view, flags, null);
    }

    public static void showSoftKeyboard(Context context, View view, int flags, ResultReceiver resultReceiver) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(view, flags, resultReceiver);
    }

    public static void hideSoftKeyboard(Context context, IBinder token) {
        hideSoftKeyboard(context, token, InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    public static void hideSoftKeyboard(Context context, IBinder token, int flags) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(token, flags);
    }

    public static void toggleSoftKeyboard(Context context, int showFlags, int hideFlags) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(showFlags, hideFlags);
    }
}
