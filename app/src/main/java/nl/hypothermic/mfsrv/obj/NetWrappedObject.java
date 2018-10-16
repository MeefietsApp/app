package nl.hypothermic.mfsrv.obj;

import nl.hypothermic.meefietsen.obj.NetworkObject;

public class NetWrappedObject<T extends Object> implements NetworkObject {

    private T value;

    public NetWrappedObject(final T value) {
        this.value = value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return this.value;
    }
}
