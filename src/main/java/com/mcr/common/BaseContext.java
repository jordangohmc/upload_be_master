package com.mcr.common;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseContext {
    private static final ThreadLocal<Long> threadLocal = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> isLoginThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> isDeleteThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<String> versionThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<String> mobileVersionThreadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        return threadLocal.get();
    }

    public static void setVersion(String version) {
        log.info("setVersion PROFILE_MOBILE_VERSION{}", version);
        versionThreadLocal.set(version);
    }

    public static String getVersion() {
        return versionThreadLocal.get();
    }

    public static void setMobileVersion(String version) {
        mobileVersionThreadLocal.set(version);
    }

    public static String getMobileVersion() {
        return mobileVersionThreadLocal.get();
    }

}
