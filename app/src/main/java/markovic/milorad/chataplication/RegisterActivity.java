package markovic.milorad.chataplication;

import android.content.Intent;
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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher{

    EditText username;
    EditText password;
    EditText email;
    Button registerButton;
    Spinner spinner;
    DatePicker datePicker;

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
        email= findViewById(R.id.registerActEditEmail);
        registerButton = findViewById(R.id.registerActButtonRegister);

        registerButton.setOnClickListener(this);
        username.addTextChangedListener(this);
        password.addTextChangedListener(this);
        email.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {
        Log.d(getResources().getString(R.string.BUTTON_LOG_TAG), getResources().getString(R.string.BUTTON_LOG_MESSAGE).toString());
        Intent contactsActivity = new Intent(this, ContactsActivity.class);
        startActivity(contactsActivity);
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
        if( !(username.getText().toString().equals("")) &
                !(password.getText().toString().equals("")) &
                (isEmailValid(email.getText().toString())) &
                (password.getText().toString().length() >= 6))
        {
            registerButton.setEnabled(true);
        } else
        {
            registerButton.setEnabled(false);
        }
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
