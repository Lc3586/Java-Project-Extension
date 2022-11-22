package project.extension.file;

import project.extension.action.IAction1;
import project.extension.system.SystemInfoUtils;
import project.extension.tuple.Tuple3;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

/**
 * 可执行文件帮助类
 *
 * @author LCTR
 * @date 2022-04-18
 */
public class ExecutableHelper {
    /**
     * 获取批处理程序文件
     */
    public static String getShell()
            throws
            Exception {
        switch (SystemInfoUtils.currentOS()) {
            case Windows:
                return "cmd.exe";
            case Linux:
            case OSX:
                return System.getenv("SHELL");
            default:
                throw new Exception(String.format("不支持在当前操作系统%s执行此操作",
                                                  SystemInfoUtils.currentOS()));
        }
    }

    /**
     * 执行可处理文件
     *
     * @param filename         文件路径
     * @param arguments        程序启动参数
     * @param inputWriteLine   输入内容
     * @param outputReadLine   输出内容
     * @param errorReadLine    输出错误
     * @param environments     环境变量
     * @param workingDirectory 工作目录
     * @param inputCharset     输入内容字符集
     * @param outputCharset    输出内容字符集
     * @param errorCharset     输出错误字符集
     * @return 程序退出码
     */
    public static int execFile(
            String filename,
            String[] arguments,
            IAction1<IAction1<String>> inputWriteLine,
            IAction1<String> outputReadLine,
            IAction1<String> errorReadLine,
            String[] environments,
            File workingDirectory,
            Charset inputCharset,
            Charset outputCharset,
            Charset errorCharset)
            throws
            Exception {
        String[] cmd = new String[arguments == null
                                  ? 1
                                  : (arguments.length + 1)];
        cmd[0] = filename;
        if (arguments != null) System.arraycopy(arguments,
                                                0,
                                                cmd,
                                                1,
                                                arguments.length);

        //执行命令获取进程
        Process process = Runtime.getRuntime()
                                 .exec(String.join(" ",
                                                   cmd),
                                       environments,
                                       workingDirectory);

        //输入信息
        if (inputWriteLine != null)
            inputWriteLine.invoke(value -> process.getOutputStream()
                                                  .write(value.getBytes(inputCharset)));

        //异步获取输出信息（一行行获取）
        CompletableFuture<Void> writeOutputTask = outputReadLine == null
                                                  ? CompletableFuture.completedFuture(null)
                                                  : CompletableFuture.runAsync(() -> {
                                                      try {
                                                          Scanner outputScanner = new Scanner(process.getInputStream(),
                                                                                              outputCharset.name());
                                                          while (outputScanner.hasNextLine())
                                                              outputReadLine.invoke(outputScanner.nextLine());
                                                          outputScanner.close();
                                                      } catch (Exception e) {
                                                          e.printStackTrace();
                                                      }
                                                  });

        //异步获取错误信息（一行行获取）
        CompletableFuture<Void> writeErrorTask = errorReadLine == null
                                                 ? CompletableFuture.completedFuture(null)
                                                 : CompletableFuture.runAsync(() -> {
                                                     try {
                                                         Scanner errorScanner = new Scanner(process.getErrorStream(),
                                                                                            errorCharset.name());
                                                         while (errorScanner.hasNextLine())
                                                             errorReadLine.invoke(errorScanner.nextLine());
                                                         errorScanner.close();
                                                     } catch (Exception ex) {
                                                         ex.printStackTrace();
                                                     }
                                                 });

        CompletableFuture<Void> allTask = CompletableFuture.allOf(writeOutputTask,
                                                                  writeErrorTask);

        //等待程序运行结束
        process.waitFor();

        //等待任务全部结束
        allTask.get();

        return process.exitValue();
    }

    /**
     * 调用可执行程序
     *
     * @param filename         文件路径
     * @param arguments        程序启动参数
     * @param input            程序启动后的要输入的内容
     * @param environments     环境变量
     * @param workingDirectory 工作目录
     * @param inputCharset     输入内容字符集
     * @param outputCharset    输出内容字符集
     * @param errorCharset     输出错误字符集
     * @return a: 信息输出,b: 错误输出,c: 程序退出码
     */
    public static Tuple3<String, String, Integer> simpleExec(
            String filename,
            String[] arguments,
            String input,
            String[] environments,
            File workingDirectory,
            Charset inputCharset,
            Charset outputCharset,
            Charset errorCharset)
            throws
            Exception {
        StringBuilder output = new StringBuilder();
        StringBuilder error = new StringBuilder();
        int exitCode;

        if (input != null)
            exitCode = execFile(
                    filename,
                    arguments,
                    inputWriteLine -> inputWriteLine.invoke(input),
                    outputValue -> output.append(String.format("%s\r\n",
                                                               outputValue)),
                    errorValue -> error.append(String.format("%s\r\n",
                                                             errorValue)),
                    environments,
                    workingDirectory,
                    inputCharset,
                    outputCharset,
                    errorCharset);
        else
            exitCode = execFile(
                    filename,
                    arguments,
                    null,
                    outputValue -> output.append(String.format("%s\r\n",
                                                               outputValue)),
                    errorValue -> error.append(String.format("%s\r\n",
                                                             errorValue)),
                    environments,
                    workingDirectory,
                    inputCharset,
                    outputCharset,
                    errorCharset);

        return new Tuple3<>(output.toString(),
                            error.toString(),
                            exitCode);
    }

    /**
     * @param cmd              命令
     * @param arguments        程序启动参数
     * @param environments     环境变量
     * @param workingDirectory 工作目录
     * @param inputCharset     输入内容字符集
     * @param outputCharset    输出内容字符集
     * @param errorCharset     输出错误字符集
     * @return a: 信息输出,b: 错误输出,c: 程序退出码
     */
    public static Tuple3<String, String, Integer> execShell(
            String cmd,
            String[] arguments,
            String[] environments,
            File workingDirectory,
            Charset inputCharset,
            Charset outputCharset,
            Charset errorCharset)
            throws
            Exception {
        return simpleExec(
                getShell(),
                arguments,
                String.format("%s&exit",
                              cmd.substring(cmd.length() - 1,
                                            1)
                                 .equals("&")
                              ? cmd.substring(0,
                                              cmd.length() - 1)
                              : cmd),
                environments,
                workingDirectory,
                inputCharset,
                outputCharset,
                errorCharset);
    }
}
