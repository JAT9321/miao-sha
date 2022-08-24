package com.jiao.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * auth:@highSky
 * create:2022/8/12
 * email:zgt9321@qq.com
 **/
public class Md5Util {

    private static String salt = "1a2b3c4d";

    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    public static String inputPassToFormPass(String inputPass) {
        String src = "" + salt.charAt(0) + salt.charAt(2) + inputPass + salt.charAt(5) + salt.charAt(4);
        return md5(src);
    }

    public static String formPassToBPass(String formPass, String salt) {
        String src = "" + salt.charAt(0) + salt.charAt(2) + formPass + salt.charAt(5) + salt.charAt(4);
        return md5(src);
    }

    public static String inputPassToBPass(String inputPass, String salt) {
        String formPass = inputPassToFormPass(inputPass);
        return formPassToBPass(formPass, salt);
    }

    public static void main(String[] args) {
        String bPass = inputPassToBPass("111111", "111111");
        System.out.println(bPass);
        String formPass = inputPassToFormPass("11111111");
        String bPass1 = formPassToBPass(formPass, "111111");
        System.out.println(bPass1);
    }

}
