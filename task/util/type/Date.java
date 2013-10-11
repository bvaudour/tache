package task.util.type;

import task.util.generic.T3;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: benjamin
 * Date: 06/10/13
 * Time: 15:32
 * To change this template use File | Settings | File Templates.
 */
public final class Date implements Comparable<Date> {

  private static final String d2 = "\\d{2}";
  private static final String d4 = "\\d{4}";
  private static final String d8 = "\\d{8}";
  private static final String sp = "\\.";

  private static final String[] REGEX = {d8, d4 + sp + d2 + sp + d2, d2 + sp + d2 + sp + d4, "-"};

  public static T3<Integer, Integer, Integer> DNULL() {
    return new T3<Integer, Integer, Integer>(0, 0, 0);
  }

  private final T3<Integer, Integer, Integer> _d = DNULL();

  public static final Date NONE = new Date(DNULL());

  public static Date today() {
    Calendar c = Calendar.getInstance();
    return new Date(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
  }

  public static Date getInstance(String s) {
    return getInstance(Parser.toDate(s));
  }

  public static Date getInstance(T3<Integer, Integer, Integer> d) {
    return d == null ? NONE : getInstance(d.e1, d.e2, d.e3);
  }

  public static Date getInstance(int y, int m, int d) {
    return isDate(y, m, d) ? new Date(y, m, d) : NONE;
  }

  private Date(int y, int m, int d) {
    _d.set(y, m, d);
  }

  private Date(T3<Integer, Integer, Integer> d) {
    this(d.e1, d.e2, d.e3);
  }

  public static boolean isYear(int y) {
    return y > 999 && y < 10000;
  }

  public static boolean isMonth(int m) {
    return m > 0 && m < 13;
  }

  public static boolean isDate(int y, int m, int d) {
    return isYear(y) && isMonth(m) && d > 0 && d <= daysInMonth(y, m);
  }

  public static boolean isDate(T3<Integer, Integer, Integer> d) {
    return d != null && isDate(d.e1, d.e2, d.e3);
  }

  public static boolean isBissextil(int y) {
    return y % 4 == 0 && !(y % 100 == 0 && y % 400 != 0);
  }

  public static int daysInMonth(int y, int m) {
    switch (m) {
      case 1:
      case 3:
      case 5:
      case 7:
      case 8:
      case 10:
      case 12:
        return 31;
      case 4:
      case 6:
      case 9:
      case 11:
        return 30;
      case 2:
        return isBissextil(y) ? 29 : 28;
    }
    return 0;
  }

  public static int dayOfWeek(int y, int m, int d) {
    int z = (m < 3) ? (y - 1) : y;
    int w = (m < 3) ? 0 : -2;
    return ((23 * m / 9 + d + 4 + y + (z / 4) - (z / 100) + (z / 400) + w) % 7);
  }

  public static int dayOfWeek(T3<Integer, Integer, Integer> d) {
    return dayOfWeek(d.e1, d.e2, d.e3);
  }

  public static T3<Integer, Integer, Integer> lastDom(int y, int m, int d) {
    T3<Integer, Integer, Integer> out = new T3<Integer, Integer, Integer>(y, m, d);
    lastDom(out);
    return out;
  }

  public static boolean lastDom(T3<Integer, Integer, Integer> d) {
    if (!format(d)) return false;
    d.e3 = daysInMonth(d.e1, d.e2);
    return true;
  }

  public static T3<Integer, Integer, Integer> lastDow(int y, int m, int d) {
    T3<Integer, Integer, Integer> out = new T3<Integer, Integer, Integer>(y, m, d);
    lastDow(out);
    return out;
  }

  public static boolean lastDow(T3<Integer, Integer, Integer> d) {
    if (!format(d)) return false;
    d.e3 += (7 - dayOfWeek(d));
    return format(d);
  }

  public static T3<Integer, Integer, Integer> format(int y, int m, int d) {
    T3<Integer, Integer, Integer> out = new T3<Integer, Integer, Integer>(y, m, d);
    format(out);
    return out;
  }

  public static boolean format(T3<Integer, Integer, Integer> d) {
    boolean b = formatMonth(d);
    while (b && d.e3 < 1) {
      --d.e2;
      b = formatMonth(d);
      if (b) d.e3 += daysInMonth(d.e1, d.e2);
    }
    int n = daysInMonth(d.e1, d.e2);
    while (b && d.e3 > n) {
      d.e3 -= n;
      ++d.e2;
      b = formatMonth(d);
      if (b) n = daysInMonth(d.e1, d.e2);
    }
    if (!b) d.set(DNULL());
    return b;
  }

  public static boolean formatMonth(T3<Integer, Integer, Integer> d) {
    while (isYear(d.e1) && d.e2 < 1) {
      d.e2 += 12;
      --d.e1;
    }
    while (isYear(d.e1) && d.e2 > 12) {
      d.e2 -= 12;
      ++d.e1;
    }
    return isYear(d.e1);
  }

  public Date copy() {
    return new Date(_d);
  }

  public boolean isNull() {
    return !isDate(_d);
  }

  public static boolean isMatched(String s) {
    if (s == null) return false;
    for (String e: REGEX)
      if (s.matches(e)) return true;
    return false;
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
    return mkstring(b, ".");
  }

  public Date add(Recurrence r) {
    return add(r, 1);
  }

  public Date add(Recurrence r, int sign) {
    if (isNull() || r.isNull()) return copy();
    int v = r.value() * sign;
    T3<Integer, Integer, Integer> d = new T3<Integer, Integer, Integer>(_d);
    boolean b = false;
    switch (r.unit()) {
      case YEAR:
        d.e1 += v;
        b = isYear(d.e1);
        if (b) d.e3 = Math.min(d.e3, daysInMonth(d.e1, d.e2));
        break;
      case MONTH:
        d.e2 += v;
        b = formatMonth(d);
        if (b) d.e3 = Math.min(d.e3, daysInMonth(d.e1, d.e2));
        break;
      case WEEK:
        v *= 7;
      case DAY:
        d.e3 += v;
        b = format(d);
        break;
    }
    return (b) ? new Date(d) : NONE;
  }

  @Override
  public int compareTo(Date d) {
    if (isNull()) return (d.isNull()) ? 0 : 1;
    if (d.isNull()) return -1;
    int c = Integer.compare(_d.e1, d._d.e1);
    if (c != 0) return c;
    c = Integer.compare(_d.e2, d._d.e2);
    return c != 0 ? c : Integer.compare(_d.e3, d._d.e3);
  }

  public boolean eq(Date d) {
    return compareTo(d) == 0;
  }
  public boolean ne(Date d) {
    return compareTo(d) != 0;
  }
  public boolean lt(Date d) {
    return compareTo(d) < 0;
  }
  public boolean gt(Date d) {
    return compareTo(d) > 0;
  }
  public boolean le(Date d) {
    return compareTo(d) <= 0;
  }
  public boolean ge(Date d) {
    return compareTo(d) >= 0;
  }

  @Override
  public String toString() {
    return out();
  }

  public int daysInMonth() {
    return daysInMonth(_d.e1, _d.e2);
  }

  public int dayOfWeek() {
    return dayOfWeek(_d);
  }

  public Date lastDow() {
    return Date.getInstance(lastDow(_d.e1, _d.e2, _d.e3));
  }

  public Date lastDom() {
    return Date.getInstance(lastDom(_d.e1, _d.e2, _d.e3));
  }

  private StringBuilder mkstring(StringBuilder b, String sep) {
    if (isNull()) return b.append("-");
    Parser.formatInt(b, _d.e1, 4).append(sep);
    Parser.formatInt(b, _d.e2, 2).append(sep);
    return Parser.formatInt(b, _d.e3, 2);
  }

}
