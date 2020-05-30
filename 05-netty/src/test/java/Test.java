import com.google.common.collect.Lists;
import org.msgpack.MessagePack;
import org.msgpack.template.Templates;

import java.util.List;

/**
 * @author WH
 * @version 1.0
 * @date 2020/5/28 23:41
 * @Description TODO
 */
public class Test {

    @org.junit.Test
    public void test() throws Exception{

        List<String> list = Lists.newArrayList();
        list.add("msgpack");
        list.add("kumofs");
        list.add("viver");

        MessagePack msgpack = new MessagePack();
        // Serialize
        byte[] raw = msgpack.write(list);
        // Deserialize directly using a template(直接使用模板反序列化)
        List<String> list1 = msgpack.read(raw, Templates.tList(Templates.TString));
        list.forEach(s -> System.out.println(s));
    }
}
