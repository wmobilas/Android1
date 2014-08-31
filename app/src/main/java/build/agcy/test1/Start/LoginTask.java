package build.agcy.test1.Start;

import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import build.agcy.test1.Api.ApiTaskBase;
import build.agcy.test1.Api.Errors.ApiError;
import build.agcy.test1.Models.CurrentUser;


public abstract class LoginTask extends ApiTaskBase<CurrentUser> {
    private String token = "";

    public LoginTask(String methodName, ArrayList<NameValuePair> args){
        super(methodName, args, false, false);
    }

    public LoginTask(final String login, final String password) {
        super("account/login", new ArrayList<NameValuePair>(){{
            add(new BasicNameValuePair("login",login));
            add(new BasicNameValuePair("password",password));
        }}, false, false);
    }

    @Override
    protected CurrentUser parse(String json) throws JSONException {
        return new Gson().fromJson(json, CurrentUser.class);
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
                url += "?" + args;
                request = new HttpGet(url);
            }
            HttpResponse httpResponse = httpClient.execute(request);
            HttpEntity httpEntity = httpResponse.getEntity();
            String responseStr = EntityUtils.toString(httpEntity);
            String token = "";
            List<Cookie> cookies = httpClient.getCookieStore().getCookies();
            for(Cookie cookie: cookies){
                if(cookie.getName().equals(".AspNet.ApplicationCookie")){
                    token = cookie.getValue();
                    break;
                }
            }

            this.token = token;
            Log.i(LOG_TAG, "Server response " + responseStr);
            return responseStr;
        } catch (Exception exp) {
            Log.e(LOG_TAG, "Loader error " + exp.toString());
            return exp;
        }
    }

    @Override
    protected void onPostExecute(Object response) {
        super.onPostExecute(response);
        if(!token.equals(""))
            onTokenRecieved(token);
    }

    public abstract void onTokenRecieved(String token);


}
