package com.example.floatview;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

public class PATray {
    public View view;
    final WindowManager.LayoutParams params = new WindowManager.LayoutParams();

    public PATray(Context context){
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.pa_tray, null);

        params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        params.flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
        params.format = PixelFormat.TRANSLUCENT;
        params.gravity = Gravity.CENTER;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
    }
}
