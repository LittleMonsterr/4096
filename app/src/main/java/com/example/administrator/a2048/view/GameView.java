package com.example.administrator.a2048.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.text.LoginFilter;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.Toast;

import com.example.administrator.a2048.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/1.
 */
public class GameView extends GridLayout {

    private static final String TAG = "GameView";
    private int target_score ;
    private int current_score = 0;
    private boolean can_revert = false;
    private Context context;
    private MainActivity activity;
    private SharedPreferences sp;

    public GameView(Context context) {
        super(context);
        init(context);
    }

    private int row_number = 4;
    private int column_number = 4;
    //记录当前棋盘上空白位置的一个数组
    //放一个（x,y)  表示空白棋盘的位置 行和列
    List<Point> blankItemList;
    NumberItem[][] itemMatrix;
    int[][] history_matrix;

    private void init(Context ctx){
        this.context = ctx;
        activity = (MainActivity) ctx;
        sp = ctx.getSharedPreferences("info",ctx.MODE_PRIVATE);
        row_number = sp.getInt("lineNumber",4);
        target_score = sp.getInt("targetScore",4096);
        column_number = row_number;
        initView(ctx);
    }

    private void initView(Context context) {
        //清空所有GameView
        removeAllViews();
        //设置GameView的行列数
        setColumnCount(column_number);
        setRowCount(row_number);
        //初始化棋盘矩阵，包括当前棋盘矩阵和上一步棋盘矩阵
        itemMatrix = new NumberItem[row_number][column_number];
        history_matrix = new int[row_number][column_number];
        blankItemList = new ArrayList<Point>();
        //重置当前分数为0
        updateCurrentScore(0);
        //通过代码获得屏幕的宽度
        WindowManager windowManager =  (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        int window_width = metrics.widthPixels;

        for(int i=0;i<row_number; i++){
            for(int j=0; j<column_number; j++){

                NumberItem numberItem = new NumberItem(context);
                itemMatrix[i][j] = numberItem;
                //初始化blanklist
                Point p = new Point();
                p.x = i;
                p.y = j;
                blankItemList.add(p);

                addView(numberItem,window_width/row_number,window_width/row_number);
            }
        }

        //随机找个位置去显示一个不为0 的数字
        addRandomNumber();
        addRandomNumber();

    }



    private void addRandomNumber() {

        updateBlankNumberList();
        //随机产生一个i  随机产生一个j   i:0-3   j:0-3
        //要求在棋盘上没有被占用的地方随机找一个位置
        int blankLength = blankItemList.size();   //16
        int random_location = (int) Math.floor(Math.random()*blankLength);
        Point point = blankItemList.get(random_location);
        NumberItem numberItem = itemMatrix[point.x][point.y];
        numberItem.setNumber(2);

    }

    //每次去找一个随机的产生数字的位置之前，需要去更新一下产生随机数的范围
    private void updateBlankNumberList() {
        blankItemList.clear();
        for(int i=0; i<row_number; i++){
            for(int j=0; j<column_number; j++){
                int number = itemMatrix[i][j].getNumber();
                if(number==0){
                    blankItemList.add(new Point(i,j));
                }
            }
        }
    }

    public void set_rowNumber(int row){
        this.row_number = row;
    }

    public void set_columnNumber(int column){
        this.column_number = column;
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //在这里会调用OnTouchListener 的onTouch callback
    int start_x;
    int start_y;
    int end_x;
    int end_y;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                start_x = (int) event.getX();
                start_y = (int) event.getY();
                Log.i(TAG,"ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG,"ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG,"ACTION_UP");
                end_x = (int) event.getX();
                end_y = (int) event.getY();
                //判断滑动的方向
                judgeDirection();
                //更新当前分数
                updateCurrentScore(current_score);
                //处理下一步做什么
                handleNext(isOver());
                break;
        }
        return true;
    }

    private void updateCurrentScore(int current_score) {
        this.current_score = current_score;
        activity.setScore(current_score);
    }

