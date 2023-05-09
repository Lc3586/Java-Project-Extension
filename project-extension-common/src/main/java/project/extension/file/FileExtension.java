package project.extension.file;

import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import project.extension.exception.CommonException;
import project.extension.number.DecimalExtension;
import project.extension.resource.ScanExtension;
import project.extension.string.StringExtension;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.Locale;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件拓展方法
 *
 * @author LCTR
 * @date 2022-04-15
 */
public class FileExtension {
    /**
     * 删除文件/文件夹
     *
     * @param path 路径
     */
    public static boolean delete(String path) {
        return delete(new File(path));
    }

    /**
     * 删除文件/文件夹
     *
     * @param file 文件/文件夹
     */
    public static boolean delete(File file) {
        if (!file.exists()) return true;

        if (file.isDirectory()) {
            File[] subFiles = file.listFiles();
            if (subFiles != null) for (File subFile : subFiles) {
                if (subFile.isDirectory()) {
                    if (!delete(subFile)) return false;
                } else {
                    if (!subFile.delete()) return false;
                }
            }
        }
        return file.delete();
    }

    /**
     * 保存文件
     *
     * @param inputStream 输入流
     * @param fileName    文件路径
     * @param length      字节数
     */
    public static void save(InputStream inputStream,
                            String fileName,
                            Long length)
            throws
            Exception {
        try (ReadableByteChannel fromChannel = Channels.newChannel(inputStream)) {
            copy(fromChannel,
                 new File(fileName),
                 length);
        }
    }

