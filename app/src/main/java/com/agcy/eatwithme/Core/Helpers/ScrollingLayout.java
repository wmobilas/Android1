package com.agcy.eatwithme.Core.Helpers;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by Freeman on 27.09.2014.
 */
public class ScrollingLayout extends RelativeLayout {
    public ScrollingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true; // With this i tell my layout to consume all the touch events from its childs
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
////        float viewWidth = v.getWidth();
////        float viewHeight = v.getHeight();
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
////                Log.d("com.agcy", String.format("ACTION_DOWN | x:%s y:%s"));
////                break;
//            case MotionEvent.ACTION_MOVE:
////                Log.d("com.agcy", String.format("ACTION_MOVE | x:%s y:%s"));
////                break;
//            case MotionEvent.ACTION_UP:
////                break;
//        }
//        return true;
//    }

}
