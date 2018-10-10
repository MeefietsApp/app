package nl.hypothermic.meefietsen.core;

import java.util.ArrayList;

import nl.hypothermic.mfsrv.obj.event.Event;

public class ClientEventManager {

    private static ClientEventManager instance;

    public static ClientEventManager getInstance() {
        if (instance == null) {
            instance = new ClientEventManager();
        }
        return instance;
    }

    private ArrayList<Event> events = new ArrayList<>();

    public void addEvent(Event event) {
        events.add(event);
    }

    public ArrayList<Event> getEvents() {
        return events;
    }
}
