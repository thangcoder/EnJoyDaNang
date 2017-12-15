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

    private String distance;

    private String category;

    public InfoWindow(int partnerId, String partnerName, String address, String image, String distance, String category) {
        this.partnerId = partnerId;
        this.partnerName = partnerName;
        this.address = address;
        this.image = image;
        this.distance = distance;
        this.category = category;
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

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
