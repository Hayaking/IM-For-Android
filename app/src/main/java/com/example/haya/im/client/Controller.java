package com.example.haya.im.client;

import android.os.AsyncTask;
import android.util.Log;

import com.example.haya.im.utils.DBUtils;
import com.example.haya.im.utils.encryption.RSAUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.security.interfaces.RSAPublicKey;
import java.util.concurrent.ExecutionException;

public class Controller {
    private static final String serverIp = "129.204.82.119";
    private static final Integer serverPort = 9898;
    private volatile DatagramSocket socket;
    private RSAPublicKey rsaPublicKey;

    public Controller(int port) {
        initSocket(port);
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public void initSocket(int port) {
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            initSocket(++port);
        }
    }

    public Message justDoIt(Object... obj) {
        AsyncTask<Object, Void, Message> execute = new Do().execute(obj);
        Message message = null;
        try {
            message = execute.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return message;
    }

    public void send(Message message) {
        Gson gson = new Gson();
        String json = gson.toJson(message);
        DatagramSocket socket = getSocket();
        Log.i("***SEND***", json);

        try {
            DatagramPacket packet = new DatagramPacket(
                    json.getBytes(),
                    json.getBytes().length,
                    InetAddress.getByName(serverIp),
                    serverPort
            );

            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendByRSA(Message message) {
        DatagramSocket socket = getSocket();
        Gson gson = new Gson();
        try {
            Log.i("pswlen", message.getPassword().length+"");
            byte[] psw = RSAUtil.encrypt(message.getPassword(), rsaPublicKey);
            message.setPassword(psw);
            String json = gson.toJson(message);
            Log.i("***SEND***", json);

            byte[] bytes = json.getBytes();
            DatagramPacket packet = new DatagramPacket(
                    bytes,
                    bytes.length,
                    InetAddress.getByName(serverIp),
                    serverPort
            );
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class Do extends AsyncTask<Object, Void, Message> {
        @Override
        protected Message doInBackground(Object... obj) {
            Message.TYPE type = (Message.TYPE) obj[0];
            String text = (String) obj[1];
            String to = (String) obj[2];
            String account = (String) obj[3];
            String psw = (String) obj[4];
            Message message = null;
            switch (type) {
                case ADD:
                    message = Message.Add(account, to);
                    break;
                case LOGIN:
                    message = Message.Login(account, psw);
                    sendByRSA(message);
                    return message;
                case SIGN:
                    message = Message.Sign(account, psw);
                    sendByRSA(message);
                    return message;
                case ACCEPT:
                    message = Message.Accept(account, to);
                    break;
                case SEND:
                    message = Message.Send(account, text, to);
                    DBUtils.insertMessage(message);
                    break;
                case SEND_SELF:
                    message = Message.Self(account, text);
                    break;
                case ACCEPTED:
                    break;
                case GET_USER:
                    message = Message.QuerryUser(to);
                    break;
                case RECEIVED:
                    break;
                case GET_CONTACT:
                    message = Message.RequireContact(account);
                    break;
                case OFFLINE:
                    message = Message.Offline(account);
                    break;
                case ONLINE:
                    message = Message.Online(account);
                    break;
                case GET_RSA_PUBLICKEY:
                    message = Message.GetRSAPublicKey();
                    break;
            }
            send(message);
            return message;
        }
    }

    public Controller setRsaPublicKey(RSAPublicKey rsaPublicKey) {
        this.rsaPublicKey = rsaPublicKey;
        return this;
    }
}
