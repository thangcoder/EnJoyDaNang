package node.com.enjoydanang.model;

import node.com.enjoydanang.utils.Utils;

/**
 * Author: Tavv
 * Created on 14/12/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class InfoWindow {

    private int partnerId;

    private String partnerName;

    private String address;

    private String image;


    public InfoWindow(int partnerId, String partnerName, String address, String image) {
        this.partnerId = partnerId;
        this.partnerName = partnerName;
        this.address = address;
        this.image = image;
    }


    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return Utils.getImageNormalOrSocial(image);
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(int partnerId) {
        this.partnerId = partnerId;
    }
}
