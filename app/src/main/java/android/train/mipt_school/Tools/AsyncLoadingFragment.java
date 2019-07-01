package android.train.mipt_school.Tools;

public interface AsyncLoadingFragment {
    boolean fragmentDataLoaded();

    void setLoadCallback(AsyncLoadCallback callback);
}
