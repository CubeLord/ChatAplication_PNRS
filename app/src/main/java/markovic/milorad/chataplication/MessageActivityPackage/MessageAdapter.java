package markovic.milorad.chataplication.MessageActivityPackage;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import markovic.milorad.chataplication.R;

public class MessageAdapter extends BaseAdapter {
    ArrayList<Message> list;
    Context context;

    MessageAdapter(Context c) {
        context = c;
        list = new ArrayList<Message>();
        Resources res = context.getResources();
        String[] messages = res.getStringArray(R.array.dummyMessages);
        int col;
        Random rnd = new Random();

        for (int i = 0; i < messages.length; i++) {
            col = rnd.nextInt(2) + 1;
            if (col == 1) {
                list.add(new Message(messages[i], ContextCompat.getColor(context, R.color.colorTurquoise), 1));
            } else {
                list.add(new Message(messages[i], ContextCompat.getColor(context, R.color.colorLightGrey), 0));
            }
        }
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
        return i;
    }

    public void remove(int i) {
        list.remove(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row = view;

        if (view == null) {
            LayoutInflater inflater = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
            row = inflater.inflate(R.layout.message_list_layout, viewGroup, false);

            ViewHolderMessage holder = new ViewHolderMessage();
            holder.setMessageText(((TextView) row.findViewById(R.id.messageActListItemText)));
            holder.setLayout(((RelativeLayout) row.findViewById(R.id.messageActLayout)));
            row.setTag(holder);
        }

        ViewHolderMessage holder = ((ViewHolderMessage) row.getTag());
        final Message message = list.get(i);
        holder.getMessageText().setText(message.getMessageText());
        if (message.getPos() == 1) {
            holder.getLayout().setGravity(Gravity.RIGHT);
        } else {
            holder.getLayout().setGravity(Gravity.LEFT);
        }

        Drawable round = context.getDrawable(R.drawable.rounded_shape);
        round.setColorFilter(message.getColor(), PorterDuff.Mode.MULTIPLY);
        holder.getMessageText().setBackground(round);


        if (message.getPos() == 1) {
            holder.getMessageText().setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        } else {
            holder.getMessageText().setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        }
        return row;
    }
}
