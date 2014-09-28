package build.agcy.test1.Main;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.makeramen.RoundedDrawable;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import build.agcy.test1.Api.Meetings.MeetingHistoryTask;
import build.agcy.test1.Core.Helpers.Converters;
import build.agcy.test1.EatWithMeApp;
import build.agcy.test1.Meetings.MeetingActivity;
import build.agcy.test1.Meetings.MeetingHistoryAdapter;
import build.agcy.test1.Models.Meeting;
import build.agcy.test1.R;

public class ProfileFragment extends Fragment {
    String backgroundUrl = Converters.getStaticMapImageUrl(
            EatWithMeApp.currentUser.longitude,
            EatWithMeApp.currentUser.latitude,
            640, 640,
            7);
    private View rootView;
    Activity activity;
    MeetingHistoryTask task;

    @Override
    public void onDestroy() {
        super.onDestroy();
        task.cancel(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = getActivity();
        if (savedInstanceState == null) {
//            TextView userNameView = (TextView) rootView.findViewById(R.id.user_name);
//            userNameView.setText(EatWithMeApp.currentUser.username);
            rootView = inflater.inflate(R.layout.fragment_profile, container, false);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
            final ListView meetingListView = (ListView) rootView.findViewById(R.id.history_list);
            task = new MeetingHistoryTask() {
                @Override
                public void onSuccess(final Meeting[] response) {
                    if (response.length == 0) {
                        rootView.findViewById(R.id.history_text).setVisibility(View.VISIBLE);
                        rootView.findViewById(R.id.history_status).setVisibility(View.GONE);
                    } else
                        rootView.findViewById(R.id.history_status).setVisibility(View.GONE);
                    final MeetingHistoryAdapter adapter = new MeetingHistoryAdapter(getActivity(), new ArrayList<Meeting>(Arrays.asList(response)));
                    meetingListView.setAdapter(adapter);
                    meetingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            Bundle bundle = new Bundle();
                            bundle.putString("id", String.valueOf(response[(int) adapter.getItemId(position)].id));
//                            bundle.putString("id", String.valueOf(response[position].id));
                            Intent intent = new Intent(activity, MeetingActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                }

                @Override
                public void onError(Exception exp) {
                    Toast.makeText(activity, "MeetingListTaskError " + exp.toString(), Toast.LENGTH_LONG).show();
                }
            };
            task.start();
            if (!prefs.contains("saved")) {
                final ImageView photoView = (ImageView) rootView.findViewById(R.id.photo);
                final ImageView backgroundView = (ImageView) rootView.findViewById(R.id.background);
                ImageLoader.getInstance().displayImage(backgroundUrl, backgroundView);
                if (EatWithMeApp.currentUser.photo != null) {
                    ImageLoader.getInstance().displayImage(EatWithMeApp.currentUser.photo, photoView);
                    Handler handler2 = new Handler();
                    handler2.postDelayed(
                            new Runnable() {
                                public void run() {
                                    Bitmap profile = RoundedDrawable.drawableToBitmap(photoView.getDrawable());
                                    saveToInternalSorage(profile, "profile");
                                }
                            }, 2000L);
                }
                Handler handler = new Handler();
                handler.postDelayed(
                        new Runnable() {
                            public void run() {
                                Bitmap map = ((BitmapDrawable) backgroundView.getDrawable()).getBitmap();
                                saveToInternalSorage(map, "map");
                            }
                        }, 2000L);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("saved", "true");
                editor.commit();
            } else {
                loadImageFromStorage("map");
                if (EatWithMeApp.currentUser.photo != null) {
                    loadImageFromStorage("profile");
                }
            }
        }

        return rootView;
    }

    private void loadImageFromStorage(String name) {
        try {
            Uri uri = Uri.parse("file://" + Environment.getExternalStorageDirectory().toString() + "/Android/data/build.agcy.test1/cache/" + name + ".png");
            Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));

            ImageView img;
            if (name.equals("map")) {
                img = (ImageView) rootView.findViewById(R.id.background);
            } else {
                img = (ImageView) rootView.findViewById(R.id.photo);
            }
            img.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private String saveToInternalSorage(Bitmap bitmapImage, String name) {
        File directory = new File(Environment.getExternalStorageDirectory().toString() + "/Android/data/build.agcy.test1/cache/");
        File mypath = new File(directory, name + ".png");

        FileOutputStream fos;
        try {

            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return directory.getAbsolutePath();
    }

}
