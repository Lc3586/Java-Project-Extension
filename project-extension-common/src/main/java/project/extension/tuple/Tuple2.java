package project.extension.tuple;

import com.alibaba.fastjson.annotation.JSONType;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 包含两个元素的元组对象
 *
 * @param <A>
 * @param <B>
 * @author LCTR
 * @date 2022-03-18
 */
@JSONType(ignores = {"count"})
public class Tuple2<A, B>
        implements ITuple {
    public A a;
    public B b;

    public Tuple2() {

    }

    public Tuple2(A a,
                  B b) {
        this.a = a;
        this.b = b;
    }

    @Override
    @JsonIgnore
    public int getCount() {
        return 2;
    }
}
