package android.train.mipt_school;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://mobile.it-edu.com/api/";
    private static RetrofitClient myInstance;
    private Retrofit retrofit;
    OkHttpClient okHttpClient;

    private RetrofitClient() {
        okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitClient getInstance() {
        if (myInstance == null)
            myInstance = new RetrofitClient();
        return myInstance;
    }

    public Api getApi() {
        return retrofit.create(Api.class);
    }
}
