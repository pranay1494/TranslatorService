package com.example.pranay.clipboardtranslator;

import android.app.Service;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Pranay on 25-07-2016.
 */

public class Clipboard extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ClipboardManager clipBoard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        clipBoard.addPrimaryClipChangedListener( new ClipboardListener(this,clipBoard) );
        Log.d("clipboard","service onstartcommand");
        Toast.makeText(this, "started", Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("clipboard","service created");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("clipboard","service destroyed");
    }
}
