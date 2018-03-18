package markovic.milorad.chataplication;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher{

    Button login;
    Button register;
    EditText username;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        login = findViewById(R.id.mainActButtonLogin);
        register = findViewById(R.id.mainActButtonRegister);
        username = (EditText) findViewById(R.id.mainActEditUsername);
        password = (EditText) findViewById(R.id.mainActEditPassword);

        login.setOnClickListener(this);
        register.setOnClickListener(this);
        username.addTextChangedListener(this);
        password.addTextChangedListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.mainActButtonRegister:
                Log.d("Button", "Register Button Pressed!");

                Intent registerActivity = new Intent(this, RegisterActivity.class);
                startActivity(registerActivity);

                break;
            case R.id.mainActButtonLogin:
                Log.d("Button", "Login Button Pressed!");

                Intent contacts = new Intent(this, ContactsActivity.class);
                startActivity(contacts);

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
        if (editable.hashCode() == username.getText().hashCode()) {
            Log.d("EditText", "Username =" + username.getText().toString());

            if((username.getText().toString().trim().equals("")) || (password.getText().toString().length() < 6))
            {
                ((Button)findViewById(R.id.mainActButtonLogin)).setEnabled(false);
            }else
            {
                ((Button)findViewById(R.id.mainActButtonLogin)).setEnabled(true);
            }
        }

        if(editable.hashCode() == password.getText().hashCode()) {
            Log.d("EditText", "Password =" + password.getText().toString());
            if((password.getText().toString().length() < 6) || (username.getText().toString().trim().equals("")))
            {
                ((Button)findViewById(R.id.mainActButtonLogin)).setEnabled(false);
            }else
            {
                ((Button)findViewById(R.id.mainActButtonLogin)).setEnabled(true);
            }
        }

    }
}
