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

    public static final long BACK_PRESS_TIMEOUT = 700; // ms
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
                            try {
                                if (!ConfigurationLoader.getString("mfapp.auth.tel", "unset").equalsIgnoreCase("unset") &&
                                    !ConfigurationLoader.getString("mfapp.auth.passwd", "unset").equalsIgnoreCase("unset")) {
                                    // NOTE: hardcoded land nummer
                                    MeefietsClient.getInstance().setNetManager(new NetManager(31, Integer.valueOf(ConfigurationLoader.getString("mfapp.auth.tel", "u")),
                                                                                                                            ConfigurationLoader.getString("mfapp.auth.passwd", "u")));
                                    if (MeefietsClient.getInstance().doLoginSynchronously(ConfigurationLoader.getString("mfapp.auth.passwd", "u"))) {
                                        startActivity(new Intent(act, FeedActivity.class));
                                        return;
                                    }
                                }
                            } catch (Exception x) {
                                x.printStackTrace();
                            }
                            startActivity(new Intent(act, WelcomeActivity.class));
                        }
                    }
                });
            }
        }.run();
    }
}
