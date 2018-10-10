package nl.hypothermic.mfsrv.obj.account;

import android.org.apache.commons.codec.DecoderException;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import nl.hypothermic.meefietsen.core.FileIO;
import nl.hypothermic.meefietsen.obj.NetworkObject;
import nl.hypothermic.mfsrv.obj.auth.TelephoneNum;

public class Account implements Serializable, NetworkObject {

    static final long serialVersionUID = 1L;

    public static Account fromFile(File path) throws ClassNotFoundException, IOException {
        return (Account) FileIO.deserialize(path);
    }

    public static Account fromSerializedString(String str) throws ClassNotFoundException, IOException, DecoderException {
        return (Account) FileIO.deserializeFromString(str);
    }

    public TelephoneNum num;

    public String userName;

    public Account(TelephoneNum num, String userName) {
        this.num = num;
        this.userName = userName;
    }

    @Override public String toString() {
        return "Account [num=" + this.num + ", userName=" + this.userName + "]";
    }

    public String toSerializedString() throws IOException {
        return FileIO.serializeToString(this);
    }
}
