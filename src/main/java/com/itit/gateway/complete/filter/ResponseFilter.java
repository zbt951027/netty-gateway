package com.itit.gateway.complete.filter;

import io.netty.handler.codec.http.HttpResponse;

/**
 * 返回结果过滤器
 *
 * @author zhangbotao
 */
public interface ResponseFilter {

    /**
     * 对返回进行处理
     *
     * @param response 响应
     */
    void filter(HttpResponse response);
}
