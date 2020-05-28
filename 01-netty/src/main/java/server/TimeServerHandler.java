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

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
        //将msg转换成Netty的ByteBuf对象
        ByteBuf buf = (ByteBuf) msg;
        //buf.readableBytes()获取缓冲区可读的字节数
        byte[] req = new byte[buf.readableBytes()];
        // 将缓冲区的字节数组复制到新的byte数组中
        buf.readBytes(req);
        String body = new String(req, "UTF-8");
        //请求信息
        log.info("客户端请求参数boyd: {}", body);
        String message = "服务端收到了你的消息：" + body;
        ByteBuf resp = Unpooled.copiedBuffer(message.getBytes());
        ctx.write(resp);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception{
        //将消息发送队列中的消息写入到SocketChannel中发送给客户端
        ctx.flush();

    }

    // 客户端断开连接监听
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.info("客户端断开了连接");
        ctx.close();
    }

}
