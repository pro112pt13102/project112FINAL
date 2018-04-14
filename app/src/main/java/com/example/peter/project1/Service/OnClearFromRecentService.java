package com.example.peter.project1.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import static com.example.peter.project1.GioHangActivity.getArryGioHang;

/**
 * Created by daovip on 3/28/2018.
 */

public class OnClearFromRecentService extends Service {
    Context c=this;

   

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("ClearFromRecentService", "Service Started");
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("ClearFromRecentService", "Service Destroyed");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.e("ClearFromRecentService", "END");

        Toast.makeText(c, "Đã gửi lên sever-"+getArryGioHang().size(), Toast.LENGTH_SHORT).show();
        //Code here
        stopSelf();
    }
}
