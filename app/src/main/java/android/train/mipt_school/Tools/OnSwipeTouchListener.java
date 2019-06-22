package android.train.mipt_school.Tools;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnTouchListener;


public class OnSwipeTouchListener implements OnTouchListener {
    private final GestureDetector gestureDetector;

    public boolean onTouch(View v, MotionEvent event) {
        return this.gestureDetector.onTouchEvent(event);
    }

    public void onSwipeRight() {
    }

    public void onSwipeLeft() {
    }

    public void onSwipeTop() {
    }

    public void onSwipeBottom() {
    }

    public OnSwipeTouchListener( Context ctx) {
        super();
        this.gestureDetector = new GestureDetector(ctx, (OnGestureListener)(new OnSwipeTouchListener.GestureListener()));
    }


    private final class GestureListener extends SimpleOnGestureListener {
        private final int SWIPE_THRESHOLD = 100;
        private final int SWIPE_VELOCITY_THRESHOLD = 100;

        public boolean onDown( MotionEvent e) {
            return true;
        }

        public boolean onFling( MotionEvent e1,  MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;

            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > (float)this.SWIPE_THRESHOLD && Math.abs(velocityX) > (float)this.SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > (float)0) {
                            OnSwipeTouchListener.this.onSwipeRight();
                        } else {
                            OnSwipeTouchListener.this.onSwipeLeft();
                        }
                    }

                    result = true;
                } else if (Math.abs(diffY) > (float)this.SWIPE_THRESHOLD && Math.abs(velocityY) > (float)this.SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > (float)0) {
                        OnSwipeTouchListener.this.onSwipeBottom();
                    } else {
                        OnSwipeTouchListener.this.onSwipeTop();
                    }
                }

                result = true;
            } catch (Exception var8) {
                var8.printStackTrace();
            }

            return result;
        }

        public GestureListener() {
        }
    }
}

