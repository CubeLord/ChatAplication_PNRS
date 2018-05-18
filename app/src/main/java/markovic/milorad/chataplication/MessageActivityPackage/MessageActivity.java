package markovic.milorad.chataplication.MessageActivityPackage;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import markovic.milorad.chataplication.ContactsActivityPackage.ContactsActivity;
import markovic.milorad.chataplication.DatabasePackage.ContactDbHelper;
import markovic.milorad.chataplication.HttpHelper;
import markovic.milorad.chataplication.HttpHelperReturn;
import markovic.milorad.chataplication.MainActivity;
import markovic.milorad.chataplication.R;
import markovic.milorad.chataplication.RegisterActivity;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    Toast toast = null;
    ListView list;
    MessageAdapter adapter;
    int reciver;
    int sender;
    String username;
    String sessionid;
    CountDownLatch countDownLatch;
    HttpHelper httpHelper;
    Handler handler = new Handler();
    HttpHelperReturn httpHelperReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        reciver = Integer.parseInt(getIntent().getExtras().get(this.getString(R.string.BUNDLE_RECEIVER_ID)).toString());
        sender = Integer.parseInt(getIntent().getExtras().get(this.getString(R.string.BUNDLE_SENDER_ID)).toString());
        username = getIntent().getExtras().getString(this.getString(R.string.BUNDLE_CONTACT_NAME));
        sessionid = getIntent().getExtras().getString("sessionid");

        Button logout = findViewById(R.id.messageActButtonLogout);
        Button send = findViewById(R.id.messageActButtonSend);
        EditText messageText = findViewById(R.id.messageActEditMessageText);
        TextView contactName = findViewById(R.id.messageActTextViewName);

        list = findViewById(R.id.messageActListView);
        adapter = new MessageAdapter((this));
        list.setAdapter(adapter);
        list.setSelection(adapter.getCount() - 1);

        final ContactDbHelper helper = new ContactDbHelper(this);
        countDownLatch = new CountDownLatch(1);
        Log.d("Debugging", "Where is error- 0");
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                JSONArray jsonArray = new JSONArray();
//                try {
//                    Log.d("Debugging", "Inside the Thread");
//                    jsonArray = httpHelper.getJSONMessagesFromURL(getResources().getString(R.string.BASE_URL) + "/message/" + username, sessionid);
//                    Log.d("Debugging", "after jsonArray asignment");
//                    countDownLatch.countDown();
////                    handler.post(new Runnable() {
////                        @Override
////                        public void run() {
////                            Toast.makeText(RegisterActivity.this, "Adding new user: " + success, Toast.LENGTH_LONG).show();
////                        }
////                    });
//                } catch (JSONException e) {
//                    Log.d("Debugging", "JSONException happened");
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    Log.d("Debugging", "IOException happened");
//                    e.printStackTrace();
//                } catch (Exception e) {
//                    Log.d("Debugging", "Exception happened");
//                }
//            }
//        }).start();
//        Log.d("Debugging", "Where is error- 1");
//        try {
//            countDownLatch.await();
//        } catch (Exception e) {
//            //ignore
//            Log.d("Debugging", "Exception happened in the countDownLatch");
//        }
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
                final Message m = new Message(msg.getText().toString(), getResources().getColor(R.color.colorTurquoise), 1, 99, sender, reciver);

                httpHelper = new HttpHelper();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("receiver", username);
                            jsonObject.put("data", m.getMessageText());
//                            Log.d("Debugging", "Sending Message");
//                            Log.d("Debugging", "Session id in MessageActivity is: " + sessionid + "\n" + "username in MessageActivity is:" + username + "\nnmessage in MessageActivity is: "+ m.getMessageText());
                            httpHelperReturn = httpHelper.postMessageJSONObjectFromURL(getResources().getString(R.string.BASE_URL) + "/message", jsonObject, sessionid);
                            final boolean success = httpHelperReturn.isSuccess();
//                            Log.d("Debugging", "Message sent!");
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MessageActivity.this, "Request Message: " + httpHelperReturn.getMessage() +"\nRequest Code: "+ Integer.toString(httpHelperReturn.getCode()), Toast.LENGTH_LONG).show();
                                }
                            });

                        } catch (JSONException e) {
                            Log.d("Debugging", "JSONException happened");
                            e.printStackTrace();
                        } catch (IOException e) {
                            Log.d("Debugging", "IOException happened");
                            e.printStackTrace();
                        }
                    }
                }).start();


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