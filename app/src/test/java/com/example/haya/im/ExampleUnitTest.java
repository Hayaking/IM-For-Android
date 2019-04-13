package com.example.haya.im;

import com.example.haya.im.utils.encryption.MD5Util;
import com.example.haya.im.utils.encryption.RSAUtil;

import org.junit.Test;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        String DATA = MD5Util.MD5( "111");
        Map<String, Object> keyMap = RSAUtil.initKey();

        RSAPublicKey rsaPublicKey = RSAUtil.getpublicKey(keyMap);
        RSAPrivateKey rsaPrivateKey = RSAUtil.getPrivateKey(keyMap);
//        System.out.println("RSA PublicKey: " + rsaPublicKey);
//        System.out.println("RSA PrivateKey: " + rsaPrivateKey);

        byte[] rsaResult = RSAUtil.encrypt(DATA.getBytes(), rsaPublicKey);
//        System.out.println(DATA + "====>>>> RSA 加密>>>>====" + BytesToHex.fromBytesToHex(rsaResult));

        byte[] plainResult = RSAUtil.decrypt(rsaResult, rsaPrivateKey);
//        System.out.println(DATA + "====>>>> RSA 解密>>>>====" + BytesToHex.fromBytesToHex(plainResult));
        System.out.println(new String(plainResult, 0, plainResult.length));
    }
}