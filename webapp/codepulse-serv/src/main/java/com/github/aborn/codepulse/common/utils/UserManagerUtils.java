package com.github.aborn.codepulse.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

import java.util.Arrays;
import java.util.List;

/**
 * @author aborn
 * @date 2023/02/10 10:19
 */
public class UserManagerUtils {

    // 用于调试的，默认的TOKEN，正常16位，这个用于测试是15位
    public static final String DEFAULT_TEST_UID_TOKEN = "8ba394513f8420e";
    private static final String ABORN_TOKEN = "0x8bf8e412";
    private static final int TOKEN_LENGTH = 10;

    /**
     * 合法的token都放在这个array里，同时也初始化。
     */
    public static final String[] INIT_UIDS = new String[]{DEFAULT_TEST_UID_TOKEN, ABORN_TOKEN};
    private static final List<String> TEST_UIDS = Arrays.asList(DEFAULT_TEST_UID_TOKEN);
    private static final List<String> VIP_UIDS = Arrays.asList(ABORN_TOKEN);

    public static boolean isLegal(String token) {
        for (String initToken : INIT_UIDS) {
            if (initToken.equals(token)) {
                return true;
            }
        }

        return token != null && token.length() == TOKEN_LENGTH && token.startsWith("0x");
    }

    public static boolean isTest(String token) {
        return TEST_UIDS.contains(token);
    }

    public static boolean validateId(String id) {
        return StringUtils.isNotBlank(id) && (!id.contains(" "));
    }

    public static String generateToken(@NonNull String id) {
        return generateToken(id.toLowerCase(), getSalt(id.toLowerCase()));
    }

    private static String getSalt(String id) {
        if (id.length() == 1) {
            return "e00" + id;
        } else if (id.length() == 2) {
            return "f0" + id;
        } else if (id.length() == 3) {
            return "g" + id;
        } else {
            return id.substring(0, 4);
        }
    }

    public static boolean validateToken(String token, String id) {
        if (StringUtils.isBlank(token) ||
                StringUtils.isBlank(id) ||
                token.length() != TOKEN_LENGTH) {
            return false;
        }

        String tokenGenerate = generateToken(id);
        return tokenGenerate.equals(token);
    }

    /**
     * 根据用户id生成十位的合法token
     * Token分为三部分：第一部分0x
     * 第二部分用户id hash反序4位
     * 第三部分当不足的时间补充
     *
     * @param id   用户id
     * @param salt 当hashstring不足够时，自己补后4位
     * @return
     */
    public static String generateToken(@NonNull String id, @NonNull String salt) {
        String hashValue = Integer.toHexString(id.toLowerCase().hashCode());
        String revs = StringUtils.reverse(hashValue);
        String hashValueSalt = Integer.toHexString(salt.toLowerCase().hashCode());

        if (revs.length() >= 8) {
            return "0x" + revs.substring(0, 8);
        } else {
            int leng = revs.length();
            if (leng + hashValueSalt.length() >= 8) {
                return "0x" + revs + hashValueSalt.substring(0, 8 - leng);
            } else {
                StringBuilder stringBuilder = new StringBuilder(revs + hashValueSalt);
                for (int i = 0; i < 8 - revs.length(); i++) {
                    stringBuilder.append("f");
                }
                return "0x" + stringBuilder.toString();
            }
        }
    }

    public static void main(String[] args) {
        String t1 = "0x8bf8e413";
        String t2 = "0x8bf8e41";
        String t3 = null;
        System.out.println(ABORN_TOKEN + " isLegal:" + isLegal(ABORN_TOKEN));
        System.out.println(t1 + " isLegal:" + isLegal(t1));
        System.out.println(t2 + " isLegal:" + isLegal(t2));
        System.out.println(t3 + " isLegal:" + isLegal(t3));
        System.out.println(DEFAULT_TEST_UID_TOKEN + " isTest:" + isTest(DEFAULT_TEST_UID_TOKEN));

        String id = "aborn";
        String token = generateToken(id);
        System.out.println(id + " token:" + token);
        System.out.println(id + " validate:" + validateToken(token, id));

        for (String item : new String[]{"aborn", "Aborn", "e", "ff", "ggg", "ab-#", "webx"}) {
            System.out.println(item + ":" + generateToken(item));
        }

    }

}
