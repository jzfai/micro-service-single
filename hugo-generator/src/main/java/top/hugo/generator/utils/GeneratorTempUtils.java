package top.hugo.generator.utils;

import cn.hutool.core.date.DateUtil;
import lombok.extern.log4j.Log4j2;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.tools.ToolManager;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @Title: FrontVmsUtils
 * @Description: 前端模板生成工具类
 * @Auther: kuanghua
 * @create 2022-05-17 13:57
 */
@Log4j2
public class GeneratorTempUtils {
    //velocity tools配置文件路径
    public static final String ToolManagerConfigPath = "velocity-tools.xml";
    public static final String VelocityTemplateDir = "D:\\github\\micro-service-plus\\velocity-tmp-dir\\";
    public static final String VelocityTemplateDirMac = "/deploy/velocity-tmp-dir/";
    public static final String ElementPlusDir = "front-vms" + File.separator + "element-plus";
    public static final String MybatisPlusDir = "back-vms" + File.separator + "mybatis-plus";
    public static final String MybatisPlusMulTbDir = "back-vms" + File.separator + "mybatis-plus-multi";

    //win
    public static final String ExportFileDir = "D:\\temp-dir\\"+ DateUtil.format(DateUtil.date(),"yyyy-MM-dd_HH-mm-ss")+"\\";
    public static final String NeedZipDir = "D:\\temp-dir\\"+ DateUtil.format(DateUtil.date(),"yyyy-MM-dd_HH-mm-ss")+"\\";
    public static final String OutputZipPath = "D:\\temp-dir\\";
    public static final String TmpSaveDir = "D:\\temp-dir\\";


    //mac和Linux
    public static final String MacExportFileDir = "/velocity-tmp/"+DateUtil.format(DateUtil.date(),"yyyy-MM-dd_HH-mm-ss")+"/";
    public static final String MacNeedZipDir = "/velocity-tmp/"+ DateUtil.format(DateUtil.date(),"yyyy-MM-dd_HH-mm-ss")+"/";
    public static final String MacOutputZipPath = "/velocity-tmp/";
    public static final String MacTmpSaveDir = "/velocity-tmp/";

    /**
     * @author 邝华
     * @date 2022-06-10 13:54
     */
    public static String getExportFileDir(String dir) {
        String os = System.getProperty("os.name");
        String path = "";
        if (os.toLowerCase().startsWith("win")) {
            path = ExportFileDir + dir + File.separator;
        } else {
            path = MacExportFileDir + dir + File.separator;
        }
        return fileMkdir(path);
    }

    public static String getExportMybatisPlusMulDir(String dir) {
        String os = System.getProperty("os.name");
        String path = "";
        if (os.toLowerCase().startsWith("win")) {
            path = ExportFileDir + "mybatis-plus-mul" + File.separator + dir + File.separator;
        } else {
            path = MacExportFileDir + "mybatis-plus-mul" + File.separator + dir + File.separator;
        }
        return fileMkdir(path);
    }

    public static String getExportMybatisPlusMulQueryDir(String dir) {
        String os = System.getProperty("os.name");
        String path = "";
        if (os.toLowerCase().startsWith("win")) {
            path = ExportFileDir + "mybatis-plus-multi-query" + File.separator + dir + File.separator;
        } else {
            path = MacExportFileDir + "mybatis-plus-multi-query" + File.separator + dir + File.separator;
        }
        return fileMkdir(path);
    }


    public static String getExportMybatisPlusDir(String exportDir, String dir) {
        String os = System.getProperty("os.name");
        String path = "";
        if (os.toLowerCase().startsWith("win")) {
            path = ExportFileDir + exportDir + File.separator + dir + File.separator;
        } else {
            path = MacExportFileDir + exportDir + File.separator + dir + File.separator;
        }
        return fileMkdir(path);
    }

    public static String getExportElementPlusDir(String dir) {
        String os = System.getProperty("os.name");
        String path = "";
        if (os.toLowerCase().startsWith("win")) {
            path = ExportFileDir + "element-plus" + File.separator + dir + File.separator;
        } else {
            path = MacExportFileDir + "element-plus" + File.separator + dir + File.separator;
        }
        return fileMkdir(path);
    }

