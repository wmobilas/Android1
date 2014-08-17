package build.agcy.test1.Start;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import java.util.ArrayList;

import build.agcy.test1.Api.ApiTaskBase;
import build.agcy.test1.Models.CurrentUser;

/**
 * Created by kiolt_000 on 17/08/2014.
 */
public abstract class LoginTask extends ApiTaskBase<CurrentUser> {
    public LoginTask(final String login, final String password) {
        super("account/login", new ArrayList<NameValuePair>(){{
            add(new BasicNameValuePair("login",login));
            add(new BasicNameValuePair("password",password));
        }}, true, false);
    }

    @Override
    protected CurrentUser parse(String json) throws JSONException {
        return new Gson().fromJson(json, CurrentUser.class);
    }




}
