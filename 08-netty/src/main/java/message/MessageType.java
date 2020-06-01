package message;

/**
 * @author WH
 * @version 1.0
 * @date 2020/5/31 13:53
 * @Description 消息类型枚举
 */
public enum MessageType {

    /**
     * 业务请求消息
     */
    SERVICE_REQ((byte) 0),
    /**
     * 业务响应（应答）消息
     */
    SERVICE_RESP((byte) 1),
    /**
     * 业务ONE WAY消息（既是请求消息又是响应消息）
     */
    ONE_WAY((byte) 2),
    /**
     * 握手请求消息
     */
    LOGIN_REQ((byte) 3),
    /**
     * 握手响应（应答）消息
     */
    LOGIN_RESP((byte) 4),
    /**
     * 心跳请求消息
     */
    HEARTBEAT_REQ((byte) 5),
    /**
     * 心跳响应（应答）消息
     */
    HEARTBEAT_RESP((byte) 6);


    private byte value;

    MessageType(byte value) {
        this.value = value;
    }

    public byte value() {
        return value;
    }


}
