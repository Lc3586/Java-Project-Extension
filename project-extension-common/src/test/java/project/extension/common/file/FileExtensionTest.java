package project.extension.common.file;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.util.StringUtils;
import project.extension.file.FileExtension;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件拓展方法测试
 *
 * @author LCTR
 * @date 2022-04-19
 */
@DisplayName("文件拓展方法测试")
public class FileExtensionTest {
    /**
     * 测试临时文件根目录
     */
    private final String testTempFileDir = String.format("test-temp\\%s",
                                                         FileExtensionTest.class.getSimpleName());

    /**
     * 创建测试临时文件
     *
     * @return 文件路径
     */
    private String createTempFile(String root)
            throws
            Throwable {
        String fileName = Paths.get(createTempDir(root),
                                    "test.txt")
                               .toString();
        File file = new File(fileName);
        if (!file.exists())
            Assertions.assertSame(true,
                                  file.createNewFile(),
                                  "创建测试文件失败");

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file,
                                                                               false))) {
            for (int i = 0; i < 1024 * 1024; i++) {
                bufferedWriter.write("测试内容\r\n");
            }
            bufferedWriter.flush();
            return fileName;
        }
    }

    /**
     * 创建测试临时文件夹
     *
     * @return 文件夹路径
     */
    private String createTempDir(String root) {
        String fileName = StringUtils.hasText(root)
                          ? Paths.get(testTempFileDir,
                                      root)
                                 .toString()
                          : testTempFileDir;
        File file = new File(fileName);
        if (!file.exists())
            Assertions.assertSame(true,
                                  file.mkdirs(),
                                  "创建测试文件夹失败");
        return fileName;
    }

    /**
     * 创建测试临时文件夹和文件
     *
     * @return 文件夹路径
     */
    private String createTempDirWithFile()
            throws
            Throwable {
        String filename = createTempDir("test");
        createTempFile("test");
        createTempFile("test\\test");
        createTempFile("test\\test\\test");
        return filename;
    }

    /**
     * 获取文件夹下的所有文件路径（包括子文件/子文件夹）
     *
     * @param dir 文件夹
     * @return 文件路径集合
     */
    private List<String> getDirFileList(File dir) {
        List<String> result = new ArrayList<>();
        File[] subFiles = dir.listFiles();
        if (subFiles != null) for (File subFile : subFiles) {
            if (subFile.isDirectory())
                result.addAll(getDirFileList(subFile));
            else
                result.add(subFile.getAbsolutePath());
        }
        return result;
    }

    /**
     * 测试删除文件
     */
    @Test
    @DisplayName("测试删除文件")
    public void deleteFile()
            throws
            Throwable {
        File file = new File(createTempFile(null));

        Assertions.assertSame(true,
                              FileExtension.delete(file),
                              "删除文件方法返回失败");

        Assertions.assertSame(false,
                              file.exists(),
                              "删除文件方法返回成功，但文件依然存在");

        System.out.println("测试已通过");
    }

    /**
     * 测试删除文件夹
     */
    @Test
    @DisplayName("测试删除文件夹")
    public void deleteDir()
            throws
            Throwable {
        File file = new File(createTempDirWithFile());

        Assertions.assertSame(true,
                              FileExtension.delete(file),
                              "删除文件方法返回失败");

        Assertions.assertSame(false,
                              file.exists(),
                              "删除文件方法返回成功，但文件依然存在");

        System.out.println("测试已通过");
    }

    /**
     * 测试复制文件
     */
    @Test
    @DisplayName("测试复制文件")
    public void copyFile()
            throws
            Throwable {
        File fromFile = new File(createTempFile(null));
        File toFile = new File(Paths.get(testTempFileDir,
                                         "test.txt")
                                    .toString());
        FileExtension.copy(fromFile,
                           toFile);

        Assertions.assertSame(true,
                              toFile.exists(),
                              "复制文件方法已调用，但复制后的文件不存在");

        FileExtension.delete(fromFile);
        FileExtension.delete(toFile);

        System.out.println("测试已通过");
    }

    /**
     * 测试复制文件夹
     */
    @Test
    @DisplayName("测试复制文件夹")
    public void copyDir()
            throws
            Throwable {
        File fromFile = new File(createTempDirWithFile());
        File toFile = new File(Paths.get(testTempFileDir,
                                         "test-copy")
                                    .toString());

        List<String> files = getDirFileList(fromFile);

        FileExtension.copy(fromFile,
                           toFile);

        Assertions.assertSame(true,
                              toFile.exists(),
                              "复制文件夹方法已调用，但复制后的文件夹不存在");

        for (String fileName : files) {
            String targetFileName = fileName.replace(fromFile.getAbsolutePath(),
                                                     toFile.getAbsolutePath());
            Assertions.assertSame(true,
                                  new File(targetFileName).exists(),
                                  String.format("复制后的文件夹内缺少此文件%s",
                                                targetFileName));
        }

        FileExtension.delete(fromFile);
        FileExtension.delete(toFile);

        System.out.println("测试已通过");
    }

    /**
     * 测试移动文件
     */
    @Test
    @DisplayName("测试移动文件")
    public void moveFile()
            throws
            Throwable {
        String fromFileName = createTempFile(null);
        String toFileName = Paths.get(testTempFileDir,
                                      "test-move.txt")
                                 .toString();
        FileExtension.move(fromFileName,
                           toFileName);

        Assertions.assertSame(true,
                              new File(toFileName).exists(),
                              "移动文件方法已调用，但移动后的文件不存在");

        Assertions.assertSame(false,
                              new File(fromFileName).exists(),
                              "移动文件方法已调用，但原始文件依然存在");

        FileExtension.delete(toFileName);

        System.out.println("测试已通过");
    }

    /**
     * 测试移动文件夹
     */
    @Test
    @DisplayName("测试移动文件夹")
    public void moveDir()
            throws
            Throwable {
        String fromFileName = createTempDirWithFile();
        String toFileName = Paths.get(testTempFileDir,
                                      "test-move")
                                 .toString();

        List<String> files = getDirFileList(new File(fromFileName));

        FileExtension.move(fromFileName,
                           toFileName);

        Assertions.assertSame(true,
                              new File(toFileName).exists(),
                              "移动文件夹方法已调用，但移动后的文件夹不存在");

        for (String fileName : files) {
            String targetFileName = fileName.replace(fromFileName,
                                                     toFileName);
            Assertions.assertSame(true,
                                  new File(targetFileName).exists(),
                                  String.format("移动后的文件夹内缺少此文件%s",
                                                targetFileName));
        }

        Assertions.assertSame(false,
                              new File(fromFileName).exists(),
                              "移动文件夹方法已调用，但原始文件夹依然存在");

        FileExtension.delete(toFileName);

        System.out.println("测试已通过");
    }

    /**
     * 测试下载文件
     */
    @Test
    @DisplayName("测试下载文件")
    public void downloadFile()
            throws
            Throwable {
        String url = "http://www.turuitech.com/Upload/201603/11/201603111045302439.png";
        String filename = Paths.get(createTempDir(null),
                                    "downloadFile.png")
                               .toString();

        FileExtension.download(url,
                               filename);

        Assertions.assertSame(true,
                              new File(filename).exists(),
                              "下载文件方法已调用，但下载后的文件不存在");

        FileExtension.delete(filename);

        System.out.println("测试已通过");
    }

    /**
     * 测试下载文件，同时从链接地址获取文件名
     */
    @Test
    @DisplayName("测试下载文件，同时从链接地址获取文件名")
    public void downloadFileAndGetNameFromUrl()
            throws
            Throwable {
        String url = "http://www.turuitech.com/Upload/201603/11/201603111045302439.png";

        String filename = FileExtension.download(url,
                                                 Paths.get(createTempDir(null),
                                                           "downloadFile.png")
                                                      .toString(),
                                                 true);

        Assertions.assertSame(true,
                              new File(filename).exists(),
                              "下载文件方法已调用，但下载后的文件不存在");

        Assertions.assertEquals("201603111045302439.png",
                                new File(filename).getName(),
                                String.format("下载文件方法已调用，但获取到的文件名不正确 %s",
                                              filename));

        FileExtension.delete(filename);

        System.out.println("测试已通过");
    }

    /**
     * 测试下载文件，同时从响应头获取文件名
     */
    @Test
    @DisplayName("测试下载文件，同时从响应头获取文件名")
    public void downloadFileAndGetNameFromHeader()
            throws
            Throwable {
        String url = "http://220112api.turuitech.com/personal-file-info/download/6D010000-3EF8-FA16-4E00-08D9FBEE9FAB";

        String filename = FileExtension.download(url,
                                                 Paths.get(createTempDir(null),
                                                           "downloadFile.png")
                                                      .toString(),
                                                 true);

        Assertions.assertSame(true,
                              new File(filename).exists(),
                              "下载文件方法已调用，但下载后的文件不存在");

        Assertions.assertEquals("甬行码二维码（霞浦派出所）.png",
                                new File(filename).getName(),
                                String.format("下载文件方法已调用，但获取到的文件名不正确 %s",
                                              filename));

        FileExtension.delete(filename);

        System.out.println("测试已通过");
    }
}