package build.agcy.test1.Core.Helpers;

/**
 * Created by kiolt_000 on 01/09/2014.
 */
public class TimeConverter {
    public static long getUnixNow(){
        return System.currentTimeMillis() / 1000L;
    }
}
