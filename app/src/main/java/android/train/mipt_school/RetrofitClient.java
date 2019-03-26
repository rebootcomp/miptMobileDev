package android.train.mipt_school;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://151.248.113.34:82/api/";
    private static RetrofitClient myInstance;
    private Retrofit retrofit;

    private RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
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
