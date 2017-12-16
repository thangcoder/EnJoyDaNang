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
    Observable<Repository<Partner>> listPlaceAround(@Field("customerId") long customerId, @Field("geoLat") String geoLat, @Field("geoLng") String geoLng);

    @FormUrlEncoded
    @POST("EnjoyApi.asmx/Get")
    Observable<Repository<UserInfo>> getUserInfoById(@Field("CustomerId") long customerId, @Field("Type") String type);

    @FormUrlEncoded
    @POST("PartnerApi.asmx/ListSearchByLocation")
    Observable<Repository<Partner>> listSearchByLocation(@Field("customerId") long customerId,
                                                         @Field("distance") int distance,
                                                         @Field("geoLat") String geoLat, @Field("geoLng") String geoLng);

}
