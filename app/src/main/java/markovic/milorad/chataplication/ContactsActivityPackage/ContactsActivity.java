package markovic.milorad.chataplication.ContactsActivityPackage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import markovic.milorad.chataplication.DatabasePackage.ContactDbHelper;
import markovic.milorad.chataplication.HttpHelper;
import markovic.milorad.chataplication.HttpHelperReturn;
import markovic.milorad.chataplication.MainActivity;
import markovic.milorad.chataplication.R;
import markovic.milorad.chataplication.RegisterActivity;

public class ContactsActivity extends AppCompatActivity implements View.OnClickListener {

    public static ContactsActivity contactsActivity;
    public static ContactsAdapter mAdapter;
    public static ContactDbHelper mdbHelper;
    public int SharedPreff;
    HttpHelper httpHelper;
    String sessionid;
    ListView list;
    Handler handler;
    Contact[] contacts;
    CountDownLatch countDownLatch;
    JSONArray jsonArray;

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

        SharedPreff = Integer.parseInt(getIntent().getExtras().getString("Login_ID"));
        sessionid = getIntent().getExtras().getString("Session_ID");
        Log.d("Debugging", "Session id in ContactsActivity: " + sessionid);

        Button logout = findViewById(R.id.contactsActButtonLogout);
        ImageButton refresh = findViewById(R.id.contactsActRefreshButton);

        contactsActivity = this;
        list = findViewById(R.id.contactsActListView);
        mAdapter = new ContactsAdapter(this, SharedPreff, sessionid);
        list.setAdapter(mAdapter);

        mdbHelper = new ContactDbHelper(this);

        httpHelper = new HttpHelper();
        handler = new Handler();
        countDownLatch = new CountDownLatch(1);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonArray = httpHelper.getJSONContactsFromURL(getResources().getString(R.string.BASE_URL) + "/contacts", sessionid);
                    countDownLatch.countDown();
                } catch (JSONException e) {
                    Log.d("Debugging", "JSONException happened in ContactsActivity");
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.d("Debugging", "IOException happened in ContactsActivity");
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            countDownLatch.await();
        } catch (Exception e) {
            Log.d("Debugging", "CountDownLatch exception happened!");
        }

        try {
            contacts = new Contact[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                contacts[i] = new Contact(jsonArray.getJSONObject(i).getString("username"), "fn", "ls", 66);
            }
        } catch (Exception e) {
            Log.d("Debugging", "Cought Exception Reading JSONArray");
        }

//        Log.d("Debugging", "Before RemovedLogedin");
        Contact[] cArray = mdbHelper.readContacts();
//        Contact[] cArray = removeLogedIn(ocArray, "Cube.Lord");
//        Log.d("Debugging", "After RemovedLogedin");
        for (int i = 0; i < cArray.length; i++) {
            mdbHelper.deleteContact(cArray[i].name);
        }
        for (int i = 0; i < contacts.length; i++) {
            mdbHelper.insert(contacts[i]);
        }

        mAdapter.update(contacts);
        logout.setOnClickListener(this);
        refresh.setOnClickListener(this);
    }

    public Contact[] removeLogedIn(Contact[] fullContacts, String User) {
        if (User == "") return fullContacts;
//        TODO: Fix removeLogedIn function to work with Server


        Contact[] ncArray = new Contact[fullContacts.length - 1];
        int x = 0;
        Log.d("Debugging", "REMOVELOGEDIN - 0");
        for (int j = 0; j < fullContacts.length; j++) {
            if (fullContacts[j].getName() != User) {
                Log.d("Debugging", "REMOVELOGEDIN - " + fullContacts[j].getName());

                ncArray[x] = fullContacts[j];
                x++;

            } else {
                Log.d("Debugging", "REMOVELOGEDIN - 11");

            }
        }
        Log.d("Debugging", "REMOVELOGEDIN - 10");
        return fullContacts;

//        return ncArray;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.contactsActButtonLogout:
                Log.d(getResources().getString(R.string.BUTTON_LOG_TAG), getResources().getString(R.string.LOGOUT_BUTTON_LOG_MESSAGE));

                Thread thread1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            final HttpHelperReturn httpHelperReturn = httpHelper.postlogoutJSONObjectFromURL(getResources().getString(R.string.BASE_URL) + "/logout", sessionid, ContactsActivity.this);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ContactsActivity.this, "Request Message: " + httpHelperReturn.getMessage() +"\nRequest Code: "+ Integer.toString(httpHelperReturn.getCode()), Toast.LENGTH_LONG).show();
                                }
                            });
                            countDownLatch.countDown();
                        } catch (JSONException e) {
                            Log.d("Debugging", "JSONException happened in ContactsActivity");
                            e.printStackTrace();
                        } catch (IOException e) {
                            Log.d("Debugging", "IOException happened in ContactsActivity");
                            e.printStackTrace();
                        }
                    }
                });
                thread1.start();
                try {
                    countDownLatch.await();
                } catch (Exception e) {
                    Log.d("Debugging", "CountDownLatch exception happened!");
                }

                Intent mainActivity = new Intent(this, MainActivity.class);
                startActivity(mainActivity);
                finish();
                break;
            case R.id.contactsActRefreshButton:
                Log.d("Debugging", "Refresh button pressed");
                httpHelper = new HttpHelper();
                handler = new Handler();

                countDownLatch = new CountDownLatch(1);
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonArray = httpHelper.getJSONContactsFromURL(getResources().getString(R.string.BASE_URL) + "/contacts", sessionid);
                            countDownLatch.countDown();
                        } catch (JSONException e) {
                            Log.d("Debugging", "JSONException happened in ContactsActivity");
                            e.printStackTrace();
                        } catch (IOException e) {
                            Log.d("Debugging", "IOException happened in ContactsActivity");
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
                try {
                    countDownLatch.await();
                } catch (Exception e) {
                    Log.d("Debugging", "CountDownLatch exception happened!");
                }

                try {
                    contacts = new Contact[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        contacts[i] = new Contact(jsonArray.getJSONObject(i).getString("username"), "fn", "ls", 66);
                    }
                } catch (Exception e) {
                    Log.d("Debugging", "Cought Exception Reading JSONArray");
                }

//        Log.d("Debugging", "Before RemovedLogedin");
                Contact[] cArray = mdbHelper.readContacts();
//        Contact[] cArray = removeLogedIn(ocArray, "Cube.Lord");
//        Log.d("Debugging", "After RemovedLogedin");
                for (int i = 0; i < cArray.length; i++) {
                    mdbHelper.deleteContact(cArray[i].name);
                }
                for (int i = 0; i < contacts.length; i++) {
                    mdbHelper.insert(contacts[i]);
                }


                Contact[] contacts = mdbHelper.readContacts();
                mAdapter.update(contacts);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}