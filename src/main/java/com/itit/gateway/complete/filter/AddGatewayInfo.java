package com.itit.gateway.complete.filter;

import io.netty.handler.codec.http.HttpResponse;

/**
 * response过滤器实现类，添加Header信息
 *
 * @author zhangbotao
 */
public class AddGatewayInfo implements ResponseFilter {

    /**
     * 添加Response的Header信息
     *
     * @param response 相应
     */
    @Override
    public void filter(HttpResponse response) {
//        response.headers().add("GateWay", "GateWay");
    }
}
