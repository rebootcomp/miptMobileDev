package android.train.mipt_school;


import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.train.mipt_school.Tools.SceneFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;


public class MainPageFragment extends Fragment implements SceneFragment {
    private WebView webView;

    private ProgressDialog progressDialog;
    private ProgressBar progressBar;
    SharedPreferences mSP;

    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> uploadMessage;
    public static final int REQUEST_SELECT_FILE = 100;
    private final static int FILECHOOSER_RESULTCODE = 1;


    private String title;
    private View moreInfoPage;

    public static MainPageFragment newInstance() {


        MainPageFragment fragment = new MainPageFragment();

        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_page, container, false);

        // setting up actionbar
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);

        moreInfoPage = view.findViewById(R.id.more_info_page);

        final View scrollView = view.findViewById(R.id.main_page_scroll_view);
        final View arrow = view.findViewById(R.id.main_page_expand_sign);
        final View swipeTipText = view.findViewById(R.id.main_page_swipe_tip_text);

        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(moreInfoPage);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {

            }

            @Override
            public void onSlide(@NonNull View view, float slideOffset) {
                arrow.animate().rotation(180f * slideOffset).setDuration(0).start();
                swipeTipText.animate().alpha(1 - slideOffset * 3).setDuration(0).start();
                scrollView.animate().alpha(slideOffset).setDuration(0).start();
            }
        });

        swipeTipText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetBehavior.getState() == bottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetBehavior.getState() == bottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });



        ///

        ///

        ///




        //webview (еще не доделано)


          //  CookieManager.getInstance().setAcceptCookie(true);

            webView = view.findViewById(R.id.webview);
            progressBar = view.findViewById(R.id.pb);

            webView.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {

                        CookieManager.getInstance().flush();



                        super.onPageStarted(view, url, favicon);
                    }


                @Override
                public void onPageFinished(WebView view, String url) {
                    if (progressBar.getVisibility() == ProgressBar.VISIBLE)
                        progressBar.setVisibility(ProgressBar.INVISIBLE);
                    CookieManager.getInstance().flush();
                    super.onPageFinished(view, url);
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    view.loadUrl(request.getUrl().toString());
                    return true;
                }
            });



            progressBar.setVisibility(ProgressBar.VISIBLE);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setAllowFileAccess(true);
            webView.getSettings().setAllowContentAccess(true);
            webView.loadUrl("https://it-edu.com/ru");



return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        title = context.getString(R.string.main_page_title);
    }

    @Override
    public void onBackButtonPressed() {

    }

    @Override
    public String getTitle() {
        return title;
    }
}

