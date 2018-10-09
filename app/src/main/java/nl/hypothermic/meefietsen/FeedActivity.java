package nl.hypothermic.meefietsen;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import nl.hypothermic.meefietsen.frags.AccountFragment;
import nl.hypothermic.meefietsen.frags.ContactsFragment;
import nl.hypothermic.meefietsen.frags.HomeFragment;

public class FeedActivity extends AppCompatActivity {

    // FIXME: dit is niet de beste manier om dit te doen...
    public static FeedActivity act;

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

        BottomNavigationView nav = findViewById(R.id.navigation);
        nav.setSelectedItemId(R.id.navigation_home);
        nav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showFragment(homeFragment);
    }

    private void showFragment(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .hide(contactsFragment)
                .hide(homeFragment)
                .hide(accountFragment)
                .commit();
        getFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .show(fragment)
                .commit();
    }
}
