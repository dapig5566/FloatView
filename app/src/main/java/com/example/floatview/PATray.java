package com.example.floatview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

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
    final WindowManager windowManager;
    final WindowManager.LayoutParams params = new WindowManager.LayoutParams();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public PATray(Context context){
        super(context);
        this.context = context;
        windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        isShown = false;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.pa_tray, this);

        params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        params.flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
        params.format = PixelFormat.TRANSLUCENT;
        params.gravity = Gravity.TOP;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;

        initializeWeather(context);
        initializeAffairList(context);
    }

    public void setPAvatar(PAAvatar pAvatar) {
        this.pAvatar = pAvatar;
    }

    public void show(){
        isShown = true;
        windowManager.addView(view, params);
    }

    public void hide(){
        isShown = false;
        windowManager.removeView(view);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent keyEvent){
        if(keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
            System.out.println("BackKey pressed.");
            if(isShown){
                hide();
//                pAvatar.afterClosingTray();
                return true;
            }
        }
        return super.dispatchKeyEvent(keyEvent);
    }

    private void initializeAffairList(Context context) {
        ListView affairList = view.findViewById(R.id.affair_list);
        List<String> affairData = new ArrayList<>();
        for(int i=0;i<10;i++) {
            affairData.add("Affair"+ i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.item_template, affairData);
        affairList.setAdapter(adapter);

    }

    private void initializeWeather(Context context) {
        ZzWeatherView weatherView = (ZzWeatherView) view.findViewById(R.id.weather_view);
        weatherView.setData(generateMockData());

        //画折线
        weatherView.setLineType(ZzWeatherView.LINE_TYPE_CURVE);
        //画曲线(已修复不圆滑问题)
        //        weatherView.setLineType(ZzWeatherView.LINE_TYPE_CURVE);

        //设置线宽，单位px
        weatherView.setLineWidth(6f);

        //设置一屏幕显示几列(最少3列)
        try {
            weatherView.setColumnNumber(6);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //设置白天和晚上线条的颜色
        weatherView.setDayAndNightLineColor(Color.BLUE, Color.RED);

        //点击某一列
        weatherView.setOnWeatherItemClickListener(new ZzWeatherView.OnWeatherItemClickListener() {
            @Override
            public void onItemClick(WeatherItemView itemView, int position, WeatherModel weatherModel) {
                Toast.makeText(context, position + "", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private List<WeatherModel> generateMockData() {
        List<WeatherModel> list = new ArrayList<WeatherModel>();
        WeatherModel model = new WeatherModel();
        model.setDate("12/07");
        model.setWeek("昨天");
        model.setDayWeather("大雪");
        model.setDayTemp(11);
        model.setNightTemp(5);
        model.setNightWeather("晴");
        model.setWindOrientation("西南风");
        model.setWindLevel("3级");
        model.setAirLevel(AirLevel.EXCELLENT);
        list.add(model);

        WeatherModel model1 = new WeatherModel();
        model1.setDate("12/08");
        model1.setWeek("今天");
        model1.setDayWeather("晴");
        model1.setDayTemp(8);
        model1.setNightTemp(5);
        model1.setNightWeather("晴");
        model1.setWindOrientation("西南风");
        model1.setWindLevel("3级");
        model1.setAirLevel(AirLevel.HIGH);
        list.add(model1);

        WeatherModel model2 = new WeatherModel();
        model2.setDate("12/09");
        model2.setWeek("明天");
        model2.setDayWeather("晴");
        model2.setDayTemp(9);
        model2.setNightTemp(8);
        model2.setNightWeather("晴");
        model2.setWindOrientation("东南风");
        model2.setWindLevel("3级");
        model2.setAirLevel(AirLevel.POISONOUS);
        list.add(model2);

        WeatherModel model3 = new WeatherModel();
        model3.setDate("12/10");
        model3.setWeek("周六");
        model3.setDayWeather("晴");
        model3.setDayTemp(12);
        model3.setNightTemp(9);
        model3.setDayPic(R.drawable.w0);
        model3.setNightPic(R.drawable.w1);
        model3.setNightWeather("晴");
        model3.setWindOrientation("东北风");
        model3.setWindLevel("3级");
        model3.setAirLevel(AirLevel.GOOD);
        list.add(model3);

        WeatherModel model4 = new WeatherModel();
        model4.setDate("12/11");
        model4.setWeek("周日");
        model4.setDayWeather("多云");
        model4.setDayTemp(13);
        model4.setNightTemp(7);
        model4.setDayPic(R.drawable.w2);
        model4.setNightPic(R.drawable.w4);
        model4.setNightWeather("多云");
        model4.setWindOrientation("东北风");
        model4.setWindLevel("3级");
        model4.setAirLevel(AirLevel.LIGHT);
        list.add(model4);

        WeatherModel model5 = new WeatherModel();
        model5.setDate("12/12");
        model5.setWeek("周一");
        model5.setDayWeather("多云");
        model5.setDayTemp(17);
        model5.setNightTemp(8);
        model5.setDayPic(R.drawable.w3);
        model5.setNightPic(R.drawable.w4);
        model5.setNightWeather("多云");
        model5.setWindOrientation("西南风");
        model5.setWindLevel("3级");
        model5.setAirLevel(AirLevel.LIGHT);
        list.add(model5);

        WeatherModel model6 = new WeatherModel();
        model6.setDate("12/13");
        model6.setWeek("周二");
        model6.setDayWeather("晴");
        model6.setDayTemp(13);
        model6.setNightTemp(6);
        model6.setDayPic(R.drawable.w5);
        model6.setNightPic(R.drawable.w6);
        model6.setNightWeather("晴");
        model6.setWindOrientation("西南风");
        model6.setWindLevel("3级");
        model6.setAirLevel(AirLevel.POISONOUS);
        list.add(model6);

        WeatherModel model7 = new WeatherModel();
        model7.setDate("12/14");
        model7.setWeek("周三");
        model7.setDayWeather("晴");
        model7.setDayTemp(19);
        model7.setNightTemp(10);
        model7.setDayPic(R.drawable.w5);
        model7.setNightPic(R.drawable.w7);
        model7.setNightWeather("晴");
        model7.setWindOrientation("西南风");
        model7.setWindLevel("3级");
        model7.setAirLevel(AirLevel.POISONOUS);
        list.add(model7);

        WeatherModel model8 = new WeatherModel();
        model8.setDate("12/15");
        model8.setWeek("周四");
        model8.setDayWeather("晴");
        model8.setDayTemp(22);
        model8.setNightTemp(4);
        model8.setDayPic(R.drawable.w5);
        model8.setNightPic(R.drawable.w8);
        model8.setNightWeather("晴");
        model8.setWindOrientation("西南风");
        model8.setWindLevel("3级");
        model8.setAirLevel(AirLevel.POISONOUS);
        list.add(model8);

        return list;
    }
}
