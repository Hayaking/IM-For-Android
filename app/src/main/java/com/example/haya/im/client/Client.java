package com.example.haya.im.client;

import android.util.Log;

import com.example.haya.im.utils.Utils;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.DatagramPacket;
import java.security.interfaces.RSAPublicKey;

public class Client {
    public static Controller getController() {
        return controller;
    }

    public static Client getInstance() {
        if (null == instance) {
            instance = new Client();
            controller = new Controller(port);
        }
        return instance;
    }
    private static Client instance;
    private static Controller controller;
    private static int port = 9090;
    private String account;
    private String psw;

    public boolean get() {
        try {
            byte[] buf = new byte[2048];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            controller.getSocket().receive(packet);
            int len = Utils.returnActualLength(buf);
            //获取指定长度的byte数组
            buf = Utils.getArrayByte(buf, len);
            String json = new String(buf, 0, buf.length);
            Gson gson = new Gson();
            Message message = gson.fromJson(json, Message.class);
            Log.i("***RECEIVE***", json);
            switch (message.getType()) {
                case SIGN_SUCCESS:
                    return true;
                case LOGIN_SUCCESS:
                    send(Message.TYPE.ONLINE);
                    return true;
                case LOGIN_FAILED:
                    return false;
                case GOT_RSA_PUBLICKEY:
                    RSAPublicKey rsaPublicKey = (RSAPublicKey) Utils.b2o(message.getBuf());
                    controller.setRsaPublicKey(rsaPublicKey);
                    return true;
                default:
                    Log.println(Log.INFO, "receive", json);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public String getAccount() {
        return account;
    }

    public Message send(Message.TYPE type, String text, String to) {
        return controller.justDoIt(type, text, to, account, psw);
    }

    public Message send(Message.TYPE type, String to) {
        return send(type, null, to);
    }

    public Message send(Message.TYPE type) {
        return send(type, null, null);
    }

    public Client setAccount(String account, String psw) {
        this.account = account;
        this.psw = psw;
        return this;
    }
}
