package com.agcy.eatwithme.Api.Meetings;

import com.agcy.eatwithme.Api.ApiTaskBase;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by kiolt_000 on 17/08/2014.
 */
public abstract class MeetingConfirmTask extends ApiTaskBase<String> {
    public MeetingConfirmTask(final String id) {
        super("meeting/confirm", new ArrayList<NameValuePair>() {{
            add(new BasicNameValuePair("id", id));
        }}, true, true);
    }

    @Override
    protected String parse(String json) throws JSONException {

        return new Gson().fromJson(json, String.class);
    }
}
