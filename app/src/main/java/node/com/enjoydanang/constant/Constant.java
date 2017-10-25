package node.com.enjoydanang.constant;



/**
 * Created by chientruong on 12/29/16.
 */

public class Constant {

    public static final String URL_HOST = "http://enjoyindanang.com/API/";
    public static final String URL_HOST_IMAGE = "http://enjoyindanang.com";

    public static String EXCHANGE_RATE_FORMAT = "1$ = %s VND";

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

    public enum ReportResult{
        NULL,
        REPORTED,
        NOREPORT;
    }
    public enum CheckRollCall{
        NULL,
        ROLLCALLED,
        NOROLLCALL;
    }

    public static final int CONNECT_TIME_OUT = 15000;

    public static final String FILE_NAME_LANGUAGE = "language.json";

    public static String EMBEB_YOUTUBE_FORMAT = "<html><body><iframe width=\"%d\" height=\"%d\" src=\"%s\" frameborder=\"0\" allowfullscreen></iframe></body></html>";

    public static final String TITLE_ERROR = "Error";

    public static final String TITLE_WARNING = "Warning";

    public static final String TITLE_SUCCESS = "Success";

}
