package cn.soilove.utils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

/**
 * 文字头像工具
 *
 * @author: Chen GuoLin
 * @create: 2020-10-14 08:26
 **/
public class AvatarUtils {

    private static final int width_height = 80;
    private static final Random random = new Random();

    /**
     * 生成文字头像
     * @param nickName
     * @param imageLocalPath
     * @throws IOException
     */
    public static String create(String nickName, String imageLocalPath) throws IOException {
        // 获取绘制文本
        String txt = getDrawTxt(nickName);

        // 创建背景图
        ImageGraphicsDrawUtils.GraphicsCreate create = ImageGraphicsDrawUtils.createGraphics(
                width_height,width_height,true,90,getRandomColor());

        // 绘制文本到背景图上
        ImageGraphicsDrawUtils.draw4Text(create,
                Color.WHITE,new Font("宋体", Font.BOLD, 30),txt,0,50,true);

        String filename = imageLocalPath + File.separator + UUID.randomUUID() + ".png";
        File file = new File(filename);
        ImageGraphicsDrawUtils.write(create,file);

        return file.getPath();
    }

    /**
     * 获得随机颜色
     * @return
     */
    private static Color getRandomColor() {
        return new Color(random.nextInt(255) + 1, random.nextInt(255) + 1, random.nextInt(255) + 1);
    }

    /**
     * 获取绘制文本
     * @param nickName
     * @return
     */
    private static String getDrawTxt(String nickName){
        // 长度小于等于2的时候，全部显示
        if (nickName.length() <= 2) {
            return nickName;
        }
        // 长度大于2的时候，如果首个字符是字母或数字，显示首个字符
        if(RegexUtils.isLetterAndNumber(nickName.substring(0,1))){
            return nickName.substring(0,1).toUpperCase();
        }
        // 其他情况显示末尾2个字符
        return nickName.substring(nickName.length() - 2);
    }

    /**
     * @throws IOException
     * @throws
     **/
    public static void main(String[] args) throws IOException {
        create("张三", "/Users/chenguolin/Downloads");
        create("y张三", "/Users/chenguolin/Downloads");
        create("张三d", "/Users/chenguolin/Downloads");
        create("Dssdsd", "/Users/chenguolin/Downloads");
    }
}



