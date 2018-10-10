package nl.hypothermic.foscamlib.net;

import nl.hypothermic.meefietsen.ResponseCode;
import nl.hypothermic.meefietsen.obj.NetworkObject;

public class NetResponse<T extends NetworkObject> {

    public ResponseCode code = ResponseCode.INTERNEL_ERR_NOT_SET;

    public T object = null;

    public boolean isSuccess() {
        return code.equals(ResponseCode.SUCCESS);
    }

    public T getObject() {
        return object;
    }
}
