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
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Timer;
import java.util.TimerTask;

public class PersonalAssistant {

    private PATray paTray;
    private PAAvatar paAvatar;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public PersonalAssistant(Context context){

        paAvatar = new PAAvatar(context);
        paTray = new PATray(context);
        paAvatar.setPTray(paTray);
        paTray.setPAvatar(paAvatar);


    }

    public void start(){
        paAvatar.show();
    }

}
