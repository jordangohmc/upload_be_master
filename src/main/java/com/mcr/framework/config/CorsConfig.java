package com.mcr.framework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;


@Configuration
public class CorsConfig {
    @Value("${myapp.serverIp}")
    private String serverIp;
    @Value("${myapp.allowIps:}")
    private List<String> allowIps;

    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //  你需要跨域的地址  注意这里的 127.0.0.1 != localhost
        corsConfiguration.addAllowedOrigin("http://" + serverIp);

        if (allowIps != null && !allowIps.isEmpty()) {
            for (String localIp : allowIps) {
                if (localIp != null && !localIp.isEmpty()) { // 防止 IP 为空或空字符串
                    corsConfiguration.addAllowedOrigin("http://" + localIp);
                    corsConfiguration.addAllowedOrigin("https://" + localIp);
                }
            }
        }
        //  跨域的请求头
        // * 表示对所有的地址都可以访问
        corsConfiguration.addAllowedHeader("*"); // 2
        //  跨域的请求方法
        corsConfiguration.addAllowedMethod("*"); // 3
        //最终的结果是可以 在跨域请求的时候获取同一个 session
        corsConfiguration.setAllowCredentials(true);
        return corsConfiguration;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        //配置 可以访问的地址
        source.registerCorsConfiguration("/**", buildConfig()); // 4
        return new CorsFilter(source);
    }
}