package project.extension.common.task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import project.extension.task.TaskExtension;
import project.extension.task.TaskQueueHandler;
import project.extension.task.TaskQueueHandlerState;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * 基础任务队列运行类测试
 *
 * @author LCTR
 * @date 2023-05-11
 */
@DisplayName("基础任务队列运行类测试")
public class TaskQueueHandlerTest {
    static class TestTaskQueueHandler
            extends TaskQueueHandler<UUID> {
        public TestTaskQueueHandler() {
            super("测试处理类",
                  -1,
                  LoggerFactory.getLogger(TestTaskQueueHandler.class));
        }

        @Override
        protected void processingTask(UUID taskKey) {
            //异步执行
            super.putConcurrentTask(taskKey,
                                    () -> this.handlerTask(taskKey),
                                    x -> super.removeConcurrentTask(taskKey));
        }

        private void handlerTask(UUID taskKey) {
            logger.info(String.format("开始执行任务：%s",
                                      taskKey));
            TaskExtension.delay(new Random().nextInt(5) * 1000);
            logger.info(String.format("任务执行完毕：%s",
                                      taskKey));
        }
    }

    /**
     * 检查状态
     */
    @Test
    @DisplayName("检查状态")
    public void checkState() {
        TestTaskQueueHandler taskQueueHandler = new TestTaskQueueHandler();

        Assertions.assertEquals(TaskQueueHandlerState.已停止,
                                taskQueueHandler.getState(),
                                "程序未启动时应该为已停止状态");

        CompletableFuture.runAsync(
                () -> taskQueueHandler.start(true,
                                             () -> {
                                                 TaskExtension.delay(2000);
                                                 return true;
                                             }));

        TaskExtension.delay(1000);

        Assertions.assertEquals(TaskQueueHandlerState.启动中,
                                taskQueueHandler.getState(),
                                "如果有启动前要执行的方法，则程序应该为启动中状态");

        taskQueueHandler.wait2Start(1500);

        Assertions.assertEquals(TaskQueueHandlerState.空闲,
                                taskQueueHandler.getState(),
                                "程序启动后无任务时应该为空闲状态");

        for (int i = 0; i < 10; i++) {
            taskQueueHandler.addTask(UUID.randomUUID());
        }

        Assertions.assertEquals(TaskQueueHandlerState.运行中,
                                taskQueueHandler.getState(),
                                "程序在处理任务时应该为运行中状态");

        taskQueueHandler.wait2Idle(20000);

        Assertions.assertEquals(TaskQueueHandlerState.空闲,
                                taskQueueHandler.getState(),
                                "程序处理完全部任务后应该为空闲状态");

        System.out.println("测试已通过");
    }
}
