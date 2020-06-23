package cn.soilove.utils;


import org.apache.commons.collections4.CollectionUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 拆分工具
 *
 * @author: Chen GuoLin
 * @create: 2019-11-30 16:32
 **/
public class SplitUtils {

    /**
     * 拆分集合
     * @param origin 集合
     * @param size 每页几条
     * @param <T>
     * @return
     */
    public static  <T> List<List<T>> splitList(List<T> origin , int size){
        if(CollectionUtils.isEmpty(origin)){
            return Collections.emptyList();
        }

        int block = (origin.size() + size - 1) / size;
        return IntStream.range(0, block).boxed().map(i -> {
            int start = i * size;
            int end = Math.min(start + size,origin.size());
            return origin.subList(start,end);
        }).collect(Collectors.toList());
    }

    /**
     * 数字随机拆分-类似发红包
     * @param allNum  被拆分的数字
     * @param split 拆成几个
     * @return 拆分后的集合
     */
    private static List<Integer> splitNum(int allNum, int split){

        if(allNum < split || allNum < 1){
            throw new IllegalArgumentException("被拆分的总数小于需要拆分的份数");
        }
        // 创建一个长度等于split的数组
        Integer[] array = new Integer[split];
        // 先给数组的每个元素赋值最基础的值：1
        Arrays.fill(array,1);
        // 减掉分配的数额
        allNum -= split;

        // while遍历总额，随机进行分配，逐个元素进行累计赋值
        int i = 0;
        Random random = new Random();
        while (allNum > 0){
            // 最后一个直接赋值给第一个
            if(allNum == 1){
                array[0] += 1;
                allNum --;
            }
            // 随机赋值
            else{
                int boud = (allNum / 2) + 1;
                boud = boud > 2 ? boud / 2 : boud;
                int num = random.nextInt(boud) + 1;
                array[i++ % split] += num;
                allNum -= num;
            }
        }
        List<Integer> res = Arrays.stream(array).collect(Collectors.toList());
        // 打乱
        Collections.shuffle(res);
        return res;
    }
}
