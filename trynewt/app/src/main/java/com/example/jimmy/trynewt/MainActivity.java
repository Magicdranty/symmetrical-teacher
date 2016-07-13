package com.example.jimmy.trynewt;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    String account = "acc2", pinfortest = "5678", date = "20160630", func = "test";
    String pinforaccess;
    Socket soc;
    connectuse connect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void createt(View v) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String buff;
                while(true)
                {
                    buff=forrand();
                    String ss = DBConnector.executeQuery("select testnum from record where pinforaccess='" +buff + "'");
                    if (ss.contains("null")) {
                        pinforaccess = buff;
                        Log.e("!!!!!", "create unique pinforaccess");
                        break;
                    }
                }
                connect = (connectuse) MainActivity.this.getApplication();
                connect.init();//
                soc = connect.getSocket();
                if (soc.isConnected()) {Log.e("CANINTIN", "OK");
                    handler.obtainMessage(1).sendToTarget();
                } else {
                    Log.e("CANNOTIN", "NOOK");
                    handler.obtainMessage(2).sendToTarget();
                }
            }
        }).start();
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Intent it = new Intent(MainActivity.this, Main2Activity.class);
                    it.putExtra("pinforaccess", pinforaccess);
                    it.putExtra("account", account);
                    it.putExtra("func", func);
                    it.putExtra("pinfortest", pinfortest);
                    it.putExtra("date", date);
                    startActivity(it);
                    break;
                case 2:
                    Toast.makeText(MainActivity.this, "!!!", Toast.LENGTH_SHORT).show();
                    break;
                case 3:

                    break;
            }
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public String forrand() {
                    StringBuffer xs = new StringBuffer();
                    int x = (int) ((Math.random() * 7) % 4);//0~3 數字個數
                    for (int i = 0; i < 4; i++) {
                        int s = (int) ((Math.random() * 10) % 2);//0 代表抓數字
                        if (s == 0 && x != 0) {
                            xs.append((char) ((int) ((Math.random() * 11) % 10) + 48));
                            x--;
                        } else if (s == 1) {
                            xs.append((char) ((int) (((Math.random() * 26) + 65))));
                        } else {
                            i--;
                        }
                    }
        return  xs.toString();

    }
}
