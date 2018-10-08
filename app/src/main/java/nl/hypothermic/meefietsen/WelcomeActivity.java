package nl.hypothermic.meefietsen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.pinball83.maskededittext.MaskedEditText;

import nl.hypothermic.foscamlib.net.NetManager;
import nl.hypothermic.meefietsen.core.MeefietsClient;

public class WelcomeActivity extends AppCompatActivity {

    private static Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        act = this;

        findViewById(R.id.btnProceed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override public void run() {
                        String input = ((MaskedEditText) findViewById(R.id.fieldPhoneNum)).getUnmaskedText();
                        if (input.startsWith("06")) {
                            input.replaceFirst("06", "");
                        }
                        if (input.length() == 8) {
                            input = "6" + input;
                        }
                        try {
                            if (input.length() == 9) {
                                NetManager man = new NetManager(31, Integer.valueOf(input), "check");
                                int res = Integer.valueOf(man.exec("auth/login?d=d", null));
                                if (res > 1) {
                                    man.sessionToken = res + "";
                                    MeefietsClient.getInstance().setNetManager(man);
                                    startActivity(new Intent(act, SplashActivity.class));
                                } else if (res == -3) {
                                    MeefietsClient.getInstance().setNetManager(man);
                                    startActivity(new Intent(act, RegisterActivity.class));
                                } else if (res == -4) {
                                    MeefietsClient.getInstance().setNetManager(man);
                                    startActivity(new Intent(act, LoginActivity.class));
                                }
                                System.out.println("--->" + res);
                            }
                        } catch (NumberFormatException nfe) {
                            nfe.printStackTrace();
                            // TODO bij fout nummer
                        }
                    }
                }.start();
            }
        });
    }
}
