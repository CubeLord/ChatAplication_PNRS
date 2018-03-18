package markovic.milorad.chataplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher{

    EditText username;
    EditText password;
    EditText email;
    Button registerButton;
    Spinner spinner;
    CalendarView calendarView;
    private static final String BUTTON_LOG_TAG = "Button";
    private static final String BUTTON_LOG_MESSAGE = "Register button pressed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        spinner = (Spinner)findViewById(R.id.registerActSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.genders, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        calendarView = (CalendarView)findViewById(R.id.registerActCalendarview);
        calendarView.setDate(1514761200000L); //postavljanje datuma na 01.01.1996 - 820450800000L, a na 01.01.2018 - 1514761200000L
        calendarView.setMaxDate(Calendar.getInstance().getTimeInMillis());

        username = (EditText)findViewById(R.id.registerActEditUsername);
        password = (EditText)findViewById(R.id.registerActEditPassword);
        email= (EditText)findViewById(R.id.registerActEditEmail);
        registerButton = (Button)findViewById(R.id.registerActButtonRegister);

        registerButton.setOnClickListener(this);
        username.addTextChangedListener(this);
        password.addTextChangedListener(this);
        email.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {
                Log.d(BUTTON_LOG_TAG, BUTTON_LOG_MESSAGE);
                Intent contactsActivity = new Intent(this, ContactsActivity.class);
                startActivity(contactsActivity);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if( !(username.getText().toString().equals("")) &
                !(password.getText().toString().equals("")) &
                !(email.getText().toString().equals("")) &
                (password.getText().toString().length() >= 6))
        {
            registerButton.setEnabled(true);
        } else
        {
            registerButton.setEnabled(false);
        }
    }
}
