package com.example.floatview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;

import me.zhouzhuo.zzweatherview.AirLevel;
import me.zhouzhuo.zzweatherview.WeatherItemView;
import me.zhouzhuo.zzweatherview.WeatherModel;
import me.zhouzhuo.zzweatherview.ZzWeatherView;

public class PATray extends RelativeLayout{
    public View view;
    public boolean isShown;
    Context context;
    PAAvatar pAvatar;
    boolean moved = false;
    final WindowManager windowManager;
    final WindowManager.LayoutParams params = new WindowManager.LayoutParams();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public PATray(Context context){
        super(context);
        this.context = context;
        windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
//        windowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

        isShown = false;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.pa_tray, this);

        params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;

        params.flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
//        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        params.format = PixelFormat.TRANSLUCENT;
        params.gravity = Gravity.TOP;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;


        View ground = view.findViewById(R.id.gray_ground);
        ground.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                closeTray();
            }
        });

        View tray = view.findViewById(R.id.tray_layout);
        tray.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                System.out.println("tray clicked.");
            }
        });

        View btn_add = view.findViewById(R.id.btn_addAffair);
        btn_add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                closeTray();
                Intent intent = new Intent();
                intent.setClass(context, MainActivity.class);
                context.startActivity(intent);
//                setTopApp(context);
            }
        });


