package com.agcy.eatwithme.Api.Users;

import com.agcy.eatwithme.Api.ApiTaskBase;
import com.agcy.eatwithme.Models.User;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by kiolt_000 on 17/08/2014.
 */
public abstract class UsersListTask extends ApiTaskBase<User[]> {

    // отправляем int offset, int count, float latitude, float longitude
    // или отправляем массив ids
    public UsersListTask(List<NameValuePair> nameValuePairs) {
        super("user/list", nameValuePairs, false, false);
    }

    @Override
    protected User[] parse(String json) throws JSONException, FileNotFoundException {
        return new Gson().fromJson((json), User[].class);
    }
}
