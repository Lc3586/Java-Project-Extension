package project.extension.tuple;

import com.alibaba.fastjson.annotation.JSONType;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 包含五个元素的元组对象
 *
 * @param <A>
 * @param <B>
 * @param <C>
 * @param <D>
 * @param <E>
 * @author LCTR
 * @date 2022-03-18
 */
@JSONType(ignores = {"count"})
public class Tuple5<A, B, C, D, E>
        implements ITuple {
    public A a;
    public B b;
    public C c;
    public D d;
    public E e;

    public Tuple5(A a,
                  B b,
                  C c,
                  D d,
                  E e) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
    }

    @Override
    @JsonIgnore
    public int getCount() {
        return 5;
    }
}
