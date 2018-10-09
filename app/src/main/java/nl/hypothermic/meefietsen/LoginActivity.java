package nl.hypothermic.meefietsen;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

import nl.hypothermic.meefietsen.async.GenericCallback;
import nl.hypothermic.meefietsen.core.ConfigurationLoader;
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
                try {
                    final String input = Hashing.sha512().hashString(((EditText) findViewById(R.id.fieldLoginPassword)).getText().toString(), StandardCharsets.UTF_8).toString();
                    System.out.println("---INPUT: " + input);
                    MeefietsClient.getInstance().doLogin(input, new GenericCallback<Boolean>() {
                        @Override
                        public void onAction(Boolean val) {
                            if (val != null && val) {
                                MeefietsClient.getInstance().isAuthenticated(new GenericCallback<Boolean>() {
                                    @Override
                                    public void onAction(Boolean val) {
                                        if (val != null && val) {
                                            new Runnable() {
                                                @Override
                                                public void run() {
                                                    ConfigurationLoader.setString("mfapp.auth.tel", MeefietsClient.getInstance().getNetManager().telephoneNum.number + "");
                                                    ConfigurationLoader.setString("mfapp.auth.passwd", input);
                                                }
                                            }.run();
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
                } catch (Exception x) {
                    x.printStackTrace();
                    onLoginFailed();
                }
            }
        });
    }

    protected void onLoginFailed() {
        ((EditText) findViewById(R.id.fieldLoginPassword)).setTextColor(Color.RED);
    }
}
