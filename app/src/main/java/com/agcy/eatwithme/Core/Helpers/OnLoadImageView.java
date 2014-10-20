package com.agcy.eatwithme.Core.Helpers;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Freeman on 22.09.2014.
 */
public class OnLoadImageView extends ImageView {

    private OnImageViewSizeChanged sizeCallback = null;

    public OnLoadImageView(Context context) {
        super(context);
    }

    public OnLoadImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OnLoadImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w == 0 || h == 0) {
            return;
        } else {
            if (sizeCallback != null)
                sizeCallback.invoke(this, w, h);
        }

    }

    public void setOnImageViewSizeChanged(OnImageViewSizeChanged _callback) {
        this.sizeCallback = _callback;

        if (getWidth() != 0 && getHeight() != 0) {
            _callback.invoke(this, getWidth(), getHeight());
        }
    }

    public interface OnImageViewSizeChanged {
        public void invoke(ImageView v, int w, int h);
    }

    OnImageChangeListiner onImageChangeListiner;

    public void setImageChangeListiner(
            OnImageChangeListiner onImageChangeListiner) {
        this.onImageChangeListiner = onImageChangeListiner;
    }

    @Override
    public void setBackgroundResource(int resid) {
        super.setBackgroundResource(resid);
        if (onImageChangeListiner != null)
            onImageChangeListiner.imageChangedinView();
    }


    @Override
    public void setBackgroundDrawable(Drawable background) {
        super.setBackgroundDrawable(background);
        if (onImageChangeListiner != null)
            onImageChangeListiner.imageChangedinView();
    }


    public static interface OnImageChangeListiner {
        public void imageChangedinView();
    }
}
