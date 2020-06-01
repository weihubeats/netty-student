package message;

import lombok.Data;

/**
 * @author WH
 * @version 1.0
 * @date 2020/5/31 13:40
 * @Description TODO
 */
@Data
public final class NettyMessage {
    /**
     * 消息头
     */
    private Header header;
    /**
     * 消息体
     */
    private Object body;


}
