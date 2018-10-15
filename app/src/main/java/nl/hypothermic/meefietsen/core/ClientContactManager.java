package nl.hypothermic.meefietsen.core;

import java.util.ArrayList;

import nl.hypothermic.mfsrv.obj.account.Account;

public class ClientContactManager {

    private static ClientContactManager instance;

    public static ClientContactManager getInstance() {
        if (instance == null) {
            instance = new ClientContactManager();
        }
        return instance;
    }

    private ArrayList<Account> contacts = new ArrayList<>();

    public void addContact(Account event) {
        contacts.add(event);
    }

    public ArrayList<Account> getContacts() {
        return contacts;
    }
}
