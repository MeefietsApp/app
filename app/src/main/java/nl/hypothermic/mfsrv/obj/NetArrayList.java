package nl.hypothermic.mfsrv.obj;

import android.org.apache.commons.codec.DecoderException;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import nl.hypothermic.meefietsen.core.FileIO;
import nl.hypothermic.meefietsen.obj.NetworkObject;

public class NetArrayList<E> extends ArrayList<E> implements NetworkObject, Serializable {
	
	static final long serialVersionUID = 1L;
	
	public static NetArrayList fromFile(File path) throws ClassNotFoundException, IOException {
		return (NetArrayList) FileIO.deserialize(path);
	}
	
	public static NetArrayList fromSerializedString(String str) throws ClassNotFoundException, IOException, DecoderException {
		return (NetArrayList) FileIO.deserializeFromString(str);
	}
	
	public void toFile(File path) throws IOException {
		FileIO.serialize(path, this);
	}
	
	public String toSerializedString() throws IOException {
		return FileIO.serializeToString(this);
	}
}
