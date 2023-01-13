package project.extension.tuple;

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
public class Tuple8<A, B, C, D, E, F, G, H>
        implements ITuple {
    public final A a;
    public final B b;
    public final C c;
    public final D d;
    public final E e;
    public final F f;
    public final G g;
    public final H h;

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
    public int getCount() {
        return 6;
    }
}
