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

import markovic.milorad.chataplication.ContactsActivityPackage.Contact;
import markovic.milorad.chataplication.ContactsActivityPackage.ContactsActivity;
import markovic.milorad.chataplication.DatabasePackage.ContactDbHelper;
import markovic.milorad.chataplication.HttpHelper;
import markovic.milorad.chataplication.HttpHelperReturn;
import markovic.milorad.chataplication.MainActivity;
import markovic.milorad.chataplication.NdkPackage.MyNDK;
import markovic.milorad.chataplication.R;
import markovic.milorad.chataplication.RegisterActivity;
import markovic.milorad.chataplication.ServicePackage.NotificationService;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    Toast toast = null;
    ListView list;
    MessageAdapter adapter;
    int receiver;
    int sender;
    String username;
    String sessionid;
    CountDownLatch countDownLatch;
    HttpHelper httpHelper;
    Handler handler = new Handler();
    HttpHelperReturn httpHelperReturn;
    JSONArray jsonArray;
    Message[] messages;
    MyNDK ndk = new MyNDK();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        receiver = Integer.parseInt(getIntent().getExtras().get(this.getString(R.string.BUNDLE_RECEIVER_ID)).toString());
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

        httpHelper = new HttpHelper();
        new Thread(new Runnable() {
            @Override
            public void run() {
                jsonArray = new JSONArray();
                try {
                    jsonArray = httpHelper.getJSONMessagesFromURL(getResources().getString(R.string.BASE_URL) + "/message/" + username, sessionid);
                    countDownLatch.countDown();

                } catch (JSONException e) {
                    Log.d("Debugging", "JSONException happened");
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.d("Debugging", "IOException happened");
                    e.printStackTrace();
                } catch (Exception e) {
                    Log.d("Debugging", "Exception happened");
                }
            }
        }).start();
        try {
            countDownLatch.await();
        } catch (Exception e) {
            //ignore
            Log.d("Debugging", "Exception happened in the countDownLatch");
        }

        try {
            int x;
            int color;
            messages = new Message[jsonArray.length()];
            String[] senders = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                senders[i] = jsonArray.getJSONObject(i).getString("sender");
                if(senders[i].equals(username)) {
                    x = 0;
                    color = getResources().getColor(R.color.colorLightGrey);
                } else
                {
                    x = 1;
                    color = getResources().getColor(R.color.colorTurquoise);
                }
                messages[i] = new Message(ndk.encryptString(jsonArray.getJSONObject(i).getString("data"), getString(R.string.SERVER_ENCRYPTION_KEY)), color, x,i, 98, 97);
            }
        } catch (Exception e) {
            Log.d("Debugging", "Cought Exception Reading JSONArray");
        }


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

        messageText.addTextChangedListener(this);
        send.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.messageActButtonLogout:
                Intent intent = new Intent(this, NotificationService.class);
                stopService(intent);

                Log.d(getResources().getString(R.string.BUTTON_LOG_TAG), getResources().getString(R.string.LOGOUT_BUTTON_LOG_MESSAGE));
                Intent main = new Intent(this, MainActivity.class);
                ContactsActivity.contactsActivity.finish();
                startActivity(main);
                finish();
                break;
            case R.id.messageActButtonSend:
                EditText msg = findViewById(R.id.messageActEditMessageText);
                final Message m = new Message(msg.getText().toString(), getResources().getColor(R.color.colorTurquoise), 1, 99, sender, receiver);
                Log.d("Debugging", "Message is: " + msg.getText().toString() + "\n AND KEY IS: "+ getString(R.string.SERVER_ENCRYPTION_KEY));
                Log.d("Debugging", "Encrypted Message is: " + ndk.encryptString(msg.getText().toString(), getString(R.string.SERVER_ENCRYPTION_KEY)));
                Log.d("Debugging", "Decrypted Message is: " + ndk.encryptString(ndk.encryptString(msg.getText().toString(), getString(R.string.SERVER_ENCRYPTION_KEY)), getString(R.string.SERVER_ENCRYPTION_KEY)));

//                Log.d("Debugging", "Encrypted Message is: " + ndk.encryptString("SomethingS", "SomethingKey"));

                httpHelper = new HttpHelper();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("receiver", username);
                            jsonObject.put("data", ndk.encryptString(m.getMessageText(), getString(R.string.SERVER_ENCRYPTION_KEY)));
//                            jsonObject.put("data", m.getMessageText());
                            httpHelperReturn = httpHelper.postMessageJSONObjectFromURL(getResources().getString(R.string.BASE_URL) + "/message", jsonObject, sessionid);
                            final boolean success = httpHelperReturn.isSuccess();
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