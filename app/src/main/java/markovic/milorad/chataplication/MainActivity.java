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

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        final Button login = findViewById(R.id.main_act_button_login);
        Button register = findViewById(R.id.main_act_button_register);
        final EditText username = (EditText)findViewById(R.id.main_act_edit_username);
        final EditText password = (EditText)findViewById(R.id.main_act_edit_password);


        login.setOnClickListener(this);
        register.setOnClickListener(this);
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("EditText", "Username =" + username.getText().toString());

                if((username.getText().toString().trim().equals("")) || (password.getText().toString().length() < 6))
                {
                    ((Button)findViewById(R.id.main_act_button_login)).setEnabled(false);
                }else
                {
                    ((Button)findViewById(R.id.main_act_button_login)).setEnabled(true);
                }
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("EditText", "Password =" + password.getText().toString());
                if((password.getText().toString().length() < 6) || (username.getText().toString().trim().equals("")))
                {
                    ((Button)findViewById(R.id.main_act_button_login)).setEnabled(false);
                }else
                {
                    ((Button)findViewById(R.id.main_act_button_login)).setEnabled(true);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.main_act_button_register:
                Log.d("Button", "Register Button Pressed!");

                Intent registerActivity = new Intent(this, RegisterActivity.class);
                startActivity(registerActivity);

                break;
            case R.id.main_act_button_login:
                Log.d("Button", "Login Button Pressed!");

                Intent contacts = new Intent(this, ContactsActivity.class);
                startActivity(contacts);

                break;
        }
    }
}
