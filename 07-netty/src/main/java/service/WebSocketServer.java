package service;

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
 * @author WH
 * @version 1.0
 * @date 2020/5/30 23:56
 * @Description TODO
 */
@Slf4j
public class WebSocketServer {

    public void  run(int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //将请求和应答消息编码或解码为HTTP消息
                            pipeline.addLast("http-codec", new HttpServerCodec());
                            //将HTTP消息的多个部分组成一条完整的HTTP消息
                            pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
                            // 用于支持浏览器和服务端进行WebSocker通信
                            pipeline.addLast("http-chunked", new ChunkedWriteHandler());
                            pipeline.addLast("handler", new WebSocketServerHandler());

                        }
                    });
            Channel ch = b.bind(port).sync().channel();
            log.info("Web socket server started at port {}", port);
            log.info("Open your browser and navigate to http://localhost:{}/", port);
            ch.closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        new WebSocketServer().run(port);

    }

}
