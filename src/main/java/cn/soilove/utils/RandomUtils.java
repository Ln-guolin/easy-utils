package cn.soilove.utils;

import org.apache.commons.lang.RandomStringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * random工具
 *
 * @author: Chen GuoLin
 * @create: 2020-02-04 10:32
 **/
public class RandomUtils {

    /**
     * 随机定长字符
     * @param length
     * @param text
     * @return
     */
    public static String randomStr(int length,String text) {
        return RandomStringUtils.random(length,text);
    }

    /**
     * 随机定长字符 - 不重复
     * @param length
     * @param text
     * @return
     */
    public static String randomStr4unique(int length,String text) {
        char[] ars = text.toCharArray();
        List<String> list = new ArrayList<>();
        for(int i = 0; i < text.length(); i ++) {
            list.add(text.charAt(i) + "");
        }
        Collections.shuffle(list);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(list.get(i));
        }
        return sb.toString();
    }


    public static void main(String[] args) {
        for (int i = 0 ; i < 20 ; i++){
            System.out.println(randomStr4unique(4,"1234567890"));
        }
    }


}
