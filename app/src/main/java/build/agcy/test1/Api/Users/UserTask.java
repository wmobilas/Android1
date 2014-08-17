package build.agcy.test1.Api.Users;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.util.List;

import build.agcy.test1.Api.ApiTaskBase;
import build.agcy.test1.Models.Meeting;

/**
 * Created by kiolt_000 on 17/08/2014.
 */
public abstract class UserTask extends ApiTaskBase<Meeting> {

    // todo: implement
    public UserTask(String methodName, List<NameValuePair> nameValuePairs, boolean post, Boolean loginRequred) {
        super(methodName, nameValuePairs, post, loginRequred);
    }

    @Override
    protected Meeting parse(String json) throws JSONException, FileNotFoundException {
        return new Gson().fromJson(json, Meeting.class);
    }
}
