package nl.hypothermic.meefietsen.core;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import nl.hypothermic.meefietsen.FeedActivity;
import nl.hypothermic.meefietsen.R;
import nl.hypothermic.meefietsen.async.GenericCallback;
import nl.hypothermic.mfsrv.obj.account.Account;

public class ContactsViewAdapter<T extends Account> extends RecyclerView.Adapter<ContactsViewAdapter.EventViewHolder> {

    private static RecyclerView.Adapter inst;

    private ArrayList<T> contacts;

    public ContactsViewAdapter(ArrayList<T> persons) {
        inst = this;
        this.contacts = persons;
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        public CardView card;
        public TextView username;
        public TextView telnum;

        public ImageView btnClose;

        public EventViewHolder(View view) {
            super(view);
            card = view.findViewById(R.id.card);
            username = view.findViewById(R.id.username);
            telnum = view.findViewById(R.id.telnum);
            btnClose = view.findViewById(R.id.btnClose);
        }
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_contact, parent, false));
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        final Account e = contacts.get(position);
        holder.username.setText(e.userName);
        holder.telnum.setText(e.num.toFormattedString());
        holder.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MeefietsClient.getInstance().deleteContact(e.num, new GenericCallback<Boolean>() {
                    @Override
                    public void onAction(Boolean val) {
                        if (val != null && val) {
                            FeedActivity.act.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    inst.notifyDataSetChanged();
                                }
                            });
                        } else {
                            System.out.println("Error deleting contact");
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }
}