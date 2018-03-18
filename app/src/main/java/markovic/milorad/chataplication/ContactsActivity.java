package markovic.milorad.chataplication;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ContactsActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        TextView contact1 = (TextView)findViewById(R.id.contacts_act_textview_contact1);
        Button logout = (Button) findViewById(R.id.contacts_act_button_logout);

        contact1.setTypeface(null, Typeface.BOLD_ITALIC);
        contact1.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.contacts_act_textview_contact1:
                Log.d("Contact", "Contact1 pressed!");

                Intent messageAct = new Intent(this, MessageActivity.class);
                startActivity(messageAct);
                break;

            case R.id.contacts_act_button_logout:
                Log.d("Button", "Logout button pressed!");
                Intent mainActivity = new Intent(this, MainActivity.class);
                startActivity(mainActivity);
                break;
        }
    }
}
