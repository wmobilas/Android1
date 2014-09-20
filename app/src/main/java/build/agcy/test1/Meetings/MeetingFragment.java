package build.agcy.test1.Meetings;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import build.agcy.test1.Api.Meetings.MeetingAcceptTask;
import build.agcy.test1.Api.Meetings.MeetingAcceptsListTask;
import build.agcy.test1.Api.Meetings.MeetingConfirmTask;
import build.agcy.test1.Api.Meetings.MeetingGetTask;
import build.agcy.test1.Core.Helpers.Converters;
import build.agcy.test1.EatWithMeApp;
import build.agcy.test1.Models.Meeting;
import build.agcy.test1.R;
import build.agcy.test1.Users.UserActivity;

/**
 * A placeholder fragment containing a simple view.
 */

public class MeetingFragment extends Fragment {

    private Meeting meeting;
    private static View meetingView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            meetingView = inflater.inflate(R.layout.fragment_meeting, container, false);
            bindData();
        }
        return meetingView;
    }

    private void bindData() {
        if (meeting != null && meetingView != null) {
            View ownerContainer = meetingView.findViewById(R.id.user_container);
            TextView descriptionView = (TextView) meetingView.findViewById(R.id.description);
            ImageView imageView = (ImageView) meetingView.findViewById(R.id.image);
            TextView ownerNameView = (TextView) ownerContainer.findViewById(R.id.user_name);
            ImageView ownerPhotoView = (ImageView) ownerContainer.findViewById(R.id.user_photo);
            ownerContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), UserActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", meeting.owner.id);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            descriptionView.setText(meeting.description);
            ownerNameView.setText("by " + meeting.owner.username);
            if (meeting.owner.photo != null) {
                ImageLoader.getInstance().displayImage(meeting.owner.photo, ownerPhotoView);
            }
            String imageUrl = Converters.getStaticMapImageUrl(meeting.longitude, meeting.latitude, 640, 360, 10, "black", "here");
            ImageLoader.getInstance().displayImage(imageUrl, imageView);
            Fragment fragment;
            Bundle arguments = getArguments();
            if (EatWithMeApp.isOwner(meeting.owner.id)) {
                if (!meeting.isConfirmed())
                    // показываем хозяину запросы
                    fragment = new AcceptListFragment();
                else {
                    // показываем кого он выбрал
                    arguments.putString("status", "owner");
                    arguments.putString("confirmer_name", meeting.confirmer.username);
                    arguments.putString("confirmer_id", meeting.confirmer.id);
                    arguments.putString("confirmer_photo", meeting.confirmer.photo);
                    fragment = new ConfirmedFragment();
                }
            } else {

                if (meeting.accept == null) {
                    if (!meeting.isConfirmed()) {
                        // показываем окошко запроса
                        arguments.putString("meetingId", meeting.id);
                        fragment = new AcceptFragment();
                    } else
                        // показываем, что уже поздно
                        fragment = new ConfirmedFragment();
                } else {
                    if (!meeting.isConfirmed()) {
                        // запрос отправлен
                        arguments.putString("id", meeting.accept.id);
                        arguments.putString("message", meeting.accept.message);
                        arguments.putInt("time", meeting.accept.time);
                        fragment = new AcceptFragment();
                    } else {
                        // с вами встретятся =)
                        arguments.putString("status", "acceptor");
                        arguments.putString("confirmer_id", meeting.confirmer.id);
                        fragment = new ConfirmedFragment();
                    }
                }
            }
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction().replace(R.id.action_container, fragment).commit();
            View loadingView = meetingView.findViewById(R.id.loading);
            loadingView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle meetingArgs = getArguments();
        String id = meetingArgs.getString("id");

        MeetingGetTask meetingTask = new MeetingGetTask(id) {
            @Override
            public void onSuccess(Meeting response) {
                meeting = response;
                bindData();

            }

            @Override
            public void onError(Exception exp) {
                Toast.makeText(getActivity(), "Error " + exp.getMessage(), Toast.LENGTH_SHORT).show();

            }
        };
        meetingTask.start();
    }

    public static class AcceptFragment extends Fragment {

        private String meetingId;
        View rootView = null;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            meetingId = getArguments().getString("meetingId", null);

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            if (savedInstanceState != null) {
            } else {
                if (meetingId == null) {
                    rootView = inflater.inflate(R.layout.fragment_accepted, null);
                    TextView messageView = (TextView) rootView.findViewById(R.id.message);
                    messageView.setText(getArguments().getString("message", "Empty message :O"));
                } else {
                    rootView = inflater.inflate(R.layout.fragment_accept, null);

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
                            MeetingAcceptTask acceptTask = new MeetingAcceptTask(meetingId, messageBox.getText().toString()) {
                                @Override
                                public void onSuccess(String response) {
                                    accepting.dismiss();
                                    messageBox.setVisibility(View.GONE);
                                    acceptButton.setVisibility(View.GONE);
                                    statusView.setText("Accept sended");
                                }

                                @Override
                                public void onError(Exception exp) {
                                    accepting.dismiss();
                                    Toast.makeText(getActivity(), "Error:" + exp.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            };
                            acceptTask.start();
                        }
                    });
                }
            }
            return rootView;
        }
    }

    public static class ConfirmedFragment extends Fragment {
        private View rootView;
        private String meetingId;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            meetingId = getArguments().getString("id", "0");
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_confirmed, null);
            TextView messageView = (TextView) rootView.findViewById(R.id.message);

            String status = getArguments().getString("status");
            if (status == null) {
                messageView.setText("This meeting is finished");
            } else {
                if (status.equals("owner")) {
                    messageView.setText("You confirmed " + getArguments().getString("confirmer_name") + " to your meeting");
                } else {
                    //todo проверка что не я владелец
                    messageView.setText("You've been confirmed to this meeting. WOWOW");
                }
            }
            return rootView;
        }
    }

    public static class AcceptListFragment extends Fragment {
        private View rootView;
        private String meetingId;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            meetingId = getArguments().getString("id", "0");
        }

        @Override
        public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_accepts_list, null);
            final ListView listView = (ListView) rootView.findViewById(R.id.list);
            final View loadingView = rootView.findViewById(R.id.loading);
            MeetingAcceptsListTask task = new MeetingAcceptsListTask(meetingId) {
                @Override
                public void onSuccess(final Meeting.Accept[] response) {
                    loadingView.setVisibility(View.GONE);
                    if (response.length > 0) {
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
                                ImageView photoView = (ImageView) rootView.findViewById(R.id.photo);
                                if (accept.acceptor.photo != null) {
                                    ImageLoader.getInstance().displayImage(accept.acceptor.photo, photoView);
                                }
                                acceptorIdView.setText(accept.acceptor.username);
                                if (accept.message != null) {
                                    messageView.setText(accept.message);
                                }
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
                    } else {
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
