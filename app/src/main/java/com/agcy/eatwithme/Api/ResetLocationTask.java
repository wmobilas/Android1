package com.agcy.eatwithme.Api;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.json.JSONException;

import java.util.ArrayList;

public abstract class ResetLocationTask extends ApiTaskBase<String> {
    public ResetLocationTask() {
        super("device/resetlocation", new ArrayList<NameValuePair>() {{
        }}, true, true);
    }

    @Override
    protected String parse(String json) throws JSONException {
        return new Gson().fromJson(json, String.class);
    }
}
