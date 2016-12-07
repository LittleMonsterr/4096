package com.example.administrator.a2048;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.a2048.view.GameView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tv_naminactivity_score;
    private SharedPreferences sp;
    private TextView tv_mainactivity_record;
    private Button bt_mainactivity_revert;
    private Button bt_mainactivity_restart;
    private Button bt_mainactivity_option;
    private GameView gameView;
    private TextView tv_mainactivity_targetscore;
    private int lineNumber;
    private int targetScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //隐藏标题栏
        getSupportActionBar().hide();

        sp = getSharedPreferences("info",MODE_PRIVATE);
        int record_score_saved = sp.getInt("record_score",0);
        targetScore = sp.getInt("targetScore",4096);

        RelativeLayout rl_mainactivity_center = (RelativeLayout) findViewById(R.id.rl_mainactivity_center);

        tv_mainactivity_targetscore = (TextView)findViewById(R.id.tv_mainactivity_targetscore);
        tv_mainactivity_record = (TextView) findViewById(R.id.tv_mainactivity_record);

        tv_naminactivity_score = (TextView) findViewById(R.id.tv_mainactivity_score);
        tv_mainactivity_record.setText(record_score_saved+"");

        bt_mainactivity_option = (Button) findViewById(R.id.bt_mainactivity_option);
        bt_mainactivity_restart = (Button) findViewById(R.id.bt_mainactivity_restart);
        bt_mainactivity_revert = (Button) findViewById(R.id.bt_mainactivity_revert);

        bt_mainactivity_restart.setOnClickListener(this);
        bt_mainactivity_revert.setOnClickListener(this);
        bt_mainactivity_option.setOnClickListener(this);

        gameView = new GameView(this);
        setTargetScore(this.targetScore);
        rl_mainactivity_center.addView(gameView);

    }

    public void updateRecordSore(int record_score) {

        int record_score_saved = sp.getInt("record_socre",0);
        if(record_score>record_score_saved){
            SharedPreferences.Editor edit = sp.edit();
            edit.putInt("record_score",record_score);
            edit.commit();
            tv_mainactivity_record.setText(record_score+"");
        }
    }

    public void setScore(int score) {
        tv_naminactivity_score.setText(score+"");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_mainactivity_option:
                option();
                break;
            case R.id.bt_mainactivity_restart:
                restart();
                break;
            case R.id.bt_mainactivity_revert:
                revert();
                break;
        }

    }

    private void revert() {
        new AlertDialog.Builder(this)
                .setTitle("撤销")
                .setMessage("确定要撤销上一步吗？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gameView.revert();
                    }
                })
                .setNegativeButton("否",null)
                .show();
    }

    private void restart() {
        new AlertDialog.Builder(this)
                .setTitle("重新开始")
                .setMessage("确定要重新开始新一局吗？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gameView.restart();
                    }
                })
                .setNegativeButton("否",null)
                .show();
    }

    private void option() {
        startActivityForResult(new Intent(this,OptionActivity.class),10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==10 && resultCode==200){
            int line = data.getIntExtra("lineNumber",4);
            int targetScore = data.getIntExtra("targetScore",2048);
            Log.i("MainActivity",targetScore+"");
            //更新设置之后的分数
            setTargetScore(targetScore);
            setScore(0);
            //更新棋盘
            gameView.set_rowNumber(line);
            gameView.set_columnNumber(line);
            gameView.restart();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setTargetScore(int score) {
        tv_mainactivity_targetscore.setText(score+"");
    }
}
