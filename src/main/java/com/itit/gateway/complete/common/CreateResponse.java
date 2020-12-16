package com.itit.gateway.complete.common;

import com.itit.gateway.complete.client.CustomClientSyncHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpHeaderValues.*;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;

/**
 * 创建基于Netty的HTTP Response
 *
 * @author zhangbotao
 */
public class CreateResponse {
    private static final Logger logger = LoggerFactory.getLogger(CreateResponse.class);

    static public FullHttpResponse createResponse(FullHttpResponse fullHttpResponse) {
        FullHttpResponse response = new DefaultFullHttpResponse(fullHttpResponse.protocolVersion(), OK,
                getByteBuf(fullHttpResponse));
        response.headers()
                .set(CONTENT_TYPE, TEXT_PLAIN)
                .setInt(CONTENT_LENGTH, response.content().readableBytes());

        boolean keepAlive = HttpUtil.isKeepAlive(fullHttpResponse);
        if (keepAlive) {
            if (!fullHttpResponse.protocolVersion().isKeepAliveDefault()) {
                response.headers().set(CONNECTION, KEEP_ALIVE);
            }
        } else {
            // Tell the client we're going to close the connection.
            response.headers().set(CONNECTION, CLOSE);
        }
        return response;
    }

    private static ByteBuf getByteBuf(FullHttpResponse msg) {
        if (msg != null) {
            logger.info("返回内容: {}", msg.content().toString(CharsetUtil.UTF_8));
            return Unpooled.wrappedBuffer(msg.content().toString(CharsetUtil.UTF_8).getBytes());
        }
        return Unpooled.EMPTY_BUFFER;
    }

    public static FullHttpResponse creat404(FullHttpRequest request) {
        FullHttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(), NOT_FOUND,
                Unpooled.EMPTY_BUFFER);
        response.headers()
                .set(CONTENT_TYPE, TEXT_PLAIN)
                .setInt(CONTENT_LENGTH, response.content().readableBytes());
        response.headers().set(CONNECTION, CLOSE);
        return response;
    }
}
