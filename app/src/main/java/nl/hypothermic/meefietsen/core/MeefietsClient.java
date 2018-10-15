package nl.hypothermic.meefietsen.core;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import nl.hypothermic.foscamlib.net.NetManager;
import nl.hypothermic.foscamlib.net.NetResponse;
import nl.hypothermic.meefietsen.ResponseCode;
import nl.hypothermic.meefietsen.async.GenericCallback;
import nl.hypothermic.meefietsen.async.MessagedCallback;
import nl.hypothermic.mfsrv.obj.NetArrayList;
import nl.hypothermic.mfsrv.obj.account.Account;
import nl.hypothermic.mfsrv.obj.auth.TelephoneNum;

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
    public Account localAccount = new Account(new TelephoneNum(00, 00000000), "...");

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

    // --- Authenticatie

    public void doLogin(final String passwd, final GenericCallback<Boolean> cb) {
        this.netman.setPassword(passwd);
        threadpool.execute(new Runnable() {
            @Override
            public void run() {
                netman.sessionToken = netman.exec("auth/login?", null).trim() + "";
                cb.onAction((netman.sessionToken.length() > 2));
                updateLocalAccount(new GenericCallback<Boolean>() {
                    @Override
                    public void onAction(Boolean val) {
                        ;
                    }
                });
            }
        });
    }

    public boolean doLoginSynchronously(final String passwd) {
        this.netman.setPassword(passwd);
        netman.sessionToken = netman.exec("auth/login?", null).trim() + "";
        System.out.println("------> SYNC SES TOKEN: " + netman.sessionToken + " LEN: " + (netman.sessionToken.trim().length() > 5));
        return netman.sessionToken.trim().length() > 5;
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

    // --- Accounts

    public void updateLocalAccount(final GenericCallback<Boolean> cb) {
        threadpool.execute(new Runnable() {
            @Override
            public void run() {
                isAuthenticated(new GenericCallback<Boolean>() {
                    @Override
                    public void onAction(Boolean val) {
                        if (val != null && val) {
                            String ret = netman.exec("account/get?", new HashMap<String, String>() {{
                                put("targetcountry", netman.telephoneNum.country + "");
                                put("targetnum", netman.telephoneNum.number + "");
                            }});
                            if (ret.startsWith("1")) {
                                try {
                                    localAccount = Account.fromSerializedString(ret.substring(1));
                                    cb.onAction(true);
                                } catch (Exception x) {
                                    x.printStackTrace();
                                    cb.onAction(false);
                                }
                            } else {
                                cb.onAction(false);
                            }
                        } else {
                            cb.onAction(false);
                        }
                    }
                });
            }
        });
    }

    public void getAccount(final TelephoneNum target, final GenericCallback<NetResponse<Account>> cb) {
        threadpool.execute(new Runnable() {
            @Override
            public void run() {
                final NetResponse<Account> res = new NetResponse<>();
                isAuthenticated(new GenericCallback<Boolean>() {
                    @Override
                    public void onAction(Boolean val) {
                        if (val != null && val) {
                            String ret = netman.exec("account/get?", new HashMap<String, String>() {{
                                put("targetcountry", target.country + "");
                                put("targetnum", target.number + "");
                            }});
                            System.out.println("GETACC RET: " + ret);
                            if (ret.startsWith("1")) {
                                try {
                                    res.object = Account.fromSerializedString(ret.substring(1));
                                    res.code = ResponseCode.SUCCESS;
                                } catch (Exception x) {
                                    x.printStackTrace();
                                    res.code = ResponseCode.INTERNAL_ERR_GENERIC;
                                }
                            }
                        } else {
                            res.code = ResponseCode.INTERNAL_ERR_NOT_AUTH;
                        }
                        cb.onAction(res);
                    }
                });
            }
        });
    }

    public void accountManageSetName(final String newValue, final GenericCallback<Boolean> cb) {
        threadpool.execute(new Runnable() {
            @Override
            public void run() {
                cb.onAction((netman != null &&
                             netman.sessionToken != "" &&
                             netman.exec("account/manage/setname?", new HashMap<String, String>() {{
                                 put("value", newValue);
                             }}).trim().equals("1")
                ));
            }
        });
    }

    public void getContacts(final GenericCallback<NetResponse<NetArrayList<TelephoneNum>>> cb) {
        threadpool.execute(new Runnable() {
            @Override
            public void run() {
                final NetResponse<NetArrayList<TelephoneNum>> res = new NetResponse<>();
                isAuthenticated(new GenericCallback<Boolean>() {
                    @Override
                    public void onAction(Boolean val) {
                        if (val != null && val) {
                            String ret = netman.exec("account/contacts/get?", null);
                            System.out.println("GETCTL RET: " + ret);
                            if (ret.startsWith("1")) {
                                try {
                                    res.object = NetArrayList.fromSerializedString(ret.substring(1));
                                    res.code = ResponseCode.SUCCESS;
                                } catch (Exception x) {
                                    x.printStackTrace();
                                    res.code = ResponseCode.INTERNAL_ERR_GENERIC;
                                }
                            }
                        } else {
                            res.code = ResponseCode.INTERNAL_ERR_NOT_AUTH;
                        }
                        cb.onAction(res);
                    }
                });
            }
        });
    }
}
