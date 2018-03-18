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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Spinner spinner = (Spinner)findViewById(R.id.registerActSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.genders, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        CalendarView calendarView = (CalendarView)findViewById(R.id.registerActCalendarview);
        calendarView.setDate(1514761200000L); //postavljanje datuma na 01.01.1996 - 820450800000L, a na 01.01.2018 - 1514761200000L
        calendarView.setMaxDate(Calendar.getInstance().getTimeInMillis());

        final EditText username = (EditText)findViewById(R.id.registerActEditUsername);
        final EditText password = (EditText)findViewById(R.id.registerActEditPassword);
        final EditText email= (EditText)findViewById(R.id.registerActEditEmail);
        final Button registerButton = (Button)findViewById(R.id.registerActButtonRegister);

        registerButton.setOnClickListener(this);

        TextWatcher watcher = new TextWatcher() {
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
        };

        username.addTextChangedListener(watcher);
        password.addTextChangedListener(watcher);
        email.addTextChangedListener(watcher);



    }

    @Override
    public void onClick(View view) {
                Log.d("Button", "Register button pressed");
                Intent contactsActivity = new Intent(this, ContactsActivity.class);
                startActivity(contactsActivity);
    }
}
