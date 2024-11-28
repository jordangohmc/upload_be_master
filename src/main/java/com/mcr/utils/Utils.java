package com.mcr.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
public class Utils {
    public static LocalDate sysTodayDate() {
        return LocalDate.now();
    }

}

//    public static boolean validFormat(String dateStr, @NotNull DateTimeFormatter formatter) {
//        try {
//            formatter.parse(dateStr);
//            return true;
//        } catch (DateTimeParseException e) {
//            return false;
//        }
//    }
//    public static boolean isValidDateFormat(String dateStr) {
//        return dateStr == null || dateStr == "" || validFormat(dateStr, DATE_FORMATTER);
//    }
//
//    public static boolean isValidDateTimeFormat(String dateStr) {
//        return dateStr == null || dateStr == "" || validFormat(dateStr, DATE_TIME_FORMATTER);
//    }
//
//    public static String getClientIpAddress(HttpServletRequest request) {
//        String xForwardedForHeader = request.getHeader("X-Real-IP");
//        if (xForwardedForHeader == null) {
//            return request.getRemoteAddr();
//        }
//        return xForwardedForHeader;
//    }
//
//    public static Long sysNowTime() {
//        LocalDateTime now = LocalDateTime.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
//        return Long.parseLong(now.format(formatter));
//    }
//    private <E> Long getEntityUid(E entity) {
//        // 示例：假设您的实体对象具有名为 "uid" 的 Long 类型属性
//        try {
//            Method method = entity.getClass().getMethod("getUid");
//            Long uid = (Long) method.invoke(entity);
//            return (uid != null) ? uid : 0L; // 如果 uid 为空，则返回默认值（例如 0L）
//        } catch (Exception e) {
//            e.printStackTrace();
//            return 0L; // 发生异常时返回默认值
//        }
//    }
//
//    private <E> Long getParentUid(E entity) {
//        // 示例：假设您的实体对象具有名为 "parentUid" 的 Long 类型属性
//        try {
//            Method method = entity.getClass().getMethod("getParentUid");
//            Long parentUid = (Long) method.invoke(entity);
//            return (parentUid != null) ? parentUid : 0L; // 如果 parentUid 为空，则返回默认值（例如 0L）
//        } catch (Exception e) {
//            e.printStackTrace();
//            return 0L; // 发生异常时返回默认值
//        }
//    }
//
//    //    private <E> String getPropertyValue(E entity, String methodName) {
//    private <E> String getPropertyValue(E entity) {
//        try {
//            Method method = entity.getClass().getMethod("getName");
//            return (String) method.invoke(entity);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//    public static LocalDateTime sysPassDate(int day) {
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime time = now.with(LocalTime.MIDNIGHT);
//        time = time.minusDays(day);
//        return time;
//    }
//
//    public static StringBuilder base64ToHexConversion(String base64Data) {
//        // 解码Base64数据
//        byte[] decodedBytes = Base64.getDecoder().decode(base64Data);
//        // 转换为十六进制
//        StringBuilder hexData = new StringBuilder();
//        for (byte b : decodedBytes) {
//            hexData.append(String.format("%02X", b));
//        }
//        System.out.println("Hex Data: " + hexData);
//        return hexData;
//    }
//
//    public static byte[] base64ToByteConversion(String base64Data) {
//        return Base64.getDecoder().decode(base64Data);
//    }
//
//
//    public static Long HexStringToLong(String hexString) {
//        // 检查是否为有效的十六进制字符串
//        if (hexString.matches("^[0-9A-Fa-f]+$")) {
//            try {
//                long result = 0;
//                for (int i = 0; i < hexString.length(); i += 16) {
//                    int endIndex = Math.min(i + 16, hexString.length());
//                    String subHex = hexString.substring(i, endIndex);
//                    long subResult = Long.parseLong(subHex, 16); // 明确指定基数为16
//                    result = (result << 64) | subResult;
//                }
//                return result;
//            } catch (NumberFormatException e) {
//                // 转换失败，处理异常
//                System.out.println("Failed to convert Hex String to Long: " + hexString);
//                e.printStackTrace();
//                return null; // 或者返回一个默认值，或者抛出异常，取决于您的需求
//            }
//        } else {
//            // 字符串不是有效的十六进制字符串
//            System.out.println("Invalid Hex String: " + hexString);
//            return null; // 或者返回一个默认值，或者抛出异常，取决于您的需求
//        }
//    }