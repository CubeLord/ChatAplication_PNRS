package markovic.milorad.chataplication.ContactsActivityPackage;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import markovic.milorad.chataplication.MainActivity;
import markovic.milorad.chataplication.MessageActivityPackage.MessageActivity;
import markovic.milorad.chataplication.R;

public class ContactsActivity extends AppCompatActivity implements View.OnClickListener {

    ListView list;
    public static ContactsActivity contactsActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        Button logout = findViewById(R.id.contactsActButtonLogout);
        contactsActivity = this;
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
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}

class Contact {
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

        for(int i = 0; i <contacts.length; i++)
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
        return i; //Because we are not using a Data base this should be the same as the ID
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row = view;
        if (row == null) {
            //LayoutInflater -> creates a new object with the same properties, which is not what findViewbyID would do
            //FindViewById -> uses the same object each time
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.listlayout, viewGroup, false);

            ViewHolderContact holder = new ViewHolderContact();
            holder.setName(((TextView) row.findViewById(R.id.contactsActTextviewContact)));
            holder.setSend(((ImageView) row.findViewById(R.id.contactsActListItemImage)));
            holder.setIcon(((TextView) row.findViewById(R.id.contactsActListItemIcon)));
            row.setTag(holder);
        }

        ViewHolderContact holder = ((ViewHolderContact) row.getTag());
        holder.getName().setTypeface(null, Typeface.BOLD_ITALIC);
        final Contact contact = list.get(i);

        holder.getSend().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(context.getResources().getString(R.string.CONTACT_LOG_TAG), context.getResources().getString(R.string.CONTACT_LOG_MESSAGE));

                Bundle bundle = new Bundle();
                bundle.putString(context.getString(R.string.BUNDLE_CONTACT_NAME), contact.name);
                Intent messageAct = new Intent(context, MessageActivity.class);
                messageAct.putExtras(bundle);

                context.startActivity(messageAct);
                ContactsActivity.contactsActivity.finish();

            }
        });


        holder.getName().setText(contact.name);
        String s = contact.name.toString().toUpperCase();
        String[] split = s.split("");
        holder.getIcon().setText((split[1]));

        Random rnd = new Random();
        holder.getIcon().setBackgroundColor(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));
        return row;
    }
}

class ViewHolderContact {
    private ImageView send = null;
    private TextView icon = null;
    private TextView name = null;

    public ImageView getSend() {
        return send;
    }

    public TextView getIcon() {
        return icon;
    }

    public TextView getName() {
        return name;
    }

    public void setSend(ImageView send) {
        this.send = send;
    }

    public void setIcon(TextView icon) {
        this.icon = icon;
    }

    public void setName(TextView name) {
        this.name = name;
    }
}