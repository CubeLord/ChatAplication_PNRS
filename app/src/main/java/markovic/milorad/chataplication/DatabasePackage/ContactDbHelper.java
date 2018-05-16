package markovic.milorad.chataplication.DatabasePackage;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import markovic.milorad.chataplication.ContactsActivityPackage.Contact;
import markovic.milorad.chataplication.MessageActivityPackage.Message;
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

        sqLiteDatabase.execSQL("CREATE TABLE " + res.getString(R.string.MESSAGE_TABLE_NAME) + " (" +
                res.getString(R.string.COLUMN_MESSAGE_ID) + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                res.getString(R.string.COLUMN_SENDER_ID) + " INTEGER, " +
                res.getString(R.string.COLUMN_RECEIVER_ID) + " INTEGER," +
                res.getString(R.string.COLUMN_MESSAGE) + " TEXT, " +
                "FOREIGN KEY(" + res.getString(R.string.COLUMN_SENDER_ID) + ") REFERENCES " +
                res.getString(R.string.TABLE_NAME) + "(" + res.getString(R.string.COLUMN_CONTACT_ID) + "), " +
                "FOREIGN KEY(" + res.getString(R.string.COLUMN_RECEIVER_ID) + ") REFERENCES " +
                res.getString(R.string.TABLE_NAME) + "(" + res.getString(R.string.COLUMN_CONTACT_ID) + ")" + ");");
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

    public void insert(Message message) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues val = new ContentValues();
        val.put(context.getResources().getString(R.string.COLUMN_SENDER_ID), message.getSender_id());
        val.put(context.getResources().getString(R.string.COLUMN_RECEIVER_ID), message.getReceiver_id());
        val.put(context.getResources().getString(R.string.COLUMN_MESSAGE), message.getMessageText());

        db.insert(context.getResources().getString(R.string.MESSAGE_TABLE_NAME), null, val);
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

    public Message[] readMessages(int sender_id, int receiver_id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM (SELECT " + context.getResources().getString(R.string.COLUMN_MESSAGE_ID) + ", " + context.getResources().getString(R.string.COLUMN_MESSAGE) +
                ", " + context.getResources().getString(R.string.COLUMN_RECEIVER_ID) +
                " FROM " + context.getResources().getString(R.string.MESSAGE_TABLE_NAME) + " WHERE " + context.getResources().getString(R.string.COLUMN_RECEIVER_ID) + "=" + Integer.toString(receiver_id) +
                " OR " + context.getResources().getString(R.string.COLUMN_RECEIVER_ID) + "=" + Integer.toString(sender_id) + ") AS t1 INNER JOIN (" +
                "SELECT " + context.getResources().getString(R.string.COLUMN_MESSAGE_ID) + ", " + context.getResources().getString(R.string.COLUMN_SENDER_ID) + " FROM " +
                context.getResources().getString(R.string.MESSAGE_TABLE_NAME) + " WHERE " + context.getResources().getString(R.string.COLUMN_SENDER_ID) + "=" + Integer.toString(sender_id) +
                " OR " + context.getResources().getString(R.string.COLUMN_SENDER_ID) + "=" + Integer.toString(receiver_id) + ") AS t2 ON t1." +
                context.getResources().getString(R.string.COLUMN_MESSAGE_ID) + "=" + "t2." + context.getResources().getString(R.string.COLUMN_MESSAGE_ID) + ";", null, null);

        if (cursor.getCount() <= 0) {
            return null;
        }

        Message[] messages = new Message[cursor.getCount()];
        int i = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            messages[i++] = createMessage(cursor);
        }

        for (i = 0; i < messages.length; i++) {
            if (messages[i].getSender_id() == sender_id) {
                messages[i].setPos(1);
                messages[i].setColor(context.getResources().getColor(R.color.colorTurquoise));
            } else if (messages[i].getReceiver_id() == receiver_id) {
                messages[i].setPos(0);
                messages[i].setColor(context.getResources().getColor(R.color.colorLightGrey));
            }
        }

        close();
        return messages;
    }

    private Message createMessage(Cursor cursor) {
        String message_id = cursor.getString(cursor.getColumnIndex(context.getResources().getString(R.string.COLUMN_MESSAGE_ID)));
        String sender_id = cursor.getString(cursor.getColumnIndex(context.getResources().getString(R.string.COLUMN_SENDER_ID)));
        String receiver_id = cursor.getString(cursor.getColumnIndex(context.getResources().getString(R.string.COLUMN_RECEIVER_ID)));
        String message = cursor.getString(cursor.getColumnIndex(context.getResources().getString(R.string.COLUMN_MESSAGE)));

        return new Message(message, context.getResources().getColor(R.color.colorLightGrey), 0, Integer.parseInt(message_id), Integer.parseInt(sender_id), Integer.parseInt(receiver_id));
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

    public Message readMessage(int message_id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(context.getResources().getString(R.string.MESSAGE_TABLE_NAME), null, context.getResources().getString(R.string.COLUMN_MESSAGE_ID) + "=?", new String[]{Integer.toString(message_id)}, null, null, null);
        cursor.moveToFirst();
        Message message = createMessage(cursor);

        close();
        return message;
    }

    public void deleteContact(String username) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(context.getResources().getString(R.string.TABLE_NAME), context.getResources().getString(R.string.COLUMN_USERNAME) + "=?", new String[]{username});
        close();
    }

    public void deleteMessage(int message_id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(context.getResources().getString(R.string.MESSAGE_TABLE_NAME), context.getResources().getString(R.string.COLUMN_MESSAGE_ID) + "=?", new String[]{Integer.toString(message_id)});
        close();
    }

}