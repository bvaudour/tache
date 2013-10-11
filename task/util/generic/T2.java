package task.util.generic;

/**
 * Created with IntelliJ IDEA.
 * User: benjamin
 * Date: 06/10/13
 * Time: 16:23
 * To change this template use File | Settings | File Templates.
 */
public class T2<E1, E2> {

  public E1 e1;
  public E2 e2;

  public T2() {
    this(null, null);
  }

  public T2(T2<E1, E2> t) {
    this(t.e1, t.e2);
  }

  public T2(E1 e1, E2 e2) {
    set(e1, e2);
  }

  public T2<E1, E2> set(E1 e1, E2 e2) {
    this.e1 = e1;
    this.e2 = e2;
    return this;
  }

  public T2<E1, E2> set(T2<E1, E2> t) {
    return set(t.e1, t.e2);
  }

  public static <E1, E2> T2<E1, E2> set(T2<E1, E2> t, E1 e1, E2 e2) {
    return t == null ? new T2<E1, E2>(e1, e2) : t.set(e1, e2);
  }

}
