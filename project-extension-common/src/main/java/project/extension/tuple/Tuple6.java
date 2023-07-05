package project.extension.tuple;

import com.alibaba.fastjson.annotation.JSONType;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 包含六个元素的元组对象
 *
 * @param <A>
 * @param <B>
 * @param <C>
 * @param <D>
 * @param <E>
 * @param <F>
 * @author LCTR
 * @date 2022-03-18
 */
@JSONType(ignores = {"count"})
public class Tuple6<A, B, C, D, E, F>
        implements ITuple {
    public A a;
    public B b;
    public C c;
    public D d;
    public E e;
    public F f;

    public Tuple6(A a,
                  B b,
                  C c,
                  D d,
                  E e,
                  F f) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
    }

    @Override
    @JsonIgnore
    public int getCount() {
        return 6;
    }
}
