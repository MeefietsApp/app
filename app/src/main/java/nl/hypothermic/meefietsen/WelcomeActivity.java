package nl.hypothermic.meefietsen;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
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
                String input = ((MaskedEditText) findViewById(R.id.fieldPhoneNum)).getUnmaskedText();
                try {
                    if (input.length() == 8) {
                        NetManager man = new NetManager(31, Integer.valueOf(input), "check");
                        System.out.println(man.exec("auth/login", null));
                        // TODO
                    }
                } catch (NumberFormatException nfe) {
                    // TODO bij fout nummer
                }
            }
        });
    }
}
