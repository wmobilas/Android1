package build.agcy.test1.Start;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by kiolt_000 on 17/08/2014.
 */
public abstract class RegisterTask extends LoginTask {
    public RegisterTask(final String login, final String password) {
        super("account/register", new ArrayList<NameValuePair>(){{
            add(new BasicNameValuePair("login",login));
            add(new BasicNameValuePair("password",password));
        }});
    }
}