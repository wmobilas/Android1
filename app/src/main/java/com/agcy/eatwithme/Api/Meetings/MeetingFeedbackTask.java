package com.agcy.eatwithme.Api.Meetings;


import com.agcy.eatwithme.Api.ApiTaskBase;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.util.ArrayList;

// оставляем отзыв
// отправляем string id встречи, string text, boolean like

public abstract class MeetingFeedbackTask extends ApiTaskBase<String> {
    public MeetingFeedbackTask(final String id, final String text, final Boolean like) {
        super("meeting/feedback", new ArrayList<NameValuePair>() {{
            add(new BasicNameValuePair("id", id));
            add(new BasicNameValuePair("text", text));
            add(new BasicNameValuePair("like", like.toString()));
        }}, true, false);
    }

    @Override
    public void onSuccess(String text) {
    }

    @Override
    protected void onPostExecute(Object response) {
        super.onPostExecute(response);
    }

    @Override
    public void onError(Exception exp) {
    }

    @Override
    protected String parse(String json) throws JSONException, FileNotFoundException {
        return new Gson().fromJson((json), String.class);
    }
}