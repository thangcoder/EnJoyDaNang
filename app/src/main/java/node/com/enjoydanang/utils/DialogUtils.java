package node.com.enjoydanang.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import org.apache.commons.lang3.StringUtils;

import cn.refactor.lib.colordialog.ColorDialog;
import cn.refactor.lib.colordialog.PromptDialog;
import node.com.enjoydanang.R;
import node.com.enjoydanang.annotation.DialogType;

import static node.com.enjoydanang.utils.Utils.getString;

/**
 * Author: Tavv
 * Created on 29/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class DialogUtils {

    /**
     * @param context    Context
     * @param type {DIALOG_TYPE_INFO : 0, DIALOG_TYPE_HELP : 1, DIALOG_TYPE_WRONG : 2, DIALOG_TYPE_SUCCESS : 3, DIALOG_TYPE_WARNING : 4, DIALOG_TYPE_DEFAULT }
     * @param title      Title dialog
     * @param msg        Message want to display
     */
    public static void showDialog(Context context,@DialogType int type, String title, String msg) {
        new PromptDialog(context)
                .setDialogType(type)
                .setAnimationEnable(true)
                .setTitleText(title)
                .setContentText(msg)
                .setPositiveListener(getString(android.R.string.ok), new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }


    /**
     * @param context Context
     * @param type    {DIALOG_TYPE_INFO : 0, DIALOG_TYPE_HELP : 1, DIALOG_TYPE_WRONG : 2, DIALOG_TYPE_SUCCESS : 3, DIALOG_TYPE_WARNING : 4, DIALOG_TYPE_DEFAULT }
     * @param title   Title dialog
     * @param msg     Message want to display
     */
    public static void showDialog(Context context, int type, String title, String msg, PromptDialog.OnPositiveListener positiveListener) {
        new PromptDialog(context)
                .setDialogType(type)
                .setAnimationEnable(true)
                .setTitleText(title)
                .setContentText(msg)
                .setPositiveListener(getString(android.R.string.ok), positiveListener).show();
    }

    /**
     * @param context            Context
     * @param title              title dialog
     * @param content            content dialog
     * @param titleBtnPositive   title button Positive
     * @param titleBtnNegative   title button Negative
     * @param onPositiveListener Event positive click
     * @param onNegativeListener Event negative click
     */
    public static void showDialogConfirm(Context context, String title, String content,
                                         String titleBtnPositive, String titleBtnNegative,
                                         ColorDialog.OnPositiveListener onPositiveListener,
                                         ColorDialog.OnNegativeListener onNegativeListener) {
        ColorDialog colorDialog = new ColorDialog(context);
        colorDialog.setTitle(title);
        colorDialog.setContentText(content);
        colorDialog.setPositiveListener(titleBtnPositive, onPositiveListener);
        colorDialog.setNegativeListener(titleBtnNegative, onNegativeListener);
        colorDialog.show();
    }


    /**
     * @param context            Context
     * @param title              title dialog
     * @param content            content dialog
     * @param titleBtnPositive   title button Positive
     * @param titleBtnNegative   title button Negative
     * @param image              image
     * @param onPositiveListener Event positive click
     * @param onNegativeListener Event negative click
     */
    public static void showDialogConfirm(Context context, String title, String content,
                                         String titleBtnPositive, String titleBtnNegative,
                                         Drawable image,
                                         ColorDialog.OnPositiveListener onPositiveListener,
                                         ColorDialog.OnNegativeListener onNegativeListener) {

        ColorDialog colorDialog = new ColorDialog(context);
        colorDialog.setTitle(title);
        colorDialog.setContentText(content);
        colorDialog.setContentImage(image);
        colorDialog.setPositiveListener(titleBtnPositive, onPositiveListener);
        colorDialog.setNegativeListener(titleBtnNegative, onNegativeListener);
        colorDialog.show();
    }

    /**
     * @param context        Context
     * @param layoutId       layout resource id
     * @param title          title dialog
     * @param btnOkRes       button Ok resource id
     * @param btnCancelRes   button Cancel resource id
     * @param okListener     Event positive click
     * @param cancelListener Event negative click
     * @param hasTitle       boolean hasTitle
     */
    public static void showAlertDialogCustom(Context context, @LayoutRes int layoutId, String title,
                                             @IdRes int btnOkRes, @IdRes int btnCancelRes,
                                             View.OnClickListener okListener,
                                             View.OnClickListener cancelListener, boolean hasTitle) {
        AlertDialog alertDialog = null;
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(layoutId, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        if (hasTitle) {
            builder.setTitle(title);
        } else {
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        alertDialog = builder.create();
        AppCompatButton btnOk = (AppCompatButton) dialogView.findViewById(btnOkRes);
        AppCompatButton btnCancel = (AppCompatButton) dialogView.findViewById(btnCancelRes);
        btnOk.setOnClickListener(okListener);
        btnCancel.setOnClickListener(cancelListener);
        alertDialog.show();
    }

    /**
     * @param type type : 1 - Success, 2 - Warning, 3 - Wrong
     * @return String title
     */

    public static String getTitleDialog(int type) {
        switch (type) {
            case 1:
                return Utils.getLanguageByResId(R.string.Dialog_Title_Success);
            case 2:
                return Utils.getLanguageByResId(R.string.Dialog_Title_Warning);
            case 3:
                return Utils.getLanguageByResId(R.string.Dialog_Title_Wrong);
            default:
                return StringUtils.EMPTY;
        }
    }
}
