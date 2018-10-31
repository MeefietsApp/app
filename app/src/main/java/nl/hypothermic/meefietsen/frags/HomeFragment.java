package nl.hypothermic.meefietsen.frags;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import nl.hypothermic.foscamlib.net.NetResponse;
import nl.hypothermic.meefietsen.FeedActivity;
import nl.hypothermic.meefietsen.R;
import nl.hypothermic.meefietsen.ResponseCode;
import nl.hypothermic.meefietsen.async.GenericCallback;
import nl.hypothermic.meefietsen.core.ClientEventManager;
import nl.hypothermic.meefietsen.core.EventViewAdapter;
import nl.hypothermic.meefietsen.core.MeefietsClient;
import nl.hypothermic.meefietsen.dialogs.EventCreateDialog;
import nl.hypothermic.mfsrv.obj.NetArrayList;
import nl.hypothermic.mfsrv.obj.NetWrappedObject;
import nl.hypothermic.mfsrv.obj.event.Event;

public class HomeFragment extends Fragment {

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FeedActivity.act.setHomeFragment(this);
        final RecyclerView events = FeedActivity.act.findViewById(R.id.event_view);
        events.setLayoutManager(new LinearLayoutManager(FeedActivity.act.getBaseContext()));
        events.setAdapter(new EventViewAdapter(/*ClientEventManager.getInstance().getEvents()*/));

        // TODO fix spaghetti code :)
        (FeedActivity.act.findViewById(R.id.fab_event_add)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EventCreateDialog<>(new GenericCallback<HashMap<String, String>>() {
                    @Override
                    public void onAction(final HashMap<String, String> val) {
                        final Calendar cal = Calendar.getInstance();

                        DatePickerDialog datedd = new DatePickerDialog(FeedActivity.act, new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker datepicker, final int selectedYear, final int selectedmonth, final int selectedDay) {
                                TimePickerDialog timedd;
                                final int selectedMonth = selectedmonth + 1;
                                timedd = new TimePickerDialog(FeedActivity.act, new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker timePicker, final int selectedHour, final int selectedMinute) {
                                        String str = (selectedMonth > 9 ? selectedMonth : "0" + selectedMonth) + " "
                                                   + (selectedDay > 9 ? selectedDay : "0" + selectedDay) + " "
                                                   + selectedYear + " "
                                                   + (selectedHour > 9 ? selectedHour : "0" + selectedHour) + " "
                                                   + (selectedMinute > 9 ? selectedMinute : "0" + selectedMinute);
                                        SimpleDateFormat df = new SimpleDateFormat("MM dd yyyy HH mm");
                                        Date date = null;
                                        try {
                                            date = df.parse(str);
                                        } catch (ParseException e) {
                                            FeedActivity.act.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(FeedActivity.act, "Failed to parse date!", Toast.LENGTH_LONG);
                                                }
                                            });
                                            e.printStackTrace();
                                        }
                                        long epoch = date.getTime() / 1000;
                                        // hardcoded: meefietsevent!!! (zie volgende regel, 1ste arg)
                                        MeefietsClient.getInstance().createEvent(1, val.get("name"), val.get("loc"), epoch, new GenericCallback<NetResponse<NetWrappedObject>>() {
                                            @Override
                                            public void onAction(final NetResponse<NetWrappedObject> val) {
                                                if (val != null && val.code == ResponseCode.SUCCESS && val.object != null) {
                                                    MeefietsClient.getInstance().eventAddUser((Integer) val.object.getValue(), MeefietsClient.getInstance().localAccount.num, new GenericCallback<Boolean>() {
                                                        @Override
                                                        public void onAction(Boolean xval) {
                                                            if (xval != null && xval) {
                                                                MeefietsClient.getInstance().getEvent((Integer) val.object.getValue(), new GenericCallback<NetResponse<Event>>() {
                                                                    @Override
                                                                    public void onAction(NetResponse<Event> val) {
                                                                        if (val != null && val.code == ResponseCode.SUCCESS && val.object != null) {
                                                                            System.out.println("Retrieved newly created event");
                                                                            ClientEventManager.getInstance().addEvent(val.object);
                                                                            System.out.println("EVT UPDATE UI");
                                                                            FeedActivity.act.runOnUiThread(new Runnable() {
                                                                                @Override
                                                                                public void run() {
                                                                                    events.getAdapter().notifyDataSetChanged();
                                                                                }
                                                                            });
                                                                        } else {
                                                                            System.out.println("Failed to retrieve newly created event");
                                                                        }
                                                                    }
                                                                });
                                                            } else {
                                                                System.out.println("Error when adding user to new event!");
                                                            }
                                                        }
                                                    });
                                                } else {
                                                    System.out.println("Failed to create event!");
                                                    FeedActivity.act.runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(FeedActivity.act, "Failed to create event! (" + val.code.getValue() + ")", Toast.LENGTH_LONG);
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                        timePicker.cancelLongPress();
                                    }
                                }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
                                timedd.setTitle("Select Time");
                                timedd.show();
                            }
                        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                        datedd.setTitle("Select Date");
                        datedd.show();
                    }
                }).setTitle("Create event")
                  .setPosNegBtns("OK", "Cancel")
                  .addField("name", "Name")
                  .addField("loc", "Location")
                  .onCreateDialog(null)
                  .show();
            }
        });

        MeefietsClient.getInstance().getEvents(new GenericCallback<NetResponse<NetArrayList<Integer>>>() {
            @Override
            public void onAction(NetResponse<NetArrayList<Integer>> val) {
                if (val != null) {
                    if (val.code == ResponseCode.SUCCESS && val.object != null) {
                        ClientEventManager.getInstance().clearEvents();
                        // TODO dit kan beter gedaan worden... er hoeven niet duizenden threads aangemaakt te worden!!
                        final GenericCallback<Void> cb = new GenericCallback<Void>() {
                            @Override
                            public void onAction(Void val) {
                                FeedActivity.act.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        events.getAdapter().notifyDataSetChanged();
                                    }
                                });
                            }
                        };
                        for (Integer id : val.object) {
                            MeefietsClient.getInstance().getEvent(id, new GenericCallback<NetResponse<Event>>() {
                                @Override
                                public void onAction(NetResponse<Event> val) {
                                    if (val != null) {
                                        if (val.code == ResponseCode.SUCCESS && val.object != null) {
                                            ClientEventManager.getInstance().addEvent(val.object);
                                            cb.onAction(null);
                                        } else {
                                            System.out.println("Failed to update events: invalid event container");
                                        }
                                    } else {
                                        System.out.println("Failed to update events: invalid event response");
                                    }
                                }
                            });
                        }
                    } else {
                        System.out.println("Failed to update events: invalid container");
                    }
                } else {
                    System.out.println("Failed to update events: invalid response");
                }
            }
        });
    }
}
