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
 * @author LCTR
 * @date 2022-09-27
 */
@JSONType(ignores = {"count"})
public class Tuple7<A, B, C, D, E, F, G>
        implements ITuple {
    public A a;
    public B b;
    public C c;
    public D d;
    public E e;
    public F f;
    public G g;

    public Tuple7(A a,
                  B b,
                  C c,
                  D d,
                  E e,
                  F f,
                  G g) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
        this.g = g;
    }

    @Override
    @JsonIgnore
    public int getCount() {
        return 6;
    }
}
