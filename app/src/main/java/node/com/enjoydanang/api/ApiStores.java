package node.com.enjoydanang.api;

import android.support.annotation.NonNull;

import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.LoginType;
import node.com.enjoydanang.model.Banner;
import node.com.enjoydanang.model.Category;
import node.com.enjoydanang.model.Contact;
import node.com.enjoydanang.model.DetailPartner;
import node.com.enjoydanang.model.ExchangeRate;
import node.com.enjoydanang.model.HistoryCheckin;
import node.com.enjoydanang.model.Introduction;
import node.com.enjoydanang.model.Language;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.model.PartnerAlbum;
import node.com.enjoydanang.model.Popup;
import node.com.enjoydanang.model.Reply;
import node.com.enjoydanang.model.Review;
import node.com.enjoydanang.model.Schedule;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.model.Utility;
import node.com.enjoydanang.model.Weather;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by chientruong on 12/15/16.
 */

public interface ApiStores {


    @GET("GlobalApi.asmx/GetBanner")
    Observable<Repository<Banner>> getBanner();

    @FormUrlEncoded
    @POST("AccountApi.asmx/SocialNetworkLogin")
    Observable<Repository<UserInfo>> doSignOrRegisterViaSocial(@Field("userId") String userId, @Field("token") String token,
                                                               @Field("type") LoginType type, @Field("image") String image,
                                                               @Field("fullname") String fullname, @Field("email") String email);


    @FormUrlEncoded
    @POST("AccountApi.asmx/NormalLogin")
    Observable<Repository<UserInfo>> normalLogin(@Field("username") String userName, @Field("password") String pwd);


    @FormUrlEncoded
    @POST("AccountApi.asmx/NormalRegister")
    Observable<Repository<UserInfo>> normalRegister(@Field("username") String userName, @Field("password") String pwd,
                                                    @Field("fullname") String fullName, @Field("email") String email,
                                                    @Field("phone") String phoneNo);


    @GET("LandingPageApi.asmx/Introduction")
    Observable<Repository<Introduction>> getIntroduction();

    @GET("PartnerApi.asmx/ListAll")
    Observable<Repository<Partner>> getPartner(@Query("page") int page);


    @GET("CategoryApi.asmx/ListAll")
    Observable<Repository<Category>> getAllCategories();

    @GET("GlobalApi.asmx/GetResourceLanguage")
    Observable<Repository<Language>> getLanguage();

    @FormUrlEncoded
    @POST("PartnerApi.asmx/ListByCategory")
    Observable<Repository<Partner>> getPartnerByCategoryId(@Field("categoryId") int categoryId, @Field("page") int page, @Field("customerId") long userId);


    @FormUrlEncoded
    @POST("PartnerApi.asmx/ListByCategoryByLocation")
    Observable<Repository<Partner>> getListPartnerByLocation(@Field("categoryId") int categoryId,
                                                             @Field("customerId") long userId,
                                                             @Field("page") int page,
                                                             @Field("geoLat") String latitude,
                                                             @Field("geoLng") String longtitude);

    @FormUrlEncoded
    @POST("PartnerApi.asmx/ListHomeByLocation")
    Observable<Repository<Partner>> getListPartnerHome(@Field("customerId") long customerId,
                                                       @Field("geoLat") String latitude,
                                                       @Field("geoLng") String longtitude);

    @FormUrlEncoded
    @POST("PartnerApi.asmx/Detail")
    Observable<Repository<DetailPartner>> getDetailPartnerById(@Field("id") int partnerId);

    @FormUrlEncoded
    @POST("PartnerApi.asmx/Picture")
    Observable<Repository<PartnerAlbum>> getAlbumPartnerById(@Field("id") int partnerId);


    @GET("GlobalApi.asmx/GetWidgetWeather")
    Observable<Repository<Weather>> getWidgetWeather();

    @GET("GlobalApi.asmx/GetWidgetExchangeRate")
    Observable<Repository<ExchangeRate>> getWidgetExchangeRate();

