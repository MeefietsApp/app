package nl.hypothermic.mfsrv.obj.event;

public class MeefietsEvent extends Event {
	
	static final long serialVersionUID = 1L;
	
	public String eventName;
	
	public String eventLocation;

	public MeefietsEvent() {
		
	}

	public String getIdentifier() {
		return eventName + "";
	}

	@Override public MeefietsEvent sanitize() {
		return new MeefietsEvent();
	}
}
