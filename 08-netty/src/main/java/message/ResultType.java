package message;

/**
 * @author WH
 * @version 1.0
 * @date 2020/5/31 13:55
 * @Description TODO
 */
public enum ResultType {
    /**
     * 认证成功
     */
    SUCCESS((byte) 0),
    /**
     * 认证失败
     */
    FAIL((byte) -1),
    ;


    private byte value;

    private ResultType(byte value) {
        this.value = value;
    }

    public byte value() {
        return this.value;
    }


    }