    @FormUrlEncoded
    @POST("PartnerApi.asmx/ListHome")
    Observable<Repository<Partner>> getListPartnerHome(@Field("customerId") long customerId);

    @FormUrlEncoded
    @POST("PartnerApi.asmx/Slider")
    Observable<Repository<PartnerAlbum>> getSlideByPartnerId(@Field("id") int partnerId);

    @FormUrlEncoded
    @POST("PartnerApi.asmx/Utility")
    Observable<Repository<Utility>> getUtilityByPartnerId(@Field("id") int partnerId);


    @FormUrlEncoded
    @POST("PartnerApi.asmx/Schedule")
    Observable<Repository<Schedule>> getScheduleByPartnerId(@Field("id") int partnerId);


    @FormUrlEncoded
    @POST("GlobalApi.asmx/Favorite")
    Observable<Repository<Partner>> getFavoriteByUserId(@Field("customerId") long userId);


    @FormUrlEncoded
    @POST("GlobalApi.asmx/ApplyFavorite")
    Observable<Repository> addFavorite(@Field("customerId") long userId, @Field("partnerId") long partnerId);

    @FormUrlEncoded
    @POST("GlobalApi.asmx/Contact")
    Observable<Repository> sendContact(@NonNull @Field("name") String name, @Field("phone") String phone, @Field("email") String email, @Field("title") String title, @Field("content") String content);

    @GET("GlobalApi.asmx/Info")
    Observable<Repository<Contact>> getInformation();

    @FormUrlEncoded
    @POST("AccountApi.asmx/ChangePassword")
    Observable<Repository<UserInfo>> changePwd(@Field("userId") long userId, @Field("oldPassword") String oldPwd, @Field("newPassword") String newPwd);

    @FormUrlEncoded
    @POST("AccountApi.asmx/UpdateProfile")
    Observable<Repository<UserInfo>> updateProfile(@Field("userId") long userId,
                                                   @Field("fullname") String fullname,
                                                   @Field("phone") String phone,
                                                   @Field("email") String email,
                                                   @Field("picture") String picBase64);

    @FormUrlEncoded
    @POST("GlobalApi.asmx/SearchApp")
    Observable<Repository<Partner>> searchWithQuery(@Field("query") String query);


    @FormUrlEncoded
    @POST("ReviewApi.asmx/ListReview")
    Observable<Repository<Review>> getListReviewByPartnerId(@Field("partnerId") int partnerId, @Field("page") int page);


    @FormUrlEncoded
    @POST("ReviewApi.asmx/WriteReview")
    Observable<Repository> writeReview(@Field("customerId") long customerId,
                                       @Field("partnerId") int partnerId,
                                       @Field("star") int star,
                                       @Field("title") String title,
                                       @Field("name") String name,
                                       @Field("content") String content,
                                       @Field("image1") String strImage1,
                                       @Field("image2") String strImage2,
                                       @Field("image3") String strImage3);

    @FormUrlEncoded
    @POST("ReviewApi.asmx/ListReplyReview")
    Observable<Repository<Reply>> getReplyByReviewId(@Field("page") int page, @Field("reviewId") int reviewId);

    @FormUrlEncoded
    @POST("ReviewApi.asmx/ListReplyReviewAll")
    Observable<Repository> getListReplyByReviewId(@Field("reviewId") int reviewId);

    @FormUrlEncoded
    @POST("PartnerApi.asmx/DetailByQRCode")
    Observable<Repository<Partner>> getInforByQRCode(@Field("qrCode") String qrCode);

    @FormUrlEncoded
    @POST("GlobalApi.asmx/CheckIn")
    Observable<Repository<HistoryCheckin>> checkIn(@Field("partnerId") int partnerId,
                                                   @Field("customerId") long customerId,
                                                   @Field("amount") int amount,
                                                   @Field("passcode") String passCode);

