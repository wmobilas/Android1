package build.agcy.test1.Core.Helpers;

import org.netcook.android.tools.CrashCatcherService;

import build.agcy.test1.Main.MainActivity;

/**
 * Created by Freeman on 28.09.2014.
 */
abstract public class AbstractService extends CrashCatcherService {

    @Override
    protected Class<?> getStartActivityAfterCrached() {
        return MainActivity.class;
    }
}