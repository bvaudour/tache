package task.util.generic;

/**
 * Created with IntelliJ IDEA.
 * User: benjamin
 * Date: 06/10/13
 * Time: 16:23
 * To change this template use File | Settings | File Templates.
 */
public class T3<E1, E2, E3> {

  public E1 e1;
  public E2 e2;
  public E3 e3;

  public T3() {
    this(null, null, null);
  }

  public T3(T3<E1, E2, E3> t) {
    this(t.e1, t.e2, t.e3);
  }

  public T3(E1 e1, E2 e2, E3 e3) {
    set(e1, e2, e3);
  }

  public T3<E1, E2, E3> set(E1 e1, E2 e2, E3 e3) {
    this.e1 = e1;
    this.e2 = e2;
    this.e3 = e3;
    return this;
  }

  public T3<E1, E2, E3> set(T3<E1, E2, E3> t) {
    return set(t.e1, t.e2, t.e3);
  }

  public static <E1, E2, E3> T3<E1, E2, E3> set(T3<E1, E2, E3> t, E1 e1, E2 e2, E3 e3) {
    return t == null ? new T3<E1, E2, E3>(e1, e2, e3) : t.set(e1, e2, e3);
  }

}
