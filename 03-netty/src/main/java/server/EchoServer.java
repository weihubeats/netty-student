package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author WH
 * @version 1.0
 * @date 2020/5/27 20:40
 * @Description 基于DelimiterBasedFrameDecoder 自定义分隔符解码器
 */
public class EchoServer {

    public static void main(String[] args){
        //配置服务端的NIO线程租
        EventLoopGroup boosGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {


            ServerBootstrap b = new ServerBootstrap();
            b.group(boosGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        public void initChannel(SocketChannel channel) {
                            //创建分隔符缓冲对象ByteBuf，以$_为分隔符
                            ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
                            //创建 DelimiterBasedFrameDecoder对象并加入到ChannelPipeline
                            // 1024表示单挑消息最大长度，超过该长度还没有找到分隔符$_则抛出TooLongFrameException一次
                            //防止因异常码流缺失分隔符导致内存溢出
                            // delimiter就是分隔符缓冲对象
                            channel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
                            channel.pipeline().addLast(new StringDecoder());
                            channel.pipeline().addLast(new EchoServerHandler());
                        }
                    });
            //绑定端口，同步等待成功
            ChannelFuture f = b.bind(8080).sync();
            //等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //优雅退出，是否线程池资源
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
