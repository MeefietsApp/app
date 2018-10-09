package nl.hypothermic.meefietsen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import nl.hypothermic.foscamlib.net.NetManager;
import nl.hypothermic.meefietsen.async.GenericCallback;
import nl.hypothermic.meefietsen.core.ConfigurationLoader;
import nl.hypothermic.meefietsen.core.MeefietsClient;

public class SplashActivity extends AppCompatActivity {

    public static Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        act = this;

        setContentView(R.layout.activity_splash);

        new Runnable() {
            @Override
            public void run() {
                MeefietsClient.getInstance().isAuthenticated(new GenericCallback<Boolean>() {
                    @Override
                    public void onAction(Boolean b) {
                        // TODO: verwijder debug prints na code review
                        if (b != null && b) {
                            startActivity(new Intent(act, FeedActivity.class));
                        } else {
                            System.out.println("debug: x1");
                            try {
                                if (!ConfigurationLoader.getString("mfapp.auth.tel", "unset").equalsIgnoreCase("unset") &&
                                    !ConfigurationLoader.getString("mfapp.auth.passwd", "unset").equalsIgnoreCase("unset")) {
                                    System.out.println("debug: inloggen met stored credentials");
                                    // NOTE: hardcoded land nummer
                                    MeefietsClient.getInstance().setNetManager(new NetManager(31, Integer.valueOf(ConfigurationLoader.getString("mfapp.auth.tel", "u")),
                                                                                                                            ConfigurationLoader.getString("mfapp.auth.passwd", "u")));
                                    if (MeefietsClient.getInstance().doLoginSynchronously(ConfigurationLoader.getString("mfapp.auth.passwd", "u"))) {
                                        System.out.println("debug: dls 0");
                                        startActivity(new Intent(act, FeedActivity.class));
                                        return;
                                    }
                                    System.out.println("debug: dls 1");
                                } else {
                                    System.out.println("debug: geen stored credentials");
                                }
                            } catch (Exception x) {
                                System.out.println("debug: geen stored credentials");
                            }
                            System.out.println("debug: x2");
                            startActivity(new Intent(act, WelcomeActivity.class));
                        }
                    }
                });
            }
        }.run();
    }
}
