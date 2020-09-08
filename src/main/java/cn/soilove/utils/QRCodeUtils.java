package cn.soilove.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;

/**
 * 二维码工具
 *
 * @author: Chen GuoLin
 * @create: 2020-09-08 21:59
 **/
public class QRCodeUtils {

    /**
     * 二维码的宽度
     */
    private static final int WIDTH = 300;
    /**
     * 二维码的高度
     */
    private static final int HEIGHT = 300;
    /**
     * 二维码的格式
     */
    private static final String FORMAT = "png";

    /**
     * 生成二维码 - 生成到本地路径
     */
    public static void writeToPath(String text,String path) throws WriterException, IOException {
        // 开始生成二维码
        BitMatrix bitMatrix = write(text);
        // 导出到本地路径
        MatrixToImageWriter.writeToPath(bitMatrix, FORMAT, new File(path).toPath());
    }

    /**
     * 生成二维码 - 生成到输出流
     */
    public static void writeToStream(String text, OutputStream stream) throws IOException, WriterException {
        // 开始生成二维码
        BitMatrix bitMatrix = write(text);
        // 导出到输出流
        MatrixToImageWriter.writeToStream(bitMatrix, FORMAT, stream);
    }

    /**
     * 生成二维码 - 生成到输出流
     */
    public static BufferedImage writeToBufferedImage(String text) throws WriterException {
        // 开始生成二维码
        BitMatrix bitMatrix = write(text);
        // 生成BufferedImage
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    /**
     * 读取二维码 - 本地文件
     * @param path
     * @return
     * @throws IOException
     * @throws NotFoundException
     */
    public static String read4Path(String path) throws IOException, NotFoundException {
        // 读取二维码为图片
        BufferedImage bufferedImage = ImageIO.read(new File(path));
        return read(bufferedImage);
    }

    /**
     * 读取二维码 - 文件流
     * @param input
     * @return
     * @throws IOException
     * @throws NotFoundException
     */
    public static String read4Stream(InputStream input) throws IOException, NotFoundException {
        // 读取二维码为图片
        BufferedImage bufferedImage = ImageIO.read(input);
        return read(bufferedImage);
    }

    /**
     * 读取二维码 - 网络图片
     * @param url
     * @return
     * @throws IOException
     * @throws NotFoundException
     */
    public static String read4Url(String url) throws IOException, NotFoundException {
        // 读取二维码为图片
        BufferedImage bufferedImage = ImageIO.read(new URL(url));
        return read(bufferedImage);
    }

    /**
     * 读取二维码
     * @param bufferedImage
     * @return
     * @throws NotFoundException
     */
    private static String read(BufferedImage bufferedImage) throws NotFoundException {
        // 获取二维码的结果
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));
        // 定义二维码的参数
        HashMap hashMap = new HashMap();
        // 设置二维码字符编码
        hashMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        // 对图像进行解码
        Result result = new MultiFormatReader().decode(binaryBitmap, hashMap);
        return result.getText();
    }

    /**
     * 生成二维码
     */
    private static BitMatrix write(String text) throws WriterException {
        // 定义二维码的参数
        HashMap hashMap = new HashMap();
        // 设置二维码字符编码
        hashMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        // 设置二维码纠错等级
        hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        // 设置二维码边距
        hashMap.put(EncodeHintType.MARGIN, 2);

        // 开始生成二维码
        return new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hashMap);
    }

    public static void main(String[] args) throws IOException, WriterException, NotFoundException {
        writeToPath("http://32e.co/a.html","/Users/chenguolin/Downloads/b.png");

        System.out.println(read4Path("/Users/chenguolin/Downloads/b.png"));
    }
}
