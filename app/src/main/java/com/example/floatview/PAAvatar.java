package com.example.floatview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

public class PAAvatar {
    public View view;
    public int initialX;
    public int initialY;
    public float initialTouchX;
    public float initialTouchY;
    public boolean moved;
    PATray pTray;
    final WindowManager windowManager;
    final WindowManager.LayoutParams params = new WindowManager.LayoutParams();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public PAAvatar(Context context){

        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.avatar_layout, null);

//        ImageView paAvatar = new ImageView(context);
//        paAvatar.setImageResource(R.drawable.ic_launcher_foreground);
//        view = paAvatar;

        params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;

        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.format = PixelFormat.TRANSLUCENT;
        params.gravity = Gravity.CENTER;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        setPaAvatarListener();
    }
    public void setPTray(PATray pTray){this.pTray = pTray;}

    public void show(){
        windowManager.addView(view, params);
    }

    private void animBeforeExpansion(){
        float viewHeight = windowManager.getDefaultDisplay().getHeight();
        float viewWidth = windowManager.getDefaultDisplay().getWidth();
        float avatarHeight = PAAvatar.this.view.getMeasuredHeight();
        float avatarWidth = PAAvatar.this.view.getMeasuredWidth();
        initialX = params.x;
        initialY = params.y;
//        System.out.println(String.format("x: %d, y:%d", initialX, initialY));


//        view.animate().x((viewWidth - avatarWidth)/2).y(viewHeight - avatarHeight).setDuration(300).start();
//        PAAvatar.this.view.animate().x(viewWidth/2 - initialX - avatarWidth/2).y(viewHeight - initialY - avatarHeight).setDuration(300).setListener(new Animator.AnimatorListener() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 1.0f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                params.x = initialX +  (int)(valueAnimator.getAnimatedFraction() * (0 - initialX));
                params.y = initialY + (int)(valueAnimator.getAnimatedFraction() * (viewHeight/2 - initialY - avatarHeight/2));
//                System.out.println(String.format("pa.x: %d", params.x));
                windowManager.updateViewLayout(PAAvatar.this.view, params);
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        valueAnimator.setDuration(300);
        valueAnimator.start();



//        PAAvatar.this.view.animate().translationX(100).setDuration(300).setUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//
////                System.out.println(String.format("animValue: %f", (Float)valueAnimator.getAnimatedFraction()));
//                params.x = initialX + (int)(valueAnimator.getAnimatedFraction() * 100);
//                System.out.println(String.format("pa.x: %d", params.x));
//                windowManager.updateViewLayout(PAAvatar.this.view, params);
//            }
//        }).setListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animator) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animator) {
//                pTray.show();
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animator) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animator) {
//
//            }
//        }).start();
    }
    private void animAfterClose(){
//        PAAvatar.this.view.animate().x(initialX).y(initialTouchY).setDuration(300).start();
        final int currentX = params.x;
        final int currentY = params.y;
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 1.0f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                params.x = currentX +  (int)(valueAnimator.getAnimatedFraction() * (initialX - currentX));
                params.y = currentY + (int)(valueAnimator.getAnimatedFraction() * (initialY - currentY));
//                System.out.println(String.format("pa.x: %d", params.x));
                windowManager.updateViewLayout(PAAvatar.this.view, params);
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        valueAnimator.setDuration(300);
        valueAnimator.start();

    }

    public void afterClosingTray(){
        animAfterClose();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setPaAvatarListener(){
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if(!pTray.isShown) {
                            moved = false;
                            initialX = params.x;
                            initialY = params.y;
                            initialTouchX = motionEvent.getRawX();
                            initialTouchY = motionEvent.getRawY();
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        if (!moved) {
                            animBeforeExpansion();
                            pTray.show();
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        if(!pTray.isShown) {
                            params.x = initialX
                                    + (int) (motionEvent.getRawX() - initialTouchX);
                            params.y = initialY
                                    + (int) (motionEvent.getRawY() - initialTouchY);
                            double norm = Math.sqrt(Math.pow(motionEvent.getRawX() - initialTouchX, 2) + Math.pow(motionEvent.getRawY() - initialTouchY, 2));
                            if (norm > 5.0d)
                                moved = true;
                            windowManager.updateViewLayout(PAAvatar.this.view, params);
                        }
                        return true;
                }
                return false;
            }
        });
    }
}
