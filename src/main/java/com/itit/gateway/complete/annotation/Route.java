package com.itit.gateway.complete.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 自定义注解
 * 用于标注函数运行前的request的路由转发设置
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Component
@Documented
public @interface Route {
}
