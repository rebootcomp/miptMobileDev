package android.train.mipt_school;

public interface ResponseCallback {
    void onResponse(String data);
    void onFailure(String message);
}
