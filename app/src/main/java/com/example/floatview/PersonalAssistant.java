package com.example.floatview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

public class PersonalAssistant extends RelativeLayout{

    private PATray paTray;
    private PAAvatar paAvatar;
    private final WindowManager windowManager;
    public final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
    public PersonalAssistant(Context context){
        super(context);
        windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

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


        paAvatar = new PAAvatar(context);
        paTray = new PATray(context);

        setPaAvatarListener();
        showPaAvatar();

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent keyEvent){
        if(keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
            System.out.println("BackKey pressed.");
            if(paTray.isShown){
                hidePaTray();
                showPaAvatar();
                params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                params.width = WindowManager.LayoutParams.WRAP_CONTENT;
                windowManager.updateViewLayout(PersonalAssistant.this, params);
                return true;
            }
        }
        return super.dispatchKeyEvent(keyEvent);
    }

    private void showPaAvatar(){
        addView(paAvatar.view, paAvatar.params);
    }

    private void hidePaAvatar(){
        removeView(paAvatar.view);
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
                            params.flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
                            params.height = WindowManager.LayoutParams.MATCH_PARENT;
                            params.width = WindowManager.LayoutParams.MATCH_PARENT;
                            windowManager.updateViewLayout(PersonalAssistant.this, params);
                            hidePaAvatar();
                            showPaTray();
                        }
                        return true;
//                    case MotionEvent.ACTION_MOVE:
//
//                        paAvatar.params.x = paAvatar.initialX
//                                + (int) (motionEvent.getRawX() - paAvatar.initialTouchX);
//                        paAvatar.params.y = paAvatar.initialY
//                                + (int) (motionEvent.getRawY() - paAvatar.initialTouchY);
//                        double norm = Math.sqrt(Math.pow(motionEvent.getRawX() - paAvatar.initialTouchX, 2) + Math.pow(motionEvent.getRawY() - paAvatar.initialTouchY, 2));
//                        if (norm > 5.0d)
//                            paAvatar.moved = true;
//                        updateViewLayout(paAvatar.view, paAvatar.params);
//                        return true;
                }
                return false;
            }
        });
    }

//    private void setPaTrayListener(){
//        final ConstraintLayout grayGround = paTray.view.findViewById(R.id.gray_ground);
//        grayGround.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int i, KeyEvent keyEvent) {
//                System.out.println(i);
//                if(i==KeyEvent.KEYCODE_VOLUME_DOWN){
//
//                    hidePaTray();
//                    showPaAvatar();
//                    return true;
//                }
//                return false;
//            }
//        });
////        grayGround.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                hidePaTray();
////                showPaAvatar();
////            }
////        });
//
////        final RelativeLayout popWind = (RelativeLayout) paTray.view.findViewById(R.id.popup_window);
////        popWind.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////
////            }
////        });
//    }

    private void showPaTray(){
        addView(paTray, paTray.params);
        paTray.isShown = true;
    }
    private void hidePaTray(){
        removeView(paTray);
        paTray.isShown = false;
    }
}
