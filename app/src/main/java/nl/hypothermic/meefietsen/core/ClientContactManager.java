package nl.hypothermic.meefietsen.core;

import java.util.ArrayList;
import java.util.Iterator;

import nl.hypothermic.mfsrv.obj.account.Account;
import nl.hypothermic.mfsrv.obj.auth.TelephoneNum;

public class ClientContactManager {

    private static ClientContactManager instance;

    public static ClientContactManager getInstance() {
        if (instance == null) {
            instance = new ClientContactManager();
        }
        return instance;
    }

    private ArrayList<Account> contacts = new ArrayList<>();

    public void addContact(Account contact) {
        contacts.add(contact);
    }

    public void deleteContact(Account contact) {
        for (Account iter : contacts) {
            if (iter.num.country == contact.num.country && iter.num.number == contact.num.number) {
                contacts.remove(iter);
            }
        }
    }

    public void deleteContact(TelephoneNum num) {
        for (Iterator<Account> it = contacts.iterator(); it.hasNext(); ) {
            Account iter = it.next();
            if (iter.num.country == num.country && iter.num.number == num.number) {
                it.remove();
                break;
            }
        }
    }

    public ArrayList<Account> getContacts() {
        return contacts;
    }
}
