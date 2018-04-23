package markovic.milorad.chataplication.ContactsActivityPackage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import markovic.milorad.chataplication.DatabasePackage.ContactDbHelper;
import markovic.milorad.chataplication.MainActivity;
import markovic.milorad.chataplication.R;

public class ContactsActivity extends AppCompatActivity implements View.OnClickListener {

    public static ContactsActivity contactsActivity;
    public static ContactsAdapter mAdapter;
    public static ContactDbHelper mdbHelper;
    public int SharedPreff;
    ListView list;

    @Override
    protected void onResume() {
        super.onResume();

        Contact[] contacts = mdbHelper.readContacts();
        mAdapter.update(removeLogedIn(contacts));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        SharedPreff = Integer.parseInt(getIntent().getExtras().getString("Login_ID"));

        Button logout = findViewById(R.id.contactsActButtonLogout);
        contactsActivity = this;
        list = findViewById(R.id.contactsActListView);
        mAdapter = new ContactsAdapter(this);
        list.setAdapter(mAdapter);

        mdbHelper = new ContactDbHelper(this);

        Contact c = new Contact("CubeLord", "Milorad", "Markovic", 0);
        mdbHelper.insert(c);
        Contact s = new Contact("SuncevoDete", "Jelena", "Boroja", 1);
        mdbHelper.insert(s);
        Contact i = new Contact("IskeCode", "Vesna", "Isic", 3);
        mdbHelper.insert(i);

        Contact[] cArray = mdbHelper.readContacts();
        mAdapter.update(removeLogedIn(cArray));

        logout.setOnClickListener(this);
    }

    public Contact[] removeLogedIn(Contact[] fullContacts) {
        if (SharedPreff == -1) return fullContacts;

        Contact[] ncArray = new Contact[fullContacts.length - 1];
        int x = 0;
        for (int j = 0; j < fullContacts.length; j++) {
            if (fullContacts[j].getId() != SharedPreff) {
                ncArray[x] = fullContacts[j];
                x++;
            }
        }

        return ncArray;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.contactsActButtonLogout:
                Log.d(getResources().getString(R.string.BUTTON_LOG_TAG), getResources().getString(R.string.LOGOUT_BUTTON_LOG_MESSAGE));
                Intent mainActivity = new Intent(this, MainActivity.class);
                startActivity(mainActivity);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}

//TODO: Ubaciti Message Tabelu (koristiti SECONDARY KEY za kontakte)
//TODO: U Register aktivitiju za unos novog kontakta koristiti INSERT SQL komandu (my bad)
//TODO: Kada korisnik odabere kontakt iz liste, prikazuju se poruke koje za ID-jeve sender-a i receiver-a imaju korisnika i njegovog odabranog kontakta (ID korisnika je u SHAREDPREFF) (za ovo koristiti SELECT SQL komandu sa INNER J0IN dodatkom)