//        initializeWeather(context);
//        initializeAffairList(context);
    }
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    public static void setTopApp(Context context) {
//        if (!isRunningForeground(context)) {
//            System.out.println("aaa");
//            /* *??????ActivityManager*/
//            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//
//            /* *?????????????????????task(??????)*/
//            List<ActivityManager.RunningTaskInfo> taskInfoList = activityManager.getRunningTasks(100);
//            for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
//                /* *?????????????????? task???????????????????????????*/
//                if (taskInfo.topActivity.getPackageName().equals(context.getPackageName())) {
//                    activityManager.moveTaskToFront(taskInfo.id, 0);
//                    break;
//                }
//            }
//        }
//    }
//
//    public static boolean isRunningForeground(Context context) {
//        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningAppProcessInfo> appProcessInfoList = activityManager.getRunningAppProcesses();
//        /*????????????*/
//        for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessInfoList) {
//            if (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
//                if (appProcessInfo.processName.equals(context.getApplicationInfo().processName)) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }


    private void writeNewAffair(String affair){
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try{
            out = context.openFileOutput("data", Context.MODE_APPEND);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.newLine();
            writer.write(affair);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                if(writer != null)
                    writer.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void setPAvatar(PAAvatar pAvatar) {
        this.pAvatar = pAvatar;
    }

    public void show(){
        isShown = true;

        View tray = PATray.this.view.findViewById(R.id.tray_layout);
        tray.setAlpha(0.0f);
        tray.animate().alpha(1.0f).setDuration(300).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                windowManager.addView(view, params);
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
        }).start();
    }

    public void hide(){
        isShown = false;
        View tray = PATray.this.view.findViewById(R.id.tray_layout);
        tray.animate().alpha(0.0f).setDuration(300).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                windowManager.removeView(view);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        }).start();
    }

    private void closeTray(){
        hide();
        pAvatar.afterClosingTray();
    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent keyEvent){
        if(keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
//            System.out.println("BackKey pressed.");
            if(isShown){
                closeTray();
                return true;
            }
        }
        return super.dispatchKeyEvent(keyEvent);
    }

    private void loadAffairList(Context context) {


        ListView affairList = view.findViewById(R.id.affair_list);
        List<String> affairData = new ArrayList<>();
        for(int i=0;i<10;i++) {
            affairData.add("Affair"+ i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.item_template, affairData);
        affairList.setAdapter(adapter);

    }

//    private void initializeWeather(Context context) {
//        ZzWeatherView weatherView = (ZzWeatherView) view.findViewById(R.id.weather_view);
//        weatherView.setData(generateMockData());
//
//        //?????????
//        weatherView.setLineType(ZzWeatherView.LINE_TYPE_CURVE);
//        //?????????(????????????????????????)
//        //        weatherView.setLineType(ZzWeatherView.LINE_TYPE_CURVE);
//
//        //?????????????????????px
//        weatherView.setLineWidth(6f);
//
//        //???????????????????????????(??????3???)
//        try {
//            weatherView.setColumnNumber(6);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        //????????????????????????????????????
//        weatherView.setDayAndNightLineColor(Color.BLUE, Color.RED);
//
//        //???????????????
//        weatherView.setOnWeatherItemClickListener(new ZzWeatherView.OnWeatherItemClickListener() {
//            @Override
//            public void onItemClick(WeatherItemView itemView, int position, WeatherModel weatherModel) {
//                Toast.makeText(context, position + "", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }

    private List<WeatherModel> generateMockData() {
        List<WeatherModel> list = new ArrayList<WeatherModel>();
        WeatherModel model = new WeatherModel();
        model.setDate("12/07");
        model.setWeek("??????");
        model.setDayWeather("??????");
        model.setDayTemp(11);
        model.setNightTemp(5);
        model.setNightWeather("???");
        model.setWindOrientation("?????????");
        model.setWindLevel("3???");
        model.setAirLevel(AirLevel.EXCELLENT);
        list.add(model);

        WeatherModel model1 = new WeatherModel();
        model1.setDate("12/08");
        model1.setWeek("??????");
        model1.setDayWeather("???");
        model1.setDayTemp(8);
        model1.setNightTemp(5);
        model1.setNightWeather("???");
        model1.setWindOrientation("?????????");
        model1.setWindLevel("3???");
        model1.setAirLevel(AirLevel.HIGH);
        list.add(model1);

        WeatherModel model2 = new WeatherModel();
        model2.setDate("12/09");
        model2.setWeek("??????");
        model2.setDayWeather("???");
        model2.setDayTemp(9);
        model2.setNightTemp(8);
        model2.setNightWeather("???");
        model2.setWindOrientation("?????????");
        model2.setWindLevel("3???");
        model2.setAirLevel(AirLevel.POISONOUS);
        list.add(model2);

        WeatherModel model3 = new WeatherModel();
        model3.setDate("12/10");
        model3.setWeek("??????");
        model3.setDayWeather("???");
        model3.setDayTemp(12);
        model3.setNightTemp(9);
        model3.setDayPic(R.drawable.w0);
        model3.setNightPic(R.drawable.w1);
        model3.setNightWeather("???");
        model3.setWindOrientation("?????????");
        model3.setWindLevel("3???");
        model3.setAirLevel(AirLevel.GOOD);
        list.add(model3);

        WeatherModel model4 = new WeatherModel();
        model4.setDate("12/11");
        model4.setWeek("??????");
        model4.setDayWeather("??????");
        model4.setDayTemp(13);
        model4.setNightTemp(7);
        model4.setDayPic(R.drawable.w2);
        model4.setNightPic(R.drawable.w4);
        model4.setNightWeather("??????");
        model4.setWindOrientation("?????????");
        model4.setWindLevel("3???");
        model4.setAirLevel(AirLevel.LIGHT);
        list.add(model4);

        WeatherModel model5 = new WeatherModel();
        model5.setDate("12/12");
        model5.setWeek("??????");
        model5.setDayWeather("??????");
        model5.setDayTemp(17);
        model5.setNightTemp(8);
        model5.setDayPic(R.drawable.w3);
        model5.setNightPic(R.drawable.w4);
        model5.setNightWeather("??????");
        model5.setWindOrientation("?????????");
        model5.setWindLevel("3???");
        model5.setAirLevel(AirLevel.LIGHT);
        list.add(model5);

        WeatherModel model6 = new WeatherModel();
        model6.setDate("12/13");
        model6.setWeek("??????");
        model6.setDayWeather("???");
        model6.setDayTemp(13);
        model6.setNightTemp(6);
        model6.setDayPic(R.drawable.w5);
        model6.setNightPic(R.drawable.w6);
        model6.setNightWeather("???");
        model6.setWindOrientation("?????????");
        model6.setWindLevel("3???");
        model6.setAirLevel(AirLevel.POISONOUS);
        list.add(model6);

        WeatherModel model7 = new WeatherModel();
        model7.setDate("12/14");
        model7.setWeek("??????");
        model7.setDayWeather("???");
        model7.setDayTemp(19);
        model7.setNightTemp(10);
        model7.setDayPic(R.drawable.w5);
        model7.setNightPic(R.drawable.w7);
        model7.setNightWeather("???");
        model7.setWindOrientation("?????????");
        model7.setWindLevel("3???");
        model7.setAirLevel(AirLevel.POISONOUS);
        list.add(model7);

        WeatherModel model8 = new WeatherModel();
        model8.setDate("12/15");
        model8.setWeek("??????");
        model8.setDayWeather("???");
        model8.setDayTemp(22);
        model8.setNightTemp(4);
        model8.setDayPic(R.drawable.w5);
        model8.setNightPic(R.drawable.w8);
        model8.setNightWeather("???");
        model8.setWindOrientation("?????????");
        model8.setWindLevel("3???");
        model8.setAirLevel(AirLevel.POISONOUS);
        list.add(model8);

        return list;
    }
}
