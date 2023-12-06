package top.hugo.generator.utils;

import cn.hutool.core.date.DateUtil;
import lombok.extern.log4j.Log4j2;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.tools.ToolManager;

import java.io.File;

/**
 * @Title: FrontVmsUtils
 * @Description: 前端模板生成工具类
 * @Auther: kuanghua
 * @create 2022-05-17 13:57
 */
@Log4j2
public class GeneratorTempUtils {
    public static final String ToolManagerConfigPath = "velocity-tools.xml";
    public static final String TemplateSaveDir = "D:\\velocity-tmp";
    public static final String MacTemplateSaveDir = "/velocity-tmp";


    public static final String ZipSaveDir = "D:\\velocity-zip";
    public static final String MacZipSaveDir = "/velocity-zip";

    /**
     * 获取velocity context
     *
     * @return velocity Context
     */
    public static Context getVelocityContext() {
        // 加载toolbox
        ToolManager manager = new ToolManager();
        manager.configure(ToolManagerConfigPath);
        return manager.createContext();
    }


    /**
     * 获取velocity模板引擎
     *
     * @param tempName 模板名称
     * @return workDir 工作目录
     */
    public static Template getTemplate(String workDir, String tempName) {
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, workDir);
        ve.init();
        return ve.getTemplate(tempName);
    }


    /**
     * 获取文件导出路径
     *
     * @param exportDir
     * @param dir       -- controller
     * @return
     */
    public static String getExportDir(String exportDir, String dir) {
        String path = getDirByEnv() + File.separator + exportDir + File.separator + dir + File.separator;
        return fileMkdir(path);
    }

    /**
     * 目录不存在则创建
     *
     * @param path
     * @return
     */
    public static String fileMkdir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    /**
     * 将生成文件的目录压缩zip
     *
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

    /**
     * 根据环境返回文件存储目录
     *
     * @return
     */
    public static String getDirByEnv() {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            return TemplateSaveDir;
        } else {
            return MacTemplateSaveDir;
        }
    }


    /**
     * 获取文件导出路径
     *
     * @param exportDir -- 包名
     * @param dir       -- controller
     * @return
     */
    public static String getZipExportDir(String exportDir, String dir) {
        String path = getZipDirByEnv() + File.separator + exportDir + File.separator + dir + File.separator;
        return fileMkdir(path);
    }

    /**
     * 根据环境返回临时压缩目录
     *
     * @return
     */
    public static String getZipDirByEnv() {
        String os = System.getProperty("os.name");
        String format = DateUtil.format(DateUtil.date(), "yyyy-MM-dd_HH-mm-ss");
        String path = "";
        if (os.toLowerCase().startsWith("win")) {
            path = ZipSaveDir + File.separator + format;
        } else {
            path = MacZipSaveDir + File.separator + format;
        }
        return fileMkdir(path);
    }

}
