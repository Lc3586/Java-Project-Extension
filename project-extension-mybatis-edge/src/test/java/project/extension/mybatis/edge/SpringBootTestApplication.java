package project.extension.mybatis.edge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 测试用的应用
 *
 * @author LCTR
 * @date 2022-12-15
 */
@org.springframework.boot.autoconfigure.SpringBootApplication(exclude = {DataSourceAutoConfiguration.class},
                                                              scanBasePackages = {"project.extension"})
public class SpringBootTestApplication {
    public static void main(String[] args) {
        System.out.printf("\033[32mSpringBootTestApplication.main\033[0m \033[33margs\033[0m : %s\r\n%n",
                          String.join(" ",
                                      args));

        SpringApplication.run(SpringBootTestApplication.class,
                              args);
    }
}
