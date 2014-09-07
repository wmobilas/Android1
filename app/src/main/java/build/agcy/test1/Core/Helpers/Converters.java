package build.agcy.test1.Core.Helpers;

/**
 * Created by kiolt_000 on 07/09/2014.
 */
public class Converters {

    public static String getStaticMapImageUrl(String longitude, String latitude, int width, int height, int zoom, String color, String label){
        String staticMapImageUrl = "http://maps.googleapis.com/maps/api/staticmap?" +
                "zoom=" + zoom +
                "&size=" + width + "x" + height +
                "&maptype=roadmap" +
                "&markers=color:" + color +
                "|label:" + label +
                "|" + latitude + "," + longitude;
        return staticMapImageUrl;
    }
}
