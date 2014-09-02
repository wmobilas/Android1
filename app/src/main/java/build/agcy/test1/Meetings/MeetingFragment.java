package build.agcy.test1.Meetings;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import build.agcy.test1.Api.Meetings.MeetingAcceptTask;
import build.agcy.test1.Api.Meetings.MeetingAcceptsListTask;
import build.agcy.test1.Api.Meetings.MeetingConfirmTask;
import build.agcy.test1.Api.Meetings.MeetingGetTask;
import build.agcy.test1.EatWithMeApp;
import build.agcy.test1.Models.Meeting;
import build.agcy.test1.R;

/**
 * A placeholder fragment containing a simple view.
 */

public class MeetingFragment extends Fragment {

    private Meeting meeting;
    private MeetingGetTask meetingTask;


    public static View meetingView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try{
            meetingView = inflater.inflate(R.layout.fragment_meeting, container, false);
        } catch (InflateException e) {
            Log.e("MeetingFragment","Cant inflate",e);
        }

        bindData();

        return meetingView;
    }

    private void bindData() {
        if (meeting != null && meetingView != null) {
            ProgressBar loadingView = (ProgressBar) meetingView.findViewById(R.id.loading);
            View containerView = meetingView.findViewById(R.id.content_container);

            TextView descriptionView = (TextView) meetingView.findViewById(R.id.description);
            ImageView imageView = (ImageView) meetingView.findViewById(R.id.image);
            TextView creatorView = (TextView) meetingView.findViewById(R.id.creator);


            if (meeting != null) {
                loadingView.setVisibility(View.GONE);
                containerView.setVisibility(View.VISIBLE);

                descriptionView.setText(meeting.description);
                creatorView.setText("by " + meeting.creator);

                String imageUrl = "" +
                        "http://maps.googleapis.com/maps/api/staticmap?zoom=10&size=640x360&maptype=roadmap" +
                        "&markers=color:red%7Clabel:Here%7C" +
                        meeting.longitude + "," +
                        meeting.latitude + "";
                ImageLoader.getInstance().displayImage(imageUrl, imageView);

                if (!meeting.isConfirmed())
                    if (EatWithMeApp.isOwner(meeting.creator)) {
                        getFragmentManager().beginTransaction().add(R.id.action_container, new AcceptListFragment()).commit();
                    } else {
                        getFragmentManager().beginTransaction().add(R.id.action_container, new AcceptFragment()).commit();
                    }
                else {
                    getFragmentManager().beginTransaction().add(R.id.action_container, new ConfirmedFragment()).commit();
                }

            } else {
                // =(
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle meetingArgs = getArguments();
        String id = meetingArgs.getString("id");

        meetingTask = new MeetingGetTask(id) {
            @Override
            public void onSuccess(Meeting response) {
                meeting = response;
                bindData();

            }

            @Override
            public void onError(Exception exp) {
                Toast.makeText(getActivity(),"Error " + exp.getMessage(),Toast.LENGTH_SHORT).show();

            }
        };
        meetingTask.start();
    }

    public class AcceptFragment extends Fragment{
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_accept,null);
            final Button acceptButton = (Button) rootView.findViewById(R.id.accept);
            final TextView messageBox = (TextView) rootView.findViewById(R.id.message);
            final TextView statusView = (TextView) rootView.findViewById(R.id.status);

            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final ProgressDialog accepting = new ProgressDialog(getActivity());
                    accepting.setMessage("Accepting");
                    accepting.setTitle("Wait pls");
                    accepting.setCancelable(false);
                    accepting.show();
                    MeetingAcceptTask acceptTask = new MeetingAcceptTask(meeting.Id, messageBox.getText().toString()) {
                        @Override
                        public void onSuccess(String response) {
                            accepting.dismiss();
                            messageBox.setVisibility(View.GONE);
                            acceptButton.setVisibility(View.GONE);

                            statusView.setText("Accept sended");
                        }

                        @Override
                        public void onError(Exception exp) {

                        }
                    };
                    acceptTask.start();

                }
            });

            return rootView;
        }
    }
    public class ConfirmedFragment extends Fragment{
        private View rootView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_confirmed,null);
            return rootView;
        }
    }

    private class AcceptListFragment extends Fragment {
        private View rootView;

        @Override
        public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_accepts_list, null);
            final ListView listView = (ListView) rootView.findViewById(R.id.list);
            final View loadingView = rootView.findViewById(R.id.loading);
            MeetingAcceptsListTask task = new MeetingAcceptsListTask(meeting.Id) {
                @Override
                public void onSuccess(final Meeting.Accept[] response) {
                    loadingView.setVisibility(View.GONE);
                    if(response.length>0) {
                        listView.setVisibility(View.VISIBLE);
                        loadingView.setVisibility(View.GONE);
                        listView.setAdapter(new BaseAdapter() {
                            @Override
                            public int getCount() {
                                return response.length;
                            }

                            @Override
                            public Meeting.Accept getItem(int i) {
                                return response[i];
                            }

                            @Override
                            public long getItemId(int i) {
                                return 0;
                            }

                            @Override
                            public View getView(int i, View view, ViewGroup viewGroup) {
                                View itemView = inflater.inflate(R.layout.item_accept, null);
                                TextView acceptorIdView = (TextView) itemView.findViewById(R.id.acceptorId);
                                TextView messageView = (TextView) itemView.findViewById(R.id.message);

                                Meeting.Accept accept = getItem(i);

                                acceptorIdView.setText(accept.acceptorId);
                                messageView.setText(accept.message);
                                return itemView;
                            }
                        });
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                final Meeting.Accept accept = response[i];
                                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                                        .setCancelable(true)
                                        .setTitle("Confirm this?")
                                        .setMessage(accept.message)
                                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                final ProgressDialog confirmingDialog = new ProgressDialog(getActivity());
                                                confirmingDialog.setMessage("Confirming");
                                                confirmingDialog.show();
                                                new MeetingConfirmTask(accept.id) {
                                                    @Override
                                                    public void onSuccess(String response) {
                                                        confirmingDialog.dismiss();
                                                    }

                                                    @Override
                                                    public void onError(Exception exp) {
                                                        confirmingDialog.dismiss();
                                                        Toast.makeText(getActivity(), "Confirming error", Toast.LENGTH_SHORT).show();

                                                    }
                                                }.start();
                                            }
                                        })
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        }).show();


                            }
                        });
                    }else{
                        TextView statusView = (TextView) rootView.findViewById(R.id.status);
                        statusView.setVisibility(View.VISIBLE);
                        statusView.setText("None still respond to your meeting");
                    }
                }

                @Override
                public void onError(Exception exp) {

                }
            };
            task.start();
            return rootView;
        }
    }
}