    @FormUrlEncoded
    @POST("GlobalApi.asmx/HistoryCheckIn")
    Observable<Repository<HistoryCheckin>> historyCheckIn(@Field("customerId") long customerId, @Field("fromDate") String fromDate, @Field("toDate") String toDate);


    @FormUrlEncoded
    @POST("ReviewApi.asmx/ListPicture")
    Observable<Repository> getListImageByReviewId(@Field("reviewId") long reviewId);


    @FormUrlEncoded
    @POST("ReviewApi.asmx/ListReplyReviewAll")
    Observable<Repository> getListReplyByReviewId(@Field("reviewId") long reviewId);


    @FormUrlEncoded
    @POST("ReviewApi.asmx/PictureReview")
    Observable<Repository> postPictureByReviewId(@Field("reviewId") int reviewId, @Field("picture") String base64);

    @FormUrlEncoded
    @POST("ReviewApi.asmx/WriteReviewReply")
    Observable<Repository> writeReplyByReviewId(@Field("reviewId") int reviewId,
                                                @Field("customerId") long customerId,
                                                @Field("partnerId") int partnerId,
                                                @Field("star") int star,
                                                @Field("title") String title,
                                                @Field("name") String name,
                                                @Field("content") String content,
                                                @Field("image1") String image1,
                                                @Field("image2") String image2,
                                                @Field("image3") String image3);

    @GET("GlobalApi.asmx/PopupAds")
    Observable<Repository<Popup>> getPopupInformation();

    @Multipart
    @Headers("Accept:application/json; charset=utf-8")
    @POST("ReviewApi.asmx/WriteReview")
    Observable<Repository> writeReview(@Part("customerId") long customerId,
                                       @Part("partnerId") int partnerId,
                                       @Part("star") int star,
                                       @Part("title") String title,
                                       @Part("name") String name,
                                       @Part("content") String content,
                                       @Part MultipartBody.Part file1,
                                       @Part MultipartBody.Part file2,
                                       @Part MultipartBody.Part file3);

    @Multipart
    @Headers("Accept:application/json; charset=utf-8")
    @POST("ReviewApi.asmx/WriteReviewReply")
    Observable<Repository> writeReplyByReviewId(@Part("reviewId") int reviewId,
                                                @Part("customerId") long customerId,
                                                @Part("partnerId") int partnerId,
                                                @Part("star") int star,
                                                @Part("title") String title,
                                                @Part("name") String name,
                                                @Part("content") String content,
                                                @Part MultipartBody.Part image1,
                                                @Part MultipartBody.Part image2,
                                                @Part MultipartBody.Part image3);


    @Multipart
    @POST("ReviewApi.asmx/WriteReview_New")
    Observable<Repository> postComment(@Part("Id") long id,
                                       @Part("CustomerId") long customerId,
                                       @Part("PartnerId") int partnerId,
                                       @Part("ReviewId") int reviewId,
                                       @Part("Star") int star,
                                       @Part("Title") String title,
                                       @Part("Content") String content,
                                       @Part MultipartBody.Part image1,
                                       @Part MultipartBody.Part image2,
                                       @Part MultipartBody.Part image3);

    @FormUrlEncoded
    @POST("PartnerApi.asmx/ListAround")
    Observable<Repository> listPlaceAround(@Field("customerId") long customerId, @Field("geoLat") String geoLat, @Field("geoLng") String geoLng);


