package task.util.type;

/**
 * Created with IntelliJ IDEA.
 * User: benjamin
 * Date: 06/10/13
 * Time: 15:34
 * To change this template use File | Settings | File Templates.
 */
public enum Priorite {

  HIGH('H', 0),
  MEDIUM('M', 1),
  LOW('L', 2),
  NONE('-', 3);

  public static final String REGEX = "(H|h|M|m|L|l|-)";

  private static final Priorite[] _l = { HIGH, MEDIUM, LOW };

  private final char _c;
  private final int  _i;

  private Priorite(char c, int i) {
    _c = c;
    _i = i;
  }

  public static Priorite getInstance(char c) {
    c = Character.toUpperCase(c);
    for (Priorite p : _l)
      if (p._c == c) return p;
    return NONE;
  }

  public static Priorite getInstance(String s) {
    return (s != null && s.length() == 1) ? getInstance(s.charAt(0)) : NONE;
  }

  public static int compare(Priorite p1, Priorite p2) {
    return Integer.compare(p1._i, p2._i);
  }

  @Override
  public String toString() {
    return String.valueOf(_c);
  }

}
