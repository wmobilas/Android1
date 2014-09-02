package build.agcy.test1.Api;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kiolt_000 on 02/09/2014.
 */
public abstract class PushRegisterTask extends ApiTaskBase<Boolean> {
    public PushRegisterTask(final String registerid) {
        super("device/register", new ArrayList<NameValuePair>(){{
            add(new BasicNameValuePair("registrationid",registerid));
        }}, true, true);
    }

    @Override
    protected Boolean parse(String json) throws JSONException, FileNotFoundException {
        return true;
    }
}
