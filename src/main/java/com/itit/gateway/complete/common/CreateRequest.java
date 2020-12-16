package com.itit.gateway.complete.common;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;

/**
 * 创建基于Netty的HTTP Request
 *
 * @author zhangbotao
 */
public class CreateRequest {

    public static FullHttpRequest create(FullHttpRequest fullHttpRequest) {
        return new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_1, HttpMethod.GET, fullHttpRequest.uri(), Unpooled.EMPTY_BUFFER);
    }

    public static FullHttpRequest create(String method, String uri, String version) {
        HttpMethod httpMethod = HttpMethod.valueOf(method);
        HttpVersion httpVersion = HttpVersion.valueOf(version);
        return new DefaultFullHttpRequest(httpVersion, httpMethod, uri, Unpooled.EMPTY_BUFFER);
    }
}
