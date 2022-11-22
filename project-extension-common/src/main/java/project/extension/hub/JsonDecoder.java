package project.extension.hub;

import com.alibaba.fastjson.JSON;
import org.atmosphere.config.managed.Decoder;

/**
 * JSON数据编码器
 *
 * @author LCTR
 * @date 2022-10-28
 */
public class JsonDecoder
        implements Decoder<Object, String> {
    @Override
    public String decode(Object s) {
        return JSON.toJSONString(s);
    }
}
