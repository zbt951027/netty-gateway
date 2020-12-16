package com.itit.gateway.complete.filter;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 容器启动时，初始化过滤器初始化类
 *
 * @author zhangbotao
 */
@Component
@Order(1)
public class Filter implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(Filter.class);

    private static final FilterChainSingleton FILTER_CHAIN_SINGLETON = FilterChainSingleton.getInstance();

    private static void addRequestFilter(RequestFilter requestFrontFilter) {
        FILTER_CHAIN_SINGLETON.registerRequestFrontFilter(requestFrontFilter);
    }

    private static void addResponseFilter(ResponseFilter responseBackendFilter) {
        FILTER_CHAIN_SINGLETON.registerResponseBackendFilter(responseBackendFilter);
    }

    /**
     * 在这个方法中添加Request的过滤操作类,在启动函数中进行调用
     */
    static void initRequestFilter() {
        addRequestFilter(new MethodToPost());
    }

    /**
     * 在这个方法中添加Response的过滤操作类，在启动函数中进行调用
     */
    static void initResponseFilter() {
        addResponseFilter(new AddGatewayInfo());
    }

    /**
     * 遍历Request过滤操作链，对Request进行处理，在Server inbound接收到Request后进行调用
     *
     * @param request request
     */
    static public void requestProcess(HttpRequest request) {
        for (RequestFilter filter : FILTER_CHAIN_SINGLETON.getRequestFrontFilterList()) {
            filter.filter(request);
        }
    }

    /**
     * 调用Response过滤操作链，对Response进行处理，在Server outbound发送Response前进行调用
     *
     * @param response response
     */
    static public void responseProcess(HttpResponse response) {
        for (ResponseFilter filter : FILTER_CHAIN_SINGLETON.getResponseBackendFilters()) {
            filter.filter(response);
        }
    }

    @Override
    public void run(ApplicationArguments args) {
        Filter.initRequestFilter();
        Filter.initResponseFilter();
        logger.info("Filter line load successfully");
    }
}
