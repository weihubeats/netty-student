package com.zou;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : wh
 * @date : 2021/10/6 10:04
 * @description:
 */
@Slf4j
public class WebSocketServer {

    // /Users/weihu/Desktop/sofe/java/netty-student/netty-websocket/src/main/resources/WebSocketServer.html
    public static void main(String[] args) throws Exception{
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 8080;
        new WebSocketServer().run(port);
    }

    public void run(int port) throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("http-codec", new HttpServerCodec()) // http 编解码处理器
                                    // http多个消息部分组合成一条完整http消息
                                    .addLast("aggregator", new HttpObjectAggregator(65536))
                                    // 支持向客户端发送html5消息，主要用于支持浏览器和服务端进行websocket 通信，如果仅是http服务不需要该处理器
                                    .addLast("http-chunked", new ChunkedWriteHandler())
                                    // 核心业务逻辑处理器
                                    .addLast("handler", new WebSocketServerHandler());
                        }
                    });
            Channel channel = bootstrap.bind(port).sync().channel();
            log.info("Web socket or http server started at port: {}", port);
            log.info("open your browser and navigate to http://localhost:{}/",port);
            channel.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

}
