package com.mcr.framework;

import com.mcr.utils.Media;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class StartupRunner implements ApplicationRunner {
    @Value("${TEMP_MEDIA_PATH}")
    private String mediaTempPath;
    @Value("${MEDIA_PATH}")
    private String mediaPath;
    @Value("${FOLDER_PATHS:}")
    private String folderStringPaths; // 注入 categoryPaths 字符串
//    private AppsProperties appsProperties;

    @Override
    public void run(ApplicationArguments args) {
        log.info("初始化，文件夹 [MediaFolderDirectoriesInitializer]");
        initializeMediaDirectories();
        log.info("初始化，文件夹完成");
        log.info("初始化 全局常量...");
        settingApp();
        log.info("全局常量完成");
    }

    public void settingApp() {
        // 执行 createDbTable 方法
//        log.info("getIN{} getOUT{}", appsProperties.getAttInLimit(), appsProperties.getAttOutLimit());
//        if (appsProperties.getAttInLimit() != null) {
//            AppConfig.CheckinLimitExceed = appsProperties.getAttInLimit();
//            log.info("AppConfig.CheckinLimitExceed{}", appsProperties.getAttInLimit());
//        }
//        if (appsProperties.getAttOutLimit() != null) {
//            AppConfig.CheckoutLimitExceed = appsProperties.getAttOutLimit();
//            log.info("AppConfig.CheckoutLimitExceed{}", appsProperties.getAttOutLimit());
//        }
//        log.info("IN{} OUT{}", CheckinLimit, CheckoutLimit);
//        log.info("AppIN{} AppOUT{}", AppConfig.CheckinLimitExceed, AppConfig.CheckoutLimitExceed);
    }

    /**
     * 创建 数据 表单
     */

    private void initializeMediaDirectories() {
        Media.mediaBasePath = mediaPath;
        // 在此 添加 检测 Folder 存在否 然后添加
        String feedbackFolder = "feedback/";

        String[] categoryPaths = {feedbackFolder};
        String[] folderPaths = folderStringPaths.split(","); // 分割 feedbackFolder

        List<String> allPaths = new ArrayList<>();
        allPaths.addAll(Arrays.asList(categoryPaths));
        allPaths.addAll(Arrays.asList(folderPaths));
        for (String folderPath : allPaths) {
            File mediaTempDir = new File(mediaPath + folderPath);
            if (!mediaTempDir.exists()) {
                if (mediaTempDir.mkdirs()) {
                    log.info("创建文件夹 {}", mediaTempDir);
                } else {
                    log.warn("创建文件夹 {}", mediaTempDir);
                }
            }
        }
        File mediaTempDir = new File(mediaTempPath);
        File folder = new File(mediaTempPath);
        if (folder.exists() && folder.isDirectory()) {
            if (folder.exists() && folder.isDirectory()) {
                File[] files = folder.listFiles();
                log.info("清理 Temp 文件夹 {}", mediaTempDir);
                if (files != null) {
                    for (File file : files) {
                        if (file.isFile()) {
                            boolean deleted = file.delete();
                            if (deleted) {
                                log.info("删除成功 Temp:{}", file.getName());
                            } else {
                                log.warn("删除失败 Temp:{}", file.getName());
                            }
                        }
                    }
                }
            }
        }
        if (!mediaTempDir.exists()) {
            if (mediaTempDir.mkdirs()) {
                log.info("创建文件夹 {}", mediaTempDir);
            } else {
                log.warn("创建文件夹 {}", mediaTempDir);
            }
        }
    }
}

//    String attendancePhotoToday = "attendance-photo/" + Utils.sysTodayDate() + "/";
//    String taskDailyMediaToday = "task-daily-media/" + Utils.sysTodayDate() + "/";