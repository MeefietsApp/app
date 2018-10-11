package nl.hypothermic.mfsrv.obj.event;

public class MeefietsEvent extends Event {
	
	static final long serialVersionUID = 2L;
	
	public String eventName;
	
	public String eventLocation;

	public long eventEpochTime;

	public MeefietsEvent() {
		
	}

	public String getIdentifier() {
		return eventName + "";
	}

	@Override public MeefietsEvent sanitize() {
	    // TODO
		return new MeefietsEvent();
	}
}
