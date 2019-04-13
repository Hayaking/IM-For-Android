package com.example.haya.im.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;

public class Utils {
    /**
     * @return 公网ip
     * @throws IOException
     */
//    public static String getPublicIp() {
//        String ip =null;
//        new GetPublicIP(ip).execute();
//        return ip;
//    }
    public static Object b2o(byte[] buffer) {
        Object obj = null;
        ByteArrayInputStream buffers = new ByteArrayInputStream(buffer);
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(buffers);
            obj = in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return obj;
    }

    public static byte[] getArrayByte(byte[] buf, int len) {
        byte[] bytes = new byte[len];
        System.arraycopy(buf, 0, bytes, 0, len);
        return bytes;
    }

//    public static String getSha1(String str) {
//
//        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
//                'a', 'b', 'c', 'd', 'e', 'f'};
//        try {
//            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
//            mdTemp.update(str.getBytes("UTF-8"));
//            byte[] md = mdTemp.digest();
//            int j = md.length;
//            char buf[] = new char[j * 2];
//            int k = 0;
//            for (int i = 0; i < j; i++) {
//                byte byte0 = md[i];
//                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
//                buf[k++] = hexDigits[byte0 & 0xf];
//            }
//            return new String(buf);
//        } catch (Exception e) {
//            return null;
//        }
//    }

    public static int returnActualLength(byte[] data) {
        int i = 0;
        for (; i < data.length; i++) {
            if (data[i] == '\0')
                break;
        }
        return i;
    }

    public static byte[] o2b(Object s) {
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(s);
        } catch (Exception e) {
            System.out.println("error");
            return null;
        } finally {
            try {
                oos.close();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bos.toByteArray();
    }
}
