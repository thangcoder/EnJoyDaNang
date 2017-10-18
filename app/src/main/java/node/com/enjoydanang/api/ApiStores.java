package node.com.enjoydanang.api;

import node.com.enjoydanang.api.model.Repository;

import node.com.enjoydanang.model.Category;
import node.com.enjoydanang.model.Partner;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by chientruong on 12/15/16.
 */

public interface ApiStores {
//    @FormUrlEncoded
//    @POST("/login")
//    Call<LoginResponsive> login(@Field("username") String username, @Field("password") String password);
    @GET("CategoryApi.asmx/ListAll")
    Observable<Category> getCategorys(@Query("page") int page);


    @FormUrlEncoded
    @POST("PartnerApi.asmx/ListAll")
    Observable<Partner> listPartner(@Field("page") int page);



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
//    Observable<List<UserInfo>> getUserListInfo(@Query("ids") String users);
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
//    Observable<List<RollCallRequestDTO>> getlistRollCall(@Path("jobid") String jobid, @Query("ids") String users);
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
