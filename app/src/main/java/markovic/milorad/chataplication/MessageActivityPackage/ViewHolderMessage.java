package markovic.milorad.chataplication.MessageActivityPackage;


import android.widget.RelativeLayout;
import android.widget.TextView;

class ViewHolderMessage {
    private TextView messageText = null;
    public RelativeLayout layout = null;

    public TextView getMessageText() {
        return messageText;
    }

    public void setMessageText(TextView messageText) {
        this.messageText = messageText;
    }

    public RelativeLayout getLayout() {
        return layout;
    }

    public void setLayout(RelativeLayout layout) {
        this.layout = layout;
    }
}
