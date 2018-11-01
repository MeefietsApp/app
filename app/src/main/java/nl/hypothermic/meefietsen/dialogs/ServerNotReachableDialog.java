package nl.hypothermic.meefietsen.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import nl.hypothermic.meefietsen.FeedActivity;
import nl.hypothermic.meefietsen.R;
import nl.hypothermic.meefietsen.SplashActivity;
import nl.hypothermic.meefietsen.WelcomeActivity;

public class ServerNotReachableDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.act);
        builder.setMessage(SplashActivity.act.getString(R.string.dialog_std_serverunreachable_title))
                .setPositiveButton(SplashActivity.act.getString(R.string.dialog_std_serverunreachable_positive), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SplashActivity.act.finishAffinity();
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                })
                .setNegativeButton(SplashActivity.act.getString(R.string.dialog_std_serverunreachable_negative), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ;
                    }
                });
        return builder.create();
    }
}
