package com.hk.commons.util;

import java.util.regex.Pattern;

/**
 * 校验工具类
 *
 * @author kevin
 * @date 2018年3月5日上午8:56:03
 */
public abstract class ValidateUtils {

    /**
     * 身份证校验 pattern
     */
    private static final Pattern ID_CARD_PATTERN = Pattern
            .compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0-2]\\d)|(3[0-1]))\\d{3}[\\dX]$");

    private static final int[] ID_CARD_WEIGHT = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

    private static final char[] ID_CARD_VALIDATE_CODE = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};

    /**
     * 邮箱号校验 pattern
     */
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w-]+@[\\w-]+\\.[\\w]+$");

    /**
     * 手机号校验 Pattern
     */
    private static final Pattern PHONE_PATTERN = Pattern.compile("^(1[3-9][0-9])\\d{8}$");

    /**
     * is id Card,必须是合法的身份证
     *
     * @param idCard idCard
     * @return true or false
     */
    public static boolean isIDCard(CharSequence idCard) {
        var matcher = ID_CARD_PATTERN.matcher(idCard);
        if (matcher.find()) {
            var sum = 0;
            for (int i = 0, size = idCard.length() - 1; i < size; i++) {
                sum += Integer.parseInt(String.valueOf(idCard.charAt(i))) * ID_CARD_WEIGHT[i];
            }
            var mod = sum % 11;
            return idCard.charAt(idCard.length() - 1) == ID_CARD_VALIDATE_CODE[mod];
        }
        return false;
    }

    /**
     * is email
     *
     * @param args args
     * @return true or false
     */
    public static boolean isEmail(String args) {
        return StringUtils.isNotEmpty(args) && EMAIL_PATTERN.matcher(args).find();
    }

    /**
     * is phone
     *
     * @param args args
     * @return true or false
     */
    public static boolean isMobilePhone(CharSequence args) {
        return StringUtils.isNotEmpty(args) && PHONE_PATTERN.matcher(args).find();
    }
}
