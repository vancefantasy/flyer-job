package com.flyer.job.common.utils;

import com.vip.vjtools.vjkit.security.CryptoUtil;
import com.vip.vjtools.vjkit.text.Charsets;
import com.vip.vjtools.vjkit.text.EncodeUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;

/**
 * 登录相关工具
 */
public class LoginUtil {

    //256位key
    private static byte[] aesKey =
        {-33, -23, 40, -102, 24, 126, -81, -16, 47, 21, -28, 125, 66, 80, -14, 105, 8, -84, 0, 116,
            -111, 100, -47, -13, 40, 80, -106, 33, 90, -96, 65, 97};

    private static int sessionTimeoutByHour = 5;

    public static String encrypt(String userName, String password)
        throws UnsupportedEncodingException {
        String formatUser = String.format("%s|%s|%d", userName, password, DateUtil.unixTime());

        byte[] arr = CryptoUtil.aesEncrypt(formatUser.getBytes(Charsets.UTF_8), aesKey);

        return EncodeUtil.encodeHex(arr);
    }

    public static String decrypt(String ciphertext) {
        byte[] arr = EncodeUtil.decodeHex(ciphertext);
        return CryptoUtil.aesDecrypt(arr, aesKey);
    }

    public static boolean checkSession(String text) {
        String[] arr = text.split("\\|");
        if (arr.length != 3) {
            return false;
        }
        for (int i = 0; i < arr.length; i++) {
            if (StringUtils.isBlank(arr[i])) {
                return false;
            }
        }
        long loginTime = 0L;

        try {
            loginTime = Long.parseLong(arr[2]);
        } catch (Exception e) {
            return false;
        }

        //登录超时
        if (DateUtil.unixTime() - loginTime > sessionTimeoutByHour * 60 * 60) {
            return false;
        }
        return true;
    }

    public static String getUserFromToken(String token){
        String text = LoginUtil.decrypt(token);
        return text.split("\\|")[0];
    }

}