    private void handleNext(int over) {
        if(over==0){
            //提示用户赢了
            activity.updateRecordSore(current_score);
            new AlertDialog.Builder(context)
                    .setTitle("恭喜你！")
                    .setMessage("您已经成功完成任务，是否再来一局？")
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            restart();
                        }
                    })
                    .setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //关闭当前acyivity
                            activity.finish();
                        }
                    })
                    .show();
        }else if(over==-1){
            activity.updateRecordSore(current_score);
            //游戏结束
            new AlertDialog.Builder(context)
                    .setTitle("很遗憾")
                    .setMessage("挑战失败，是否再来一局？")
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            restart();
                        }
                    })
                    .setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.finish();
                        }
                    })
                    .show();
        }else {
            //over==1 ，表示可以正常继续
            if(can_slide){
                addRandomNumber();
                can_slide = false;
            }/*else {
                Toast.makeText(activity,"当前滑动方向没有可合并的数字",Toast.LENGTH_SHORT).show();
            }*/
        }
    }

    //再来一局
    public void restart() {
        initView(activity);
    }

    //判断玩家滑动的方向
    private void judgeDirection() {
        int dx = Math.abs(end_x-start_x);
        int dy = Math.abs(end_y-start_y);
        //在滑动前保存记录，方便退一步操作
        saveToHistory();

        if(dx>dy){    //水平方向滑动
            if(end_x-start_x>100){
                slideRight();    //右滑
            }else if(start_x-end_x>100){
                slideLeft();     //左滑
            }
        }else{        //竖直方向滑动
            if(end_y-start_y>100){
                slideDown();    //下滑
            }else if(start_y-end_y>100){
                slideUp();     //上滑
            }
        }
    }

    boolean can_slide = false;
    private void slideUp() {
        Log.i(TAG,"上滑");
        ArrayList<Integer> cacuList = new ArrayList<Integer>(column_number);
        for(int i=0;i<column_number;i++){
            int pre_number = -1;  //用于遍历时保存前一个item上的数字
            for(int j = 0;j<row_number;j++){
                int current_number = itemMatrix[j][i].getNumber();
                //当前拿到的数字为0，直接忽略
                if(current_number==0){
                    continue;
                }else{    //当前number不为0
                    //前一个数与当前number相同，两数相加，将结果保存到cacuList里
                    if(current_number==pre_number){
                        cacuList.add(pre_number*2);
                        current_score+=pre_number*2;
                        pre_number = -1;
                    }else{      //当前number与前一个不同
                        //pre_number保存的是-1，则需进一步去看下一个数字
                        if(pre_number==-1){
                            pre_number = current_number;

                        }else{       //pre_number保存有不为-1的数字（跟前一个不相等）
                            cacuList.add(pre_number);     //将前一个数字加到cacuList里
                            pre_number = current_number;  //将当前数字赋给pre_number
                        }
                    }
                }
            }
            //最后一个item上有数字，前一个没有，会漏掉最后一个数字
            if(pre_number!=-1){
                cacuList.add(pre_number);
            }

            if(cacuList.size()<row_number){can_slide = true;}
            //计算的结果有可能小于这一列的长度
            for (int k = 0; k < cacuList.size(); k++) {
                Integer new_number = cacuList.get(k);
                //can_slide = true;
                itemMatrix[k][i].setNumber(new_number);
            }
            //剩下的部分，填充0
            for (int p = cacuList.size(); p < row_number; p++) {
                itemMatrix[p][i].setNumber(0);
            }
            cacuList.clear();
        }

    }

    private void slideDown() {
        Log.i(TAG,"下滑");
        ArrayList<Integer> cacucList = new ArrayList<Integer>(column_number);
        //每一列扫描
        for(int i=0;i<column_number;i++){
            int pre_number = -1;
            for(int j=row_number-1;j>=0;j--){
                int current_number = itemMatrix[j][i].getNumber();
                if(current_number==0){
                    continue;
                }else{

                    if(current_number==pre_number){
                        cacucList.add(pre_number*2);
                        current_score+=pre_number*2;
                        pre_number = -1;
                    }else{

                        if(pre_number==-1){
                            pre_number = current_number;
                        }else{
                            cacucList.add(pre_number);
                            pre_number = current_number;
                        }
                    }
                }
            }
            if(pre_number!=-1){
                cacucList.add(pre_number);
            }

            if(cacucList.size()<row_number)
                can_slide = true;

            for (int k = 0; k < cacucList.size(); k++) {
                Integer new_number = cacucList.get(k);
                itemMatrix[row_number - 1 - k][i].setNumber(new_number);
                //can_slide = true;
            }
            for (int p = row_number - 1 - cacucList.size(); p >= 0; p--) {
                itemMatrix[p][i].setNumber(0);
            }

            cacucList.clear();

        }
    }

    private void slideLeft() {
        Log.i(TAG,"左滑");
        ArrayList<Integer> cacuList = new ArrayList<Integer>(column_number);
        //遍历
        for(int i=0;i<row_number;i++){
            int pre_number = -1;
            for(int j=0;j<column_number;j++){
                int current_number = itemMatrix[i][j].getNumber();
                if(current_number==0){
                    continue;
                }else{
                    if(current_number==pre_number){
                        cacuList.add(current_number*2);
                        current_score+=pre_number*2;
                        pre_number = -1;
                    }else {
                        if(pre_number==-1){
                            pre_number = current_number;
                        }else {
                            cacuList.add(pre_number);
                            pre_number = current_number;
                        }
                    }
                }
            }
            if(pre_number!=-1){
                cacuList.add(pre_number);
            }

            if(cacuList.size()<row_number)
                can_slide = true;

            for (int k = 0; k < cacuList.size(); k++) {
                int new_number = cacuList.get(k);
                itemMatrix[i][k].setNumber(new_number);
                //can_slide = true;
            }
            for (int p = cacuList.size(); p < column_number; p++) {
                itemMatrix[i][p].setNumber(0);
            }

            cacuList.clear();
        }
    }

    private void slideRight() {
        Log.i(TAG,"右滑");
        ArrayList<Integer> cacuList = new ArrayList<Integer>(column_number);
        for(int i=0;i<row_number;i++){
            int pre_number = -1;
            for(int j=column_number-1;j>=0;j--){
                int current_number = itemMatrix[i][j].getNumber();
                if(current_number==0){
                    continue;
                }else{
                    if(current_number==pre_number){
                        cacuList.add(current_number*2);
                        current_score+=pre_number*2;
                        pre_number = -1;
                    }else{
                        if(pre_number==-1){
                            pre_number = current_number;
                        }else {
                            cacuList.add(pre_number);
                            pre_number = current_number;
                        }
                    }
                }
            }
            if(pre_number!=-1){
                cacuList.add(pre_number);
            }

            if(cacuList.size()<row_number)
                can_slide = true;

            for (int k = 0; k < cacuList.size(); k++) {
                Integer new_number = cacuList.get(k);
                itemMatrix[i][column_number - 1 - k].setNumber(new_number);
                //can_slide = true;
            }
            for (int p = column_number - cacuList.size() - 1; p >= 0; p--) {
                itemMatrix[i][p].setNumber(0);
            }

            cacuList.clear();
        }
    }

    //-1 fail  ;0 win; 1  nut over,continue
    private int isOver() {
        int over = 1;
        //棋盘中只要有一个item的数字等于target_score，则胜利
        for(int i=0;i<row_number;i++){
            for(int j=0;j<column_number;j++){
                if(itemMatrix[i][j].getNumber()==target_score){
                    return 0;
                }
            }
        }
        //判断水平方向是否可以合并
        int pre_number = -1;
        for(int i=0;i<row_number;i++){
            for(int j=0;j<column_number;j++){
                int current_number = itemMatrix[i][j].getNumber();
                if(pre_number!=-1){
                    if(pre_number==current_number){
                        return 1;       //存在可以合并的两个item
                    }
                    pre_number = current_number;
                }else{
                    pre_number = current_number;
                }
            }
        }
        //判断竖直方向是否可以合并
        pre_number = -1;
        for(int i=0;i<column_number;i++){
            for(int j=0;j<row_number;j++){
                int current_number = itemMatrix[j][i].getNumber();
                if(pre_number!=-1){
                    if(pre_number==current_number){
                        return 1;
                    }
                    pre_number = current_number;
                }else {
                    pre_number = current_number;
                }
            }
        }

        updateBlankNumberList();
        //如果没有空白的位置出现新数字，则游戏结束
        if(blankItemList.size()==0){
            return -1;
        }

        return over;
    }


    private void saveToHistory() {
        for(int i=0;i<row_number;i++){
            for(int j= 0;j<column_number;j++){
                history_matrix[i][j] = itemMatrix[i][j].getNumber();
            }
        }
        can_revert = true;
    }

    //回到上一步棋盘的样子
    public void revert(){
        if (can_revert)
            for (int i=0;i<row_number;i++)
                for (int j=0;j<column_number;j++){
                    itemMatrix[i][j].setNumber(history_matrix[i][j]);
                }
    }
}
