package markovic.milorad.chataplication.DatabasePackage;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import markovic.milorad.chataplication.ContactsActivityPackage.Contact;
import markovic.milorad.chataplication.R;

public class ContactDbHelper extends SQLiteOpenHelper {

    private SQLiteDatabase mdb = null;
    private Context context;

    public ContactDbHelper(Context context) {
        super(context, context.getResources().getString(R.string.DATABASE_NAME), null, R.integer.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Resources res = context.getResources();
        sqLiteDatabase.execSQL("CREATE TABLE " + res.getString(R.string.TABLE_NAME) + " (" +
                res.getString(R.string.COLUMN_CONTACT_ID) + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                res.getString(R.string.COLUMN_USERNAME) + " TEXT, " +
                res.getString(R.string.COLUMN_FIRSTNAME) + " TEXT," +
                res.getString(R.string.COLUMN_LASTNAME) + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insert(Contact contact) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues val = new ContentValues();
        val.put(context.getResources().getString(R.string.COLUMN_USERNAME), contact.getName());
        val.put(context.getResources().getString(R.string.COLUMN_FIRSTNAME), contact.getFirstName());
        val.put(context.getResources().getString(R.string.COLUMN_LASTNAME), contact.getLastName());

        db.insert(context.getResources().getString(R.string.TABLE_NAME), null, val);
        close();
    }

    public Contact[] readContacts() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(context.getResources().getString(R.string.TABLE_NAME), null, null, null, null, null, null, null);

        if (cursor.getCount() <= 0) {
            return null;
        }

        Contact[] contacts = new Contact[cursor.getCount()];
        int i = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            contacts[i++] = createContact(cursor);
        }

        close();
        return contacts;
    }

    private Contact createContact(Cursor cursor) {
        String username = cursor.getString(cursor.getColumnIndex(context.getResources().getString(R.string.COLUMN_USERNAME)));
        String firstName = cursor.getString(cursor.getColumnIndex(context.getResources().getString(R.string.COLUMN_FIRSTNAME)));
        String lastName = cursor.getString(cursor.getColumnIndex(context.getResources().getString(R.string.COLUMN_LASTNAME)));
        String id = cursor.getString(cursor.getColumnIndex(context.getResources().getString(R.string.COLUMN_CONTACT_ID)));

        return new Contact(username, firstName, lastName, Integer.parseInt(id));
    }

    public Contact readContact(String username) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(context.getResources().getString(R.string.TABLE_NAME), null, context.getResources().getString(R.string.COLUMN_USERNAME) + "=?", new String[]{username}, null, null, null);
        cursor.moveToFirst();
        Contact contact = createContact(cursor);

        close();
        return contact;
    }

    public void deleteContact(String username) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(context.getResources().getString(R.string.TABLE_NAME), context.getResources().getString(R.string.COLUMN_USERNAME) + "=?", new String[]{username});
        close();
    }
}
