package nl.hypothermic.foscamlib.net;

import nl.hypothermic.meefietsen.ResponseCode;
import nl.hypothermic.meefietsen.obj.NetworkObject;

public class NetResponse<T extends NetworkObject> {

    public ResponseCode code;

    public boolean isSuccess() {
        return code.equals(ResponseCode.SUCCESS);
    }
}
