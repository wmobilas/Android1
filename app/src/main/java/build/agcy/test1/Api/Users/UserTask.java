package build.agcy.test1.Api.Users;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import build.agcy.test1.Api.ApiTaskBase;
import build.agcy.test1.Models.Meeting;
import build.agcy.test1.Models.User;

/**
 * Created by kiolt_000 on 17/08/2014.
 */
public abstract class UserTask extends ApiTaskBase<User> {

    // todo: юзертаск наследует от апитаска всё, что в нём происходит. грубо говоря у тебя один и тот же класс
    // ApiTaskBase на все случаи жизни, и ты просто делаешь расширения его под каждый отдельный случай.
    //а вместо того что ниже мне оставить только  add(new BasicNameValuePair("id",userid));?
    // это готовый класс, его не нужно менять. просто как пример того как другие такие должны выглядеть.
    public UserTask(final String userid) {
        super("user/get", new ArrayList<NameValuePair>(){{
            add(new BasicNameValuePair("id",userid));
        }}, false, false);
    }

    @Override
    protected User parse(String json) throws JSONException, FileNotFoundException {
        return new Gson().fromJson(json, User.class);
    }
}
