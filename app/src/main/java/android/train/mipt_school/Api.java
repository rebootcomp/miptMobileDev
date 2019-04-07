package android.train.mipt_school;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {

    @FormUrlEncoded
    @POST("getmdluser")
    Call<ResponseBody> getmdluser(
            @Field("username") String username,
            @Field("password") String passsword
    );

    @FormUrlEncoded
    @POST("allevents")
    Call<ResponseBody> allevents();

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
}