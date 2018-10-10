package nl.hypothermic.meefietsen.frags;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import nl.hypothermic.meefietsen.FeedActivity;
import nl.hypothermic.meefietsen.R;
import nl.hypothermic.meefietsen.async.GenericCallback;
import nl.hypothermic.meefietsen.core.MeefietsClient;
import nl.hypothermic.meefietsen.dialogs.InputSmallTextDialog;

public class AccountFragment extends Fragment {

    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.account_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final MeefietsClient inst = MeefietsClient.getInstance();
        final FeedActivity act = FeedActivity.act;
        act.setAccountFragment(this);
        act.findViewById(R.id.accountDetails).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new InputSmallTextDialog(getString(R.string.activity_account_manage_name_title),
                                         getString(R.string.dialog_std_inputsmalltext_positive),
                                         getString(R.string.dialog_std_inputsmalltext_negative),
                                         new GenericCallback<String>() {
                    @Override
                    public void onAction(String val) {
                        inst.accountManageSetName(val, new GenericCallback<Boolean>() {
                            @Override
                            public void onAction(Boolean val) {
                                if (val) {
                                    inst.updateLocalAccount(new GenericCallback<Boolean>() {
                                        @Override
                                        public void onAction(Boolean val) {
                                            if (val) {
                                                ((TextView) act.findViewById(R.id.accountTel)).setText(inst.localAccount.num.toFormattedString());
                                                ((TextView) act.findViewById(R.id.accountName)).setText(inst.localAccount.userName);
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                }).onCreateDialog(null).show();
            }
        });
    }
}
