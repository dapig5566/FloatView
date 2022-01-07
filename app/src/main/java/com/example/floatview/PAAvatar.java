package com.example.floatview;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class PAAvatar {
    public View view;
    public int initialX;
    public int initialY;
    public float initialTouchX;
    public float initialTouchY;
    public boolean moved;
    final WindowManager.LayoutParams params = new WindowManager.LayoutParams();

    public PAAvatar(Context context){
        ImageView paAvatar = new ImageView(context);
        paAvatar.setImageResource(R.drawable.ic_launcher_foreground);
        view = paAvatar;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else{
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.format = PixelFormat.TRANSLUCENT;
        params.gravity = Gravity.CENTER;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
    }
}
