package node.com.enjoydanang.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.EditText;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

import cn.refactor.lib.colordialog.PromptDialog;
import node.com.enjoydanang.GlobalApplication;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.Constant;

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

    public static String getHtmlfromVideoId(String videoId) {
        String html = "<iframe class=\"youtube-player\" " + "style=\"border: 0; width: 100%; height: 96%;"
                + "padding:0px; margin:0px\" " + "id=\"ytplayer\" type=\"text/html\" "
                + "src=\"http://www.youtube.com/embed/" + videoId
                + "?&theme=dark&autohide=2&modestbranding=1&showinfo=0&autoplay=1\fs=0\" frameborder=\"0\" "
                + "allowfullscreen autobuffer " + "controls onclick=\"this.play()\">\n" + "</iframe>\n";
        return html;
    }

    /**
     * @param context Context
     * @param type    {DIALOG_TYPE_INFO : 0, DIALOG_TYPE_HELP : 1, DIALOG_TYPE_WRONG : 2, DIALOG_TYPE_SUCCESS : 3, DIALOG_TYPE_WARNING : 4, DIALOG_TYPE_DEFAULT }
     * @param title   Title dialog
     * @param msg     Message want to display
     */
    public static void showDialog(Context context, int type, String title, String msg) {
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

}
