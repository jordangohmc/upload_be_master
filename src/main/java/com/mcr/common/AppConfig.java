package com.mcr.common;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;

@Data
@Configuration
public class AppConfig {
    public static final DateTimeFormatter YEAR_MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter DATE_FILE_FORMATTER = DateTimeFormatter.ofPattern("yyMMdd");

    /**
     * Redis
     */
    public static String RedisAuthorize_Path_USER = "AUTHORIZE:USER-";
    public static String RedisAuthorize_Path_TOKEN = "AUTHORIZE:TOKEN-";

}


