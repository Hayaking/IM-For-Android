package com.example.haya.im;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.haya.im.activity.MessageActivity;
import com.example.haya.im.activity.ResultActivity;
import com.example.haya.im.bean.Historical;
import com.example.haya.im.bean.User;
import com.example.haya.im.client.Client;
import com.example.haya.im.client.Message;
import com.example.haya.im.fragment.History;
import com.example.haya.im.utils.DBUtils;
import com.example.haya.im.utils.Utils;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.LinkedList;

public class IMService extends Service {
    private final EchoServiceBinder echoServiceBinder = new EchoServiceBinder();
    private boolean flag = true;
    private HashMap<String, LinkedList<String>> contacts;

    public IMService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return echoServiceBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        flag = false;
    }

    public void receive() {
        DatagramSocket socket = Client.getController().getSocket();
        new Thread(() -> {
            while (flag) {
                try {
                    byte[] buf = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    int len = Utils.returnActualLength(buf);
                    buf = Utils.getArrayByte(buf, len);
                    String json = new String(buf, 0, buf.length);
                    Gson gson = new Gson();
                    Message message = gson.fromJson(json, Message.class);
                    Log.i("***RECEIVE***", json);
                    Historical item;
                    switch (message.getType()) {
                        case GOT_CONTACT:
                            byte[] bytes = message.getBuf();
                            contacts = (HashMap<String, LinkedList<String>>) Utils.b2o(bytes);
                            break;
                        case RECEIVED:
                            item = new Historical(message.getAccount(), message.getAccount(), Historical.TYPE.NORMAL);
                            item.setFlag(true);
                            DBUtils.insertHistory(item);
                            DBUtils.insertMessage(message);
                            if (null != MessageActivity.viewHolder)
                                MessageActivity.viewHolder.setrMsg(message);
                            History.viewHolder.setNewItem();
                            test();
                            break;
                        case RECEIVED_SELF:

                            DBUtils.insertMessage(message);
                            if (null != MessageActivity.viewHolder)
                                MessageActivity.viewHolder.setrMsg(message);
                            test();
                            break;
                        case GOT_USER:
                            String json1 = message.getText();
                            User user = gson.fromJson(json1, User.class);
                            ResultActivity.viewHolder.setUser(user);
                            break;
                        case ADD:
                            item = new Historical(message.getAccount(), "请求添加您为好友", Historical.TYPE.NOTICE_ADD);
                            item.setFlag(true);
                            DBUtils.insertHistory(item);
                            History.viewHolder.setNewItem();
                            break;
                        case ACCEPTED:
                            item = new Historical("添加完成", "添加完成", Historical.TYPE.NOTICE_ACCRPTED);
                            item.setFlag(true);
                            DBUtils.insertHistory(item);
                            History.viewHolder.setNewItem();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }

    public void test() {
        NotificationManager notificationManager = (NotificationManager) getSystemService
                (NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

        //设置标题
        mBuilder.setContentTitle("我是标题")
                //设置内容
                .setContentText("我是内容")
                //设置大图标
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                //设置小图标
                .setSmallIcon(R.mipmap.ic_launcher_round)
                //设置通知时间
                .setWhen(System.currentTimeMillis())
                //首次进入时显示效果
                .setTicker("我是测试内容")
                //设置通知方式，声音，震动，呼吸灯等效果，这里通知方式为声音
                .setDefaults(Notification.DEFAULT_SOUND);
        //发送通知请求
        notificationManager.notify(10, mBuilder.build());

    }

    public class EchoServiceBinder extends Binder {

        public HashMap<String, LinkedList<String>> getContacts() {
            while (IMService.this.contacts == null) {
                continue;
            }
            return IMService.this.contacts;
        }

        public IMService getService() {
            return IMService.this;
        }

    }
}
