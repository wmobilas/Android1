package com.agcy.eatwithme.Api.Meetings;

import com.agcy.eatwithme.Api.ApiTaskBase;
import com.agcy.eatwithme.Models.Meeting;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by kiolt_000 on 17/08/2014.
 */
public abstract class MeetingAcceptsListTask extends ApiTaskBase<Meeting.Accept[]> {
    public MeetingAcceptsListTask(final String id) {
        super("meeting/acceptslist", new ArrayList<NameValuePair>() {{
            add(new BasicNameValuePair("id", id));
        }}, false, true);
    }

    @Override
    protected Meeting.Accept[] parse(String json) throws JSONException, FileNotFoundException {
        return new Gson().fromJson((json), Meeting.Accept[].class);
    }
}
