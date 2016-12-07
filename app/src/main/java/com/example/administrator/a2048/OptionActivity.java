package com.example.administrator.a2048;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;

public class OptionActivity extends AppCompatActivity implements View.OnClickListener{

    private int lineNumber;
    private int targetScore;
    private SharedPreferences sp;
    private Button bt_optionactivity_setlines;
    private Button bt_optionactivity_setscore;
    private int choice ;
    private  int select;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        getSupportActionBar().hide();

        /*// 实例化广告条
        AdView adView = new AdView(this, AdSize.FIT_SCREEN);

        // 获取要嵌入广告条的布局
        LinearLayout adLayout=(LinearLayout)findViewById(R.id.adLayout);

        // 将广告条加入到布局中
        adLayout.addView(adView);*/

        sp = getSharedPreferences("info",MODE_PRIVATE);
        //从存储信息的info里拿出设置期盼的行数和目标分
        lineNumber = sp.getInt("lineNumber",4);
        targetScore = sp.getInt("targetScore",4096);

        switch (lineNumber){
            case 4:
                choice = 0;
                break;
            case 5:
                choice = 1;
                break;
            case 6:
                choice = 2;
                break;
        }

        switch (targetScore) {
            case 1024:
                select = 0;
                break;
            case 2048:
                select = 1;
                break;
            case 4096:
                select = 2;
                break;
        }

        Button bt_optionactivity_back = (Button) findViewById(R.id.bt_optionactivity_back);
        Button bt_optionactivity_done = (Button) findViewById(R.id.bt_optionactivity_done);
        bt_optionactivity_setlines = (Button) findViewById(R.id.bt_optionactivity_setlines);
        bt_optionactivity_setscore = (Button) findViewById(R.id.bt_optionactivity_setscore);

        bt_optionactivity_setlines.setText(lineNumber+"");
        bt_optionactivity_setscore.setText(targetScore+"");

        bt_optionactivity_back.setOnClickListener(this);
        bt_optionactivity_done.setOnClickListener(this);
        bt_optionactivity_setlines.setOnClickListener(this);
        bt_optionactivity_setscore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_optionactivity_back:
                back();
                break;
            case R.id.bt_optionactivity_done:
                done();
                break;
            case R.id.bt_optionactivity_setlines:
                setLines();
                break;
            case R.id.bt_optionactivity_setscore:
                setScore();
                break;
        }
    }


    private void setScore() {
        final String[] targets = {"1024","2048","4096"};
        new AlertDialog.Builder(this)
                .setTitle("设置游戏目标分数")
                .setSingleChoiceItems(targets, select, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        select = which;
                    }
                })
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        targetScore = Integer.parseInt(targets[select]);
                        bt_optionactivity_setscore.setText(targets[select]);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


    private void setLines() {
        final String[] choices = {"4","5","6"};
        new AlertDialog.Builder(this)
                .setTitle("设置棋盘的行数")
                .setSingleChoiceItems(choices, choice, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        choice = which;
                    }
                })
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        lineNumber = Integer.parseInt(choices[choice]);
                        bt_optionactivity_setlines.setText(choices[choice]);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void done() {
        //将设置的棋盘行数和目标分使用sp保存到info里
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt("lineNumber",lineNumber);
        edit.putInt("targetScore",targetScore);
        edit.commit();
        //把设置的信息带回游戏主界面即MainActivity
        Intent intent = new Intent();
        intent.putExtra("lineNumber",lineNumber);
        intent.putExtra("targetScore",targetScore);
        setResult(200,intent);
        finish();
    }

    private void back() {
        finish();
    }
}
