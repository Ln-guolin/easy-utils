package cn.soilove.utils;

import java.io.File;

/**
 * 系统路径工具
 *
 * @author: Chen GuoLin
 * @create: 2020-03-16 16:08
 **/
public class PathUtils {

    /**
     * 获取项目路径
     * @return
     */
    public static String getProjectPath(){
        return System.getProperty("user.dir") + "/";
    }

    /**
     * 获取项目临时文件路径
     * @return
     */
    public static String getProjectTmpPath(){

        String path = getProjectPath() + "tmp/";

        File file = new File(path);

        // 如果文件夹不存在
        if(!file.exists()) {
            // 创建文件夹
            file.mkdir();
        }
        return path;
    }

}
