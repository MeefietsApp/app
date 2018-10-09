package nl.hypothermic.meefietsen.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import nl.hypothermic.meefietsen.WelcomeActivity;

public class ServerNotReachableDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.act);
        builder.setMessage("Server not reachable.")
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                })
                .setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ;
                    }
                });
        return builder.create();
    }
}