    @FormUrlEncoded
    @POST("PartnerApi.asmx/ListSearchByLocation")
    Observable<Repository> listSearchByLocation(@Field("customerId") long customerId, @Field("distance") String distance,
                                                @Field("geoLat") String geoLat, @Field("geoLng") String geoLng);

//    @FormUrlEncoded
//    @POST("/login")
//    Observable<Repository> login(@Field("username") String username, @Field("password") String password,
//                                      @Field("device_token") String deviceToken, @Field("os") String os);
//    @FormUrlEncoded
//    @POST("v1/autologin")
//    Observable<Repository> autoLogin(@Field("username") String username,
//                                     @Field("access_token") String access_token, @Field("device_token") String deviceToken);

//    @FormUrlEncoded
//    @POST("v1/logout")
//    Observable<Response<Void>> logout(@Field("username") String username, @Field("device_token") String deviceToken);
//
//    @GET("v1/threads/{thread_id}")
//    Observable<ThreadInfo> getThreadInfo(@Path("thread_id") String threadId);
//
//    @GET("v1/users")
//    Observable<Repository<UserInfo>> getUserListInfo(@Query("ids") String users);
//
//    @GET("v1/users/{user_id}")
//    Observable<UserInfo> getUserInfo(@Path("user_id") String userId);
//
//    @POST("v1/roll-call/check")
//    Observable<RollCallMemberReponse> checkRollCall(@Body RollCallMemberRequest request);
//
//    @GET("v1/sign-report/{jobid}/check")
//    Observable<Boolean> checkReport(@Path("jobid") String jobid);
//
//    @GET("v1/roll-call/{jobid}/check")
//    Observable<Boolean> checkRollCall(@Path("jobid") String jobid);
//
//
//    @PUT("v1/sign-report/{jobid}")
//    Observable<Void> signReport(@Path("jobid") String jobid);
//
//    @FormUrlEncoded
//    @PUT("v1/roll-call/{user}")
//    Observable<String> requestRollCall(@Path("user") String user, @Field("job_id") String jobID);
//
////    @Multipart
////    @POST("/v1/upload")
////    Observable<Repository> uploadPhoto(@Part("author") RequestBody author,@Part("thread_id") RequestBody threadId,
////                                                                @Part("file") RequestBody photo);
//
//    @GET("v1/roll-call/{jobid}")
//    Observable<Repository<RollCallRequestDTO>> getlistRollCall(@Path("jobid") String jobid, @Query("ids") String users);
//
//    @Multipart
//    @Headers("Accept:application/json; charset=utf-8")
//    @POST("/v1/upload")
//    Observable<Repository> uploadPhoto(@Part("author") RequestBody author, @Part("thread_id") RequestBody threadId,
//                                       @Part MultipartBody.Part photo);
//
//    /*@Multipart
//    @POST("/v1/upload")
//    Observable<Repository> uploadPhotoSign(@Part("author") String author,@Part("thread_id") String threadId,
//                                     @Part MultipartBody.Part photo);*/
//
//    @FormUrlEncoded
//    @POST("/api/officelist.php")
//    Observable<OfficeListReponse> getOfficelist(@Field("access_token") String access_token);
//
//    @FormUrlEncoded
//    @POST("/api/getoffice.php")
//    Observable<OfficeReponse> getOffice(@Field("access_token") String access_token);
//
//    @FormUrlEncoded
//    @POST("/api/returnfixtureslist.php")
//    Observable<ReturnFixturesReponse> getReturnFixtureList(@Field("access_token") String access_token);
//
//    @FormUrlEncoded
//    @POST("/api/getpointbalance.php")
//    Observable<PointBalanceReponse> getPointBalance(@Field("access_token") String access_token);
//
//    @FormUrlEncoded
//    @POST("/api/getexchangegoods.php")
//    Observable<ProductListReponse> getExchangeGoods(@Field("access_token") String access_token);
//
//    @FormUrlEncoded
//    @POST("/api/searchgoods.php")
//    Observable<ProductListReponse> searchGoods(@Field("access_token") String access_token, @Field("goods_name") String goods_name);
//
//    @FormUrlEncoded
//    @POST("/api/decreasepoint.php")
//    Observable<DecreasepointReponse> decreasePoint(@Field("access_token") String access_token, @Field("goods_id") String goods_id);
//
//    @FormUrlEncoded
//    @POST("/api/getnotification.php")
//    Observable<NoticeReponse> getAllNotice(@Field("access_token") String access_token, @Field("sort_key") int sort_key,
//
//                                           @Field("limit") int limit, @Field("offset") int offset, @Field("notification_type") int notification_type);
//
//    @FormUrlEncoded
//    @POST("/api/changereadflg.php")
//    Observable<BaseReponse> changeReadFlg(@Field("access_token") String access_token, @Field("notification_id") String notification_id);
//
//    @FormUrlEncoded
//    @POST("/api/callmemberlist.php")
//    Observable<RollCallLeaderReponse> callmemberlist(@Field("access_token") String access_token, @Field("job_id") String job_id);
//
//    @FormUrlEncoded
//    @POST("/api/jobinfo.php")
//    Observable<JobInfoReponse> getJobinfo(@Field("access_token") String access_token, @Field("job_id") String job_id);
//
//    @FormUrlEncoded
//    @POST("/api/finishsign.php")
//    Observable<BaseReponse> confirmSign(@Field("access_token") String access_token, @Field("job_id") String job_id, @Field("sign_photo") String sign_photo);
//
//    @FormUrlEncoded
//    @POST("/api/transitionschedule.php")
//    Observable<GetURLReponse> transitionSchedule(@Field("access_token") String access_token);
//
//    @FormUrlEncoded
//    @POST("/api/transitionsetting.php")
//    Observable<GetURLReponse> transitionSetting(@Field("access_token") String access_token);
//
//    @FormUrlEncoded
//    @POST("/api/transitionreport.php")
//    Observable<GetURLReponse> transitionReport(@Field("access_token") String access_token, @Field("job_id") String job_id);
//
//    @FormUrlEncoded
//    @POST("/api/transitionjob.php")
//    Observable<GetURLReponse> transitionJob(@Field("access_token") String access_token, @Field("job_id") String job_id);
//
//    @FormUrlEncoded
//    @POST("/api/glchangegooutflg.php")
//    Observable<BaseReponse> glchangegooutflg(@Field("access_token") String access_token, @Field("job_id") String job_id, @Field("user_id") String user_id);
//
//    @FormUrlEncoded
//    @POST("/api/lendfixtureslist.php")
//    Observable<LoaniFxturesReponse> lendFixturesList(@Field("access_token") String access_token, @Field("office_cd") String office_cd);
//
//    @FormUrlEncoded
//    @POST("/api/changelendflg.php")
//    Observable<BaseReponse> changeLendflgs(@Field("access_token") String access_token, @Field("fixtures_list") String fixtures_list,
//                                           @Field("scheduled_return_date") String scheduled_return_date,
//                                           @Field("office_cd") String office_cd);
//
//    @FormUrlEncoded
//    @POST("/api/changereturnflg.php")
//    Observable<BaseReponse> changereturnflg(@Field("access_token") String access_token, @Field("fixtures_list") String fixtures_list);
//
//    @FormUrlEncoded
//    @POST("/api/changegetupflg.php")
//    Observable<BaseReponse> changegetupflg(@Field("access_token") String access_token, @Field("job_id") int jobId);
//    @FormUrlEncoded
//    @POST("/api/changegooutflg.php")
//    Observable<BaseReponse> changegooutflg(@Field("access_token") String access_token, @Field("job_id") int jobId);
//
//
//    @FormUrlEncoded
//    @POST("/api/alarmlist.php")
//    Observable<AlarmList> getListAlarm(@Field("access_token") String access_token);
////
////    @FormUrlEncoded
////    @POST("api/alarmlist-android.php")
////    Observable<AlarmList> getListAlarm(@Field("access_token") String access_token);
//
//    @FormUrlEncoded
//    @POST("/api/getgpsdata.php")
//    Observable<GPSDataReponse> getgpsdata(@Field("access_token") String access_token, @Field("job_id") String job_id);
//
//    @GET(Constant.URL_VER)
//    Observable<VersionReponse> checkVersion();
}
