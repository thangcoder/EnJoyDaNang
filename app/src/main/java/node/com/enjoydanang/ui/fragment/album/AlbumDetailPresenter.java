package node.com.enjoydanang.ui.fragment.album;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import node.com.enjoydanang.BasePresenter;
import node.com.enjoydanang.model.AlbumModel;
import node.com.enjoydanang.utils.network.NetworkError;

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

    void onFetchAlbumSuccess(List<AlbumModel> images){
        mvpView.onFetchAlbumSuccess(images);
    }

    void onFetchFail(NetworkError error){
        mvpView.onFetchFail(error);
    }

    void doDummyData(){
        String currentDateAndTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        List<AlbumModel> albums = new ArrayList<>();
        AlbumModel albumModel_1 = new AlbumModel("Chibi Riru", "http://s7.sinaimg.cn/middle/001fnu9Wzy75JOP6cKO06&690", currentDateAndTime);
        AlbumModel albumModel_2 = new AlbumModel("Chibi Kira", "http://s3.sinaimg.cn/middle/001fnu9Wzy75JOOQ26Sb2&690", currentDateAndTime);
        AlbumModel albumModel_3 = new AlbumModel("Chibi Sina", "http://s5.sinaimg.cn/middle/001fnu9Wzy75JONcztOf4&690", currentDateAndTime);
        AlbumModel albumModel_4 = new AlbumModel("Chibi Cokr", "http://s2.sinaimg.cn/middle/001fnu9Wzy74Wl2ukJH71&690", currentDateAndTime);
        AlbumModel albumModel_5 = new AlbumModel("Chibi Manu", "http://s6.sinaimg.cn/middle/001fnu9Wty71UGuB49L65&690", currentDateAndTime);
        albums.add(albumModel_1);
        albums.add(albumModel_2);
        albums.add(albumModel_3);
        albums.add(albumModel_4);
        albums.add(albumModel_5);
        mvpView.onFetchAlbumSuccess(albums);
    }
}
