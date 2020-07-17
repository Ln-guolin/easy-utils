package cn.soilove.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang.RandomStringUtils;

import java.util.*;

/**
 * random工具
 *
 * @author: Chen GuoLin
 * @create: 2020-02-04 10:32
 **/
public class RandomUtils {

    private static final Random random = new Random();

    /**
     * 随机指定获取集合元素
     * @param data
     * @param num
     * @param <T>
     * @return
     */
    public static <T> List<T> randomList(List<T> data,int num){
        List<T> res = Lists.newArrayList();
        while (res.size() < num){
            res.add(data.get(random.nextInt(data.size())));
        }
        return res;
    }

    /**
     * 随机指定获取集合元素 - 不重复
     * @param data
     * @param num
     * @param <T>
     * @return
     */
    public static <T> List<T> randomList4unique(List<T> data,int num){
        Set<Integer> idxs = Sets.newHashSet();
        while (idxs.size() < num){
            idxs.add(random.nextInt(data.size()));
        }
        List<T> res = Lists.newArrayList();
        for(Integer idx : idxs){
            res.add(data.get(idx));
        }
        return res;
    }

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
