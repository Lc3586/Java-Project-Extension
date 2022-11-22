package project.extension.tuple;

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
public class Tuple6<A, B, C, D, E, F> implements ITuple {
    public final A a;
    public final B b;
    public final C c;
    public final D d;
    public final E e;
    public final F f;

    public Tuple6(A a, B b, C c, D d, E e, F f) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
    }

    @Override
    public int getCount() {
        return 6;
    }
}
