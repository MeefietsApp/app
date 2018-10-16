package nl.hypothermic.meefietsen.core;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import nl.hypothermic.meefietsen.R;
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

        public EventViewHolder(View view) {
            super(view);
            card = view.findViewById(R.id.card);
            eventName = view.findViewById(R.id.event_name);
            eventLocation = view.findViewById(R.id.event_loc);
            eventTime = view.findViewById(R.id.event_time);
            eventDate = view.findViewById(R.id.event_date);
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
        Event e = events.get(position);
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
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}