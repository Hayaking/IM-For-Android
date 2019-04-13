package com.example.haya.im.utils;

import android.os.AsyncTask;

import com.example.haya.im.client.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class GetPublicIP extends AsyncTask<Void, Void, Void> {
    public static String getIp() {
        GetPublicIP getPublicIP = new GetPublicIP();
        getPublicIP.execute();
        return ip;
    }
    private static String ip;
    private Message msg;

    @Override
    protected Void doInBackground(Void... voids) {
        InputStream ins = null;

        try {
            URL url = new URL("http://pv.sohu.com/cityjson?ie=utf-8");
            URLConnection con = url.openConnection();

            ins = con.getInputStream();
            byte[] buf = new byte[1024];
            ins.read(buf);
            StringBuffer str = new StringBuffer(new String(buf, 0, buf.length));
            int a = str.indexOf(":") + 3;
            int b = str.indexOf(",") - 1;
            ip = str.substring(a, b);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                ins.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
