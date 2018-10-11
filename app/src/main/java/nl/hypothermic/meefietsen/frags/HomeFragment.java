package nl.hypothermic.meefietsen.frags;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nl.hypothermic.meefietsen.FeedActivity;
import nl.hypothermic.meefietsen.R;
import nl.hypothermic.meefietsen.core.ClientEventManager;
import nl.hypothermic.meefietsen.core.EventViewAdapter;
import nl.hypothermic.mfsrv.obj.event.EventBuilder;
import nl.hypothermic.mfsrv.obj.event.MeefietsEvent;

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
        RecyclerView events = FeedActivity.act.findViewById(R.id.event_view);
        events.setLayoutManager(new LinearLayoutManager(FeedActivity.act.getBaseContext()));
        events.setAdapter(new EventViewAdapter(ClientEventManager.getInstance().getEvents()));

        ClientEventManager.getInstance().addEvent(new EventBuilder<MeefietsEvent>(new MeefietsEvent()).setName("Voorbeeld event 1").build());
        ClientEventManager.getInstance().addEvent(new EventBuilder<MeefietsEvent>(new MeefietsEvent()).setName("Paul Elstak: 90s party").setLocation("Zaal Dijk").setTime(1539462600).build());
        ClientEventManager.getInstance().addEvent(new EventBuilder<MeefietsEvent>(new MeefietsEvent()).setName("Oktoberfest").setLocation("Alteveer").setTime(1539456300).build());
    }
}
