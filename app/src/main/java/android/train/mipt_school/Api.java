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
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("allevents")
    Call<ResponseBody> allevents();
}