    /**
     * @return
     * @author 邝华
     * @date 2022-06-26 13:54
     */
    public static String getExportFileDir() {
        String os = System.getProperty("os.name");
        String path = "";
        if (os.toLowerCase().startsWith("win")) {
            path = ExportFileDir;
        } else {
            path = MacExportFileDir;
        }
        return fileMkdir(path);
    }

    /**
     * @return
     * @author 邝华
     * @date 2022-06-10 13:53
     */
    public static String getNeedZipDir() {
        String os = System.getProperty("os.name");
        String path = "";
        if (os.toLowerCase().startsWith("win")) {
            path = NeedZipDir;
        } else {
            path = MacNeedZipDir;
        }
        return fileMkdir(path);
    }

    /**
     * 获取临时存储目录路径
     *
     * @return
     * @author 邝华
     * @date 2022-06-10 13:54
     */
    public static String getTmpSaveDir() {
        String os = System.getProperty("os.name");
        String path = "";
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        if (os.toLowerCase().startsWith("win")) {
            path = TmpSaveDir + uuid + File.separator;
        } else {
            path = MacTmpSaveDir + uuid + File.separator;
        }
        return fileMkdir(path);
    }


    /**
     * @return
     * @author 邝华
     * @date 2022-06-10 13:53
     */
    public static String getOutputZipPath() {
        String os = System.getProperty("os.name");
        String path = "";
        if (os.toLowerCase().startsWith("win")) {
            path = OutputZipPath;
        } else {
            path = MacOutputZipPath;
        }
        return fileMkdir(path);
    }

    /**
     * @return
     * @author 邝华
     * @date 2022-06-10 13:56
     */
    public static String fileMkdir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }


    /**
     * @return
     * @author 邝华
     * @date 2022-06-10 13:56
     */
    public static String getTemplateDir(String dirPath) {
        String os = System.getProperty("os.name");
        String path = "";
        if (os.toLowerCase().startsWith("win")) {
            path = VelocityTemplateDir + dirPath;
        } else {
            path = VelocityTemplateDirMac + dirPath;
        }
        return fileMkdir(path);
    }


    /**
     * @return velocity Context
     */
    public static Context getVelocityContext() {
        // 加载toolbox
        ToolManager manager = new ToolManager();
        manager.configure(ToolManagerConfigPath);
        return manager.createContext();
    }


    /**
     * @param tempName 模板名称
     * @return Template
     */
    public static Template getMybatisPlusTemp(String tempName) {
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, getTemplateDir(MybatisPlusDir));
        ve.init();
        return ve.getTemplate(tempName);
    }

    /**
     * @param tempName 模板名称
     * @return workDir 工作目录
     */
    public static Template getTmpSaveDirTemp(String workDir, String tempName) {
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, workDir);
        ve.init();
        return ve.getTemplate(tempName);
    }

    /**
     * @param tempName 模板名称
     * @return Template
     */
    public static Template getMybatisPlusMulTbTemp(String tempName) {
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, getTemplateDir(MybatisPlusMulTbDir));
        ve.init();
        return ve.getTemplate(tempName);
    }


    /**
     * @param tempName 模板名称
     * @return Template
     */
    public static Template getElementPlusTemp(String tempName) {
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, getTemplateDir(ElementPlusDir));
        ve.init();
        return ve.getTemplate(tempName);
    }

    /**
     * @param filePath 相对于resource的路径
     * @return 读取的文件内容
     * @throws IOException
     */
    public static String readFileToString(String filePath) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(filePath));
//        ClassPathResource classPathResource = new ClassPathResource(filePath);
//        InputStream inputStream = classPathResource.getInputStream();
        String s = StreamUtils.copyToString(fileInputStream, StandardCharsets.UTF_8);
        fileInputStream.close();
        return s;
    }

    /**
     * @param outputZipPath 导出zip包的路径 如  file://xxx.zip
     * @param needZipDir    需要压缩的目录名字 如 file://doc
     */
    public static void createZipFile(String outputZipPath, String needZipDir) {
        try {
            new ZipFile(outputZipPath).addFolder(new File(needZipDir));
        } catch (ZipException e) {
            throw new RuntimeException("压缩文件夹报错" + e);
        }
    }
}
