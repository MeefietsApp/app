package nl.hypothermic.meefietsen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import nl.hypothermic.meefietsen.async.GenericCallback;
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
                        if (b != null && b) {
                            startActivity(new Intent(act, FeedActivity.class));
                        } else {
                            startActivity(new Intent(act, WelcomeActivity.class));
                        }
                    }
                });
            }
        }.run();
    }
}
