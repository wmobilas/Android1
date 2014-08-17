package build.agcy.test1.Meetings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import build.agcy.test1.Chat.AndyChatActivity;
import build.agcy.test1.R;

/**
 * Created by Freeman on 15.08.2014.
 */

public class MeetingsListActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_list);

        runOnUiThread(new Runnable() {
                          public void run() {
                              ArrayList<HashMap<String, String>> meetingsList = new ArrayList<HashMap<String, String>>();
                              HashMap<String, String> map = new HashMap<String, String>();
                              map.put("textreference", "peopleference");
                              map.put("textname", "peoplename");
                              meetingsList.add(map);
                              meetingsList.add(map);
                              ListView lv = (ListView) findViewById(R.id.list);
                              ListAdapter adapter = new SimpleAdapter(MeetingsListActivity.this, meetingsList,
                                      R.layout.item_meeting,
                                      new String[]{"textreference", "textname"}, new int[]{
                                      R.id.reference, R.id.name});

                              lv.setAdapter(adapter);
                              lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                  @Override
                                  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                      startActivity(new Intent(getBaseContext(), MeetingActivity.class));
                                  }
                              });
                              // todo: open an MeetingActivity normally
                              // Adding data into listview прежде чем перенаправлять на активити
                              // ну я еще не умеюнастроек нужно сделать диалог, что у юзера отключен гпс
                          }
                      });


    Bundle bundle = this.getIntent().getExtras();
    //        Object message = getIntent().getStringExtra("EXTRA_MESSAGE");
//        String[] mess = new String[2];
    String[] mess =(bundle.getStringArray("1"));
        if ((mess[1]!="") || (mess[0]!="")){
    CToast(mess[1], mess[0], "red");}
        else{CToast("no", "thing", "red");}
}
    public void chatOpen(View view) {
        Intent intent = new Intent(this,AndyChatActivity.class);
        startActivity(intent);
    }

    public void mapOpen(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }
    public void CToast(String t1, String t2, String c) {
        if (c == "same") {
            c = "444444";
        } else if (c == "blue") {
            c = "0099cc";
        } else if (c == "red") {
            c = "cc0000";
        }

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout,
                (ViewGroup) findViewById(R.id.toast_layout_root));
        TextView textCToast = (TextView) layout.findViewById(R.id.text);

        String text2 = "<font color=#ffffff>" + t1 + "</font> <font color=#" + c + ">" + t2 + "</font";
        textCToast.setText(Html.fromHtml(text2));

        Toast toast = new Toast(this);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }


}
