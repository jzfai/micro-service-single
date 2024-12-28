package top.hugo.generator.utils;

import lombok.extern.log4j.Log4j2;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * @Title: FileSelfUtils
 * @Description:
 * @Auther: kuanghua
 * @create 2022/12/10 10:57
 */
@Log4j2
public class FileSelfUtils {
    /**
     * 将包名转换成目录
     */
    public static String changeDiagToDir(String pathString){
        return  pathString.replace("-",File.separator);
    }


    public static String changeDotToDir(String pathString){
        return  pathString.replace(".",File.separator);
    }

    //复制文件到目标目录下
    public static void copyFileToDir(String sourceFile,String targetString){
        // 源文件路径
        Path source = Paths.get(sourceFile);
        // 目标文件夹路径
        Path targetDir = Paths.get(targetString);

        // 检查目标文件夹是否存在，如果不存在则创建它
        if (!Files.exists(targetDir)) {
            try {
                Files.createDirectories(targetDir);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        // 构建目标文件路径（源文件名不变，只是改变其所在的目录）
        Path target = targetDir.resolve(source.getFileName());
        try {
            // 复制文件
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("文件复制成功!");
        } catch (IOException e) {
            throw new RuntimeException(e+"文件复制失败");
        }
    }



    /*复制文件目录到另一个目录*/
    public static   void copyDirectory(File source, File dest) {
        if (!dest.exists()) {
            dest.mkdirs();
        }
        if (source.isDirectory()) {
            String[] children = source.list();
            for (int i = 0; i < children.length; i++) {
                copyDirectory(new File(source, children[i]), new File(dest, children[i]));
            }
        } else {
            try {
                Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException(e+"文件复制报错");
            }
        }
    }

    /**
     * 根据传入的路径生成目录
     *
     * @param dirPath
     */
    public static void createDir(String dirPath) {
        File directory = new File(dirPath);
        // 创建目录
        boolean result = directory.mkdirs();
//        if (!result) {
//            throw new RuntimeException("创建目录失败" + dirPath);
//        }
    }


    /**
     * 根据传入的路径生成目录
     *
     * @param filePath 传入文件路径
     * @param content 文件内容
     */
    public static void createFile(String filePath, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("文件创建或写入失败：" + e.getMessage());
        }
    }


    /**
     * 上传文件存储到本地
     *
     * @param file
     */
    public static void savaFileByMulti(MultipartFile file, String id, String fileName) {
        String tmpSaveDir = getTemplateSaveRootDir();
        //创建目录
        fileMkdir(tmpSaveDir + File.separator + id);
        String filePath = tmpSaveDir + File.separator + id + File.separator + fileName;
        File f = new File(filePath);
        log.info("保存文件路径" + filePath);
        try {
            file.transferTo(f);
        } catch (IOException e) {
            log.error("savaFileByMulti报错" + e);
            e.printStackTrace();
        }
    }

    /**
     * 上传文件存储到本地
     *
     * @param file
     */
    public static void savaFileByMultiBack(MultipartFile file, Integer id, Integer fileId,String fileName) {
        String tmpSaveDir = getTemplateSaveRootDir();
        //创建目录
        String mkdirSting=tmpSaveDir + File.separator + id+ File.separator+ fileId;
        fileMkdir(mkdirSting);
        String filePath = mkdirSting+File.separator+fileName;
        File f = new File(filePath);
        log.info("保存文件路径" + filePath);
        try {
            file.transferTo(f);
        } catch (IOException e) {
            log.error("savaFileByMulti报错" + e);
            e.printStackTrace();
        }
    }

    /**
     * 单个文件存储到本地
     *
     * @param id
     * @param fileName
     * @param code     文件内容
     */
    public static void savaFileByName(Integer id, String fileName, String code) {
        try {
            String savePath = GeneratorTempUtils.getDirByEnv() + File.separator + id + File.separator + fileName;
            log.info(savePath);
            FileWriter fw = new FileWriter(savePath);
            fw.write(code);
            fw.close();
        } catch (IOException e) {
            log.error("savaFileByName" + fileName);
            e.printStackTrace();
        }
    }


    /**
     * 单个文件存储到本地
     *
     * @param id
     * @param fileName
     * @param code     文件内容
     */
    public static void savaFileByNameBack(Integer id,Integer fileId, String fileName, String code) {
        try {
            String saveDir=GeneratorTempUtils.getDirByEnv() + File.separator + id + File.separator+fileId;
            FileSelfUtils.createDir(saveDir);
            String savePath = saveDir+File.separator + fileName;
            log.info(savePath);
            FileWriter fw = new FileWriter(savePath);
            fw.write(code);
            fw.close();
        } catch (IOException e) {
            log.error("savaFileByName" + fileName);
            e.printStackTrace();
        }
    }

    /*
     * 获取模版存储根目录
     * */
    public static String getTemplateSaveRootDir() {
        return GeneratorTempUtils.getDirByEnv();
    }

    /**
     * @param filePath 文件绝对路径
     * @return 读取的文件内容
     * @throws IOException
     */
    public static String readFileToString(String filePath) {
        String readString = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(filePath));
            readString = StreamUtils.copyToString(fileInputStream, StandardCharsets.UTF_8);
            fileInputStream.close();
        } catch (IOException e) {
            log.error("readFileToString" + filePath);
            e.printStackTrace();
        }
        return readString;
    }

    /**
     * 删除文件
     *
     * @param path 要删除的文件路径
     * @return
     * @author 邝华
     * @email kuanghua@aulton.com
     * @date 2022-06-13 11:22
     */
    public static void deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    /*文件夹*/

    /**
     * 创建文件夹
     *
     * @author 邝华
     * @date 2022-06-10 13:56
     */
    public static void fileMkdir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        } else {
            log.info("已存在" + path);
        }
    }

    /**
     * 递归删除文件夹
     *
     * @param path 文件绝对路径
     * @date 2022-06-10 14:13
     */
    public static void deleteDir(String path) {
        File directory = new File(path);
        deleteDeepDir(directory);
    }

    public static void deleteDeepDir(File directory) {
        //获取目录下所有文件和目录
        File files[] = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                deleteDeepDir(file);
            } else {
                file.delete();
                log.info(file.getName() + "：：文件已删除");
            }
        }
        //最终把该目录也删除
        directory.delete();
        log.info(directory.getName() + "：：目录已删除");
    }

    /*导出文件*/
    public static void exportFile(HttpServletResponse response, String exportFilePath, String exportFileName) {
        response.setHeader("Access-Control-Expose-Headers", "file-name");
        response.setHeader("file-name", exportFileName);
        File file = new File(exportFilePath);
        if (!file.exists()) {
            throw new RuntimeException("文件不存在");
        }
        ServletOutputStream outputStream = null;
        try {
            byte[] buf = new byte[2048];
            int len;
            FileInputStream in = new FileInputStream(file);
            outputStream = response.getOutputStream();
            while ((len = in.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
                outputStream.flush();
            }
            outputStream.flush();
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭流
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
