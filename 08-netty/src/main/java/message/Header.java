package message;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author WH
 * @version 1.0
 * @date 2020/5/31 13:42
 * @Description TODO
 */
@Data
public final class Header {

    private int crcCode = 0xadaf0105;
    /**
     * 消息长度
     */
    private int length;
    /**
     * 会话ID
     */
    private long sessionId;
    /**
     * 消息类型
     */
    private byte type;
    /**
     * 消息优先级
     */
    private byte priority;
    /**
     * 附件
     */
    private Map<String, Object> attachment = new HashMap<>();

}
