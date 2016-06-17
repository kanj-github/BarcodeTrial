package apps.test.kanj.barcodetrial;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class TestService extends Service {
    private Object lock;
    public TestService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("Kanj", "onCreate");
        lock = new Object();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String str = intent.getStringExtra("extra");
        synchronized (lock) {
            Log.v("Kanj", str);
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    synchronized (lock) {
                        try {
                            Thread.sleep(7000);
                            stopSelf();
                        } catch (InterruptedException ie) {
                            ie.printStackTrace();
                        }
                    }
                }
            });
            t.start();
        }
        return (START_REDELIVER_INTENT);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("Kanj", "onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
