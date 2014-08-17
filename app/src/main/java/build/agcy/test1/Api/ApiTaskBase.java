package build.agcy.test1.Api;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.List;

import build.agcy.test1.Api.Errors.ApiError;
import build.agcy.test1.Api.Errors.ApiParseError;

/**
 * Created by kiolt_000 on 06-May-14.
 */
public abstract class ApiTaskBase<T> extends AsyncTask<Object, Void, Object> {
    private static final String LOG_TAG = "Application Tag";
    private static final String apiUrl = "http://eathwithme.agcy.co.il/api/";
    private final String methodName;
    private final boolean post;
    private final Boolean loginRequred;
    List<NameValuePair> nameValuePairs;

    //посмотри что внутри метода происходит
    public ApiTaskBase(String methodName, List<NameValuePair> nameValuePairs, boolean post, Boolean loginRequred) {
        this.nameValuePairs = nameValuePairs;
        this.methodName = methodName;
        this.post = post;
        this.loginRequred = loginRequred;
    }

    @Override
    protected Object doInBackground(Object... params) {
        try {
            String url = apiUrl + methodName;

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpRequestBase request;
            if (post) {
                request = new HttpPost(url);
                ((HttpPost) request).setEntity(new UrlEncodedFormEntity(nameValuePairs));
            } else {
                String args = URLEncodedUtils.format(nameValuePairs, "utf-8");
                url += args;
                request = new HttpGet(url);
            }
            if(loginRequred){
                // todo: Token передаётся через куки. Чуть позже разберусь как конкретно.
            }
            HttpResponse httpResponse = httpClient.execute(request);
            HttpEntity httpEntity = httpResponse.getEntity();
            String responseStr = EntityUtils.toString(httpEntity);
            Log.i(LOG_TAG, "Server response " + responseStr);
            return responseStr;

        } catch (Exception exp) {
            Log.e(LOG_TAG, "Loader error " + exp.toString());
            return exp;
        }
    }

    @Override
    protected void onPostExecute(Object response) {
        if(response instanceof Exception){
            onError((Exception) response);
        }else{
            try {
                JSONObject jsonObject = new JSONObject((String) response);
                int code = jsonObject.getInt("code");
                if(code==1){
                    onSuccess(parse(jsonObject.getString("response")));
                }else{
                    onError(new ApiError(code, jsonObject.getString("response")));
                }
                return;
            } catch (JSONException e) {
                Log.e(LOG_TAG,"Cant parse api response: " +response, e);
            } catch (FileNotFoundException e) {
                Log.e(LOG_TAG, "Cant parse api response: " + response, e);
            }
            onError(new ApiParseError());
        }
    }
    protected abstract T parse(String json) throws JSONException, FileNotFoundException;//new Gson().fromJson(jsonObject.getString("response"), YOURCLASS);
    public abstract void onSuccess(T response);
    public abstract void onError(Exception exp);
}
