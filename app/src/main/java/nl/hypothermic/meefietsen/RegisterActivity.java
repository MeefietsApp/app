package nl.hypothermic.meefietsen;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import nl.hypothermic.VerifyActivity;
import nl.hypothermic.meefietsen.async.MessagedCallback;
import nl.hypothermic.meefietsen.core.MeefietsClient;

public class RegisterActivity extends AppCompatActivity {

    private static Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.act = this;
        setContentView(R.layout.activity_register);
        TextView strRegisterLegal = findViewById(R.id.strRegisterLegal);
        strRegisterLegal.setMovementMethod(LinkMovementMethod.getInstance());
        strRegisterLegal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent docs = new Intent(Intent.ACTION_VIEW);
                docs.setData(Uri.parse("https://www.hypothermic.nl/meefietsen/tos.html"));
                startActivity(docs);
            }
        });
        findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String input = ((EditText) findViewById(R.id.fieldRegisterPassword)).getText().toString();
                if (input.length() >= 8) {
                    MeefietsClient.getInstance().doRegister(input, new MessagedCallback<Boolean>() {
                        @Override
                        public void onAction(Boolean val, String msg) {
                            if ((val != null && val) || msg.trim().equals("-6")) {
                                startActivity(new Intent(act, VerifyActivity.class));
                            } else {
                                onRegisterFailed(msg);
                            }
                        }
                    });
                } else {
                    onRegisterFailed("het wachtwoord moet 8 karakters bevatten.");
                }
            }
        });
    }

    protected void onRegisterFailed(final String msg) {
        ((EditText) findViewById(R.id.fieldRegisterPassword)).setTextColor(Color.RED);
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(act, "Registratie mislukt: " + ((msg.length() > 1) ? msg : "onbekende fout."), Toast.LENGTH_LONG);
            }
        });
        System.out.println("REGISTER ERROR: " + msg);
    }
}
