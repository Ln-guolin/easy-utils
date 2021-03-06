package cn.soilove.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 日志工具
 *
 * @author chenguolin
 * @createTime 2017-08-08 20:19
 **/
public class LogUtils {

    /**
     * 获取具体的堆栈信息
     * @param e
     * @return
     */
    public static String getStackTraceAsString(Exception e) {
        try {
            // StringWriter将包含堆栈信息
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            // 获取堆栈信息
            e.printStackTrace(printWriter);
            // 转换成String，并返回该String
            StringBuffer error = stringWriter.getBuffer();
            return error.toString();
        } catch (Exception e2) {
            return "获取堆栈信息异常";
        }
    }
}
