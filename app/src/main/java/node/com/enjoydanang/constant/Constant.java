package node.com.enjoydanang.constant;


import node.com.enjoydanang.R;

/**
 * Created by chientruong on 12/29/16.
 */

public class Constant {

    public static final String URL_HOST = "http://enjoyindanang.com/API/";
    public static final String URL_HOST_IMAGE = "http://enjoyindanang.com";
    public static final String URL_FORGOT_PWD = "http://enjoyindanang.com/Account/ForgotPassword";

    public static final String URL_DIRECTION_MAPS = "http://maps.google.com/maps?f=d&hl=en&saddr=%1$,.2f,%1$,.2f&daddr=%1$,.2f,%1$,.2f";

    public static String EXCHANGE_RATE_FORMAT = "1$ = %s VND";

    public static final String DATE_SERVER_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    public static final int SPLASH_TIME_OUT = 1000;

    public static final String DATE_FORMAT_DMY = "dd-MM-yyyy";

    public static final String REGEX_URL = "((http)[s]?(://).*)";

    public static final String API_OK = "200";
    public static final String API_401 = "401";
    public static final String API_402 = "402";
    public static final String API_403 = "403";
    public static final String API_404 = "404";
    public static final String API_405 = "405";
    public static final String API_501 = "501";
    public static final String API_502 = "502";
    public static final String MSG_SUCCESS = "success";
    public static final String MSG_WARNING = "warning";
    public static final String MSG_FAILURE = "fail";

    public static final int CONNECT_TIME_OUT = 15000;

    public static final String FILE_NAME_LANGUAGE = "language.json";

    public static String EMBEB_YOUTUBE_FORMAT = "<html><body><iframe width=\"100%\" height=\"%d\" src=\"%s\" frameborder=\"0\" allowfullscreen></iframe></body></html>";

    public static final String TITLE_ERROR = "Error";

    public static final String TITLE_WARNING = "Warning";

    public static final String TITLE_SUCCESS = "Success";


    public static final int[] ICON_MENU_NORMAL = new int[]{0, R.drawable.ic_introduction, R.drawable.ic_contact, R.drawable.ic_favorite,
            R.drawable.ic_log_checkin, 0, R.drawable.ic_profile, R.drawable.ic_change_password, R.drawable.ic_logout};

    public static final int[] ICON_MENU_NO_LOGIN = new int[]{0, R.drawable.ic_introduction, R.drawable.ic_contact, 0, R.drawable.ic_logout};

    public static final Integer[] INDEX_HEADER_NORMAL = new Integer[]{0, 5};

    public static final Integer[] INDEX_HEADER_NO_LOGIN = new Integer[]{0, 3};

    public static final String FROM_DATE  = "FromDate";
    public static final String TO_DATE  = "ToDate";

    public static final int MAX_SIZE_GALLERY_SELECT = 3;

    public static final int FETCH_UPDATE_TIME = 0; // fetch every 1h
}
