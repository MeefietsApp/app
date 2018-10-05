package nl.hypothermic.meefietsen.core;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import nl.hypothermic.foscamlib.net.NetManager;

public class MeefietsClient {

    private static MeefietsClient instance;

    public static MeefietsClient getInstance() {
        if (instance == null) {
            instance = new MeefietsClient();
        }
        return instance;
    }

    public static final String url = "https://api.hypothermic.nl/";

    private ExecutorService threadpool = Executors.newCachedThreadPool();
    private NetManager netman;

    // --- set+get

    private MeefietsClient() {
        ;
    }

    public void setLoginParameters(int telCountry, int telNum, String passwd) {
        netman = new NetManager(telCountry, telNum, passwd);
    }

    // --- methods

    public boolean isAuthenticated() {
        return netman != null &&
               netman.sessionToken != "" &&
               netman.exec("auth/sync?", null) == "1";
    }
}
