package cn.soilove.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.event.SyncReadListener;
import com.alibaba.excel.write.metadata.WriteSheet;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

/**
 * excel工具
 *  <pre>
 *      注解描述：
 *      类：
 *          表格列宽：@ColumnWidth(25)
 *          表头行高：@HeadRowHeight(20)
 *          内容行高：@ContentRowHeight(10)
 *      字段：
 *          列名：@ExcelProperty("列名")
 *          列宽：@ColumnWidth(50)
 *          忽略字段：@ExcelIgnore
 *          时间格式：@DateTimeFormat("yyyy年MM月dd日 HH时mm分ss秒")
 *  </pre>
 * @author: Chen GuoLin
 * @create: 2020-02-02 17:28
 **/
@Slf4j
public class ExcelUtils {

    /**
     * Excel写入本地
     * @param fileName 文件名称
     * @param sheetName sheet表名称
     * @param head 表头
     * @param data 数据
     * @return Excel文件本地路径
     */
    public static String write(String fileName,String sheetName,Class head,List data){
        log.info("[excel][write]开始文件写入...fileName="+fileName);
        String filePath = PathUtils.getProjectTmpPath() + fileName + "-" + System.currentTimeMillis() + ".xlsx";
        EasyExcel.write(filePath)
                .head(head)
                .sheet(sheetName)
                .doWrite(data);
        log.info("[excel][write]文件写入完成！路径=" + filePath);
        return filePath;
    }

    /**
     * Excel写入本地 - 多表
     * <pre>
     *     datas数据描述： Map<sheet名，Map<表头，数据>>
     * </pre>
     * @param fileName 文件名称
     * @param datas Map<String, Map<Class,List>> datas = Maps.newLinkedHashMap(); 保障sheet顺序
     */
    public static String write(String fileName, Map<String, Map<Class,List>> datas){
        log.info("[excel][write]开始文件写入并输出流...fileName="+fileName);
        String filePath = PathUtils.getProjectTmpPath() + fileName  + "-" +  System.currentTimeMillis() + ".xlsx";
        try {
            ExcelWriter excelWriter = EasyExcel.write(filePath).build();
            // excel writer操作
            excelWriterManyCalc(excelWriter,datas);
            log.info("[excel][write]文件写入和输出完成！fileName="+fileName);
            return filePath;
        } catch (Exception e) {
            log.error("[excel][write]文件写入和输出异常，EasyExcel.write 报错！fileName="+fileName,e);
            return null;
        }
    }

    /**
     * Excel写入输出流
     * <pre>
     *  Controller下载示例：
     *
     *     @PostMapping(value = "/download")
     *     public void download(HttpServletResponse response){
     *          // 获取数据
     *          List<Test> data = queryDatas();
     *          // 设置头部信息
     *          responseCalc(response,"文件名");
     *          // 输出流
     *          ExcelUtils.write(response.getOutputStream(),"Sheet表名", Test.class,data);
     *      }
     * </pre>
     * @param sheetName
     * @param head
     * @param data
     */
    public static void write(OutputStream outputStream,  String sheetName, Class head, List data){
        log.info("[excel][write]开始文件写入并输出流...sheetName=" + sheetName);
        try {
            EasyExcel.write(outputStream)
                    .head(head)
                    .sheet(sheetName)
                    .doWrite(data);
            log.info("[excel][write]文件写入和输出完成！sheetName=" + sheetName);
        } catch (Exception e) {
            log.error("[excel][write]文件写入和输出异常，EasyExcel.write 报错！",e);
            return;
        }
    }

    /**
     * Excel写入输出流 - 多表
     * <pre>
     *  Controller下载示例：
     *
     *     @PostMapping(value = "/download")
     *     public void download(HttpServletResponse response){
     *          // 获取数据
     *          Map<String, Map<Class,List>> datas = queryDatas();
     *          // 设置头部信息
     *          responseCalc(response,"文件名");
     *          // 输出流
     *          ExcelUtils.write(response.getOutputStream(),datas);
     *      }
     * </pre>
     * @param datas Map<String, Map<Class,List>> datas = Maps.newLinkedHashMap(); 保障sheet顺序
     */
    public static void write(OutputStream outputStream, Map<String, Map<Class,List>> datas){
        log.info("[excel][write]开始文件写入并输出流...");
        try {

            ExcelWriter excelWriter = EasyExcel.write(outputStream).build();
            // excel writer操作
            excelWriterManyCalc(excelWriter,datas);
            log.info("[excel][write]文件写入和输出完成！");
        } catch (Exception e) {
            log.error("[excel][write]文件写入和输出异常，EasyExcel.write 报错！",e);
            return;
        }
    }

    /**
     * 加载下载文件头部信息
     * @param response
     * @param fileName
     */
    public static void buildDownloadResponse(HttpServletResponse response, String fileName){
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        try {
            // 这里URLEncoder.encode可以防止中文乱码
            fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            log.error("[excel][write][buildDownloadResponse]文件输出异常，URLEncoder.encode 报错！",e);
        }
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
    }


    /**
     * 上传Excel
     * @param inputStream MultipartFile.getInputStream()
     * @param head
     * @return
     */
    public static List read(InputStream inputStream, Class head){
        try {
            log.info("[excel][read]开始解析文件..head=" + head.getName());
            SyncReadListener listener = new SyncReadListener();
            EasyExcel.read(inputStream, head, listener).sheet().doRead();
            log.info("[excel][read]解析文件完成，head=" + head.getName() + "，一共解析行数：" + listener.getList().size());
            return listener.getList();
        } catch (Exception e) {
            log.error("[excel][read]文件解析异常！",e);
            return null;
        }
    }

    /**
     * 封装sheet表map对象，多excel下载会用到
     * @param classz
     * @param list
     * @return Map<表头，数据>
     */
    public static  Map<Class,List> sheetMap(Class classz,List list){
        Map<Class,List> data = new HashMap<>();
        data.put(classz,list);
        return data;
    }

    /**
     * ExcelWriter对象处理 - 多表
     * @param excelWriter
     * @param datas
     */
    private static void excelWriterManyCalc(ExcelWriter excelWriter, Map<String, Map<Class,List>> datas){
        // 定义sheet表下标
        int i = 0;
        // 遍历数据
        for(String sheetName : datas.keySet()){
            // sheet表
            Map<Class,List> sheet = datas.get(sheetName);
            for(Class head : sheet.keySet()){
                // sheet表和数据写入
                WriteSheet writeSheet = EasyExcel.writerSheet(i, sheetName).head(head).build();
                excelWriter.write(sheet.get(head), writeSheet);
                // 自增sheet表
                i ++;
            }
        }
        excelWriter.finish();
    }


}
