package android.qiao.androidlearn.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyTestService extends Service {
    public MyTestService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.w("TAG", "service run: start service");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.w("TAG", "service run: start command");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w("TAG", "service run: destory service");
    }
}
