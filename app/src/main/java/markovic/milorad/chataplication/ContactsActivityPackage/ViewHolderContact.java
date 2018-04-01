package markovic.milorad.chataplication.ContactsActivityPackage;

import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolderContact {
    private ImageView send = null;
    private TextView icon = null;
    private TextView name = null;

    public ImageView getSend() {
        return send;
    }

    public TextView getIcon() {
        return icon;
    }

    public TextView getName() {
        return name;
    }

    public void setSend(ImageView send) {
        this.send = send;
    }

    public void setIcon(TextView icon) {
        this.icon = icon;
    }

    public void setName(TextView name) {
        this.name = name;
    }
}
