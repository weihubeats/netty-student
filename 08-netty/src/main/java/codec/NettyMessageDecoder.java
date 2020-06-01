package codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import message.Header;
import message.NettyMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author WH
 * @version 1.0
 * @date 2020/5/31 13:48
 * @Description TODO
 */
public final class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {

    MarshallingDecoder marshallingDecoder;

    /**
     *
     * @param maxFrameLength 第一个参数代表最大的序列化长度
     * @param lengthFieldOffset 代表长度属性的偏移量 简单来说就是message中 总长度的起始位置（Header中的length属性的起始位置）   本例中为4
     * @param lengthFieldLength 代表长度属性的长度 整个属性占多长（length属性为int，占4个字节）  4
     * @throws IOException
     */
    public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset,
                               int lengthFieldLength) throws IOException {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
        marshallingDecoder = new MarshallingDecoder();
}

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in)
            throws Exception {
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        if (frame == null) {
            return null;
        }

        NettyMessage message = new NettyMessage();
        Header header = new Header();
        // 加通信标记认证逻辑
        header.setCrcCode(frame.readInt());
        header.setLength(frame.readInt());
        header.setSessionId(frame.readLong());
        header.setType(frame.readByte());
        header.setPriority(frame.readByte());

        int size = frame.readInt();
        //附件个数大于0，则需要解码操作
        if (size > 0) {
            Map<String, Object> attch = new HashMap<String, Object>(size);
            int keySize = 0;
            byte[] keyArray = null;
            String key = null;
            for (int i = 0; i < size; i++) {
                keySize = frame.readInt();
                keyArray = new byte[keySize];
                frame.readBytes(keyArray);
                key = new String(keyArray, "UTF-8");
                attch.put(key, marshallingDecoder.decode(frame));
            }
            keyArray = null;
            key = null;
            //解码完成放入attachment
            header.setAttachment(attch);
        }
        message.setHeader(header);
        //对于ByteBuf来说，读一个数据，就会少一个数据，所以读完header，剩下的应该就是body了
        if (frame.readableBytes() > 4) {
            message.setBody(marshallingDecoder.decode(frame));
        }

        return message;
    }
}
