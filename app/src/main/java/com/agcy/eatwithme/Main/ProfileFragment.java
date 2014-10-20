package com.agcy.eatwithme.Main;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.agcy.eatwithme.Api.Meetings.MeetingGetTask;
import com.agcy.eatwithme.Api.Meetings.MeetingHistoryTask;
import com.agcy.eatwithme.Core.Helpers.Converters;
import com.agcy.eatwithme.EatWithMeApp;
import com.agcy.eatwithme.Meetings.MeetingActivity;
import com.agcy.eatwithme.Meetings.MeetingHistoryAdapter;
import com.agcy.eatwithme.Models.Meeting;
import com.agcy.eatwithme.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {
    String backgroundUrl = Converters.getStaticMapImageUrl(
            EatWithMeApp.currentUser.longitude,
            EatWithMeApp.currentUser.latitude,
            640, 640,
            7);
    private View rootView;
    Activity activity;
    Meeting[] response;
    MeetingHistoryTask task;
    ListView meetingListView;
    final int[] counter = {1};

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (task != null) {
            task.cancel(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(getActivity());

        activity = getActivity();
        if (savedInstanceState == null) {
            rootView = inflater.inflate(R.layout.fragment_profile, container, false);
            final ImageView backgroundView = (ImageView) rootView.findViewById(R.id.background);
            ImageLoader _imageLoader = ImageLoader.getInstance();
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                    .build();
            _imageLoader.displayImage(backgroundUrl, backgroundView, options);
// , new ImageLoadingListener() {
//                    @Override
//                    public void onLoadingStarted(String s, View view) {
//
//                    }
//
//                    @Override
//                    public void onLoadingFailed(String s, View view, FailReason failReason) {
//
//                    }
//
//                    @Override
//                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
//                        backgroundView.setImageBitmap(bitmap);
//                        Bitmap image = ((BitmapDrawable) backgroundView.getDrawable()).getBitmap();
//                        saveToInternalSorage(image, "map");
//                    }
//
//                    @Override
//                    public void onLoadingCancelled(String s, View view) {
//
//                    }
//                });
            if (EatWithMeApp.currentUser.photo != null) {
                final ImageView photoView = (ImageView) rootView.findViewById(R.id.photo);
                _imageLoader.displayImage(EatWithMeApp.currentUser.photo, photoView, options);
//                            , new ImageLoadingListener() {
//                        @Override
//                        public void onLoadingStarted(String s, View view) {
//                        }
//
//                        @Override
//                        public void onLoadingFailed(String s, View view, FailReason failReason) {
//                        }
//
//                        @Override
//                        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
//                            photoView.setImageBitmap(bitmap);
//                            Bitmap image = RoundedDrawable.drawableToBitmap(photoView.getDrawable());
////                            saveToInternalSorage(image, "photo");
//                        }
//
//                        @Override
//                        public void onLoadingCancelled(String s, View view) {
//                        }
//                    });

            }
//                loadImageFromStorage("map");
//                if (EatWithMeApp.currentUser.photo != null) {
//                    loadImageFromStorage("photo");
//                }
//        final View myView = getActivity().getLayoutInflater().inflate(R.layout.fragment_meeting_list, null);
//            final View myView = rootView.findViewById(R.id.meeting_list_fragment);
            final ProgressBar loadingStatus = (ProgressBar) rootView.findViewById(R.id.history_status_loading);
            meetingListView = (ListView) rootView.findViewById(R.id.history_list);
            final ArrayList<Meeting> arrayList = new ArrayList<Meeting>();
            final MeetingHistoryAdapter adptr = new MeetingHistoryAdapter(activity, arrayList);
            meetingListView.setAdapter(adptr);
            final boolean[] loading = {true};
            meetingListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view,
                                                 int scrollState) {
                    // Do nothing
                    Log.d("", "ONSCROLL STATE CHANGED");
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {
                    Log.d("", "ONSCROLL NOW");
                    // threshold being indicator if bottom of list is hit
                    if ((loading[0]) && (firstVisibleItem > totalItemCount - visibleItemCount - 4)) {
                        loadingStatus.setVisibility(View.VISIBLE);

                        for (int i = counter[0] * 10; i < counter[0] * 10 + 10; i++) {
                            final int finalI = i;
                            loading[0] = false;
                            final int finalI1 = i;
                            new MeetingGetTask(String.valueOf(finalI)) {

                                @Override
                                public void onSuccess(Meeting m) {
                                    arrayList.add(m);
                                    if (finalI1 == counter[0] * 10 + 9) {
                                        loading[0] = true;
                                        loadingStatus.setVisibility(View.GONE);
                                        counter[0]++;
                                    }
                                    adptr.notifyDataSetChanged();

                                }

                                @Override
                                public void onError(Exception exp) {
//                                    loading[0]=false;
                                }
                            }.start();
                        }
//                        if (success){}
                    }
                }
            });

            meetingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    Bundle bundle = new Bundle();

                    bundle.putString("id", String.valueOf(adptr.getItem(position).id));
//                        bundle.putString("id", String.valueOf(response[(int) adptr.getItemId(position)].id));
//                            bundle.putString("id", String.valueOf(response[position].id));
                    Intent intent = new Intent(activity, MeetingActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            for (int i = 1; i < 10; i++) {
                final int finalI = i;
                new MeetingGetTask(String.valueOf(finalI)) {
                    @Override
                    public void onSuccess(Meeting m) {
                        arrayList.add(m);
                        adptr.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Exception exp) {

                    }
                }.start();

            }
//            if (!sp.contains("meetings")) {
//                rootView.findViewById(R.id.history_status).setVisibility(View.VISIBLE);
//                final ListView meetingListView = (ListView) rootView.findViewById(R.id.history_list);
//                task = new MeetingHistoryTask() {
//                    @Override
//                    public void onSuccess(final Meeting[] response) {
//                        if (response.length == 0) {
//                            rootView.findViewById(R.id.history_text).setVisibility(View.VISIBLE);
//                            rootView.findViewById(R.id.history_status).setVisibility(View.GONE);
//                        } else {
//                            sp.edit().putString("meetings", new Gson().toJson(response)).apply();
//                            ProfileFragment.this.response = response;
//                            rootView.findViewById(R.id.history_status).setVisibility(View.GONE);
//                            final MeetingHistoryAdapter adapter = new MeetingHistoryAdapter(getActivity(), new ArrayList<Meeting>(Arrays.asList(response)));
//                            adapter.notifyDataSetChanged();
//                            meetingListView.setAdapter(adapter);
//                            meetingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//                                @Override
//                                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                                    Bundle bundle = new Bundle();
//                                    task.cancel(false);
//                                    bundle.putString("id", String.valueOf(response[(int) adapter.getItemId(position)].id));
////                            bundle.putString("id", String.valueOf(response[position].id));
//                                    Intent intent = new Intent(activity, MeetingActivity.class);
//                                    intent.putExtras(bundle);
//                                    startActivity(intent);
//                                }
//                            });
//                        }
//                    }
//
//                    @Override
//                    public void onError(Exception exp) {
//                        Toast.makeText(activity, "MeetingListTaskError " + exp.toString(), Toast.LENGTH_LONG).show();
//                    }
//                };
//                task.start();

//            } else {
//                rootView = inflater.inflate(R.layout.fragment_profile, container, false);
//                response = new Gson().fromJson((sp.getString("meetings", "")), Meeting[].class);
//                final ListView meetingListView = (ListView) rootView.findViewById(R.id.history_list);
//                final MeetingHistoryAdapter adapter = new MeetingHistoryAdapter(getActivity(), new ArrayList<Meeting>(Arrays.asList(response)));
//                meetingListView.setAdapter(adapter);
//                meetingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                        Bundle bundle = new Bundle();
//                        bundle.putString("id", String.valueOf(response[(int) adapter.getItemId(position)].id));
////                            bundle.putString("id", String.valueOf(response[position].id));
//                        Intent intent = new Intent(activity, MeetingActivity.class);
//                        intent.putExtras(bundle);
////                        task.cancel(true);
//                        startActivity(intent);
//                    }
//                });
//
//            }
        }
        return rootView;
    }

//    private void loadImageFromStorage(String name) {
//        try {
//            Uri uri = Uri.parse("file:/" + getActivity().getApplicationContext().getCacheDir()+"/" + name + ".png");
////                    "file://" + Environment.getDownloadCacheDirectory().toString() + name + ".png");
//            Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
//
//            if (name.equals("map")) {
//                ImageView img= (ImageView) rootView.findViewById(R.id.background);
//                img.setImageBitmap(bitmap);
//            } else {
//                ImageView imgr= (ImageView) rootView.findViewById(R.id.photo);
//                imgr.setImageBitmap(bitmap);
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    private String saveToInternalSorage(Bitmap bitmapImage, String name) {
//        File directory = new File(getActivity().getApplicationContext().getCacheDir()+"/"+ name + ".png");
////        File directory = new File(Environment.getDownloadCacheDirectory().toString() + name + ".png");
////        File mypath = new File(directory, name + ".png");
//
//        FileOutputStream fos;
//        try {
//
//            fos = new FileOutputStream(directory);
//            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
//            fos.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return directory.getAbsolutePath();
//    }
}