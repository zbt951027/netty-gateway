package com.itit.gateway.complete.client;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

/**
 * 同步客户端
 *
 * @author zhangbotao
 */
public interface ClientSync {

    /**
     * 任务执行：将请求转发到后台服务器，获得响应后返回
     *
     * @param request        请求
     * @param serverOutbound server outbound
     * @return
     */
    FullHttpResponse execute(FullHttpRequest request, Channel serverOutbound);
}
