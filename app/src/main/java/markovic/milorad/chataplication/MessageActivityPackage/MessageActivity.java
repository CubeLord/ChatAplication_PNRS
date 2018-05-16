package markovic.milorad.chataplication.MessageActivityPackage;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import markovic.milorad.chataplication.ContactsActivityPackage.ContactsActivity;
import markovic.milorad.chataplication.DatabasePackage.ContactDbHelper;
import markovic.milorad.chataplication.MainActivity;
import markovic.milorad.chataplication.R;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    Toast toast = null;
    ListView list;
    MessageAdapter adapter;
    int reciver;
    int sender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        reciver = Integer.parseInt(getIntent().getExtras().get(this.getString(R.string.BUNDLE_RECEIVER_ID)).toString());
        sender = Integer.parseInt(getIntent().getExtras().get(this.getString(R.string.BUNDLE_SENDER_ID)).toString());

        Button logout = findViewById(R.id.messageActButtonLogout);
        Button send = findViewById(R.id.messageActButtonSend);
        EditText messageText = findViewById(R.id.messageActEditMessageText);
        TextView contactName = findViewById(R.id.messageActTextViewName);

        list = findViewById(R.id.messageActListView);
        adapter = new MessageAdapter((this));
        list.setAdapter(adapter);
        list.setSelection(adapter.getCount() - 1);

        final ContactDbHelper helper = new ContactDbHelper(this);
        Message[] messages = helper.readMessages(sender, reciver);
        if (messages != null) {
            adapter.update(messages);
        }

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                helper.deleteMessage((int)adapter.getItemId(i));
                adapter.remove(i);
                adapter.notifyDataSetChanged();
                return true;
            }
        });

        contactName.setText(getIntent().getExtras().get(this.getString(R.string.BUNDLE_CONTACT_NAME)).toString());

        SQLiteDatabase db = helper.getReadableDatabase();

        messageText.addTextChangedListener(this);
        send.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.messageActButtonLogout:
                Log.d(getResources().getString(R.string.BUTTON_LOG_TAG), getResources().getString(R.string.LOGOUT_BUTTON_LOG_MESSAGE));
                Intent main = new Intent(this, MainActivity.class);
                ContactsActivity.contactsActivity.finish();
                startActivity(main);
                finish();
                break;
            case R.id.messageActButtonSend:
                EditText msg = findViewById(R.id.messageActEditMessageText);
                Message m = new Message(msg.getText().toString(), getResources().getColor(R.color.colorTurquoise), 1, 99, sender, reciver);
                ContactDbHelper helper = new ContactDbHelper(this);
                helper.insert(m);
                Message[] messages = helper.readMessages(m.getSender_id(), m.getReceiver_id());
                m.setId(messages[messages.length-1].getId());
                adapter.list.add(m);
                list.setSelection(adapter.getCount() - 1);
                msg.setText("");
                if (toast != null) toast.cancel();
                toast = Toast.makeText(this, getResources().getString(R.string.SENT_LOG_MESSAGE), Toast.LENGTH_LONG);
                toast.show();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (((EditText) findViewById(R.id.messageActEditMessageText)).getText().toString().equals("")) {
            findViewById(R.id.messageActButtonSend).setEnabled(false);
        } else {
            findViewById(R.id.messageActButtonSend).setEnabled(true);
        }
    }


}