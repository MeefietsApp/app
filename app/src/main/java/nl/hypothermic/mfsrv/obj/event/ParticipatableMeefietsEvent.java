package nl.hypothermic.mfsrv.obj.event;

import nl.hypothermic.mfsrv.obj.NetArrayList;
import nl.hypothermic.mfsrv.obj.auth.TelephoneNum;

public class ParticipatableMeefietsEvent extends MeefietsEvent {

	static final long serialVersionUID = 1L;
	
	public NetArrayList<TelephoneNum> participants = new NetArrayList<TelephoneNum>();
	
	public boolean inviteOnly; 

	public boolean isPrivate;
	
	public boolean isExpired;
	
	public ParticipatableMeefietsEvent() {
		
	}
	
	@Override public String getIdentifier() {
		return eventName + "";
	}
	
	@Override public ParticipatableMeefietsEvent sanitize() {
		return new ParticipatableMeefietsEvent();
	}

	@Override public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.inviteOnly ? 1231 : 1237);
		result = prime * result + (this.isExpired ? 1231 : 1237);
		result = prime * result + (this.isPrivate ? 1231 : 1237);
		return result;
	}

	@Override public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ParticipatableMeefietsEvent other = (ParticipatableMeefietsEvent) obj;
		if (this.inviteOnly != other.inviteOnly)
			return false;
		if (this.isExpired != other.isExpired)
			return false;
		if (this.isPrivate != other.isPrivate)
			return false;
		return true;
	}

	@Override public String toString() {
		return "ParticipatableMeefietsEvent [inviteOnly=" + this.inviteOnly + ", isPrivate=" + this.isPrivate + ", isExpired=" + this.isExpired + ", eventName=" + this.eventName + ", eventLocation=" + this.eventLocation + ", eventEpochTime=" + this.eventEpochTime + ", eventId=" + this.eventId + "]";
	}
}
