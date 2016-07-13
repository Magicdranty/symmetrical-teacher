package com.example.jimmy.trynewt;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;

public class Main2Activity extends AppCompatActivity {
    String pfa, func, account, pft, date;
    Thread thread;
    Socket soc;
    BufferedWriter bwt;
    BufferedReader brt;
    Boolean flag = true;
    TextView people;
    RadioGroup group;
    String testtype = "test1";//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent it = getIntent();
        pfa = it.getStringExtra("pinforaccess");
        pft = it.getStringExtra("pinfortest");
        account = it.getStringExtra("account");
        func = it.getStringExtra("func");
        date = it.getStringExtra("date");
        TextView tv = (TextView) findViewById(R.id.textView);
        people = (TextView) findViewById(R.id.textView3);
        tv.setText(pfa);
        thread t = new thread();
        t.start();
        group = (RadioGroup) this.findViewById(R.id.radiogroup);

    }

    class thread extends Thread {
        connectuse connect = (connectuse) Main2Activity.this.getApplication();

        @Override
        public void run() {
            soc = connect.getSocket();
            brt = connect.getread();
            bwt = connect.getwrite();
            try {
                if (soc.isConnected()) {
                    bwt.write(account + "\n" + func + "\n" + pfa + "\n");
                    bwt.flush();
                    Log.e("!!!!!!", account + func + pfa);
                    while (flag) {
                        String x = brt.readLine().toString();
                        handler.obtainMessage(1, x).sendToTarget();
                    }
                }
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
                    people.setText(msg.obj.toString());
                    break;
                case 2:
                    Toast.makeText(Main2Activity.this, "!!!!", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    break;
            }
        }
    };

    public void start(View v) {

        switch (group.getCheckedRadioButtonId()) {
            case R.id.radioButton2:
                testtype = "race";
                break;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.e(testtype, "!");
                    String x = DBConnector.executeQuery("Insert into record(taccount,pinfortest,date,pinforaccess) values ('" + account + "','" + pft + "','" + date + "','" + pfa + "')");
                    bwt.write("start\n" + testtype + "\n");
                    bwt.flush();
                    flag = false;//按下開始 就停止更新人數
                    if (testtype.equals("test1")) {
                        soc.close();
                        Log.e("soc","CLOSE");
                    }//地一種測驗按下開始 就切斷socket
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        if (testtype.equals("race")) {
            Toast.makeText(Main2Activity.this, "RACE", Toast.LENGTH_SHORT).show();
            Intent it = new Intent(Main2Activity.this, race.class);
            it.putExtra("accesspin", pfa);
            startActivity(it);
        }else{

            Toast.makeText(Main2Activity.this, "test", Toast.LENGTH_SHORT).show();
            Intent it = new Intent(Main2Activity.this, MainActivity.class);
            it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(it);
            }

}

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {   //確定按下退出鍵
            try {
                soc.close();
                finish();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
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
}
