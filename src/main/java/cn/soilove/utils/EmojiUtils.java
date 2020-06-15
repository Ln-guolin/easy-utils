package cn.soilove.utils;

import com.vdurmont.emoji.EmojiParser;

/**
 * emoji表情处理
 *
 * @author: Chen GuoLin
 * @create: 2020-05-21 16:58
 **/
public class EmojiUtils {

    /**
     * 表情转字符
     * @param emoji
     * @return
     */
    public static String parseToText(String emoji){
        return EmojiParser.parseToAliases(emoji);
    }

    /**
     * 字符转表情
     * @param text
     * @return
     */
    public static String parseToEmoji(String text){
        return EmojiParser.parseToUnicode(text);
    }

}
