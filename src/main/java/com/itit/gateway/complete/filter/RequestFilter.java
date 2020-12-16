package com.itit.gateway.complete.filter;

import io.netty.handler.codec.http.HttpRequest;

/**
 * 请求过滤器
 * @author zhangbotao
 */
public interface RequestFilter {

    /**
     * 对请求进行处理
     * @param request 请求
     */
    void filter(HttpRequest request);
}
