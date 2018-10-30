package nl.hypothermic.meefietsen.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import nl.hypothermic.mfsrv.obj.event.Event;
import nl.hypothermic.mfsrv.obj.event.MeefietsEvent;

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
        System.out.println("#### ADDING EVENT: " + event.eventId + " " + event.getIdentifier());
        events.add(event);
        sort();
    }

    public class Iterator implements java.util.Iterator {

        public java.util.Iterator it;

        private Iterator() {
            this.reset();
        }

        public void reset() {
            it = instance.events.iterator();
        }

        public boolean hasNext() {
            return it.hasNext();
        }

        public Object next() {
            return it.next();
        }
    }

    public Iterator getIterator() {
        return new Iterator();
    }

    public void sort() {
        Collections.sort(events, new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                if (o1 instanceof MeefietsEvent &&
                    o2 instanceof MeefietsEvent) {
                    MeefietsEvent mo1 = (MeefietsEvent) o1;
                    MeefietsEvent mo2 = (MeefietsEvent) o2;
                    if (mo2.eventEpochTime > mo1.eventEpochTime) {
                        return -1;
                    } else if (mo2.eventEpochTime < mo1.eventEpochTime) {
                        return 1;
                    } else {
                        return 0;
                    }
                } else {
                    if (o2.eventId > o1.eventId) {
                        return -1;
                    } else if (o2.eventId < o1.eventId) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            }
        });
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void clearEvents() {
        events.clear();
    }
}
