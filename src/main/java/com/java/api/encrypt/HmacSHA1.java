package com.java.api.encrypt;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by kevintian on 2017/9/28.
 */
public class HmacSHA1 {
    public static void main(String[] args) {
        String key = "7Te4G1c674be4fe48fE87d8f87N5c90a";
        String content = "eyJzY29wZSI6IjExMDgyMjFfNzBmZGEyOTAxMDI0YTYzYmU1MmQ2ZjgzNjU0Y2U5MmJmNmRhYTU3OSIsImRlYWRsaW5lIjoxNTA2NjIxNDIzfQ";
        String sign = getHmacSHA1(key, content);
        String signEncode = Base64.encodeBase64URLSafeString(sign.getBytes());
        System.out.println(signEncode);
    }

    private static String getHmacSHA1(String key, String content) {
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "HmacSHA1");
        Mac mac;
        try {
            mac = Mac.getInstance("HmacSHA1");
            mac.init(keySpec);
        } catch (Exception e) {//should never occur
            throw new IllegalStateException("unexpected", e);
        }
        byte[] macBytes = mac.doFinal(content.getBytes());
        return new String(Hex.encodeHex(macBytes));
    }
}
