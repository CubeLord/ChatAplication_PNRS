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

    private static final String BUTTON_LOG_TAG = "Button";
    private static final String BUTTON_LOG_MESSAGE = "Contact1 pressed!";
    private static final String CONTACT_LOG_TAG = "Contact";
    private static final String CONTACT_LOG_MESSAGE = "Logout Button Pressed!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        TextView contact1 = (TextView)findViewById(R.id.contactsActTextviewContact1);
        Button logout = (Button) findViewById(R.id.contactsActButtonLogout);

        contact1.setTypeface(null, Typeface.BOLD_ITALIC);
        contact1.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.contactsActTextviewContact1:
                Log.d(CONTACT_LOG_TAG, CONTACT_LOG_MESSAGE);

                Intent messageAct = new Intent(this, MessageActivity.class);
                startActivity(messageAct);
                break;

            case R.id.contactsActButtonLogout:
                Log.d(BUTTON_LOG_TAG, BUTTON_LOG_MESSAGE);
                Intent mainActivity = new Intent(this, MainActivity.class);
                startActivity(mainActivity);
                break;
        }
    }
}
