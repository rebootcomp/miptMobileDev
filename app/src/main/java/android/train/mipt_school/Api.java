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
    Call<ResponseBody> authenticateuser(
            @Field("username") String username,
            @Field("password") String passsword
    );

    @FormUrlEncoded
    @POST("allevents")
    Call<ResponseBody> allevents();

    //@FormUrlEncoded
    @POST("allschedules")
    Call<ResponseBody> allschedules(
            @Header("Authorization") String auth
    );

    //@FormUrlEncoded
    @POST("allusers")
    Call<ResponseBody> allusers(
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

    @POST("usergroups/{id}")
    Call<ResponseBody> userInfo(
            @Path("id") Long id,
            @Header("Authorization") String auth
    );

}