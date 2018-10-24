package nl.hypothermic.meefietsen;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import nl.hypothermic.meefietsen.async.GenericCallback;
import nl.hypothermic.meefietsen.core.MeefietsClient;
import nl.hypothermic.meefietsen.frags.AccountFragment;
import nl.hypothermic.meefietsen.frags.ContactsFragment;
import nl.hypothermic.meefietsen.frags.HomeFragment;

public class FeedActivity extends AppCompatActivity {

    // FIXME: dit is niet de beste manier om dit te doen...
    public static FeedActivity act;

    public static void showToast(final String msg) {
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(act, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setAccountFragment(AccountFragment accountFragment) {
        this.accountFragment = accountFragment;
    }

    public void setContactsFragment(ContactsFragment contactsFragment) {
        this.contactsFragment = contactsFragment;
    }

    public void setHomeFragment(HomeFragment homeFragment) {
        this.homeFragment = homeFragment;
    }

    private AccountFragment accountFragment;
    private ContactsFragment contactsFragment;
    private HomeFragment homeFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_contacts:
                    showFragment(contactsFragment);
                    return true;
                case R.id.navigation_home:
                    showFragment(homeFragment);
                    return true;
                case R.id.navigation_account:
                    showFragment(accountFragment);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        act = this;

        ((BottomNavigationView) findViewById(R.id.navigation)).setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((BottomNavigationView) findViewById(R.id.navigation)).setSelectedItemId(R.id.navigation_home);
        showFragment(homeFragment);

        final MeefietsClient inst = MeefietsClient.getInstance();
        inst.updateLocalAccount(new GenericCallback<Boolean>() {
            @Override
            public void onAction(Boolean val) {
                ((TextView) findViewById(R.id.accountTel)).setText(inst.localAccount.num.toFormattedString());
                ((TextView) findViewById(R.id.accountName)).setText(inst.localAccount.userName);
            }
        });
    }

    private void showFragment(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .hide(contactsFragment)
                .hide(homeFragment)
                .hide(accountFragment)
                .commit();
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.anim_fade_in, R.anim.anim_fade_in)
                .show(fragment)
                .commit();
    }

    private long lastBackPress = 0;

    @Override
    public void onBackPressed() {
        if (lastBackPress + SplashActivity.BACK_PRESS_TIMEOUT > System.currentTimeMillis()) {
            this.finishAffinity();
        } else {
            lastBackPress = System.currentTimeMillis();
        }
    }
}
