package com.agcy.eatwithme.Api.Users;

import com.agcy.eatwithme.Api.ApiTaskBase;
import com.agcy.eatwithme.Models.User;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by kiolt_000 on 17/08/2014.
 */
public abstract class UserTask extends ApiTaskBase<User> {

    public UserTask(final String userid) {
        super("user/get", new ArrayList<NameValuePair>() {{
            add(new BasicNameValuePair("id", userid));
        }}, false, false);
    }

    @Override
    protected User parse(String json) throws JSONException, FileNotFoundException {
        return new Gson().fromJson(json, User.class);
    }
}
