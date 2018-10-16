package nl.hypothermic.meefietsen.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.Map;

import nl.hypothermic.meefietsen.FeedActivity;
import nl.hypothermic.meefietsen.async.GenericCallback;

public class EventCreateDialog<T> extends DialogFragment {

    private String title = "", posBtn = "", negBtn = "";
    private final HashMap<String, String> fields = new HashMap<>();
    private final GenericCallback<T> cb;

    public EventCreateDialog() {
        this.cb = null;
    }

    @SuppressLint("ValidFragment")
    public EventCreateDialog(GenericCallback<T> cb) {
        super();
        this.cb = cb;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FeedActivity.act);
        final LinearLayout layout = new LinearLayout(FeedActivity.act);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        for (final Map.Entry<String, String> iter : fields.entrySet()) {
            final EditText field = new EditText(FeedActivity.act);
            field.setHint(iter.getValue());
            field.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
            field.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String key = iter.getKey();
                    fields.remove(iter.getKey());
                    fields.put(key, s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            layout.addView(field);
        }
        builder.setView(layout);
        builder.setTitle(title)
                .setPositiveButton(posBtn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (cb != null) {
                            cb.onAction((T) fields); // TODO
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

    // --- "builder" funcs

    public EventCreateDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public EventCreateDialog setPosNegBtns(String posBtn, String negBtn) {
        this.posBtn = posBtn;
        this.negBtn = negBtn;
        return this;
    }

    public EventCreateDialog addField(String fieldKey, String fieldHint) {
        this.fields.put(fieldKey, fieldHint);
        return this;
    }
}
