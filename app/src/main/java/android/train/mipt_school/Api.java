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
            @Field("password") String passsword,
            @Field("device_token") String device_token
    );

    @FormUrlEncoded
    @POST("allevents")
    Call<ResponseBody> alleEents();

    //@FormUrlEncoded
    @POST("allschedules")
    Call<ResponseBody> allSchedules(
            @Header("Authorization") String auth
    );

    @POST("allrooms")
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
            @Header("Authorization") String auth,
            @Field("subject") String subject,
            @Field("body") String body,
            @Field("event_id") long eventId
    );

    @FormUrlEncoded
    @POST("addschedule/{group_id}")
    Call<ResponseBody> addSchedule(
            @Header("Authorization") String auth,
            @Path("group_id") long id,
            @Field("start") long start,
            @Field("end") long end,
            @Field("comment") String comment,
            @Field("title") String title
    );

    @POST("deleteschedule/{schdedule_id}")
    Call<ResponseBody> deleteSchedule(
            @Header("Authorization") String auth,
            @Path("schdedule_id") long id
    );

    @FormUrlEncoded
    @POST("updateschedule/{schdedule_id}")
    Call<ResponseBody> updateSchedule(
            @Header("Authorization") String auth,
            @Path("schdedule_id") long id,
            @Field("start") long start,
            @Field("end") long end,
            @Field("comment") String comment,
            @Field("title") String title
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

    @FormUrlEncoded

    @POST("adduserintogroup/{group_id}")
    Call<ResponseBody> addUserIntoGroup(
            @Header("Authorization") String auth,
            @Path("group_id") long id,
            @Field("users") String users
    );

    @FormUrlEncoded
    @POST("deleteuserfromgroup/{group_id}")
    Call<ResponseBody> deleteUserFromGroup(
            @Header("Authorization") String auth,
            @Path("group_id") long id,
            @Field("users") String users
    );

    @FormUrlEncoded
    @POST("setgroupname/{group_id}")
    Call<ResponseBody> renameGroup(
            @Header("Authorization") String auth,
            @Path("group_id") long id,
            @Field("group_name") String groupName
    );

    @POST("updateuser/{user_id}")
    Call<ResponseBody> updatePhone(
            @Header("Authorization") String auth,
            @Path("user_id") long userId,
            @Field("phone") String phone,
            @Field("vk_id") String vk
    );

}