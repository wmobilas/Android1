package com.agcy.eatwithme.Core.Helpers;

import com.agcy.eatwithme.Main.MainActivity;

import org.netcook.android.tools.CrashCatcherService;

/**
 * Created by Freeman on 28.09.2014.
 */
abstract public class AbstractService extends CrashCatcherService {

    @Override
    protected Class<?> getStartActivityAfterCrached() {
        return MainActivity.class;
    }
}