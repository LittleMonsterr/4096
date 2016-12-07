package com.example.administrator.a2048.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.administrator.a2048.R;

/**
 * Created by Administrator on 2016/8/1.
 */
public class NumberItem extends FrameLayout {

    private TextView tv_numberItem;
    private int number;

    public NumberItem(Context context) {
        super(context);
        init(context);
    }

    public void init(Context context) {
        tv_numberItem = new TextView(context);
        tv_numberItem.setText("");
        tv_numberItem.setBackgroundResource(R.drawable.textview_numberitem_bg);
        tv_numberItem.setGravity(Gravity.CENTER);
        tv_numberItem.setTextSize(20);
        setNumber(0);
        tv_numberItem.setBackgroundColor(Color.rgb(195,216,157));

        //给每一个TextView设置一个margin
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(5,5,5,5);
        //要把TextView增加到FrameLayout，所以必须给一个FrameLayout的LayoutParams
        addView(tv_numberItem,params);

    }

    public void setNumber(int number) {
        this.number = number;
       /* if(number==2){
            setText("2");
            tv_numberItem.setBackgroundColor(Color.parseColor("#F5F5DC"));
            *//*setBackgroundColor用法
            setBackgroundColor(Color.parseColor("#F5F5DC"));
            setBackgroundColor(Color.argb(0,79,79,79));  //0完全透明  255不透明*//*
        }*/
        switch (number){
            case 0:
                setText("");
                tv_numberItem.setBackgroundColor(Color.parseColor("#C3D89D")); //ARGB
                break;
            case 2:
                setText("2");
                tv_numberItem.setBackgroundColor(0xFFFFF5EE);

                break;
            case 4:
                setText("4");
                tv_numberItem.setBackgroundColor(0xFFFFEC8B);

                break;
            case 8:
                setText("8");
                tv_numberItem.setBackgroundColor(0xFFFFE4C4);
                break;
            case 16:
                setText("16");
                tv_numberItem.setBackgroundColor(0xFFFFDAB9);
                break;
            case 32:
                setText("32");
                tv_numberItem.setBackgroundColor(0xFFFFC125);
                break;
            case 64:
                setText("64");
                tv_numberItem.setBackgroundColor(0xFFFFB6C1);
                break;
            case 128:
                setText("128");
                tv_numberItem.setBackgroundColor(0xFFFFA500);
                break;
            case 256:
                setText("256");
                tv_numberItem.setBackgroundColor(0xFFFF83FA);
                break;
            case 512:
                setText("512");
                tv_numberItem.setBackgroundColor(0xFFFF7F24);
                break;
            case 1024:
                setText("1024");
                tv_numberItem.setBackgroundColor(0xFFFF6A6A);
                break;
            case 2048:
                setText("2048");
                tv_numberItem.setBackgroundColor(0xFFFF1493);
                break;
            case 4096:
                setText("4096");
                tv_numberItem.setBackgroundColor(0xFFFF3030);
                break;

        }
    }

    public int getNumber(){
        return number;
    }

    public void setText(String numberString) {
        tv_numberItem.setText(numberString);
    }

    public NumberItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
