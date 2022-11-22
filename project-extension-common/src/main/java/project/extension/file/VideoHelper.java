package project.extension.file;

import com.alibaba.fastjson.JSONObject;
import project.extension.tuple.Tuple3;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 视频文件帮助类
 *
 * @author LCTR
 * @date 2022-04-18
 */
public class VideoHelper {
    /**
     * 视频截图
     * <p>图片质量 31</p>
     * <p>图片类型 mjpeg</p>
     *
     * @param videoFilename  视频文件路径
     * @param ffmpegFilename ffmpeg应用程序文件路径
     * @param imageFilename  截图文件存储路径
     * @param time           时间轴位置（示例：0:00:02.003）
     */
    public static void screenshot(String videoFilename,
                                  String ffmpegFilename,
                                  String imageFilename,
                                  String time)
            throws
            Exception {
        screenshot(videoFilename,
                   ffmpegFilename,
                   imageFilename,
                   time,
                   null,
                   null);
    }

    /**
     * 视频截图
     * <p>图片质量 31</p>
     * <p>图片类型 mjpeg</p>
     *
     * @param videoFilename  视频文件路径
     * @param ffmpegFilename ffmpeg应用程序文件路径
     * @param imageFilename  截图文件存储路径
     * @param time           时间轴位置（示例：0:00:02.003）
     * @param width          图片质量（取值范围：2-31）
     * @param height         图片宽度
     */
    public static void screenshot(String videoFilename,
                                  String ffmpegFilename,
                                  String imageFilename,
                                  String time,
                                  Integer width,
                                  Integer height)
            throws
            Exception {
        screenshot(videoFilename,
                   ffmpegFilename,
                   imageFilename,
                   time,
                   width,
                   height,
                   31,
                   "mjpeg");
    }

    /**
     * 视频截图
     * <p>https://ffmpeg.org/ffmpeg.html</p>
     *
     * @param videoFilename  视频文件路径
     * @param ffmpegFilename ffmpeg应用程序文件路径
     * @param imageFilename  截图文件存储路径
     * @param time           时间轴位置
     *                       <p>示例 https://ffmpeg.org/ffmpeg-utils.html#time-duration-syntax</p>
     *                       <p>‘55’ 55 seconds</p>
     *                       <p>‘0.2’ 0.2 seconds</p>
     *                       <p>‘200ms’ 200 milliseconds, that’s 0.2s</p>
     *                       <p>‘200000us’ 200000 microseconds, that’s 0.2s</p>
     *                       <p>‘12:03:45’ 12 hours, 03 minutes and 45 seconds</p>
     *                       <p>‘23.189’ 23.189 seconds</p>
     * @param width          图片质量（取值范围：2-31）
     * @param height         图片宽度
     * @param quality        图片高度
     * @param imageFormat    图片格式（示例：mjpeg）
     */
    public static void screenshot(String videoFilename,
                                  String ffmpegFilename,
                                  String imageFilename,
                                  String time,
                                  Integer width,
                                  Integer height,
                                  int quality,
                                  String imageFormat)
            throws
            Exception {
        File videoFile = new File(videoFilename);
        if (!videoFile.exists())
            throw new Exception(String.format("未找到视频文件%s",
                                              videoFilename));

        File imageFile = new File(imageFilename);
        File imageDir = imageFile.getParentFile();
        if (!imageDir.exists()) {
            if (!imageDir.mkdir())
                throw new Exception(String.format("创建文件夹失败, %s",
                                                  imageDir.getPath()));
        }

        List<String> arguments = new ArrayList<>();
        //时间轴位置
        arguments.add(String.format("-ss %s",
                                    time));
        //视频文件路径
        arguments.add(String.format("-i \"%s\"",
                                    videoFilename));
        //截图质量
        arguments.add(String.format("-q:v %s",
                                    quality));
        arguments.add("-frames:v 1");
        arguments.add("-an");
        arguments.add("-y");
        arguments.add(String.format("-f %s",
                                    imageFormat));

        if (width != null && height != null)
            arguments.add(String.format("-s %sx%s \"%s\"",
                                        width,
                                        height,
                                        imageFilename));
        else
            arguments.add(String.format("-s \"%s\"",
                                        imageFilename));

        Tuple3<String, String, Integer> result = ExecutableHelper.simpleExec(
                ffmpegFilename,
                arguments.toArray(new String[0]),
                null,
                null,
                null,
                StandardCharsets.UTF_8,
                StandardCharsets.UTF_8,
                StandardCharsets.UTF_8);

        if (result.c != 0)
            throw new Exception(String.format("截图失败, exitCode: %s, output: %s, error: %s",
                                              result.c,
                                              result.a,
                                              result.b));
    }

    /**
     * 获取视频信息
     *
     * @param videoFilename   视频文件路径
     * @param ffprobeFilename fffprobe应用程序文件路径
     * @return 视频信息
     */
    public static VideoInfo getVideoInfo(String videoFilename,
                                         String ffprobeFilename)
            throws
            Exception {
        return getVideoInfo(videoFilename,
                            ffprobeFilename,
                            true,
                            true,
                            true,
                            true,
                            true);
    }

    /**
     * 获取视频信息
     *
     * @param videoFilename   视频文件路径
     * @param ffprobeFilename fffprobe应用程序文件路径
     * @param format          获取有关输入多媒体流的容器格式的信息
     * @param streams         获取有关输入多媒体流中包含的每个媒体流的信息
     * @param chapters        获取有关以该格式存储的章节的信息
     * @param programs        获取有关程序及其输入多媒体流中包含的流的信息
     * @param version         获取与程序版本有关的信息、获取与库版本有关的信息、获取与程序和库版本有关的信息
     * @return 视频信息
     */
    public static VideoInfo getVideoInfo(String videoFilename,
                                         String ffprobeFilename,
                                         boolean format,
                                         boolean streams,
                                         boolean chapters,
                                         boolean programs,
                                         boolean version)
            throws
            Exception {
        File videoFile = new File(videoFilename);
        if (!videoFile.exists())
            throw new Exception(String.format("未找到视频文件%s",
                                              videoFilename));

        List<String> arguments = new ArrayList<>();
        arguments.add(String.format("-i \"%s\"",
                                    videoFilename));
        arguments.add("-print_format json");
        arguments.add("-show_data");

        if (format)
            arguments.add("-show_format");
        if (streams)
            arguments.add("-show_streams");
        if (chapters)
            arguments.add("-show_chapters");
        if (programs)
            arguments.add("-show_programs");
        if (version)
            arguments.add("-show_versions");

        Tuple3<String, String, Integer> result = ExecutableHelper.simpleExec(
                ffprobeFilename,
                arguments.toArray(new String[0]),
                null,
                null,
                null,
                StandardCharsets.UTF_8,
                StandardCharsets.UTF_8,
                StandardCharsets.UTF_8);

        if (result.c != 0)
            throw new Exception(String.format("获取视频信息失败, exitCode: %s, output: %s, error: %s",
                                              result.c,
                                              result.a,
                                              result.b));

        return JSONObject.parseObject(result.a,
                                      VideoInfo.class);
    }
}
