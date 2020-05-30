package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author WH
 * @version 1.0
 * @date 2020/5/30 18:28
 * @Description TODO
 */
@Slf4j
public class HttpFileServer {

    private static final String DEFAULT_URL = "/src/main";

    public void run(final int port, final String url) throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        public void initChannel(SocketChannel ch) throws Exception{
                            // http解码器会生成多个消息对象
                            ch.pipeline().addLast("http-decoder", new HttpRequestDecoder());
                            // HttpObjectAggregator 解码器 将多个消息转换为单一的 FullHttpRequest 或者 FullHttpResponse
                            ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));
                            // 添加 HTTP 响应编码器对 HTTP 响应进行编码
                            ch.pipeline().addLast("http-encoder", new HttpResponseEncoder());
                            // 添加 ChunkedWriteHandler 支持异步发送大的码流(例如打的文件)，但不占用过多的内存，防止发送Java内存溢出
                            ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                            // 添加 HttpFileServerHandler 用于文件服务器业务处理
                            ch.pipeline().addLast("fileServerHandler", new HttpFileServerHandler(url));

                        }
                    });
            ChannelFuture future = b.bind("127.0.0.1", port).sync();
            log.info("HTTP 文件目录服务器启动，网址为: http://127.0.0.1:{}{}",port, url);
            future.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws Exception{
        int port = 8080;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        String url = DEFAULT_URL;
        if (args.length > 1) {
            url = args[1];
        }
        new HttpFileServer().run(port, url);


    }
}
