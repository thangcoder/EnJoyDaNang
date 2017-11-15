package node.com.enjoydanang.utils.helper;

import node.com.enjoydanang.constant.Constant;

/**
 * Author: Tavv
 * Created on 15/11/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class DomainHelper {

    public enum DomainType {
        VN, KR
    }

    public DomainHelper(DomainType type) {
        switch (type) {
            case VN:
                Constant.URL_HOST_IMAGE = "http://enjoyindanang.vn";
                Constant.URL_HOST = "http://enjoyindanang.vn/API/";
                Constant.URL_FORGOT_PWD = "http://enjoyindanang.vn/Account/ForgotPassword";
                break;
            case KR:
                Constant.URL_HOST_IMAGE = "http://enjoyindanang.com";
                Constant.URL_HOST = "http://enjoyindanang.com/API/";
                Constant.URL_FORGOT_PWD = "http://enjoyindanang.com/Account/ForgotPassword";
                break;
            default:
                break;
        }
    }
}
