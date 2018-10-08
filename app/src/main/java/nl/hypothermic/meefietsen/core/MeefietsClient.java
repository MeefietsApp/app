package nl.hypothermic.meefietsen.core;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import nl.hypothermic.foscamlib.net.NetManager;
import nl.hypothermic.meefietsen.async.GenericCallback;
import nl.hypothermic.meefietsen.async.MessagedCallback;

public class MeefietsClient {

    private static MeefietsClient instance;

    public static MeefietsClient getInstance() {
        if (instance == null) {
            instance = new MeefietsClient();
        }
        return instance;
    }

    //public static final String url = "http://149.202.45.240:7000/"; // temp fix voor dns propagation
    public static final String url = "http://api.hypothermic.nl:7000/";

    private ExecutorService threadpool = Executors.newCachedThreadPool();
    private NetManager netman;

    // --- set+get

    private MeefietsClient() {
        ;
    }

    public void setNetManager(NetManager netman) {
        this.netman = netman;
    }

    public NetManager getNetManager() {
        return netman;
    }

    // --- methods

    public void doLogin(final String passwd, final GenericCallback<Boolean> cb) {
        this.netman.setPassword(passwd);
        threadpool.execute(new Runnable() {
            @Override
            public void run() {
                netman.sessionToken = netman.exec("auth/login?", null).trim() + "";
                cb.onAction((netman.sessionToken.length() > 2));
            }
        });
    }

    public void doRegister(final String passwd, final MessagedCallback<Boolean> cb) {
        this.netman.setPassword(passwd);
        threadpool.execute(new Runnable() {
            @Override
            public void run() {
                int res = Integer.valueOf(netman.exec("auth/register?", null).trim());
                System.out.println("RRES:" + res);
                cb.onAction(res == 1, res + "");
            }
        });
    }

    public void doVerify(final String passwd, final MessagedCallback<Boolean> cb) {
        this.netman.setPassword(passwd);
        threadpool.execute(new Runnable() {
            @Override
            public void run() {
                int res = Integer.valueOf(netman.exec("auth/verify?", new HashMap<String, String>() {{
                    put("verifytoken", passwd);
                }}).trim());
                System.out.println("VRES:" + res);
                cb.onAction(res == 1, res + "");
            }
        });
    }

    public void isAuthenticated(final GenericCallback<Boolean> cb) {
        threadpool.execute(new Runnable() {
            @Override
            public void run() {
                cb.onAction((netman != null &&
                             netman.sessionToken != "" &&
                             netman.exec("auth/sync?", null).trim().equals("1")));
            }
        });
    }
}
