package build.agcy.test1.Api.Meetings;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import java.util.ArrayList;

import build.agcy.test1.Api.ApiTaskBase;

/**
 * Created by kiolt_000 on 17/08/2014.
 */
public abstract class MeetingAcceptTask extends ApiTaskBase<String> {
    // предлагаем встретиться
    // отправляем string id втречи
    public MeetingAcceptTask(final String id, final String message) {
        super("meeting/accept", new ArrayList<NameValuePair>(){{
            add(new BasicNameValuePair("id",id));
            add(new BasicNameValuePair("message",message));
        }}, true, true);
    }
    @Override
    protected String parse(String json) throws JSONException {
        return new Gson().fromJson(json,String.class);
    }
}
