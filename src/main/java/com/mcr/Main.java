package com.mcr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
@EnableCaching
@EnableScheduling
public class Main {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Main.class);
        application.setApplicationStartup(new BufferingApplicationStartup(2048));
        application.run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ 　Ｓｔａｒｔｉｎｇ   ლ(´ڡ`ლ)ﾞ");
//        ApplicationContext context = SpringApplication.run(Main.class, args);
//        ApplicationContext context = SpringApplication.run(Main.class, args);
//        CorsConfig corsConfig = context.getBean(CorsConfig.class);
//        log.info("CorsConfig bean: {}", corsConfig);
    }
}

// 