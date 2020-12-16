package com.itit.gateway.complete.server;

import com.itit.gateway.complete.client.ClientSync;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * @author zhangbotao
 */
public class ServerSyncInitializer extends ChannelInitializer<SocketChannel> {

    private final ClientSync client;

    ServerSyncInitializer(ClientSync clientAsync) {
        this.client = clientAsync;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();

        pipeline.addLast("encoder", new HttpResponseEncoder());
        pipeline.addLast("decoder", new HttpRequestDecoder());
        pipeline.addLast("aggregator", new HttpObjectAggregator(1048576));
        pipeline.addLast("my handler", new ServerSyncHandler(client));
    }
}
