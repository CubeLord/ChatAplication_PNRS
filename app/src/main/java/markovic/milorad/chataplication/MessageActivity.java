package markovic.milorad.chataplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static android.widget.Toast.*;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Button logout = (Button)findViewById(R.id.message_act_button_logout);
        Button send = (Button)findViewById(R.id.message_act_button_send);

        send.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.message_act_button_logout:
                //Go to main_activity
                Intent main = new Intent(this, MainActivity.class);
                startActivity(main);
                break;
            case R.id.message_act_button_send:
                Toast.makeText(this,"Message is sent!", Toast.LENGTH_LONG).show();

                break;
        }
    }
}
