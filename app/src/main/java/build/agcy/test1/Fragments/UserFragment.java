package build.agcy.test1.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import build.agcy.test1.Api.Users.UserTask;
import build.agcy.test1.EatWithMeApp;
import build.agcy.test1.Models.User;
import build.agcy.test1.R;

/**
 * Created by Freeman on 16.09.2014.
 */
public class UserFragment extends Fragment {

    private String username;
    public User user;

    public UserFragment() {
    }

    private static View user_activity_View;

    private Handler handler = new Handler();
    public ListView msgView;
    public ArrayAdapter<String> msgList;

    public void sendMessageToServer(String str) {

        final String str1 = str;
        new Thread(new Runnable() {

            @Override
            public void run() {
                //String host = "opuntia.cs.utep.edu";
                String host = "10.0.2.2";
                String host2 = "127.0.0.1";
                PrintWriter out;
                try {
                    Socket socket = new Socket(host, 8008);
                    out = new PrintWriter(socket.getOutputStream());

                    // out.println("hello");
                    out.println(str1);
                    Log.d("", "hello");
                    out.flush();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    Log.d("", "hello222");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("", "hello4333");
                }

            }
        }).start();
    }


    public void receiveMsg() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                //final  String host="opuntia.cs.utep.edu";
                final String host = "10.0.2.2";
                //final String host="localhost";
                Socket socket = null;
                BufferedReader in = null;
                try {
                    socket = new Socket(host, 8008);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    if (socket != null) {
                        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                while (true) {
                    String msg = null;
                    try {
                        if (in != null) {
                            msg = in.readLine();
                        }
                        Log.d("", "MSGGG:  " + msg);

                        msgList.add(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (msg == null) {
                        break;
                    } else {
                        displayMsg(msg);
                    }
                }

            }
        }).start();


    }

    public void displayMsg(String msg) {
        final String mssg = msg;
        handler.post(new Runnable() {

            @Override
            public void run() {
                msgList.add(mssg);
                msgView.setAdapter(msgList);
                msgView.smoothScrollToPosition(msgList.getCount() - 1);
                Log.d("", "hi");
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//            if (user_activity_View != null) {
//                ViewGroup parent = (ViewGroup) user_activity_View.getParent();
//                if (parent != null)
//                    parent.removeView(user_activity_View);
//            }
        if (savedInstanceState != null) {
        } else {
            user_activity_View = inflater.inflate(R.layout.fragment_user, container, false);
            Activity activity = getActivity();
            if (activity != null) {
                Intent intent = activity.getIntent();
                Bundle bundle = intent.getExtras();
                String user_id = bundle.getString("id");
                UserTask task = new UserTask(user_id) {
                    @Override
                    public void onSuccess(User user1) {
                        user = user1;
                        ((TextView) user_activity_View.findViewById(R.id.description)).setText(user.username);
                        (user_activity_View.findViewById(R.id.status)).setVisibility(View.GONE);
                        ImageLoader.getInstance().displayImage(EatWithMeApp.currentUser.photo, (ImageView) user_activity_View.findViewById(R.id.photo));

                        if (user.photo != null) {
                            ImageLoader.getInstance().displayImage(user.photo, (ImageView) user_activity_View.findViewById(R.id.photo));
                        } else {
                            if (EatWithMeApp.currentUser.photo != null) {
                                ImageLoader.getInstance().displayImage(EatWithMeApp.currentUser.photo, (ImageView) user_activity_View.findViewById(R.id.photo));
                            }
                        }
                    }

                    @Override
                    public void onError(Exception exp) {
                        Toast.makeText(getActivity(),
                                "usertask error" + exp.toString(), Toast.LENGTH_LONG).show();
                    }
                };
                task.start();
            } else {
                user = new User() {{
                    id = "123";
                    username = "Ivan";
                }};
                ((TextView) user_activity_View.findViewById(R.id.description)).setText(user.username);
                (user_activity_View.findViewById(R.id.status)).setVisibility(View.GONE);
            }

        }

        msgView = (ListView) user_activity_View.findViewById(R.id.message_list);
        msgList = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
//        msgList = new ArrayAdapter<String>(getActivity(),R.layout.drawer_list_item);
        msgView.setAdapter(msgList);
        msgView.smoothScrollToPosition(msgList.getCount() - 1);
        Button btnSend = (Button) user_activity_View.findViewById(R.id.btn_Send);
//        	receiveMsg();

        EditText input = (EditText) user_activity_View.findViewById(R.id.txt_inputText);
        input.setText("msg here");
        displayMsg("getting messages here item");
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText txtEdit = (EditText) user_activity_View.findViewById(R.id.txt_inputText);
                //msgList.add(txtEdit.getText().toString());
                sendMessageToServer(txtEdit.getText().toString());
                msgView.smoothScrollToPosition(msgList.getCount() - 1);
            }
        });
        return user_activity_View;
    }
}