    /**
     * 保存文件
     *
     * @param base64   base64编码数据
     * @param filename 文件路径
     */
    public static void save(String base64,
                            String filename)
            throws
            Exception {
        byte[] buffer = Base64Utils.decodeFromString(base64);
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer)) {
            save(inputStream,
                 filename,
                 Long.parseLong(Integer.toString(buffer.length)));
        }
    }

    /**
     * 复制文件
     *
     * @param fromFileName 源（文件/文件夹）
     * @param toFileName   目标（文件/文件夹）
     */
    public static void copy(String fromFileName,
                            String toFileName)
            throws
            Exception {
        copy(new File(fromFileName),
             new File(toFileName));
    }

    /**
     * 复制文件
     *
     * @param fromFile 源（文件/文件夹）
     * @param toFile   目标（文件/文件夹）
     */
    public static void copy(File fromFile,
                            File toFile)
            throws
            Exception {
        if (fromFile.isDirectory()) {
            if (!toFile.mkdir()) throw new Exception(String.format("创建文件夹失败, %s",
                                                                   toFile.getAbsolutePath()));

            File[] fromSubFiles = fromFile.listFiles();
            if (fromSubFiles != null) for (File fromSubFile : fromSubFiles) {
                File toSubFile = new File(
                        fromSubFile.getAbsolutePath()
                                   .replace(fromFile.getAbsolutePath(),
                                            toFile.getAbsolutePath()));
                copy(fromSubFile,
                     toSubFile);
            }
        } else {
            try (FileChannel fromChannel = new RandomAccessFile(fromFile,
                                                                "rw").getChannel()) {
                copy(fromChannel,
                     toFile,
                     fromChannel.size());
            }
        }
    }

    /**
     * 复制文件
     *
     * @param from   源
     * @param toFile 目标文件
     * @param length 字节数
     */
    public static void copy(ReadableByteChannel from,
                            File toFile,
                            Long length)
            throws
            Exception {
        File parentDir = toFile.getParentFile();
        if (!parentDir.exists() && !parentDir.mkdirs())
            throw new Exception(String.format("创建文件夹失败, %s",
                                              parentDir.getAbsolutePath()));
        try (FileChannel toChannel = new RandomAccessFile(toFile,
                                                          "rw").getChannel()) {
            toChannel.transferFrom(from,
                                   0,
                                   length);
        }
    }

    /**
     * 复制
     *
     * @param file         文件
     * @param outputStream 输出流
     */
    public static void copy(File file,
                            OutputStream outputStream)
            throws
            Exception {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            copy(fileInputStream,
                 outputStream,
                 true);
        }
    }

    /**
     * 复制
     *
     * @param inputStream      输入流
     * @param outputStream     输出流
     * @param closeInputStream 操作结束后是否关闭输入流
     */
    public static void copy(InputStream inputStream,
                            OutputStream outputStream,
                            boolean closeInputStream)
            throws
            Exception {
        int length;
        byte[] bytes = new byte[1024];
        try {
            while ((length = inputStream.read(bytes)) > 0) {
                outputStream.write(bytes,
                                   0,
                                   length);
            }
        } finally {
            if (closeInputStream)
                inputStream.close();
        }
    }

    /**
     * 移动文件
     *
     * @param fromFileName 源（文件/文件夹）
     * @param toFileName   目标（文件/文件夹）
     */
    public static void move(String fromFileName,
                            String toFileName)
            throws
            Exception {
        copy(fromFileName,
             toFileName);
        delete(fromFileName);
    }

    /**
     * 下载文件
     *
     * @param url      下载链接地址
     * @param fileName 文件名
     */
    public static void download(String url,
                                String fileName)
            throws
            Exception {
        download(url,
                 fileName,
                 false);
    }

    /**
     * 下载文件
     * <p>关于文件名获取规则的优先级：</p>
     * <p>1、在响应头中获取文件名</p>
     * <p>2、在链接地址中获取文件名</p>
     *
     * @param url 下载链接地址
     * @return a: 输入流, b: 字节数, c: 文件名（可能为空）
     */
    public static FileDownloadInfo downloadInfo(String url)
            throws
            Exception {
        URLConnection urlConnection = new URL(url).openConnection();
        String fileName = null;
        String disposition = urlConnection.getHeaderField("Content-Disposition");
        if (StringUtils.hasText(disposition)) {
            Matcher matcher = Pattern.compile("filename=['\"](.*?)['\"]",
                                              Pattern.CASE_INSENSITIVE)
                                     .matcher(
                                             disposition);
            if (matcher.find() && StringUtils.hasText(matcher.group(1)))
                fileName = URLDecoder.decode(matcher.group(1),
                                             StandardCharsets.UTF_8.name());
        }

        if (!StringUtils.hasText(fileName)) {
            Matcher matcher = Pattern.compile("^https?://.*/([^.].*.[^.].*)$",
                                              Pattern.CASE_INSENSITIVE)
                                     .matcher(url);
            if (matcher.find() && StringUtils.hasText(matcher.group(1)))
                fileName = URLDecoder.decode(matcher.group(1),
                                             StandardCharsets.UTF_8.name());
        }

        FileDownloadInfo fileDownloadInfo = new FileDownloadInfo();
        fileDownloadInfo.setInputStream(urlConnection.getInputStream());
        fileDownloadInfo.setLength(urlConnection.getContentLengthLong());
        fileDownloadInfo.setContentType(urlConnection.getContentType());
        fileDownloadInfo.setFullName(fileName);
        if (StringUtils.hasText(fileName)) {
            fileDownloadInfo.setExtension(PathExtension.getExtension(fileName));
            fileDownloadInfo.setName(PathExtension.trimExtension(fileName));
        }
        fileDownloadInfo.setHeaderFields(urlConnection.getHeaderFields());
        return fileDownloadInfo;
    }

    /**
     * 下载文件
     * <p>关于尝试获取文件名时的优先级：</p>
     * <p>1、在响应头中获取文件名</p>
     * <p>2、在链接地址中获取文件名</p>
     *
     * @param url        下载链接地址
     * @param fileName   文件保存路径
     * @param tryGetName 尝试获取文件名（如果成功获取到文件名，将会更改文件保存路径）
     * @return 最终的文件保存路径
     */
    public static String download(String url,
                                  String fileName,
                                  boolean tryGetName)
            throws
            Exception {
        FileDownloadInfo downloadInfo = downloadInfo(url);
        if (tryGetName && StringUtils.hasText(downloadInfo.getFullName()))
            fileName = fileName.replace(new File(fileName).getName(),
                                        downloadInfo.getFullName());
        try (InputStream inputStream = downloadInfo.getInputStream()) {
            save(inputStream,
                 fileName,
                 downloadInfo.getLength());
        }
        return fileName;
    }

    /**
     * 获取文件大小
     * <p>默认单位1024 精度2</p>
     *
     * @param length 字节数
     */
    public static String getFileSize(long length) {
        return getFileSize(length,
                           1024,
                           2);
    }

    /**
     * 获取文件大小
     *
     * @param length    字节数
     * @param unit      单位
     * @param precision 精度
     */
    public static String getFileSize(long length,
                                     int unit,
                                     int precision) {
        if (length <= 0) return "0 KB";

        String[] formats = new String[]{"KB",
                                        "MB",
                                        "GB",
                                        "TB",
                                        "PB",
                                        "EB",
                                        "ZB",
                                        "YB"};

        for (int i = 0; i < formats.length; i++) {
            double value = length / Math.pow(unit,
                                             i + 1);

            if (value < unit)
                return String.format("%s %s",
                                     DecimalExtension.round(value,
                                                            precision),
                                     formats[i]);
        }

        return String.format("%s %s",
                             DecimalExtension.round(length / Math.pow(unit,
                                                                      formats.length),
                                                    precision),
                             formats[formats.length - 1]);
    }

    /**
     * 进行MD5校验
     *
     * @param fileName 文件路径
     * @return md5校验值
     * @deprecated 此为不安全的哈希算法，建议使用sha256()方法
     */
    @Deprecated
    public static String md5(String fileName)
            throws
            Exception {
        return calc(new File(fileName),
                    "MD5");
    }

    /**
     * 进行SHA-256校验
     *
     * @param fileName 文件路径
     * @return SHA-256校验值
     */
    public static String sha256(String fileName)
            throws
            Exception {
        return calc(new File(fileName),
                    "SHA-256");
    }

    /**
     * 进行MD5校验
     *
     * @param file 文件
     * @return md5校验值
     * @deprecated 此为不安全的哈希算法，建议使用sha256()方法
     */
    @Deprecated
    public static String md5(File file)
            throws
            Exception {
        return calc(file,
                    "MD5");
    }

    /**
     * 进行SHA-256校验
     *
     * @param file 文件
     * @return SHA-256校验值
     */
    public static String sha256(File file)
            throws
            Exception {
        return calc(file,
                    "SHA-256");
    }

    /**
     * 进行MD5校验
     *
     * @param file      文件
     * @param algorithm 算法
     * @return md5校验值
     */
    public static String calc(File file,
                              String algorithm)
            throws
            Exception {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file,
                                                                      "rw")) {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            byte[] buffer = new byte[1024];
            while (true) {
                int length = randomAccessFile.read(buffer,
                                                   0,
                                                   buffer.length);
                if (length <= 0) break;

                messageDigest.update(buffer,
                                     0,
                                     length);
            }

            return DatatypeConverter.printHexBinary(messageDigest.digest())
                                    .toLowerCase(Locale.ROOT);
        }
    }

    /**
     * 进行MD5校验
     *
     * @param fileName 文件路径
     * @param md5      md5校验值
     * @return 计算结果和给定值是否一致（忽略大小写）
     * @deprecated 此为不安全的哈希算法，建议使用sha256()方法
     */
    @Deprecated
    public static boolean md5(String fileName,
                              String md5)
            throws
            Exception {
        return StringExtension.ignoreCaseEquals(md5(fileName),
                                                md5);
    }

    /**
     * 进行SHA-256校验
     *
     * @param fileName 文件路径
     * @param sha256   SHA-256校验值
     * @return 计算结果和给定值是否一致（忽略大小写）
     */
    public static boolean sha256(String fileName,
                                 String sha256)
            throws
            Exception {
        return StringExtension.ignoreCaseEquals(sha256(fileName),
                                                sha256);
    }

    /**
     * 读取文件全部数据
     * <p>如果文件太大则会抛出异常</p>
     *
     * @param fileName 文件路径
     * @return 字节数组
     */
    public static byte[] readAllByte(String fileName)
            throws
            Exception {
        return readAllByte(new File(fileName));
    }

    /**
     * 读取文件全部数据
     * <p>如果文件太大则会抛出异常</p>
     *
     * @param file 文件
     * @return 字节数组
     */
    public static byte[] readAllByte(File file)
            throws
            Exception {
        if (!file.exists())
            throw new Exception(String.format("文件不存在%s",
                                              file.getAbsolutePath()));

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file,
                                                                      "rw")) {
            if (randomAccessFile.length() > Integer.MAX_VALUE)
                throw new Exception(String.format("文件字节数超出了数组的最大长度限制%s>%s",
                                                  randomAccessFile.length(),
                                                  Integer.MAX_VALUE));

            byte[] buffer = new byte[Integer.parseInt(Long.toString(randomAccessFile.length()))];
            randomAccessFile.read(buffer,
                                  0,
                                  buffer.length);
            return buffer;
        }
    }

    /**
     * 压缩文件
     *
     * @param fileName     文件路径
     * @param outputStream 输出流
     */
    public static void compress(String fileName,
                                OutputStream outputStream)
            throws
            Exception {
        //TODO
        throw new Exception("暂未实现");
    }

    /**
     * 获取jar文件地址
     */
    public static String getJarFilePath() {
        String jarFilePath = System.getProperty("java.class.path");

        if (!StringUtils.hasText(jarFilePath)
                || !jarFilePath.endsWith(".jar")) {
            throw new CommonException("获取当前jar文件失败");
        }

        return jarFilePath;
    }

    /**
     * 提取jar文件中的单个资源
     *
     * @param targetFileInJar         文件在jar中的路径
     * @param destinationFileInOutput 存储提取的文件的输出流
     */
    public static void extractSingleFileFromJar(String targetFileInJar,
                                                OutputStream destinationFileInOutput)
            throws
            CommonException {
        targetFileInJar = targetFileInJar.replaceAll("\\\\",
                                                     "/");

        try (JarFile jar = new JarFile(getJarFilePath())) {
            Enumeration<JarEntry> fileList = jar.entries();
            while (fileList.hasMoreElements()) {
                //获取条目
                JarEntry jarEntry = fileList.nextElement();
                //获取条目名称
                String name = jarEntry.getName();

                //判断是否为需要处理的条目
                if (!name.equals(targetFileInJar)) {
                    continue;
                }

                //复制文件
                try (InputStream jarInputStream = jar.getInputStream(jar.getEntry(name))) {
                    FileExtension.copy(jarInputStream,
                                       destinationFileInOutput,
                                       true);
                }

                break;
            }
        } catch (Exception ex) {
            throw new CommonException(String.format("提取jar文件中的%s文件失败",
                                                    targetFileInJar),
                                      ex);
        }
    }

    /**
     * 提取jar文件中的资源
     *
     * @param targetFileInJar       文件在jar中的路径
     * @param destinationFileInDisk 在磁盘中存储提取的文件的路径
     */
    public static void extractFileFromJar(String targetFileInJar,
                                          String destinationFileInDisk)
            throws
            CommonException {
        targetFileInJar = targetFileInJar.replaceAll("\\\\",
                                                     "/");

        try (JarFile jar = new JarFile(getJarFilePath())) {
            Enumeration<JarEntry> fileList = jar.entries();
            while (fileList.hasMoreElements()) {
                //获取条目
                JarEntry jarEntry = fileList.nextElement();
                //获取条目名称
                String name = jarEntry.getName();

                //判断是否为需要处理的条目
                if (!name.equals(targetFileInJar)
                        && !name.startsWith(targetFileInJar)) {
                    continue;
                }

                String newName = jarEntry.getName()
                                         .replace(targetFileInJar,
                                                  "");

                File destinationFile = Paths.get(destinationFileInDisk,
                                                 newName)
                                            .toFile();

                if (jarEntry.isDirectory()) {
                    //创建文件夹
                    if (!destinationFile.exists()
                            && !destinationFile.mkdirs())
                        throw new CommonException(String.format("创建%s目录失败",
                                                                destinationFileInDisk));
                } else {
                    //复制文件
                    try (InputStream jarInputStream = jar.getInputStream(jar.getEntry(name));
                         FileOutputStream disOutputStream = new FileOutputStream(destinationFile)) {
                        FileExtension.copy(jarInputStream,
                                           disOutputStream,
                                           true);
                    }
                }
            }
        } catch (Exception ex) {
            throw new CommonException(String.format("提取jar文件中的%s文件失败",
                                                    targetFileInJar),
                                      ex);
        }
    }

    /**
     * 程序是否在jar中运行
     */
    public static boolean isRunInJar()
            throws IOException {
        return "jar".equals(ScanExtension.getResource("")
                                         .getURL()
                                         .getProtocol());
    }
}
