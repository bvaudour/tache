package task.util.type;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: benjamin
 * Date: 06/10/13
 * Time: 15:32
 * To change this template use File | Settings | File Templates.
 */
public final class Heure implements Comparable<Heure> {

  private static final String d2 = "\\d{2}";
  private static final String d4 = "\\d{4}";
  private static final String sp = ":";

  private static final String[] REGEX = {d4, d2 + sp + d2, "-"};

  public static final int HNULL = -1;

  private final int _h;

  public static final Heure NONE = new Heure(HNULL);

  public static Heure today() {
    Calendar c = Calendar.getInstance();
    return new Heure(c.get(Calendar.HOUR_OF_DAY) * 60 + c.get(Calendar.MINUTE));
  }

  public static Heure getInstance(String s) {
    return getInstance(Parser.toHeure(s));
  }

  public static Heure getInstance(int h) {
    return isValid(h) ? new Heure(h) : NONE;
  }

  public static boolean isMatched(String s) {
    if (s == null) return false;
    for (String e : REGEX)
      if (s.matches(e)) return true;
    return false;
  }

  private Heure(int h) {
    _h = h;
  }

  public Heure copy() {
    return new Heure(_h);
  }

  public boolean isNull() {
    return _h == HNULL;
  }

  public String in() {
    return in(new StringBuilder()).toString();
  }

  public StringBuilder in(StringBuilder b) {
    return mkstring(b, "");
  }

  public String out() {
    return out(new StringBuilder()).toString();
  }

  public StringBuilder out(StringBuilder b) {
    return mkstring(b, ":");
  }

  @Override
  public String toString() {
    return out();
  }

  public Heure add(Recurrence r) {
    return add(r, 1);
  }

  public Heure add(Recurrence r, int sign) {
    if (isNull() || r.isNull()) return copy();
    int v = r.value() * sign;
    switch (r.unit()) {
      case HOUR: v *= 60;
      case MIN:  break;
      default:   return NONE;
    }
    return getInstance(_h + v);
  }

  public Recurrence substract(Heure h) {
    Recurrence.Unit u = Recurrence.Unit.MIN;
    if (isNull()) return h.isNull() ? Recurrence.NONE : Recurrence.getInstance(Integer.MAX_VALUE, u);
    if (h.isNull()) return Recurrence.getInstance(Integer.MIN_VALUE, u);
    int d = _h - h._h;
    return Recurrence.getInstance(d, u);
  }

  @Override
  public int compareTo(Heure h) {
    return Integer.compare(_h, h._h);
  }

  public boolean eq(Heure h) {
    return compareTo(h) == 0;
  }
  public boolean ne(Heure h) {
    return compareTo(h) != 0;
  }
  public boolean lt(Heure h) {
    return compareTo(h) < 0;
  }
  public boolean gt(Heure h) {
    return compareTo(h) > 0;
  }
  public boolean le(Heure h) {
    return compareTo(h) <= 0;
  }
  public boolean ge(Heure h) {
    return compareTo(h) >= 0;
  }

  private static boolean isValid(int h) {
    return h >= 0 && h < 1440;
  }

  private StringBuilder mkstring(StringBuilder b, String sep) {
    return isNull() ? b.append("-") : Parser.formatInt(Parser.formatInt(b, _h / 60, 2).append(sep), _h % 60, 2);
  }

}
