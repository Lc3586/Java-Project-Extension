package project.extension.task;

import java.util.concurrent.CompletableFuture;

/**
 * 异步任务拓展方法
 *
 * @author LCTR
 * @date 2022-04-02
 */
public class TaskExtension {
    /**
     * 延时
     *
     * @param millis 毫秒数
     */
    public static void delay(long millis)
            throws
            Exception {
        delayTask(millis)
                .get();
    }

    /**
     * 延时
     *
     * @param millis 毫秒数
     */
    public static CompletableFuture<Void> delayTask(long millis) {
        return CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException ignore) {

            }
        });
    }
}
