package nl.hypothermic.meefietsen.frags;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

import nl.hypothermic.foscamlib.net.NetResponse;
import nl.hypothermic.meefietsen.FeedActivity;
import nl.hypothermic.meefietsen.R;
import nl.hypothermic.meefietsen.ResponseCode;
import nl.hypothermic.meefietsen.async.GenericCallback;
import nl.hypothermic.meefietsen.core.ClientContactManager;
import nl.hypothermic.meefietsen.core.ContactsViewAdapter;
import nl.hypothermic.meefietsen.core.MeefietsClient;
import nl.hypothermic.meefietsen.dialogs.EventCreateDialog;
import nl.hypothermic.meefietsen.integrity.PhoneNumberFormatter;
import nl.hypothermic.mfsrv.obj.NetArrayList;
import nl.hypothermic.mfsrv.obj.account.Account;
import nl.hypothermic.mfsrv.obj.auth.TelephoneNum;

public class ContactsFragment extends Fragment {

    public static ContactsFragment newInstance() {
        return new ContactsFragment();
    }

    private RecyclerView contacts;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contacts_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FeedActivity.act.setContactsFragment(this);

        contacts = FeedActivity.act.findViewById(R.id.contacts_view);
        contacts.setLayoutManager(new LinearLayoutManager(FeedActivity.act.getBaseContext()));
        contacts.setAdapter(new ContactsViewAdapter(ClientContactManager.getInstance().getContacts()));
        this.updateContacts();

        (FeedActivity.act.findViewById(R.id.fab_contact_add)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EventCreateDialog<>(new GenericCallback<HashMap<String, String>>() {
                    @Override
                    public void onAction(final HashMap<String, String> val) {
                        TelephoneNum target;
                        try {
                            target = PhoneNumberFormatter.toTelephoneNum(val.get("num"));
                        } catch (PhoneNumberFormatter.PhoneNumberParseException e) {
                            FeedActivity.showToast(FeedActivity.act.getString(R.string.interact_contacts_add_incorrect_number));
                            e.printStackTrace();
                            return;
                        }
                        MeefietsClient.getInstance().addContact(target, new GenericCallback<Boolean>() {
                            @Override
                            public void onAction(Boolean val) {
                                if (val != null) {
                                    if (val) {
                                        updateContacts();
                                    } else {
                                        FeedActivity.showToast(FeedActivity.act.getString(R.string.interact_contacts_add_error));
                                    }
                                }
                            }
                        });

                    }
                // TODO: lokaliseren
                }).setTitle(FeedActivity.act.getString(R.string.dialog_std_contactadd_title))
                    .setPosNegBtns(FeedActivity.act.getString(R.string.dialog_std_contactadd_positive),
                                   FeedActivity.act.getString(R.string.dialog_std_contactadd_negative))
                    .addField("num",
                              FeedActivity.act.getString(R.string.dialog_std_contactadd_field_number))
                    .onCreateDialog(null)
                    .show();
            }
        });
    }

    public void updateContacts() {
        MeefietsClient.getInstance().getContacts(new GenericCallback<NetResponse<NetArrayList<TelephoneNum>>>() {
            @Override
            public void onAction(NetResponse<NetArrayList<TelephoneNum>> val) {
                if (val != null) {
                    if (val.code == ResponseCode.SUCCESS && val.object != null) {
                        ClientContactManager.getInstance().clearContacts();
                        // TODO dit kan beter gedaan worden... er hoeven niet duizenden runnables aangemaakt te worden!!
                        final GenericCallback<Void> cb = new GenericCallback<Void>() {
                            @Override
                            public void onAction(Void val) {
                                System.out.println("CTS UPDATE UI");
                                FeedActivity.act.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        contacts.getAdapter().notifyDataSetChanged();
                                    }
                                });
                            }
                        };
                        for (Object num : val.object) {
                            System.out.println("REC CONTACT: " + num.toString());
                            MeefietsClient.getInstance().getAccount((TelephoneNum) num, new GenericCallback<NetResponse<Account>>() {
                                @Override
                                public void onAction(NetResponse<Account> val) {
                                    if (val != null) {
                                        if (val.code == ResponseCode.SUCCESS && val.object != null) {
                                            System.out.println("REC CONTACT ACC: " + val.toString());
                                            ClientContactManager.getInstance().addContact(val.object);
                                            cb.onAction(null);
                                        } else {
                                            FeedActivity.showToast(FeedActivity.act.getString(R.string.interact_contacts_update_failed));
                                            System.out.println("Failed to update contacts: invalid account container");
                                        }
                                    } else {
                                        FeedActivity.showToast(FeedActivity.act.getString(R.string.interact_contacts_update_failed));
                                        System.out.println("Failed to update contacts: invalid account response");
                                    }
                                }
                            });
                        }
                    } else {
                        FeedActivity.showToast(FeedActivity.act.getString(R.string.interact_contacts_update_failed));
                        System.out.println("Failed to update contacts: invalid container");
                    }
                } else {
                    FeedActivity.showToast(FeedActivity.act.getString(R.string.interact_contacts_update_failed));
                    System.out.println("Failed to update contacts: invalid response");
                }
            }
        });
    }
}
