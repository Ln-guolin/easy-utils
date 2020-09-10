package cn.soilove.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.text.AttributedString;
import java.util.ArrayList;

/**
 * 图片绘制
 *
 * @author: Chen GuoLin
 * @create: 2020-09-10 11:02
 **/
public class ImageGraphicsDrawUtils {

    /**
     * 画布创建，指定图片画布
     * @param imageUrl 网络图片地址
     * @param hasArcw 是否圆角
     * @param arcw 圆角值，一般设置为80
     * @return
     */
    public static GraphicsCreate createGraphics(String imageUrl,boolean hasArcw, int arcw) throws IOException {
        // 设置背景
        BufferedImage bg = ImageIO.read(new URL(imageUrl));

        // 圆角处理
        if(hasArcw){
            bg = calcImageArcw(bg,arcw);
        }

        BufferedImage canvas = new BufferedImage(bg.getWidth(), bg.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g =  (Graphics2D) canvas.getGraphics();

        // 设置抗锯齿，画背景
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g.drawImage(bg.getScaledInstance(bg.getWidth(), bg.getHeight(), Image.SCALE_DEFAULT), 0, 0, null);

        return new GraphicsCreate(g,canvas);
    }

    /**
     * 画布创建，直接绘制白色画布
     * @param width 宽
     * @param height 高
     * @param hasArcw 是否圆角
     * @param arcw 一般设置为80
     * @return
     * @throws IOException
     */
    public static GraphicsCreate createGraphics(int width, int height, boolean hasArcw, int arcw) {
        // 设置背景
        BufferedImage bg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g4bg = bg.getGraphics();
        g4bg.setColor(new Color(255, 255, 255));
        for (int i = 0; i < bg.getWidth(); i++) {
            for (int j = 0; j < bg.getHeight(); j++) {
                g4bg.drawLine(i, j, bg.getWidth(), bg.getHeight());
            }
        }
        g4bg.dispose();

        // 圆角处理
        if(hasArcw){
            bg = calcImageArcw(bg,arcw);
        }

        BufferedImage canvas = new BufferedImage(bg.getWidth(), bg.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g =  (Graphics2D) canvas.getGraphics();

        // 设置抗锯齿，画背景
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g.drawImage(bg.getScaledInstance(bg.getWidth(), bg.getHeight(), Image.SCALE_DEFAULT), 0, 0, null);

        return new GraphicsCreate(g,canvas);
    }

    /**
     * 图片圆角处理
     * @param midImage
     * @param arcw
     * @return
     */
    public static BufferedImage calcImageArcw(BufferedImage midImage, int arcw) {
        BufferedImage outputImage = new BufferedImage(midImage.getWidth(), midImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = outputImage.createGraphics();
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, midImage.getWidth(), midImage.getHeight(), arcw, arcw));
        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(midImage, 0, 0, null);
        g2.dispose();
        return outputImage;
    }

    /**
     * 绘制圆形图片 到 画布上
     * @param g 画布
     * @param imgUrl 网络图片地址
     * @param widthHeight 图片宽高
     * @param x 位置坐标，x
     * @param y 位置坐标，y
     * @throws IOException
     */
    public static void draw4RoundImage(Graphics2D g, String imgUrl,int widthHeight, int x, int y) throws IOException {

        // 图片
        BufferedImage img = ImageIO.read(new URL(imgUrl));

        // 透明底的图片
        BufferedImage newImg = new BufferedImage(widthHeight, widthHeight, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics = newImg.createGraphics();

        // 抗锯齿处理
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 留一个像素的空白区域，这个很重要，画圆的时候把这个覆盖
        int border = 1;

        // 图片是一个圆型
        Ellipse2D.Double shape = new Ellipse2D.Double(border, border, widthHeight - border * 2, widthHeight - border * 2);
        // 需要保留的区域
        graphics.setClip(shape);
        graphics.drawImage(img, border, border, widthHeight - border * 2, widthHeight - border * 2, null);
        graphics.dispose();

        // 新创建一个graphics，这样画的圆不会有锯齿
        graphics = newImg.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 画笔是4.5个像素，BasicStroke的使用可以查看下面的参考文档
        // 使画笔时基本会像外延伸一定像素，具体可以自己使用的时候测试
        graphics.setStroke(new BasicStroke(2F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        graphics.setColor(Color.WHITE);
        graphics.drawOval(border, border, widthHeight - border * 2, widthHeight - border * 2);
        graphics.dispose();

        // 绘制
        g.drawImage(newImg.getScaledInstance(widthHeight, widthHeight, Image.SCALE_DEFAULT), x, y, null);
    }

    /**
     * 绘制图片 到 画布上
     * @param g 画布
     * @param imgUrl 网络图片地址
     * @param width 图片宽
     * @param height 图片高
     * @param x 位置，x
     * @param y 位置，y
     * @param hasArcw 是否圆角
     * @param arcw 圆角值
     * @throws IOException
     */
    public static void draw4Image(Graphics2D g, String imgUrl,int width, int height, int x, int y, boolean hasArcw, int arcw) throws IOException {

        // 设置背景
        BufferedImage image = ImageIO.read(new URL(imgUrl));
        calcDrawImage(g, width, height, x, y, hasArcw, arcw, image);

    }

    /**
     * 绘制图片 到 画布上
     * @param g 画布
     * @param imgInputStream 图片输入流
     * @param width 图片宽
     * @param height 图片高
     * @param x 位置，x
     * @param y 位置，y
     * @param hasArcw 是否圆角
     * @param arcw 圆角值
     * @throws IOException
     */
    public static void draw4Image(Graphics2D g, InputStream imgInputStream, int width, int height, int x, int y, boolean hasArcw, int arcw) throws IOException {

        // 设置背景
        BufferedImage image = ImageIO.read(imgInputStream);

        // 图片参数设置
        calcImage4Condition(g,image,width,height,x,y,hasArcw,arcw);
    }

    private static void calcImage4Condition(Graphics2D g, BufferedImage image, int width, int height, int x, int y, boolean hasArcw, int arcw){
        // 圆角处理
        if(hasArcw){
            image = calcImageArcw(image,arcw);
        }

        // 绘制到画布上
        g.drawImage(image.getScaledInstance(width, height, Image.SCALE_DEFAULT), x, y, null);
    }

    /**
     * 绘制图片 到 画布上
     * @param g 画布
     * @param file 图片文件
     * @param width 图片宽
     * @param height 图片高
     * @param x 位置，x
     * @param y 位置，y
     * @param hasArcw 是否圆角
     * @param arcw 圆角值
     * @throws IOException
     */
    public static void draw4Image(Graphics2D g, File file, int width, int height, int x, int y, boolean hasArcw, int arcw) throws IOException {

        // 设置背景
        BufferedImage image = ImageIO.read(file);

        // 图片参数设置
        calcImage4Condition(g,image,width,height,x,y,hasArcw,arcw);
    }

    /**
     * 绘制文字 到 画布上 ，自动换行
     *
     * @param g           画布
     * @param color       颜色
     * @param font        字体样式
     * @param text        文字
     * @param lastWidth   最大长度  （多少长度后需要换行）
     * @param x           文字位置坐标 x
     * @param y           文字位置坐标 Y
     * @param yn          每次换行偏移多少pt
     */
    public static void draw4Text(Graphics2D g, Color color, Font font, String text, int lastWidth, int x, int y, int yn) {

        g.setColor(color);
        g.setFont(font);
        FontMetrics fg = g.getFontMetrics(font);
        ArrayList<String> ls = new ArrayList<>(2);
        getListText(fg, text, lastWidth, ls);
        for (int i = 0; i < ls.size(); i++) {
            if (i == 0) {
                g.drawString(ls.get(i), x, y);
            } else {
                g.drawString(ls.get(i), x, y + (yn * i));
            }
        }
    }

    /**
     * 绘制文字 到 画布上
     * @param g
     * @param color
     * @param font
     * @param text
     * @param x
     * @param y
     */
    public static void draw4Text(Graphics2D g, Color color, Font font, String text, int x, int y) {
        g.setColor(color);
        g.setFont(font);
        g.drawString(text, x, y);
    }

    /**
     * 绘制文字 到 画布上 , 带删除线
     * @param g
     * @param color
     * @param font
     * @param text
     * @param x
     * @param y
     */
    public static void draw4TextDelLine(Graphics2D g, Color color, Font font, String text, int x, int y) {
        g.setColor(color);
        AttributedString as = new AttributedString(text);
        as.addAttribute(TextAttribute.FONT, font);
        as.addAttribute(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON, 0, text.length());
        g.drawString(as.getIterator(), x, y);
    }

    /**
     * 生成最终图片
     * @param create
     * @param output
     * @throws IOException
     */
    public static void write(GraphicsCreate create,OutputStream output) throws IOException {
        create.getGraphics2D().dispose();
        ImageIO.write(create.getCanvas(), "png", output);
    }

    /**
     * 生成最终图片
     * @param create
     * @param file
     * @throws IOException
     */
    public static void write(GraphicsCreate create,File file) throws IOException {
        create.getGraphics2D().dispose();
        ImageIO.write(create.getCanvas(), "png", file);
    }

    /**
     * 将字节写入文件
     * @param b
     * @param path
     * @throws Exception
     */
    public static void write(byte[] b,String path) throws Exception{
        FileOutputStream fout = new FileOutputStream(path);
        fout.write(b);
        fout.close();
    }

    /**
     * 递归 切割字符串
     * @param fg
     * @param text
     * @param widthLength
     * @param ls
     */
    private static void getListText(FontMetrics fg, String text, int widthLength, ArrayList<String> ls) {
        String ba = text;
        boolean b = true;
        int i = 1;
        while (b) {
            if (fg.stringWidth(text) > widthLength) {
                text = text.substring(0, text.length() - 1);
                i++;
            } else {
                b = false;
            }
        }
        if (i != 1) {
            ls.add(ba.substring(0, ba.length() - i));
            getListText(fg, ba.substring(ba.length() - i), widthLength, ls);
        } else {
            ls.add(text);
        }
    }

    /**
     * 图片参数设置
     * @param g
     * @param width
     * @param height
     * @param x
     * @param y
     * @param hasArcw
     * @param arcw
     * @param image
     */
    private static void calcDrawImage(Graphics2D g, int width, int height, int x, int y, boolean hasArcw, int arcw, BufferedImage image) {
        // 圆角处理
        if (hasArcw) {
            image = calcImageArcw(image, arcw);
        }

        // 绘制到画布上
        g.drawImage(image.getScaledInstance(width, height, Image.SCALE_DEFAULT), x, y, null);
    }

    public static class GraphicsCreate{
        private Graphics2D graphics2D;
        private BufferedImage canvas;

        public GraphicsCreate() {
        }

        public GraphicsCreate(Graphics2D graphics2D, BufferedImage canvas) {
            this.graphics2D = graphics2D;
            this.canvas = canvas;
        }

        public Graphics2D getGraphics2D() {
            return graphics2D;
        }

        public void setGraphics2D(Graphics2D graphics2D) {
            this.graphics2D = graphics2D;
        }

        public BufferedImage getCanvas() {
            return canvas;
        }

        public void setCanvas(BufferedImage canvas) {
            this.canvas = canvas;
        }
    }
}
