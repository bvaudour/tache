package task.util.color;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: benjamin
 * Date: 22/09/13
 * Time: 17:57
 * To change this template use File | Settings | File Templates.
 */
public final class Ansi {

  public static final boolean FORMAT = true;

  public static final Ansi END        = new Ansi(Couleur.NONE);
  public static final Ansi NORMAL     = new Ansi(Effet.NORMAL);
  public static final Ansi BOLD       = new Ansi(Effet.BOLD);
  public static final Ansi UNDERLINE  = new Ansi(Effet.UNDERLINE);
  public static final Ansi ITALIC     = new Ansi(Effet.ITALIC);
  public static final Ansi BLINK      = new Ansi(Effet.BLINK);
  public static final Ansi INVERTED   = new Ansi(Effet.INVERTED);
  public static final Ansi BUNDERLINE = new Ansi(Effet.BOLD, Effet.UNDERLINE);
  public static final Ansi BITALIC    = new Ansi(Effet.BOLD, Effet.ITALIC);

  private final Couleur    _f;
  private final Couleur    _b;
  private final Set<Effet> _e = new HashSet<Effet>();

  private Ansi(Couleur c1, Couleur c2, Set<Effet> l) {
    _f = c1;
    _b = c2;
    for (Effet e : l) _e.add(e);
  }

  public Ansi(Couleur c1, Couleur c2, Effet e1, Effet e2) {
    _f = c1;
    _b = c2;
    _add(e1, e2);
  }

  public Ansi(Couleur c1, Couleur c2, Effet e) {
    this(c1, c2, e, Effet.NONE);
  }

  public Ansi(Couleur c1, Couleur c2) {
    this(c1, c2, Effet.NONE);
  }

  public Ansi(Couleur c, Effet e1, Effet e2) {
    this(c, Couleur.NONE, e1, e2);
  }

  public Ansi(Couleur c, Effet e) {
    this(c, Couleur.NONE, e);
  }

  public Ansi(Couleur c) {
    this(c, Couleur.NONE);
  }

  public Ansi(Effet e1, Effet e2) {
    this(Couleur.NONE, e1, e2);
  }

  public Ansi(Effet e) {
    this(Couleur.NONE, e);
  }

  private void _add(Effet e) {
    if (e != Effet.NONE) _e.add(e);
  }

  private void _add(Effet e1, Effet e2) {
    _add(e1);
    _add(e2);
  }

  public Ansi color(Couleur c1, Couleur c2) {
    return new Ansi(c1, c2, _e);
  }

  public Ansi font(Couleur c) {
    return color(c, _b);
  }

  public Ansi back(Couleur c) {
    return color(_f, c);
  }

  private static void _addStr(StringBuilder b, String s) {
    if (s.isEmpty()) return;
    if (b.length() != 0) b.append(";");
    b.append(s);
  }

  @Override
  public String toString() {
    StringBuilder b = new StringBuilder();
    for (Effet e : _e) _addStr(b, e.value());
    _addStr(b, _f.font());
    _addStr(b, _b.back());
    return b.insert(0, "\033[").append("m").toString();
  }

  public String format(StringBuilder b) {
    return FORMAT ? b.insert(0, this).append(END).toString() : b.toString();
  }

  public String format(String s) {
    return format(new StringBuilder(s));
  }

  public static Ansi normal(Couleur c1, Couleur c2) {
    return END.color(c1, c2);
  }
  public static Ansi normal(Couleur c) {
    return END.font(c);
  }
  public static Ansi bold(Couleur c1, Couleur c2) {
    return BOLD.color(c1, c2);
  }
  public static Ansi bold(Couleur c) {
    return BOLD.font(c);
  }
  public static Ansi underline(Couleur c1, Couleur c2) {
    return UNDERLINE.color(c1, c2);
  }
  public static Ansi underline(Couleur c) {
    return UNDERLINE.font(c);
  }
  public static Ansi italic(Couleur c1, Couleur c2) {
    return ITALIC.color(c1, c2);
  }
  public static Ansi italic(Couleur c) {
    return ITALIC.font(c);
  }
  public static Ansi blink(Couleur c1, Couleur c2) {
    return BLINK.color(c1, c2);
  }
  public static Ansi blink(Couleur c) {
    return BLINK.font(c);
  }
  public static Ansi inverted(Couleur c1, Couleur c2) {
    return INVERTED.color(c1, c2);
  }
  public static Ansi inverted(Couleur c) {
    return INVERTED.font(c);
  }
  public static Ansi bunderline(Couleur c1, Couleur c2) {
    return BUNDERLINE.color(c1, c2);
  }
  public static Ansi bunderline(Couleur c) {
    return BUNDERLINE.font(c);
  }
  public static Ansi bitalic(Couleur c1, Couleur c2) {
    return BITALIC.color(c1, c2);
  }
  public static Ansi bitalic(Couleur c) {
    return BITALIC.font(c);
  }

}
