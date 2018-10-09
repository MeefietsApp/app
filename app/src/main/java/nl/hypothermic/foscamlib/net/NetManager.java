package nl.hypothermic.foscamlib.net;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import nl.hypothermic.meefietsen.core.MeefietsClient;
import nl.hypothermic.meefietsen.obj.auth.TelephoneNum;

/******************************\
 * > NetManager.java		< *
 * FoscamAPI by hypothermic	  *
 * www.github.com/hypothermic *
 *                            *
 *  Modified by MeefietsApp   *
\******************************/

public class NetManager {

    private final NetExecutor x = new NetExecutor();

    private int telCountry, telNum;
    public TelephoneNum telephoneNum;
    private String passwd;
    public String sessionToken = "";

    public NetManager(int telCountry, int telNum,  String passwd) {
        this.telCountry = telCountry;
        this.telNum = telNum;
        this.passwd = passwd;
        this.telephoneNum = new TelephoneNum(telCountry, telNum);
    }

    public void setPassword(String value) {
        this.passwd = value;
    }

    /**
     * Execute a HTTP GET request
     * Warning: privileges may be needed!
     * @param command = Command that needs to be executed
     * @param params = {@literal HashMap<String (parameter), String (value)>}. Can be null if no parameters.
     * @return <s>RxData with Result and xml</s> String with raw result
     */
    public String exec(String relativeUrl, HashMap<String, String> params) {
        if (params == null) {
            params = new HashMap<String, String>();
        }
        try {
            StringBuffer sb = new StringBuffer();
            sb.append(MeefietsClient.url + relativeUrl);
            for (Map.Entry<String, String> p : params.entrySet()) {
                sb.append("&" + URLEncoder.encode(p.getKey()) + "=" + URLEncoder.encode(p.getValue()));
            }
            sb.append("&country=" + telCountry + "&num=" + telNum + "&passwd=" + passwd + (sessionToken != "" ? ("&token=" + sessionToken) : ""));
            return x.get(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return "ERROR";
        }
    }
}
