package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.msgpack.MessagePack;
import org.msgpack.annotation.Message;

/**
 * @author WH
 * @version 1.0
 * @date 2020/5/29 0:07
 * @Description TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Message
public class User{

    private String name;
    private Integer age;
}
