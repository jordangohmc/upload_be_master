//package com.tf.framework.config;
//
//
//import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
//import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
//import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
//import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
//import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//
//
///**
// * 配置MP的分页插件
// */
//@Configuration
//public class MybatisPlusConfig {
//    @Bean
//    public MybatisPlusInterceptor mybatisPlusInterceptor() {
//        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
//        // 阻止全表更新与删除
//        BlockAttackInnerInterceptor blockAttackInnerInterceptor = new BlockAttackInnerInterceptor();
//        interceptor.addInnerInterceptor(blockAttackInnerInterceptor);
//        // 添加动态表
//        DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();
////        dynamicTableNameInnerInterceptor.setTableNameHandler(
////                //可以传多个表名参数，指定哪些表使用MonthTableNameHandler处理表名称
////                new TableNameHandler("tb_employee_attendance")
////        );
//        interceptor.addInnerInterceptor(dynamicTableNameInnerInterceptor);
//        // 添加分页插件
//        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
//        // 添加乐观锁插件
//        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
//        return interceptor;
//    }
//}
//
//
////    @Bean
////    @Profile({"dev", "test"})
////    public PerformanceInterceptor performanceInterceptor() {
////        return new PerformanceInterceptor();
////    }