package build.agcy.test1.Api.Users;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import build.agcy.test1.Api.ApiTaskBase;
import build.agcy.test1.Models.Meeting;
import build.agcy.test1.Models.User;

/**
 * Created by kiolt_000 on 17/08/2014.
 */
public abstract class UsersListTask extends ApiTaskBase<User[]> {

    // отправляем int offset, int count, float latitude, float longtitude
    // или отправляем массив ids
    public UsersListTask(String methodName, List<NameValuePair> nameValuePairs, boolean post, Boolean loginRequred) {
        super("user/list", nameValuePairs, false, false);
    }

    @Override
    protected User[] parse(String json) throws JSONException, FileNotFoundException {
        return new Gson().fromJson(new FileReader(json), User[].class);
    }
}