package nl.hypothermic.mfsrv.obj.event;

public class EventBuilder<T extends MeefietsEvent> {

    private T event;

    public EventBuilder(T event) {
        this.event = event;
    }

    public T setName(String name) {
        event.eventName = name;
        return event;
    }

    public T setLocation(String location) {
        event.eventLocation = location;
        return event;
    }
}
