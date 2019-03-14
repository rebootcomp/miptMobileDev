package android.train.mipt_school;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainPageFragment extends Fragment {


    public static MainPageFragment newInstance() {

        MainPageFragment fragment = new MainPageFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_page, container, false);

        View moreInfoPage = view.findViewById(R.id.more_info_page);

        final View scrollView = view.findViewById(R.id.main_page_scroll_view);
        final View arrow = view.findViewById(R.id.main_page_expand_sign);
        final View swipeTipText = view.findViewById(R.id.main_page_swipe_tip_text);

        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(moreInfoPage);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                /*if (i == bottomSheetBehavior.STATE_EXPANDED) {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.BELOW, R.id.main_page_expand_sign);
                    scrollView.setLayoutParams(params);
                }*/
                /*if (i == bottomSheetBehavior.STATE_DRAGGING) {
                    arrow.setVisibility(View.VISIBLE);
                    swipeTipText.setVisibility(View.VISIBLE);
                }*/
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
        return view;
    }

}
