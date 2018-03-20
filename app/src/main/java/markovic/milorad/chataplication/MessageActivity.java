package markovic.milorad.chataplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    Toast toast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Button logout = (Button)findViewById(R.id.messageActButtonLogout);
        Button send = (Button)findViewById(R.id.messageActButtonSend);
        EditText messageText = (EditText)findViewById(R.id.messageActEditMessageText);

        messageText.addTextChangedListener(this);
        send.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.messageActButtonLogout:
                Log.d(getResources().getString(R.string.BUTTON_LOG_TAG), getResources().getString(R.string.LOGOUT_BUTTON_LOG_MESSAGE));
                Intent main = new Intent(this, MainActivity.class);
                startActivity(main);
                break;
            case R.id.messageActButtonSend:
                ((EditText)findViewById(R.id.messageActEditMessageText)).setText("");
                if(toast != null) toast.cancel();
                toast = Toast.makeText(this,getResources().getString(R.string.SENT_LOG_MESSAGE), Toast.LENGTH_LONG);
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
        if(((EditText)findViewById(R.id.messageActEditMessageText)).getText().toString().equals("")){
            ((Button)findViewById(R.id.messageActButtonSend)).setEnabled(false);
        } else {
            ((Button)findViewById(R.id.messageActButtonSend)).setEnabled(true);
        }
    }
}
