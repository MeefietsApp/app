package nl.hypothermic.meefietsen.core;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import nl.hypothermic.meefietsen.FeedActivity;
import nl.hypothermic.meefietsen.R;
import nl.hypothermic.meefietsen.async.GenericCallback;
import nl.hypothermic.meefietsen.dialogs.EventCreateDialog;
import nl.hypothermic.meefietsen.integrity.PhoneNumberFormatter;
import nl.hypothermic.mfsrv.obj.auth.TelephoneNum;
import nl.hypothermic.mfsrv.obj.event.Event;
import nl.hypothermic.mfsrv.obj.event.MeefietsEvent;

public class EventViewAdapter<T extends Event> extends RecyclerView.Adapter<EventViewAdapter.EventViewHolder> {

    private static final DateFormat shortDateFormat = new SimpleDateFormat("HH:mm");
    private static final DateFormat ddmmDateFormat = new SimpleDateFormat("dd-MM");

    private ArrayList<T> events;

    public EventViewAdapter(ArrayList<T> persons) {
        this.events = persons;
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        public CardView card;
        public TextView eventName;
        public TextView eventLocation;
        public TextView eventTime;
        public TextView eventDate;

        public LinearLayout options;
        public ImageView optionAdd;

        public EventViewHolder(View view) {
            super(view);
            card = view.findViewById(R.id.card);
            eventName = view.findViewById(R.id.event_name);
            eventLocation = view.findViewById(R.id.event_loc);
            eventTime = view.findViewById(R.id.event_time);
            eventDate = view.findViewById(R.id.event_date);

            options = view.findViewById(R.id.options);
            optionAdd = view.findViewById(R.id.option_add);
        }

        public void setTime(String formattedTime) {
            this.eventTime.setText(formattedTime);
            this.eventTime.setVisibility(View.VISIBLE);
        }

        public void setDate(String formattedDate) {
            this.eventDate.setText(formattedDate);
            this.eventDate.setVisibility(View.VISIBLE);
        }
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_meefietsevent, parent, false));
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        final Event e = events.get(position);
        holder.eventName.setText(e.getIdentifier());
        if (events.get(position) instanceof MeefietsEvent) {
            MeefietsEvent me = (MeefietsEvent) e;
            if (me.eventLocation != null) {
                holder.eventLocation.setText(me.eventLocation);
            } else {
                holder.eventLocation.setHeight(0);
            }
            if (me.eventEpochTime > 0) {
                Date date = new Date(me.eventEpochTime * 1000);
                holder.setTime(shortDateFormat.format(date));
                holder.setDate(ddmmDateFormat.format(date));
            }
            holder.optionAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new EventCreateDialog<>(new GenericCallback<HashMap<String, String>>() {
                        @Override
                        public void onAction(final HashMap<String, String> val) {
                            TelephoneNum target;
                            try {
                                target = PhoneNumberFormatter.toTelephoneNum(val.get("num"));
                            } catch (PhoneNumberFormatter.PhoneNumberParseException e) {
                                FeedActivity.showToast(FeedActivity.act.getString(R.string.interact_contacts_add_incorrect_number));
                                e.printStackTrace();
                                return;
                            }
                            MeefietsClient.getInstance().eventAddUser(e.eventId, target, new GenericCallback<Boolean>() {
                                @Override
                                public void onAction(Boolean val) {
                                    if (val != null && val) {
                                        FeedActivity.showToast(FeedActivity.act.getString(R.string.interact_events_invite_success));
                                    } else {
                                        FeedActivity.showToast(FeedActivity.act.getString(R.string.interact_events_invite_error));
                                    }
                                }
                            });
                        }
                    }).setTitle(FeedActivity.act.getString(R.string.dialog_std_userinvite_title))
                            .setPosNegBtns(FeedActivity.act.getString(R.string.dialog_std_userinvite_positive),
                                           FeedActivity.act.getString(R.string.dialog_std_userinvite_negative))
                            .addField("num", FeedActivity.act.getString(R.string.dialog_std_userinvite_field_number))
                            .onCreateDialog(null)
                            .show();
                }
            });
        } else {
            holder.options.setLayoutParams(new LinearLayout.LayoutParams(holder.options.getLayoutParams().width, 0));
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}