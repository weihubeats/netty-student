package client;

import com.google.common.collect.Lists;
import entity.User;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WH
 * @version 1.0
 * @date 2020/5/27 21:21
 * @Description TODO
 */
@Slf4j
public class EchoClientHandler extends ChannelHandlerAdapter {

    private int counter;

    private final int sendNumber;

    private static List<User> users;


    public EchoClientHandler(int sendNumber) {
        this.sendNumber = sendNumber;

        users = new ArrayList<>(sendNumber);
        for (int i = 0; i < sendNumber; i++) {
            String name = "阿离" + i;
            User user = new User(name, i);
            users.add(user);
        }

    }




    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        for (User user : users) {
            ctx.write(user);
            log.info("客户端发送消息为" + user);
            ctx.writeAndFlush(user);
        }
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("接收到服务器的消息: {}", msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
