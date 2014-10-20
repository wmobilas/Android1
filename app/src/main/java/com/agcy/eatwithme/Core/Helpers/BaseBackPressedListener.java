package com.agcy.eatwithme.Core.Helpers;

import android.app.Activity;
import android.app.FragmentManager;

/**
 * Created by Freeman on 22.09.2014.
 */
public class BaseBackPressedListener implements OnBackPressedListener {
    private final Activity activity;

    public BaseBackPressedListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void doBack() {
        activity.getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}