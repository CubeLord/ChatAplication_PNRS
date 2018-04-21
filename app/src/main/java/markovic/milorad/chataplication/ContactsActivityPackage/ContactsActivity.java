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
    ListView list;

    @Override
    protected void onResume() {
        super.onResume();

        Contact[] contacts = mdbHelper.readContacts();
        mAdapter.update(contacts);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

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
        mAdapter.update(cArray);


        logout.setOnClickListener(this);
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
//TODO: Kada se korisnik Login-uje on se ne pojavkljuje medju kontaktima
//TODO: Nakon uspesne registracije korisnik se prosledjuje na Login aktiviti
//TODO: U Register aktivitiju za unos novog kontakta koristiti INSERT SQL komandu (my bad)
//TODO: Tokom Login-a proveravamo da li se korisnik nalazi u bazi, ako se nalazi onda njegov ID sacuvati kao SHAREDPREFF, ovaj podatak ce se koristiti da se ne ispisuje medju kontaktima ulogovani korisnik i za trazenje poruka. Ako se korisnik ne nalazi u bazi onda mu ispisati poruku i ostaviti ka ga login activitiju
//TODO: Kada korisnik odabere kontakt iz liste, prikazuju se poruke koje za ID-jeve sender-a i receiver-a imaju korisnika i njegovog odabranog kontakta (ID korisnika je u SHAREDPREFF) (za ovo koristiti SELECT SQL komandu sa INNER J0IN dodatkom)
