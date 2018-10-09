package nl.hypothermic.meefietsen.obj.account;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import nl.hypothermic.meefietsen.core.FileIO;
import nl.hypothermic.meefietsen.obj.NetworkObject;
import nl.hypothermic.meefietsen.obj.auth.TelephoneNum;

public class Account implements Serializable, NetworkObject {

    public static Account fromFile(File path) throws ClassNotFoundException, IOException {
        return (Account) FileIO.deserialize(path);
    }

    public static Account fromSerializedString(String str) throws ClassNotFoundException, IOException {
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
