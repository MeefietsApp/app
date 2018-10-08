package nl.hypothermic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import nl.hypothermic.meefietsen.R;
import nl.hypothermic.meefietsen.SplashActivity;
import nl.hypothermic.meefietsen.async.GenericCallback;
import nl.hypothermic.meefietsen.async.MessagedCallback;
import nl.hypothermic.meefietsen.core.MeefietsClient;

public class VerifyActivity extends AppCompatActivity {

    private static Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.act = this;
        setContentView(R.layout.activity_verify);
        ((Button) findViewById(R.id.btnVerify)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String input = ((EditText) findViewById(R.id.fieldVerifyNum)).getText().toString().trim();
                if (input.length() == 5) {
                    MeefietsClient.getInstance().doVerify(input, new MessagedCallback<Boolean>() {
                        @Override
                        public void onAction(Boolean val, String msg) {
                            if (val != null && val) {
                                MeefietsClient.getInstance().doLogin(input, new GenericCallback<Boolean>() {
                                    @Override
                                    public void onAction(Boolean val) {
                                        //if (val != null && val) {
                                            startActivity(new Intent(act, SplashActivity.class));
                                        /*} else {
                                            onVerifyError("fout bij inloggen.");
                                        }*/
                                    }
                                });
                            } else {
                                onVerifyError("server reageerde met foutcode " + msg);
                            }
                        }
                    });
                } else {
                    onVerifyError("verificatienummer moet 5 cijfers lang zijn");
                }
            }
        });
    }

    protected void onVerifyError(final String msg) {
        ((EditText) findViewById(R.id.fieldVerifyNum)).setTextColor(Color.RED);
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(act, "Registratie mislukt: " + ((msg.length() > 1) ? msg : "onbekende fout."), Toast.LENGTH_LONG);
            }
        });
    }
}
