package project.extension.file;

import project.extension.tuple.Tuple2;
import sun.misc.BASE64Decoder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 图像文件帮助类
 *
 * @author LCTR
 * @date 2022-06-23
 */
public class ImageHelper {
    /**
     * 获取Base64编码的图片
     *
     * @param base64 Base64编码
     * @return 文件数据输入流
     */
    public static Tuple2<InputStream, Integer> getBase64Image(String base64)
            throws
            IOException {
        byte[] buffer = new BASE64Decoder().decodeBuffer(base64);
        return new Tuple2<>(new ByteArrayInputStream(buffer),
                            buffer.length);
    }
}
