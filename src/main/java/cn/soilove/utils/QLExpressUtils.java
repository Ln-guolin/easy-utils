package cn.soilove.utils;

import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 /**
 * ql工具
 * <pre>
 *    支持 +,-,*,/,<,>,<=,>=,==,!=,<>【等同于!=】,%,mod【取模等同于%】,++,--,
 *    支持 &&,||,!,min,max,round,print,println,like,in等操作符
 *    支持 for，break、continue、if then else 等标准的程序控制逻辑
 * </pre>
 * @author: Chen GuoLin
 * @create: 2019-02-25 17:27
 **/
@Slf4j
public class QLExpressUtils {

    /**
     * 定义ExpressRunner
     * aIsPrecise 是否需要高精度计算支持
     * aIstrace 是否跟踪执行指令的过程
     */
    private static final ExpressRunner expressRunner = new ExpressRunner(true,false);
    private static final ExpressRunner expressRunner4NoPrecise = new ExpressRunner(false,false);

    /**
     * 表达式执行 - 默认高精度
     * @param express 表达式，如：3*8
     * @return
     */
    public static Object execute(String express){
        try{
            // 表达式执行
            Object r = expressRunner.execute(express, null, null, true, false);
            log.info("[QL]表达式执行！express="+ express + "，执行结果=" + r.toString());
            return r;
        }catch (Exception e){
            log.error("[QL]表达式执行异常！express=" + express,e);
            return null;
        }
    }

    /**
     * 表达式执行 - 损失精度
     * @param express 表达式，如：3*8
     * @return
     */
    public static Object execute4NoPrecise(String express){
        try{
            // 表达式执行
            Object r = expressRunner4NoPrecise.execute(express, null, null, true, false);
            log.info("[QL]表达式执行！express="+ express + "，执行结果=" + r.toString());
            return r;
        }catch (Exception e){
            log.error("[QL]表达式执行异常！express=" + express,e);
            return null;
        }
    }

    /**
     * 表达式执行，提供元数据 - 默认高精度
     * @param express 表达式，如：语文+10
     * @param matedata 元数据，如：Map<"语文", 99>
     * @return
     */
    public static Object execute(String express, Map<String, Object> matedata){
        try{
            // 数据源加载
            DefaultContext<String, Object> context = new DefaultContext<>();
            matedata.forEach(context::put);

            // 表达式执行
            Object r = expressRunner.execute(express, context, null, true, false);
            log.info("[QL]表达式执行！express="+ express + "，执行结果=" + r.toString() + "，content=" + context.toString());
            return r;
        }catch (Exception e){
            log.error("[QL]表达式执行异常！express=" + express,e);
            return null;
        }
    }

    /**
     * 表达式执行，提供元数据和macro集合 - 默认高精度
     * @param express 表达式，如："平均成绩+10"
     * @param matedata 元数据，如：Map<"语文", 99>
     * @param macro Macro表达式，如：Map<"平均成绩", "(语文+数学)/2">
     * @return
     */
    public static Object execute(String express, Map<String, Object> matedata, Map<String, String> macro){
        return execute(express,matedata,macro,null);
    }

    /**
     * 表达式执行，提供元数据和macro集合、别名 - 默认高精度
     * @param express 表达式，如："平均成绩"
     * @param matedata 元数据，如：Map<"语文", 99>
     * @param macro Macro表达式，如：Map<"平均成绩", "(语文+数学)/2">
     * @param alias 别名，如：Map<"如果", "if">
     * @return
     */
    public static Object execute(String express, Map<String, Object> matedata, Map<String, String> macro,Map<String, String> alias){
        try{

            // 这里用本地新建的new ExpressRunner，因为macro不可重复
            ExpressRunner expressRunner = new ExpressRunner(true,false);

            // 数据源加载
            DefaultContext<String, Object> context = new DefaultContext<>();
            matedata.forEach(context::put);

            // Macro表达式加载
            for(String key : macro.keySet()){
                expressRunner.addMacro(key, macro.get(key));
            }

            // 别名替换
            if(alias != null && alias.size() > 0){
                for(String key : alias.keySet()){
                    expressRunner.addOperatorWithAlias(key, alias.get(key),null);
                }
            }

            // 表达式执行
            Object r = expressRunner.execute(express, context, null, true, false);
            log.info("[QL]表达式执行！express="+ express + "，执行结果=" + r.toString() + "，content=" + context.toString()+ "，macro=" + macro.toString());
            return r;
        }catch (Exception e){
            log.error("[QL]表达式执行异常！express=" + express,e);
            return null;
        }
    }

    /**
     * Macro表达式执行，提供元数据和macro集合 - 默认高精度
     * @param matedata 元数据，如：Map<"语文", 99>
     * @param macro Macro表达式，如：Map<"平均成绩", "(语文+数学)/2">
     * @return
     */
    public static Map<String, Object> execute4macro(Map<String, Object> matedata, Map<String, String> macro){
        return execute4macro(matedata,macro,null);
    }

    /**
     * Macro表达式执行，提供元数据和macro集合 - 默认高精度
     * @param matedata 元数据，如：Map<"语文", 99>
     * @param macro Macro表达式，如：Map<"平均成绩", "(语文+数学)/2">
     * @param alias 别名，如：Map<"如果", "if">
     * @return macro的所有结果集
     */
    public static Map<String, Object> execute4macro( Map<String, Object> matedata, Map<String, String> macro,Map<String, String> alias){
        try{

            // 这里用本地新建的new ExpressRunner，因为macro不可重复
            ExpressRunner expressRunner = new ExpressRunner(true,false);

            // 数据源加载
            DefaultContext<String, Object> context = new DefaultContext<>();
            matedata.forEach(context::put);

            // Macro表达式加载
            for(String key : macro.keySet()){
                expressRunner.addMacro(key, macro.get(key));
            }

            // 别名替换
            if(alias != null && alias.size() > 0){
                for(String key : alias.keySet()){
                    expressRunner.addOperatorWithAlias(key, alias.get(key),null);
                }
            }

            // 表达式执行
            Map<String,Object> result = new HashMap<>();
            for(String key : macro.keySet()){
                Object r = expressRunner.execute(key, context, null, true, false);
                result.put(key,r);
                log.info("[QL]表达式执行！key="+ key + "，公式：" + macro.get(key) + "，执行结果=" + r.toString());
            }
            return result;
        }catch (Exception e){
            log.error("[QL]表达式执行异常",e);
            return null;
        }
    }


    public static void main(String[] args) {
        try{
            // 测试表达式写循环
            List<Integer> list = Arrays.asList(1,13,4,5,6,9);
            // 排序
            Collections.sort(list);
            Map<String, Object> context = new HashMap<>();
            context.put("P1集合",list);
            context.put("档位值",7);
            String express = "num = 0; for(int i=0;i<P1集合.size();i++){ if(档位值 >= P1集合.get(i)) { num = P1集合.get(i); }} return num;";
            Object r = QLExpressUtils.execute(express, context);
            System.out.println(r);
        }catch (Exception e){
            log.error("[QL]表达式执行异常!" ,e);
        }
    }

}
