package project.extension.tuple;

/**
 * 包含三个元素的元组对象
 *
 * @param <A>
 * @param <B>
 * @param <C>
 * @author LCTR
 * @date 2022-03-18
 */
public class Tuple3<A, B, C> implements ITuple {
    public final A a;
    public final B b;
    public final C c;

    public Tuple3(A a, B b, C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
