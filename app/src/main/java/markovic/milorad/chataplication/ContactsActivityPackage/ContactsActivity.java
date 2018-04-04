package markovic.milorad.chataplication.ContactsActivityPackage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import markovic.milorad.chataplication.MainActivity;
import markovic.milorad.chataplication.R;

public class ContactsActivity extends AppCompatActivity implements View.OnClickListener {

    ListView list;
    public static ContactsActivity contactsActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        Button logout = findViewById(R.id.contactsActButtonLogout);
        contactsActivity = this;
        list = findViewById(R.id.contactsActListView);
        list.setAdapter(new ContactsAdapter(this));

        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.contactsActButtonLogout:
                Log.d(getResources().getString(R.string.BUTTON_LOG_TAG), getResources().getString(R.string.LOGOUT_BUTTON_LOG_MESSAGE));
                Intent mainActivity = new Intent(this, MainActivity.class);
                startActivity(mainActivity);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}