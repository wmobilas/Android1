package build.agcy.test1.Api.Meetings;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import build.agcy.test1.Api.ApiTaskBase;
import build.agcy.test1.Models.Meeting;

/**
 * Created by kiolt_000 on 17/08/2014.
 */
public abstract class MeetingListTask extends ApiTaskBase<Meeting[]> {

    // отправляем int offset, int count, float latitude, float longtitude
    // или отправляем массив ids
    public MeetingListTask(List<NameValuePair> nameValuePairs) {
        super("meetings/list", nameValuePairs, false, false);
    }

    @Override
    protected Meeting[] parse(String json) throws JSONException, FileNotFoundException {
            return new Gson().fromJson(new FileReader(json), Meeting[].class);

    }

}
