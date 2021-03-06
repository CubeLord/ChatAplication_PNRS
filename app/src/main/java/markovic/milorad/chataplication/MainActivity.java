package markovic.milorad.chataplication;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import markovic.milorad.chataplication.ContactsActivityPackage.Contact;
import markovic.milorad.chataplication.ContactsActivityPackage.ContactsActivity;
import markovic.milorad.chataplication.DatabasePackage.ContactDbHelper;
import markovic.milorad.chataplication.NdkPackage.MyNDK;
import markovic.milorad.chataplication.ServicePackage.NotificationService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    final String[] sessionid = new String[1];
    final CountDownLatch countDownLatch = new CountDownLatch(1);
    Button login;
    Button register;
    EditText username;
    EditText password;
    Toast toast;
    private Handler handler;
    private HttpHelper httpHelper;
    HttpHelperReturn httpHelperReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);


        login = findViewById(R.id.mainActButtonLogin);
        register = findViewById(R.id.mainActButtonRegister);
        username = findViewById(R.id.mainActEditUsername);
        password = findViewById(R.id.mainActEditPassword);
        toast = null;

        handler = new Handler();
        httpHelper = new HttpHelper();

        login.setOnClickListener(this);
        register.setOnClickListener(this);
        username.addTextChangedListener(this);
        password.addTextChangedListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mainActButtonRegister:
                Log.d(getResources().getString(R.string.BUTTON_LOG_TAG), getResources().getString(R.string.BUTTON_LOG_MESSAGE));

                Intent registerActivity = new Intent(this, RegisterActivity.class);
                startActivity(registerActivity);
                finish();

                break;
            case R.id.mainActButtonLogin:
                ContactDbHelper mdbHelper = new ContactDbHelper(this);
                SQLiteDatabase db = mdbHelper.getReadableDatabase();

                Cursor cursor = db.query(getResources().getString(R.string.TABLE_NAME), null, getResources().getString(R.string.COLUMN_USERNAME) + "=?", new String[]{username.getText().toString()}, null, null, null);
                cursor.moveToLast();

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("username", username.getText().toString());
                            jsonObject.put("password", password.getText().toString());
                             httpHelperReturn = (httpHelper.postJSONObjectFromURL(getResources().getString(R.string.BASE_URL) + "/login", jsonObject, MainActivity.this));
                            final boolean success = httpHelperReturn.isSuccess();
                            Log.d("Debugging", "SessionID in MainActivity: " + httpHelperReturn.getSessionid());
                            sessionid[0] = httpHelperReturn.getSessionid();

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "Request Message: " + httpHelperReturn.getMessage() +"\nRequest Code: "+ Integer.toString(httpHelperReturn.getCode()), Toast.LENGTH_LONG).show();
                                }
                            });
                            countDownLatch.countDown();
                        } catch (JSONException e) {
                            Log.d("Debugging", "JSONException happened");
                            e.printStackTrace();
                        } catch (IOException e) {
                            Log.d("Debugging", "IOException happened");
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
                try {
                    countDownLatch.await();
                    if (httpHelperReturn.getCode() != 200) {
                        Log.d("Debugging", "Code is: "+ httpHelperReturn.getCode());
                        IOException e = new IOException();
                        throw e;
                    }
                    int id = cursor.getInt(cursor.getColumnIndex(getResources().getString(R.string.COLUMN_CONTACT_ID)));
                    Intent contacts = new Intent(this, ContactsActivity.class);
                    Bundle b = new Bundle();
                    b.putString("Login_ID", Integer.toString(id));
                    String sessionid_0 = sessionid[0];
                    b.putString("Session_ID", sessionid_0);
                    contacts.putExtras(b);
                    startActivity(contacts);
                    finish();
                } catch (Exception e) {
                    if (toast != null) toast.cancel();
                    toast = Toast.makeText(this, getResources().getString(R.string.WRONT_IDENTIFICATION_TOAST), Toast.LENGTH_LONG);
                    toast.show();
                }
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
        Log.d(getResources().getString(R.string.EDITTEXT_LOG_TAG), getResources().getString(R.string.EDITTEXT_LOG_USERNAME_MESSAGE) + username.getText().toString());
        Log.d(getResources().getString(R.string.EDITTEXT_LOG_TAG), getResources().getString(R.string.EDITTEXT_LOG_PASSWORD_MESSAGE) + password.getText().toString());

        if ((username.getText().toString().trim().equals("")) || (password.getText().toString().length() < 6)) {
            findViewById(R.id.mainActButtonLogin).setEnabled(false);
        } else {
            findViewById(R.id.mainActButtonLogin).setEnabled(true);
        }
    }
}