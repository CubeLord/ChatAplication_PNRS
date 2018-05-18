package markovic.milorad.chataplication.ContactsActivityPackage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import markovic.milorad.chataplication.DatabasePackage.ContactDbHelper;
import markovic.milorad.chataplication.HttpHelper;
import markovic.milorad.chataplication.HttpHelperReturn;
import markovic.milorad.chataplication.MessageActivityPackage.MessageActivity;
import markovic.milorad.chataplication.R;

public class ContactsAdapter extends BaseAdapter {
    ArrayList<Contact> list;
    Context context;
    int sender;
    String ssesionid;
    Handler handler = new Handler();
    HttpHelper httpHelper = new HttpHelper();
    JSONArray jsonArray;

    ContactsAdapter(Context c, int sender_id, String session) {
        context = c;
        sender = sender_id;
        list = new ArrayList<Contact>();
        ssesionid = session;

/*
        Resources res = context.getResources();
        String[] contacts = res.getStringArray(R.array.contacts);

        for (int i = 0; i < contacts.length; i++) {
            list.add(new Contact(contacts[i]));
        }
*/

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i; //Because we are not using a Data base this should be the same as the ID
    }

    public void update(Contact[] contacts) {
        list.clear();
        if (contacts != null) {
            for (Contact contact : contacts) {
                list.add(contact);
            }
        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row = view;
        if (row == null) {
            //LayoutInflater -> creates a new object with the same properties, which is not what findViewbyID would do
            //FindViewById -> uses the same object each time
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.listlayout, viewGroup, false);

            ViewHolderContact holder = new ViewHolderContact();
            holder.setName(((TextView) row.findViewById(R.id.contactsActTextviewContact)));
            holder.setSend(((ImageView) row.findViewById(R.id.contactsActListItemImage)));
            holder.setIcon(((TextView) row.findViewById(R.id.contactsActListItemIcon)));
            row.setTag(holder);
        }

        final ViewHolderContact holder = ((ViewHolderContact) row.getTag());
        holder.getName().setTypeface(null, Typeface.BOLD_ITALIC);
        final Contact contact = list.get(i);


        holder.getSend().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(context.getResources().getString(R.string.CONTACT_LOG_TAG), context.getResources().getString(R.string.CONTACT_LOG_MESSAGE));

                Bundle bundle = new Bundle();
                bundle.putString(context.getString(R.string.BUNDLE_CONTACT_NAME), contact.name);
                bundle.putString(context.getString(R.string.BUNDLE_RECEIVER_ID), Integer.toString(contact.getId()));
                bundle.putString(context.getString(R.string.BUNDLE_SENDER_ID), Integer.toString(sender));
                bundle.putString("sessionid", ssesionid);
                Intent messageAct = new Intent(context, MessageActivity.class);
                messageAct.putExtras(bundle);

                context.startActivity(messageAct);
            }
        });

        holder.getName().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            final HttpHelperReturn httpHelperReturn = httpHelper.httpDelete(context.getResources().getString(R.string.BASE_URL) + "/contact/" + holder.getName().getText().toString(), ssesionid);
                            final boolean success = httpHelperReturn.isSuccess();
                            Log.d("Debugging", "Prosli smo Delete - 0");
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, "Request Message: " + httpHelperReturn.getMessage() + "\nRequest Code: " + Integer.toString(httpHelperReturn.getCode()), Toast.LENGTH_LONG).show();
                                }
                            });
                        } catch (JSONException e) {
                            Log.d("Debugging", "JSONException happened");
                            e.printStackTrace();
                        } catch (IOException e) {
                            Log.d("Debugging", "IOException happened");
                            e.printStackTrace();
                        }
                    }
                }).start();
                notifyDataSetChanged();
                ContactDbHelper mdbHelper = new ContactDbHelper(context);
                httpHelper = new HttpHelper();
                handler = new Handler();
                Contact[] contacts = mdbHelper.readContacts();
                final CountDownLatch countDownLatch = new CountDownLatch(1);
                jsonArray = new JSONArray();

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonArray = httpHelper.getJSONContactsFromURL(context.getResources().getString(R.string.BASE_URL) + "/contacts", ssesionid);
                            countDownLatch.countDown();
                        } catch (JSONException e) {
                            Log.d("Debugging", "JSONException happened in ContactsActivity");
                            e.printStackTrace();
                        } catch (IOException e) {
                            Log.d("Debugging", "IOException happened in ContactsActivity");
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
                try {
                    countDownLatch.await();
                } catch (Exception e) {
                    Log.d("Debugging", "CountDownLatch exception happened!");
                }

                try {
                    contacts = new Contact[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        contacts[i] = new Contact(jsonArray.getJSONObject(i).getString("username"), "fn", "ls", 66);
                    }
                } catch (Exception e) {
                    Log.d("Debugging", "Cought Exception Reading JSONArray");
                }

                Contact[] cArray = mdbHelper.readContacts();
                Log.d("Debugging", "Length of cArray is: " + Integer.toString(cArray.length) +" \n Length of contacts is: " + Integer.toString(contacts.length));
                for (int i = 0; i < cArray.length; i++) {
                    mdbHelper.deleteContact(cArray[i].name);
                }
                for (int i = 0; i < contacts.length; i++) {
                    mdbHelper.insert(contacts[i]);
                }

                notifyDataSetChanged();
                return true;
            }

        });


        holder.getName().setText(contact.name);
        String s = contact.getName().toString().toUpperCase();
        String[] split = s.split("");
        holder.getIcon().setText((split[1]));

        Random rnd = new Random();
        holder.getIcon().setBackgroundColor(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));
        return row;
    }
}