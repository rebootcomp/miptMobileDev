package android.train.mipt_school;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface Api {

    @FormUrlEncoded
    @POST("authenticateuser")
    Call<ResponseBody> authenticateUser(
            @Field("username") String username,
            @Field("password") String passsword
    );

    @FormUrlEncoded
    @POST("allevents")
    Call<ResponseBody> alleEents();

    //@FormUrlEncoded
    @POST("allschedules")
    Call<ResponseBody> allSchedules(
            @Header("Authorization") String auth
    );

    @POST("allschedules")
    Call<ResponseBody> allRooms(
            @Header("Authorization") String auth
    );

    //@FormUrlEncoded
    @POST("allusers")
    Call<ResponseBody> allUsers(
            @Header("Authorization") String auth
    );

    @FormUrlEncoded
    @POST("email/os")
    Call<ResponseBody> sendQuestion(
            @Field("user_id") long userId,
            @Field("fio") String fio,
            @Field("from_email") String fromEmail,
            @Field("to_email") String toEmail,
            @Field("subject") String subject,
            @Field("body") String body
    );

    @FormUrlEncoded
    @POST("addschedule/{group_id}")
    Call<ResponseBody> addSchedule(
            @Header("Authorization") String auth,
            @Path("groud_id") long id,
            @Field("room_id") long userId,
            @Field("start") long start,
            @Field("end") long end,
            @Field("comment") String comment,
            @Field("title") String title
    );

    @FormUrlEncoded
    @POST("deleteschedule/{schdedule_id}")
    Call<ResponseBody> deleteSchedule(
            @Header("Authorization") String auth,
            @Path("schdedule_id") long id
    );

    @POST("user/{id}")
    Call<ResponseBody> userInfo(
            @Path("id") long id,
            @Header("Authorization") String auth
    );

    @POST("groups/{id}")
    Call<ResponseBody> groupInfo(
            @Path("id") long id,
            @Header("Authorization") String auth
    );


}