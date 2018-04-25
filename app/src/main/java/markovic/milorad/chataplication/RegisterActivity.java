package markovic.milorad.chataplication;

import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import markovic.milorad.chataplication.ContactsActivityPackage.Contact;
import markovic.milorad.chataplication.DatabasePackage.ContactDbHelper;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    EditText username;
    EditText password;
    EditText email;
    Button registerButton;
    Spinner spinner;
    DatePicker datePicker;

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        spinner = findViewById(R.id.registerActSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.genders, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        datePicker = findViewById(R.id.registerActDatePicker);
        datePicker.setMaxDate(Calendar.getInstance().getTimeInMillis());

        username = findViewById(R.id.registerActEditUsername);
        password = findViewById(R.id.registerActEditPassword);
        email = findViewById(R.id.registerActEditEmail);
        registerButton = findViewById(R.id.registerActButtonRegister);

        registerButton.setOnClickListener(this);
        username.addTextChangedListener(this);
        password.addTextChangedListener(this);
        email.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {
        Log.d(getResources().getString(R.string.BUTTON_LOG_TAG), getResources().getString(R.string.BUTTON_LOG_MESSAGE).toString());

        Contact newContact = new Contact(username.getText().toString(), ((EditText) findViewById(R.id.registerActEditFirstName)).getText().toString(), ((EditText) findViewById(R.id.registerActEditLastName)).getText().toString(), 98);
        ContactDbHelper mdbHelper = new ContactDbHelper(this);

        SQLiteDatabase mdb = mdbHelper.getWritableDatabase();
        Resources res = this.getResources();
        mdb.execSQL("INSERT OR REPLACE INTO " + res.getString(R.string.TABLE_NAME) + "(" +
                res.getString(R.string.COLUMN_USERNAME) + ", " +
                res.getString(R.string.COLUMN_FIRSTNAME) + ", " +
                res.getString(R.string.COLUMN_LASTNAME) + ") " +
                "VALUES (\"" + newContact.getName() + "\", \"" + newContact.getFirstName() + "\", \"" + newContact.getLastName() + "\");");

        Intent mainActivity = new Intent(this, MainActivity.class);
        startActivity(mainActivity);
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (!(username.getText().toString().equals("")) &
                !(password.getText().toString().equals("")) &
                (isEmailValid(email.getText().toString())) &
                (password.getText().toString().length() >= 6)) {
            registerButton.setEnabled(true);
        } else {
            registerButton.setEnabled(false);
        }
    }
}

//TODO: Back na RegisterActivitiju Treba da vraca na Login, Ili da izlazi iz aplikacije - Pitaj Asistente?