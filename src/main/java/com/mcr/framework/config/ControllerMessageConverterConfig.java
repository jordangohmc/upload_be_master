package com.mcr.framework.config;

import com.mcr.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Slf4j
@Configuration
public class ControllerMessageConverterConfig {

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        log.info("扩展mvc框架的信息转化器...");
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        return messageConverter;
    }

    @Bean
    public WebMvcConfigurer controllerWebMvcConfigurer(MappingJackson2HttpMessageConverter messageConverter) {
        return new WebMvcConfigurer() {
            @Override
            public void configureMessageConverters(@NotNull List<HttpMessageConverter<?>> converters) { //
                converters.add(1, messageConverter);
            }
        };
    }
}
