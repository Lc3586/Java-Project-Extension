package project.extension.json.fastjson;

import com.alibaba.fastjson.serializer.*;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * 字符串数组序列化器
 *
 * @author LCTR
 * @date 2022-07-28
 */
public class ToStringArraySerializer
        implements ObjectSerializer {
    public static final ToStringArraySerializer instance = new ToStringArraySerializer();

    @Override
    public void write(JSONSerializer serializer,
                      Object object,
                      Object fieldName,
                      Type fieldType,
                      int features)
            throws
            IOException {
        SerializeWriter out = serializer.out;

        if (object == null) {
            out.writeNull(SerializerFeature.WriteNullListAsEmpty);
            return;
        }

        Object[] array = (Object[]) object;
        int size = array.length;

        out.write('[');
        for (int i = 0; i < size; ++i) {
            if (i != 0) {
                out.write(',');
            }
            Object item = array[i];

            if (item == null) {
                if (out.isEnabled(SerializerFeature.WriteNullStringAsEmpty) && object instanceof String[]) {
                    out.writeString("");
                } else {
                    out.writeString("null");
                }
            } else {
                ToStringSerializer.instance.write(serializer,
                                                  item,
                                                  i,
                                                  null,
                                                  0);
            }
        }
        out.write(']');
    }
}
