package com.example.jimmy.trynewt;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DBConnector {
    public static String executeQuery(String query_string) {

        String result = "";

        try {

            HttpClient httpClient = new DefaultHttpClient();


            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);//连接时间
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 3000);

            HttpPost httpPost = new HttpPost("http://192.168.43.9/aaa.php");
            //HttpPost httpPost = new HttpPost("http://192.168.100.2/aaa.php");
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("query_string", query_string));

            Log.e("aaa", query_string);

            httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            HttpResponse httpResponse = httpClient.execute(httpPost);

            HttpEntity httpEntity = httpResponse.getEntity();
            InputStream inputStream = httpEntity.getContent();
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = bufReader.readLine()) != null) {
                Log.e("111", line);
                if (!(line.startsWith("<"))) {
                    builder.append(line + "\n");
                    Log.e("00000", line);
                }
            }            inputStream.close();            result = builder.toString();

        } catch (Exception e) {
            if (e.toString().contains("timed out")) {
                Log.e("log_taxxxx", e.toString());
                return e.toString();
            } else {
                Log.e("log_tagggg", e.toString());
            }
        }

        return result;
    }
}
