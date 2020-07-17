package cn.soilove.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * map处理
 */
public class KeyMapUtils {

    private static final String SPLIT_BUF = ":";
    private static final String SPLIT_BREAK = ",";

    /**
     * 字符转map
     * @param str 如：颜色:红色,尺寸:XL
     * @return
     */
    public static Map<String, String> parse2Map(String str,String splitBuf,String splitBreak) {
        if (StringUtils.isBlank(str)) {
            return new HashMap<>();
        }

        String[] kvs = str.split(splitBreak);
        Map<String, String> keyMap = new HashMap<>();
        for (String kv : kvs){
            String[] kvSplit = kv.split(splitBuf);
            String key = kvSplit[0];
            String value = kvSplit[1];
            keyMap.put(key,value);
        }
        return keyMap;
    }

    /**
     * 字符转map
     * @param str 如：颜色:红色,尺寸:XL
     * @return
     */
    public static Map<String, String> parse2Map(String str) {
        return parse2Map(str,SPLIT_BUF,SPLIT_BREAK);
    }

    /**
     * map转字符
     * @param map 如：颜色:红色,尺寸:XL
     * @return
     */
    public static String parse2Str(Map<String, String> map,String splitBuf,String splitBreak) {
        if (map == null || map.size() <= 0) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        map.forEach((k,v) -> {
            if(sb.length() > 0){
                sb.append(splitBreak);
            }
            sb.append(k).append(splitBuf).append(v);
        });
        return sb.toString();
    }

    /**
     * map转字符
     * @param map 如：颜色:红色,尺寸:XL
     * @return
     */
    public static String parse2Str(Map<String, String> map) {
        return parse2Str(map,SPLIT_BUF,SPLIT_BREAK);
    }

    public static void main(String[] args) {
        String t = "颜色:红色,尺寸:XL";
        Map<String, String> map = parse2Map(t);
        System.out.println(map);
        String str = parse2Str(map);
        System.out.println(str);
        System.out.println(parse2Str(map,"=",";"));
    }
}
