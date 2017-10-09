package node.com.enjoydanang.constant;



/**
 * Created by chientruong on 12/29/16.
 */

public class Constant {

    public static final String URL_HOST = "http://172.16.1.100:3000";

    public static final String API_OK = "200";
    public static final String API_401 = "401";
    public static final String API_402 = "402";
    public static final String API_403 = "403";
    public static final String API_404 = "404";
    public static final String API_405 = "405";
    public static final String API_501 = "501";
    public static final String API_502 = "502";
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
}
