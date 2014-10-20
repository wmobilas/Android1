package com.agcy.eatwithme.Api;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import java.util.ArrayList;

public abstract class UpdateLocationTask extends ApiTaskBase<String> {
    public UpdateLocationTask(final String latitude, final String longitude) {
        super("device/checklocation", new ArrayList<NameValuePair>() {{
            add(new BasicNameValuePair("latitude", latitude));
            add(new BasicNameValuePair("longitude", longitude));
        }}, true, true);
    }

    @Override
    protected String parse(String json) throws JSONException {
        return new Gson().fromJson(json, String.class);
    }
}
