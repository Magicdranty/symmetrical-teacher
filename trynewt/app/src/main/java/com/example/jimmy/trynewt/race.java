package com.example.jimmy.trynewt;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class race extends AppCompatActivity {
    List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
    String accesspin, realans;
    TextView max, num, time, question, score;
    int now = 0, scores = 0, maxq = 0;
    Button a, b, c, d;
    BufferedReader brt;
    Socket soc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race);
        ////
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects() //探测SQLite数据库操作
                .penaltyLog() //打印logcat
                .penaltyDeath()
                .build());
        ////
        Intent it = getIntent();
        accesspin = it.getStringExtra("accesspin");
        String result = DBConnector.executeQuery("SELECT question.question,question.A,question.B,question.C,question.D,question.ans FROM record,testlist,question,testinside WHERE record.pinforaccess='" + accesspin + "' AND record.pinfortest=testlist.KEYIN AND testlist.num=testinside.testtitleid and testinside.questionid=question.id");
        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                JSONObject jsonData = jsonArray.getJSONObject(i);
                map.put("Q", jsonData.getString("question"));
                map.put("A", jsonData.getString("A"));
                map.put("B", jsonData.getString("B"));
                map.put("C", jsonData.getString("C"));
                map.put("D", jsonData.getString("D"));
                map.put("ans", jsonData.getString("ans"));
                lists.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ///
        score = (TextView) findViewById(R.id.score);
        max = (TextView) findViewById(R.id.max);
        max.setText(String.valueOf(maxq = lists.size()));
        num = (TextView) findViewById(R.id.num);
        a = (Button) findViewById(R.id.button2);
        b = (Button) findViewById(R.id.button3);
        c = (Button) findViewById(R.id.button4);
        d = (Button) findViewById(R.id.button5);
        question = (TextView) findViewById(R.id.Q);
        time = (TextView) findViewById(R.id.time);
        connectuse con = (connectuse) race.this.getApplication();
        brt = con.getread();soc=con.getSocket();
    }

    public void set() {
        num.setText(String.valueOf(now + 1));//EX 顯示1~9 實際在list內的0~8
        question.setText(lists.get(now).get("Q").toString());
        a.setText(lists.get(now).get("A").toString());
        b.setText(lists.get(now).get("B").toString());
        c.setText(lists.get(now).get("C").toString());
        d.setText(lists.get(now).get("D").toString());
        realans = lists.get(now).get("ans").toString();
        score.setText(String.valueOf(scores));
        now++;
        down dd = new down();
        dd.start();
    }

    class down extends Thread {
        String temp;
        @Override
        public void run() {
            try {
                Log.e("downcounting", "QQ");
                while (true) {
                    temp = brt.readLine();
                    handler.obtainMessage(1, temp).sendToTarget();
                    if (temp.equals("0")) {
                        break;
                    }
                }
                jump();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    time.setText(msg.obj.toString());
                    break;
            }
        }
    };

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_race, menu);
        return true;
    }

    public void onResume() {
        super.onResume();
        set();
    }
    public boolean onKeyDown(int keyCode,KeyEvent event){

        if(keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0){   //確定按下退出鍵and防止重複按下退出鍵
          dia();
        }
        return false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void jump() {
        Intent it = new Intent(race.this, ranking.class);
        Bundle bd = new Bundle();
        bd.putInt("now", now);
        bd.putInt("max", maxq);
        bd.putString("pfa",accesspin);
        it.putExtras(bd);
        startActivity(it);
    }
   public void dia()
   {
       new AlertDialog.Builder(race.this)
               .setTitle("警告")
               .setMessage("是否要離開測驗(將會使此次測驗失效)")
               .setPositiveButton("是", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       DBConnector.executeQuery("delete from buffer where testpfa='" + accesspin + "'");
                       DBConnector.executeQuery("delete from record where pinforaccess='" + accesspin + "'");
                       try {
                           soc.close();
                       } catch (IOException e) {
                           e.printStackTrace();
                       }
                       Intent it=new Intent(race.this,MainActivity.class);
                       it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                       startActivity(it);
                   }
               })
               .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {

                   }
               }).show();

   }

}
