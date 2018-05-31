package markovic.milorad.chataplication.ServicePackage;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import markovic.milorad.chataplication.HttpHelper;
import markovic.milorad.chataplication.R;

public class NotificationService extends Service {

    private static final String LOG_TAG = "Debugging";
    private static final long PERIOD = 5000L;
    String sessionid;
    Boolean notified;

    private ThreadExample mThread;
//    private RunnableExample mRunnable;
    HttpHelper httpHelper;
    Context context;

    @Override
    public void onCreate() {
        Log.d("Debugging", "OnCreate Service was called");
        super.onCreate();
        mThread = new ThreadExample();
        mThread.start();
        context = this;
        httpHelper = new HttpHelper();
        notified = false;

//        mRunnable = new RunnableExample();
//        mRunnable.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sessionid = intent.getStringExtra("sessionid");
        Log.d("Debugging", "THIS IS FROM NOTIFICATION: " + sessionid);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("Debugging", "OnDestroy Service was called");
        super.onDestroy();
        mThread.exit();
//        mRunnable.stop();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private class ThreadExample extends Thread {
        private boolean mRun = true;

        @Override
        public synchronized void start() {
            mRun = true;
            super.start();
        }

        public synchronized void exit() {
            mRun = false;
        }

        @Override
        public void run() {
            while(mRun) {

                try {
                    Boolean notification = httpHelper.getNotificationFromURL(getResources().getString(R.string.BASE_URL) + "/getfromservice", sessionid);
                    Log.d("Debugging", "RESULT IS: " + notification.toString());
                    if (notified && !notification) {
                        notified = false;
                    }
                    if (notification && !notified) {
                        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                                .setDefaults(NotificationCompat.DEFAULT_ALL)
                                .setSmallIcon(R.mipmap.send_icon)
                                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.send_icon))
                                .setContentTitle("ChatApplication")
                                .setContentText("New Message!");
                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify(1, notificationBuilder.build());
                        notified = true;
                    }
                    Thread.sleep(PERIOD); //milliseconds
                } catch (InterruptedException e) {
                    // interrupted finish thread
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    private class RunnableExample implements Runnable {
//        private Handler mHandler;
//        private boolean mRun = false;
//
//        public RunnableExample() {
//            mHandler = new Handler(getMainLooper());
//        }
//
//        public void start() {
//            mRun = true;
//            mHandler.postDelayed(this, PERIOD);
//        }
//
//        public void stop() {
//            mRun = false;
//            mHandler.removeCallbacks(this);
//        }
//
//        @Override
//        public void run() {
//            if (!mRun) {
//                return;
//            }
//
//            Log.d(LOG_TAG, "Hello from Runnable");
//            mHandler.postDelayed(this, PERIOD);
//        }
//    }
}