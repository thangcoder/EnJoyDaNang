package node.com.enjoydanang.constant;

/**
 * Author: Tavv
 * Created on 13/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public enum LoginType {

    FACEBOOK("Facebook"), GOOGLE("Google"), KAKAOTALK("Kakaotalk");

    private String strValue;

    LoginType(String strValue) {
        this.strValue = strValue;
    }

    @Override
    public String toString(){
        return strValue;
    }
}
