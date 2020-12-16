package com.itit.gateway.complete.common;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;

import java.util.HashMap;
import java.util.Map;

public class HttpUtil {
    public static Map<String, Object> getRequestHeader(FullHttpRequest request) {
        HttpHeaders headers = request.headers();
        Map<String, Object> headerMap = new HashMap<>();
        headers.names().forEach(name -> {
            headerMap.put(name, headers.get(name));
        });
        return headerMap;
    }
}
