package clinet;

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

    private byte[] req;
    private int count;

    public TimeClinetHandler() {
        // 以空格为消息分割符号来进行包的拆分
        String meg = "你好服务器" + System.getProperty("line.separator");
        req = meg.getBytes();
        log.info("发送消息 req: {}", new String(req));
    }

    //当服务器TCP链路建立成功后，调用 channelActive 方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ByteBuf firstMessage = null;
        log.info("开始发送消息");
        for (int i = 0; i < 100; i++) {
            firstMessage = Unpooled.buffer(req.length);
            firstMessage.writeBytes(req);
            ctx.writeAndFlush(firstMessage);
        }

    }

    //当服务器返回应答消息时，调用 channelRead 方法
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
        /*ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "UTF-8");*/
        //直接获取解码的字符串
        String body = (String) msg;
        log.info("服务器返回消息为：{},消息总数为{}",body,++count);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.warn("发生异常" + cause.getMessage());
        ctx.close();
    }

}
