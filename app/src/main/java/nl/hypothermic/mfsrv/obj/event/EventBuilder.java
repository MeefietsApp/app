package nl.hypothermic.mfsrv.obj.event;

public class EventBuilder<T extends MeefietsEvent> {

    private T event;

    public EventBuilder(T event) {
        this.event = event;
    }

    public EventBuilder setName(String name) {
        event.eventName = name;
        return this;
    }

    public EventBuilder setLocation(String location) {
        event.eventLocation = location;
        return this;
    }

    public T build() {
        return event;
    }
}
