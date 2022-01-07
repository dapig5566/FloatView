package com.example.floatview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class PersonalAssistant {

    private WindowManager windowManager;
    private PATray paTray;
    private PAAvatar paAvatar;

    public PersonalAssistant(Context context){
        Context mContext = context.getApplicationContext();
        windowManager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);

        paAvatar = new PAAvatar(context);
        paTray = new PATray(context);

        setPaTrayListener();
        setPaAvatarListener();

    }

    private void showPaAvatar(){
        windowManager.addView(paAvatar.view, paAvatar.params);
    }

    private void hidePaAvatar(){
        windowManager.removeView(paAvatar.view);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setPaAvatarListener(){
        paAvatar.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hidePaAvatar();
                showPaTray();
            }
        });

        paAvatar.view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        paAvatar.moved = false;
                        paAvatar.initialX = paAvatar.params.x;
                        paAvatar.initialY = paAvatar.params.y;
                        paAvatar.initialTouchX = motionEvent.getRawX();
                        paAvatar.initialTouchY = motionEvent.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        if (!paAvatar.moved) {
                            hidePaAvatar();
                            showPaTray();
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:

                        paAvatar.params.x = paAvatar.initialX
                                + (int) (motionEvent.getRawX() - paAvatar.initialTouchX);
                        paAvatar.params.y = paAvatar.initialY
                                + (int) (motionEvent.getRawY() - paAvatar.initialTouchY);
                        double norm = Math.sqrt(Math.pow(motionEvent.getRawX() - paAvatar.initialTouchX, 2) + Math.pow(motionEvent.getRawY() - paAvatar.initialTouchY, 2));
                        if (norm > 3.5d)
                            paAvatar.moved = true;
                        windowManager.updateViewLayout(paAvatar.view, paAvatar.params);
                        return true;
                }
                return false;
            }
        });
    }

    private void setPaTrayListener(){
        final LinearLayout grayGround = (LinearLayout) paTray.view.findViewById(R.id.gray_ground);
        grayGround.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hidePaTray();
                showPaAvatar();
            }
        });

        final RelativeLayout popWind = (RelativeLayout) paTray.view.findViewById(R.id.popup_window);
        popWind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void showPaTray(){
        windowManager.addView(paTray.view, paTray.params);
    }
    private void hidePaTray(){
        windowManager.removeView(paTray.view);
    }

    public void show(){
        showPaAvatar();
    }
}
