package com.itit.gateway.complete.route;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 容器启动时，初始化路由表
 *
 * @author zhangbotao
 */
@Component
@Order(1)
public class RouteTable implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(RouteTable.class);

    static final private RouteTableSingleton ROUTE = RouteTableSingleton.getInstance();

    static void initTable() {
        ROUTE.readJsonConfig();
    }

    @Deprecated
    static public Map<String, String> getTarget(String url) {
        return ROUTE.getTarget(url);
    }

    static public String getTargetUrl(String url) {
        return ROUTE.getTargetUrl(url);
    }

    @Override
    public void run(ApplicationArguments args) {
        RouteTable.initTable();
        logger.info("Route table init successfully");
    }
}
