package client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author WH
 * @version 1.0
 * @date 2020/5/24 22:43
 * @Description TODO
 */
@Slf4j
public class TimeClinetHandler extends ChannelHandlerAdapter {

    private final ByteBuf firstMessage;

    public TimeClinetHandler() {
        byte[] req = "你好服务器".getBytes();
        firstMessage = Unpooled.buffer(req.length);
        firstMessage.writeBytes(req);
        log.info("发送消息 req: {}", new String(req));
    }

    //当服务器TCP链路建立成功后，调用 channelActive 方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("开始发送消息");
        ctx.writeAndFlush(firstMessage);
    }

    //当服务器返回应答消息时，调用 channelRead 方法
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "UTF-8");
        log.info("服务器返回消息为：" + body);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.warn("发生异常" + cause.getMessage());
        ctx.close();
    }

}
