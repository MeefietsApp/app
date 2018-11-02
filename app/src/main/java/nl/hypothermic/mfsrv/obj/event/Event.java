package nl.hypothermic.mfsrv.obj.event;

import android.org.apache.commons.codec.DecoderException;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import nl.hypothermic.meefietsen.core.FileIO;
import nl.hypothermic.meefietsen.obj.NetworkObject;

public abstract class Event implements Serializable, NetworkObject {

	static final long serialVersionUID = 1L;

	public static Event fromFile(File path) throws ClassNotFoundException, IOException {
		return castFromObject(FileIO.deserialize(path));
	}

	public static Event fromSerializedString(String str) throws ClassNotFoundException, IOException, DecoderException {
		return castFromObject(FileIO.deserializeFromString(str));
	}

	private static Event castFromObject(Object obj) throws ClassNotFoundException {
		if (obj instanceof ParticipatableMeefietsEvent) {
			return (ParticipatableMeefietsEvent) obj;
		} else if (obj instanceof MeefietsEvent) {
			return (MeefietsEvent) obj;
		} else if (obj instanceof Event) {
			return (Event) obj;
		} else {
			throw new ClassNotFoundException("Event type not supported.");
		}
	}

	public int eventId;

	public Event() {

	}

	public String getIdentifier() {
		return eventId + "";
	}

    public abstract Event sanitize();

	public String toSerializedString() throws IOException {
		return FileIO.serializeToString(this);
	}
}
