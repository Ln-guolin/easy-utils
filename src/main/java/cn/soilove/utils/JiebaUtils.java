package cn.soilove.utils;

import com.huaban.analysis.jieba.JiebaSegmenter;

import java.util.List;

/**
 * 结巴分词
 *
 * @author: Chen GuoLin
 * @create: 2020-05-21 16:58
 **/
public class JiebaUtils {

    /**
     * 执行分词
     * @param sentence
     * @return
     */
    public static List<String> process(String sentence){
        return new JiebaSegmenter().sentenceProcess(sentence);
    }

}
