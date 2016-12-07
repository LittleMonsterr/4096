package com.example.administrator.a2048.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/8/1.
 */
public class MyTextView extends TextView{

    //在代码中传入上下文，去new TextView
    public MyTextView(Context context) {
        super(context);
    }

    //系统在new TextView 的时候调用，AttributeSet表示属性集合，系统解析xml文件里写的TextView的属性
    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //重新定义一个新的TextView，让TextView除了显示文字之外，再显示边框
    @Override
    protected void onDraw(Canvas canvas) {

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),paint);

        //系统的TextView会去做原本TextView应该显示的东西
        super.onDraw(canvas);
    }
}
