package com.mcr.framework;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class SysScheduledTask {

    @Scheduled(cron = "0 0 0 2 * ?") // 每个月的最后第二天执行
//    @Scheduled(cron = "15 * * * * ?") // 每分钟测试执行
    public void createDbTable() {
        LocalDate today = LocalDate.now();
        LocalDate nextMonth = today.plusMonths(1);
        String yearMonth = nextMonth.format(DateTimeFormatter.ofPattern("yyyyMM"));
        log.info("执行 每分钟的第15秒 createDbTable 方法：{}", yearMonth);
    }

    //    @Scheduled(cron = "0 0/1  * * * ?") // 每个15秒执行
    @Scheduled(cron = "30 0/1 * * * ?") // 每个15秒执行
    public void IoTService() {
    }

    @Scheduled(cron = "00 0/5 * * * ?") // 每个5分钟执行
    public void saveSystemStatus() {
    }
}
