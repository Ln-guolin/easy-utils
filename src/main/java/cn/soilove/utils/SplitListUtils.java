package cn.soilove.utils;


import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 拆分数组
 *
 * @author: Chen GuoLin
 * @create: 2019-11-30 16:32
 **/
public class SplitListUtils {

    /**
     * 拆分集合
     * @param origin 集合
     * @param size 每页几条
     * @param <T>
     * @return
     */
    public static  <T> List<List<T>> split(List<T> origin , int size){
        if(CollectionUtils.isEmpty(origin)){
            return Collections.emptyList();
        }

        int block = (origin.size() + size -1) / size;
        return IntStream.range(0, block).boxed().map(i -> {
            int start = i * size;
            int end = Math.min(start + size,origin.size());
            return origin.subList(start,end);
        }).collect(Collectors.toList());
    }
}
