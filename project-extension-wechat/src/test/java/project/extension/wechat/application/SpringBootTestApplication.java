package project.extension.wechat.application;

import ch.qos.logback.core.ConsoleAppender;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 测试用的应用
 *
 * @author LCTR
 * @date 2023-03-17
 */
@org.springframework.boot.autoconfigure.SpringBootApplication(exclude = {DataSourceAutoConfiguration.class},
                                                              scanBasePackages = {"project.extension"})
public class SpringBootTestApplication {
    public static void main(String[] args) {
        System.out.printf("\033[32mSpringBootTestApplication.main\033[0m \033[33margs\033[0m : %s\r\n%n",
                          String.join(" ",
                                      args));

        System.out.printf("\033[34mslf4j\033[0m ：%s%n",
                          LoggerFactory.class.getResource(""));
        System.out.printf("\033[34mlogback\033[0m ：%s%n",
                          ConsoleAppender.class.getResource(""));

        SpringApplication.run(SpringBootTestApplication.class,
                              args);
    }
}
