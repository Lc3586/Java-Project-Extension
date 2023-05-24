package project.extension.tuple;

import com.alibaba.fastjson.annotation.JSONType;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 包含四个元素的元组对象
 *
 * @param <A>
 * @param <B>
 * @param <C>
 * @param <D>
 * @author LCTR
 * @date 2022-03-18
 */
@JSONType(ignores = {"count"})
public class Tuple4<A, B, C, D>
        implements ITuple {
    public A a;
    public B b;
    public C c;
    public D d;

    public Tuple4(A a,
                  B b,
                  C c,
                  D d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    @Override
    @JsonIgnore
    public int getCount() {
        return 4;
    }
}
