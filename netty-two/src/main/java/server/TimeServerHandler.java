package server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author WH
 * @version 1.0
 * @date 2020/5/24 21:48
 * @Description 继承ChannelHandlerAdapter 对网络事件进行读写
 */
@Slf4j
public class TimeServerHandler extends ChannelHandlerAdapter {

    private int count;

    //客户端返回结果调用
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
        System.out.println("返回消息");
        String body = (String) msg;
        //请求信息
        log.info("客户端请求参数boyd: {}", body);
        // 以空格为消息分割符号来进行包的拆分
        String message = "服务端收到了你的消息：" + body + System.getProperty("line.separator");
        log.info("服务端收到的消息总数count: {}", ++count);
        ByteBuf resp = Unpooled.copiedBuffer(message.getBytes());
        ctx.writeAndFlush(resp);

    }


    // 客户端断开连接监听
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.info("客户端断开了连接");
        ctx.close();
    }

}
