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
public abstract class MeetingCreateTask extends ApiTaskBase<String> {
    //    public MeetingCreateTask(String methodName, ArrayList<NameValuePair> args){
//        super(methodName, args, true, false);
//    }
    public MeetingCreateTask(final String latitude, final String longitude, final String time, final String selectedType) {
        super("meeting/create", new ArrayList<NameValuePair>() {{
            add(new BasicNameValuePair("latitude", latitude));
            add(new BasicNameValuePair("longitude", longitude));
            add(new BasicNameValuePair("time", time));
            add(new BasicNameValuePair("description", selectedType));
        }}, true, true);
    }

    @Override
    protected String parse(String json) throws JSONException {
        return String.valueOf(new Gson().fromJson(json, MeetingCreateResponse.class).id);
    }

    private class MeetingCreateResponse {
        public int id;
    }
}
