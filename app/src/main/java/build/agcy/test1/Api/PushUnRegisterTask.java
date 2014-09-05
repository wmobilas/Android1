package build.agcy.test1.Api;

import org.apache.http.NameValuePair;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by kiolt_000 on 02/09/2014.
 */
public abstract class PushUnRegisterTask extends ApiTaskBase<Boolean> {
    public PushUnRegisterTask() {
        super("device/unregister", new ArrayList<NameValuePair>() {{
        }}, true, true);
    }

    @Override
    protected Boolean parse(String json) throws JSONException, FileNotFoundException {
        return true;
    }
}
