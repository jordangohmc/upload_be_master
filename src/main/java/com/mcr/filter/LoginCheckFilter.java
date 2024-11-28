package com.mcr.filter;

import com.alibaba.fastjson.JSON;
import com.mcr.common.AppConfig;
import com.mcr.common.BaseContext;
import com.mcr.common.R;
import com.mcr.common.entity.TenantEmployee;
import com.mcr.framework.config.cache.RedisUtils;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;
import java.util.Enumeration;

//    @Value("${myapp.serverIp}")
//    private String serverIp;
//@CrossOrigin(origins = {"http://localhost:5003", "http://localhost:8080", "${myapp.serverIp}"}, maxAge = 3600, allowedHeaders = "*", allowCredentials = "true")
@Slf4j
@Component
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    private static final Logger apiLogger = LoggerFactory.getLogger("APILogger");

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            filterChain.doFilter(request, response);
            return;
        }
        String userId = null;
        String clientIP = getClientIpAddress(request);
        String requestURI = request.getRequestURI();
        String reqMethod = request.getMethod();
        String authorizeToken = request.getHeader("AUTHORIZE_TOKEN");
        String serverToken = request.getHeader("SERVER_TOKEN");
        String browser = getBrowser(request);
        String otherInfo = getOtherInfo(request);

        if (serverToken != null) {
            userId = "800000";
            apiLogger.info("ID[{}]-API[{}][{}]-TKN[{}]-IP[{}]-BRS[{}]{}", userId, reqMethod, requestURI, authorizeToken, clientIP, browser, otherInfo);
            BaseContext.setCurrentId(Long.parseLong(userId));
            filterChain.doFilter(request, response);
            return;
        }

        String[] urls = new String[]{
                "/employee/login",
                "/user/login",
                "/user/forget",
                "/dist/**",
                "/test/**",
                "/swagger-ui/**",
                "/favicon.ico",
                "/v3/**"
        };
        boolean check = check(urls, requestURI);
        if (check) {
            boolean isLogin = PATH_MATCHER.match("/user/login", requestURI);
            if (isLogin) {
                apiLogger.info("LOGIN请求-API[{}][{}]-IP[{}]-BRS[{}]{}", reqMethod, requestURI, clientIP, browser, otherInfo);
            } else {
                apiLogger.info("OTHER请求-URI[{}]-IP[{}]-BRS[{}]", clientIP, requestURI, browser);
            }
            filterChain.doFilter(request, response);
            return;
        }
        //WebSocket
        if (PATH_MATCHER.match("/ws/**", requestURI)) {
            apiLogger.warn("WEBSOCKET升级-BRS[{}]-IP[{}]", browser, clientIP);
            // 將提取出的部分賦值給 authorizeToken
            authorizeToken = requestURI.substring(requestURI.indexOf("/ws/") + 4);
        }

        if (authorizeToken != null) {
            TenantEmployee tenantEmp = RedisUtils.getCacheObject(AppConfig.RedisAuthorize_Path_TOKEN + authorizeToken);
            if (tenantEmp != null)
                userId = tenantEmp.getEmpId().toString();
        }

        /*请求非法-TOKEN无效-返回异常*/
        if (authorizeToken == null || userId == null) {
            String errorMessage = authorizeToken == null ? "Illegal Require !" : "TOKEN expired !";
            String logMessage = authorizeToken == null ? "NO-TOKEN" : "TOKEN EXPIRED";
            apiLogger.warn("拦截到[{}]请求-API[{}][{}]-IP[{}]{}", logMessage, reqMethod, requestURI, clientIP, otherInfo);
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Origin", "*"); // 设置允许的源  localIp
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS"); // 设置允许的 HTTP 方法
            response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type"); // 设置允许的请求头
            String errorResponse = JSON.toJSONString(R.error(errorMessage));
            response.getWriter().write(errorResponse);
            return;
        }

        /*请求合法-正常返回*/
//        String version = redisTemplate.opsForValue().get("PROFILE_VERSION");
//        String mobileVersion = redisTemplate.opsForValue().get("PROFILE_MOBILE_VERSION");
//        BaseContext.setVersion(version != null ? version : "Error");
//        BaseContext.setMobileVersion(mobileVersion != null ? mobileVersion : "1600000000");

        apiLogger.info("ID[{}]-API[{}][{}]-TKN[{}]-IP[{}]-BRS[{}]{}", userId, reqMethod, requestURI, authorizeToken, clientIP, browser, otherInfo);
        BaseContext.setCurrentId(Long.parseLong(userId));

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            System.out.println(headerName + ": " + request.getHeader(headerName));
        }
        // 检查 Content-Type 是否为 multipart/form-data
        String contentType = request.getContentType();
        System.out.println("Content-Type: " + contentType);

        // 打印请求的路径和参数
        System.out.println("Request URL: " + request.getRequestURL());
        System.out.println("Request Method: " + request.getMethod());
        filterChain.doFilter(request, response);

    }

    private String getOtherInfo(HttpServletRequest request) {
        String result = "";
        if (request.getQueryString() != null) {
            result = "-QUERY[" + request.getQueryString() + "]";
        }
        return result;
    }

    private String getBrowser(HttpServletRequest request) {
//        String allHeaders = Collections.list(request.getHeaderNames())
//                .stream()
//                .map(headerName -> headerName + ": " + request.getHeader(headerName))
//                .collect(Collectors.joining("\n"));
//        log.info("All Headers:\n{}", allHeaders);
        String userAgent = request.getHeader("User-Agent");
        log.info(userAgent);
        String browserType = "Unknown" + userAgent;
        if (userAgent != null) {
            userAgent = userAgent.toLowerCase();
            if (userAgent.contains("msie")) {
                browserType = "Internet Explorer";
            } else if (userAgent.contains("firefox")) {
                browserType = "Firefox";
            } else if (userAgent.contains("chrome")) {
                browserType = "Chrome";
            } else if (userAgent.contains("safari")) {
                browserType = "Safari";
            } else if (userAgent.contains("opera")) {
                browserType = "Opera";
            } else if (userAgent.contains("android")) {
                browserType = "Android";
            } else if (userAgent.contains("iphone")) {
                browserType = "iPhone";
            }
        }
        return browserType;
    }

    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Real-IP");
        if (xForwardedForHeader == null) {
            return request.getRemoteAddr();
        }
        return xForwardedForHeader;
    }
}

