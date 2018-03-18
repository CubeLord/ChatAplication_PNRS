package markovic.milorad.chataplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static android.widget.Toast.*;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String SENT_LOG_MESSAGE = "Message is sent!";
    Toast toast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Button logout = (Button)findViewById(R.id.messageActButtonLogout);
        Button send = (Button)findViewById(R.id.messageActButtonSend);

        send.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.messageActButtonLogout:
                Intent main = new Intent(this, MainActivity.class);
                startActivity(main);
                break;
            case R.id.messageActButtonSend:
                if(toast != null) toast.cancel();
                toast = Toast.makeText(this,SENT_LOG_MESSAGE, Toast.LENGTH_LONG);
                toast.show();
                break;
        }
    }
}
