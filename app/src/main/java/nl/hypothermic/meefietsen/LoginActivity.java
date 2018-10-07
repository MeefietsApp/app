package nl.hypothermic.meefietsen;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import nl.hypothermic.meefietsen.async.GenericCallback;
import nl.hypothermic.meefietsen.core.MeefietsClient;

public class LoginActivity extends AppCompatActivity {

    private static Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        act = this;

        findViewById(R.id.fieldLoginPassword).setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                ((EditText) findViewById(R.id.fieldLoginPassword)).setTextColor(Color.BLACK);
                return false;
            }
        });

        findViewById(R.id.btnLoginProceed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MeefietsClient.getInstance().doLogin(((EditText) findViewById(R.id.fieldLoginPassword)).getText().toString(), new GenericCallback<Boolean>() {
                    @Override
                    public void onAction(Boolean val) {
                        if (val != null && val) {
                            MeefietsClient.getInstance().isAuthenticated(new GenericCallback<Boolean>() {
                                @Override
                                public void onAction(Boolean val) {
                                    if (val != null && val) {
                                        startActivity(new Intent(act, SplashActivity.class));
                                    } else {
                                        onLoginFailed();
                                    }
                                }
                            });
                        } else {
                            onLoginFailed();
                        }
                    }
                });
            }
        });
    }

    protected void onLoginFailed() {
        ((EditText) findViewById(R.id.fieldLoginPassword)).setTextColor(Color.RED);
    }
}
