package node.com.enjoydanang.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

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

    public static <T, B> boolean isNotEmptyContent(Repository<T> lstModel) {
        return lstModel != null
                && StringUtils.endsWithIgnoreCase(lstModel.getStatus(), Constant.MSG_SUCCESS)
                && CollectionUtils.isNotEmpty(lstModel.getData());
    }

}
