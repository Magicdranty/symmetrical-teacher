package com.example.jimmy.trynewt;

import android.app.Application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class connectuse extends Application {
    private Socket soc;
    private BufferedReader brt;
    private BufferedWriter bwt;
    public  void init()
    {
        try {
            this.soc=new Socket();
            SocketAddress addr=new InetSocketAddress("192.168.43.9",509);
            //SocketAddress addr=new InetSocketAddress("192.168.100.2",509);
            soc.connect(addr,1000);
            brt=new BufferedReader(new InputStreamReader(soc.getInputStream(), "UTF-8"));
            bwt = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream(), "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public Socket getSocket() {
        return soc;
    }

    public BufferedWriter getwrite()
    {
        return bwt;
    }

    public BufferedReader getread()
    {
        return brt;
    }

}
