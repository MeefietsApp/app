package nl.hypothermic.meefietsen.obj.auth;

import java.io.Serializable;

import nl.hypothermic.meefietsen.obj.NetworkObject;

public class TelephoneNum implements Serializable, NetworkObject {

    public final int country;

    public final int number;

    public TelephoneNum(int country, int number) {
        this.country = country;
        this.number = number;
    }

    @Override
    public String toString() {
        return "TelephoneNum [country=" + this.country + ", number=" + this.number + "]";
    }
}
