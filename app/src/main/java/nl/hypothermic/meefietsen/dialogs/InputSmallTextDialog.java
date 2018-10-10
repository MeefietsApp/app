package nl.hypothermic.meefietsen.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

import nl.hypothermic.meefietsen.FeedActivity;
import nl.hypothermic.meefietsen.R;
import nl.hypothermic.meefietsen.async.GenericCallback;

public class InputSmallTextDialog extends DialogFragment {

    private final String title, posBtn, negBtn;
    private final GenericCallback<String> cb;

    public InputSmallTextDialog() {
        this.cb = null;
        this.title = getResources().getString(R.string.dialog_std_inputsmalltext_title);
        this.posBtn = getResources().getString(R.string.dialog_std_inputsmalltext_positive);
        this.negBtn = getResources().getString(R.string.dialog_std_inputsmalltext_negative);
    }

    @SuppressLint("ValidFragment")
    public InputSmallTextDialog(String title, String posBtn, String negBtn, GenericCallback<String> cb) {
        super();
        this.cb = cb;
        this.title = title;
        this.posBtn = posBtn;
        this.negBtn = negBtn;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FeedActivity.act);
        final EditText input = new EditText(FeedActivity.act);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        builder.setView(input);
        builder.setTitle(title)
                .setPositiveButton(posBtn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (cb != null) {
                            cb.onAction(input.getText().toString());
                        }
                    }
                })
                .setNegativeButton(negBtn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ;
                    }
                });
        return builder.create();
    }
}
