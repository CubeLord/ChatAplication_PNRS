package markovic.milorad.chataplication;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    Toast toast = null;
    ListView list;
    MessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Button logout = findViewById(R.id.messageActButtonLogout);
        Button send = findViewById(R.id.messageActButtonSend);
        EditText messageText = findViewById(R.id.messageActEditMessageText);
        TextView contactName = findViewById(R.id.messageActTextViewName);
        //Bundle bundle = getIntent().getExtras();

        list = findViewById(R.id.messageActListView);
        adapter = new MessageAdapter((this));
        list.setAdapter(adapter);

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                list.removeViewInLayout(view);
                adapter.remove(i);
                adapter.notifyDataSetChanged();
                return true;
            }
        });

        contactName.setText(getIntent().getExtras().get("NAME").toString());
        messageText.addTextChangedListener(this);
        send.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.messageActButtonLogout:
                Log.d(getResources().getString(R.string.BUTTON_LOG_TAG), getResources().getString(R.string.LOGOUT_BUTTON_LOG_MESSAGE));
                Intent main = new Intent(this, MainActivity.class);
                startActivity(main);
                break;
            case R.id.messageActButtonSend:
                EditText msg = findViewById(R.id.messageActEditMessageText);
                adapter.list.add(new Message(msg.getText().toString(), 0xffffffff, 1));
                list.setSelection(adapter.getCount() -1);
                msg.setText("");
                if(toast != null) toast.cancel();
                toast = Toast.makeText(this,getResources().getString(R.string.SENT_LOG_MESSAGE), Toast.LENGTH_LONG);
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
        if(((EditText)findViewById(R.id.messageActEditMessageText)).getText().toString().equals("")){
            findViewById(R.id.messageActButtonSend).setEnabled(false);
        } else {
            findViewById(R.id.messageActButtonSend).setEnabled(true);
        }
    }
}

class Message {
    String messageText;
    int color;
    int pos;

    Message(String s, int c, int p) {
        this.messageText = s;
        this.color = c;
        this.pos = p;
    }
}

class MessageAdapter extends BaseAdapter {
    ArrayList<Message> list;
    Context context;

    MessageAdapter(Context c) {
        context = c;
        list = new ArrayList<Message>();
        Resources res = context.getResources();
        String[] messages = res.getStringArray(R.array.dummyMessages);
        int col;
        Random rnd = new Random();

        for (int  i = 0; i < messages.length; i++) {
            col = rnd.nextInt(2) + 1;
            //TODO fix color representation
            if(col == 1) {
                list.add(new Message(messages[i], 0xffffffff, 1));
            } else {
                list.add(new Message(messages[i], 0x44444444, 0));
            }
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
        return i;
    }

    public void remove(int i) {
        list.remove(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        View row = inflater.inflate(R.layout.message_list_layout, viewGroup, false);

        TextView messageText = row.findViewById(R.id.messageActListItemText);
        final Message message = list.get(i);
        messageText.setText(message.messageText);
        messageText.setBackgroundColor(message.color);
        if (message.pos == 1) {
            messageText.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        } else {
            messageText.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        }
        return row;
    }
}