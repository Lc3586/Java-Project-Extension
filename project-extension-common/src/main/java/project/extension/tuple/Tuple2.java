package project.extension.tuple;

/**
 * 包含两个元素的元组对象
 *
 * @param <A>
 * @param <B>
 * @author LCTR
 * @date 2022-03-18
 */
public class Tuple2<A, B> implements ITuple {
    public A a;
    public B b;

    public Tuple2() {

    }

    public Tuple2(A a, B b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
