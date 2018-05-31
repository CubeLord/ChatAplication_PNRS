package markovic.milorad.chataplication.ServicePackage;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class NotificationService extends Service {

    Handler handler;
    Context context = this;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(context,"Service started...0", Toast.LENGTH_SHORT).show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true)
                {
                    try {
                        Toast.makeText(context,"Service started...", Toast.LENGTH_SHORT).show();
                        Thread.sleep(100);
                        handler.sendEmptyMessage(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        Toast.makeText(context,"Service started...1", Toast.LENGTH_SHORT).show();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Servise Destroyed...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate() {
         handler = new Handler() {
             @Override
             public void handleMessage(Message msg) {
                 super.handleMessage(msg);
                 Toast.makeText(context,"Service started...", Toast.LENGTH_SHORT).show();
             }
         };
        super.onCreate();
    }
}
