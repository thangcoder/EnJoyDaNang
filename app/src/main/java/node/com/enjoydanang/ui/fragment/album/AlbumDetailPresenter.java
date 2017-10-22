package node.com.enjoydanang.ui.fragment.album;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import node.com.enjoydanang.BasePresenter;
import node.com.enjoydanang.model.PartnerAlbum;
import node.com.enjoydanang.constant.AppError;

/**
 * Author: Tavv
 * Created on 10/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class AlbumDetailPresenter extends BasePresenter<iAlbumView>{

    public AlbumDetailPresenter(iAlbumView view) {
        super(view);
    }

    void onFetchAlbumSuccess(List<PartnerAlbum> images){
        mvpView.onFetchAlbumSuccess(images);
    }

    void onFetchFail(AppError error){
        mvpView.onFetchFail(error);
    }

    void doDummyData(){
        String currentDateAndTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        List<PartnerAlbum> albums = new ArrayList<>();
        PartnerAlbum partnerAlbum_1 = new PartnerAlbum("Chibi Riru", "http://s7.sinaimg.cn/middle/001fnu9Wzy75JOP6cKO06&690", currentDateAndTime);
        PartnerAlbum partnerAlbum_2 = new PartnerAlbum("Chibi Kira", "http://s3.sinaimg.cn/middle/001fnu9Wzy75JOOQ26Sb2&690", currentDateAndTime);
        PartnerAlbum partnerAlbum_3 = new PartnerAlbum("Chibi Sina", "http://s5.sinaimg.cn/middle/001fnu9Wzy75JONcztOf4&690", currentDateAndTime);
        PartnerAlbum partnerAlbum_4 = new PartnerAlbum("Chibi Cokr", "http://s2.sinaimg.cn/middle/001fnu9Wzy74Wl2ukJH71&690", currentDateAndTime);
        PartnerAlbum partnerAlbum_5 = new PartnerAlbum("Chibi Manu", "http://s6.sinaimg.cn/middle/001fnu9Wty71UGuB49L65&690", currentDateAndTime);
        albums.add(partnerAlbum_1);
        albums.add(partnerAlbum_2);
        albums.add(partnerAlbum_3);
        albums.add(partnerAlbum_4);
        albums.add(partnerAlbum_5);
        mvpView.onFetchAlbumSuccess(albums);
    }
}
