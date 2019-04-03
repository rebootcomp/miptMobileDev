package android.train.mipt_school.DataHolders;


import android.train.mipt_school.RetrofitClient;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllEvents {
    private String events = "";
    private static AllEvents instance;
    private ResponseCallback rc;

    public static synchronized AllEvents getInstance() {
        if (instance == null)
            instance = new AllEvents();
        return instance;
    }

    public interface ResponseCallback {
        void call(String s);
    }

    public void update() {
        rc = new ResponseCallback() {
            @Override
            public void call(String s) {
                events = s;
            }
        };
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .allevents();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        rc.call(response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    public String getEvents() {
        return events;
    }
}
