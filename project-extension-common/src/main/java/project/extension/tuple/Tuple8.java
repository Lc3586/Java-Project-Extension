package project.extension.tuple;

import com.alibaba.fastjson.annotation.JSONType;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 包含七个元素的元组对象
 *
 * @param <A>
 * @param <B>
 * @param <C>
 * @param <D>
 * @param <E>
 * @param <F>
 * @param <G>
 * @param <H>
 * @author LCTR
 * @date 2022-09-27
 */
@JSONType(ignores = {"count"})
public class Tuple8<A, B, C, D, E, F, G, H>
        implements ITuple {
    public A a;
    public B b;
    public C c;
    public D d;
    public E e;
    public F f;
    public G g;
    public H h;

    public Tuple8(A a,
                  B b,
                  C c,
                  D d,
                  E e,
                  F f,
                  G g,
                  H h) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
        this.g = g;
        this.h = h;
    }

    @Override
    @JsonIgnore
    public int getCount() {
        return 6;
    }
}
