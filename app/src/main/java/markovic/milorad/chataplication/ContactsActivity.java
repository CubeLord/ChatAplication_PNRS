package markovic.milorad.chataplication;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity implements View.OnClickListener {

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        Button logout = findViewById(R.id.contactsActButtonLogout);

        list = findViewById(R.id.contactsActListView);
        list.setAdapter(new ContactsAdapter(this));

        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.contactsActButtonLogout:
                Log.d(getResources().getString(R.string.BUTTON_LOG_TAG), getResources().getString(R.string.LOGOUT_BUTTON_LOG_MESSAGE));
                Intent mainActivity = new Intent(this, MainActivity.class);
                startActivity(mainActivity);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}

class Contact {
    //TODO add Contact images
    String name;

    Contact(String name){
        this.name = name;
    }
}

class ContactsAdapter extends BaseAdapter {
    ArrayList<Contact> list;
    Context context;

    ContactsAdapter(Context c){
        context = c;
        list = new ArrayList<Contact>();
        Resources res = context.getResources();
        String[] contacts = res.getStringArray(R.array.contacts);

        for(int i = 0; i <(res.getStringArray(R.array.contacts)).length; i++)
        {
            list.add(new Contact(contacts[i]));
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i; //Because we are not using a Database this should be the same as the ID
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //LayoutInflater -> creates a new object with the same properties, which is not what findViewbyID would do
        //FindViewById -> uses the same object each time
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.listlayout, viewGroup, false);

        TextView name = row.findViewById(R.id.contactsActTextviewContact);
        name.setTypeface(null, Typeface.BOLD_ITALIC);

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(context.getResources().getString(R.string.CONTACT_LOG_TAG), context.getResources().getString(R.string.CONTACT_LOG_MESSAGE));

                Intent messageAct = new Intent(context, MessageActivity.class);
                context.startActivity(messageAct);
            }
        });

        Contact contact = list.get(i);
        name.setText(contact.name);
        return row;
    }
}