package task.util.type;

/**
 * Created with IntelliJ IDEA.
 * User: benjamin
 * Date: 06/10/13
 * Time: 15:33
 * To change this template use File | Settings | File Templates.
 */
public final class Recurrence {

  public static enum Unit {

    YEAR('y'),
    MONTH('m'),
    WEEK('w'),
    DAY('d'),
    HOUR('h'),
    MIN('\''),
    NONE('-');

    public static final String REGEX = "(Y|y|M|m|W|w|D|d|H|h|')";

    private static final Unit[] _l = { YEAR, MONTH, WEEK, DAY, HOUR, MIN };
    private static final String _aut = "ymwdh'";

    private final char _c;

    private Unit(char c) {
      _c = c;
    }

    public static Unit getInstance(char c) {
      int i = _aut.indexOf(c);
      return i < 0 ? NONE : _l[i];
    }

    @Override
    public String toString() {
      return String.valueOf(_c);
    }

  }

  private static final String REGEX = "\\d+" + Unit.REGEX;

  public static final Recurrence NONE = new Recurrence(0, Unit.NONE);

  private final int  _v;
  private final Unit _u;

  private Recurrence(int v, Unit u) {
    _v = v;
    _u = u;
  }

  public int value() {
    return _v;
  }

  public Unit unit() {
    return _u;
  }

  public boolean isNull() {
    return _v == 0;
  }

  public boolean eq(Recurrence r) {
    return r != null && _v == r._v && _u == r._u;
  }

  public boolean ne(Recurrence r) {
    return !eq(r);
  }

  public static Recurrence getInstance(int v, Unit u) {
    return (v != 0 && u != Unit.NONE) ? new Recurrence(v, u) : NONE;
  }

  public static Recurrence getInstance(String s, int sign) {
    if (s == null || !s.matches(REGEX)) return NONE;
    int l = s.length() - 1;
    return getInstance(Parser.toInt(s.substring(0, l)) * sign, Unit.getInstance(s.charAt(l)));
  }

  public static Recurrence getInstance(String s) {
    return getInstance(s, 1);
  }

  public static boolean isMatched(String s) {
    return s != null && (s.equals("-") || s.matches(REGEX));
  }

  @Override
  public String toString() {
    return (isNull()) ? "-" : String.valueOf(_v) + _u;
  }

}
