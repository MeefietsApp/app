package nl.hypothermic.meefietsen.core;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import nl.hypothermic.meefietsen.R;
import nl.hypothermic.mfsrv.obj.event.Event;

public class EventViewAdapter extends RecyclerView.Adapter<EventViewAdapter.EventViewHolder> {

    private ArrayList<Event> events;

    public EventViewAdapter(ArrayList<Event> persons) {
        this.events = persons;
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        public CardView card;
        public TextView eventName;

        public EventViewHolder(View view) {
            super(view);
            card = view.findViewById(R.id.card);
            eventName = view.findViewById(R.id.event_name);
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
        holder.eventName.setText(events.get(position).getIdentifier());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}