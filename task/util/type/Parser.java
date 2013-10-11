package task.util.type;

import task.util.generic.T3;

/**
 * Created with IntelliJ IDEA.
 * User: benjamin
 * Date: 06/10/13
 * Time: 15:23
 * To change this template use File | Settings | File Templates.
 */
public class Parser {

  // Conversion String en int
  public static int toInt(String s) {
    return (s != null && s.matches("\\d+")) ? Integer.valueOf(s) : 0;
  }

  // Conversion int en String numérique de longueur nbDigit
  public static StringBuilder formatInt(StringBuilder b, int i, int nbDigits) {
    int p10 = 1;
    while (--nbDigits > 0) {
      p10 *= 10;
      if (i < p10) b.append(0);
    }
    return b.append(i);
  }

  // Conversion String en date (dans l'ordre : y, m, d)
  public static T3<Integer, Integer, Integer> toDate(String s) {
    T3<Integer, Integer, Integer> d = Date.DNULL();
    if (s == null) return d;
    if (s.matches("\\d{8}"))
      d.set(toInt(s.substring(0, 4)), toInt(s.substring(4, 6)), toInt(s.substring(6)));
    else if (s.matches("\\d{4}.\\d{2}.\\d{2}"))
      d.set(toInt(s.substring(0, 4)), toInt(s.substring(5, 7)), toInt(s.substring(8)));
    else if (s.matches("\\d{2}.\\d{2}.\\d{4}"))
      d.set(toInt(s.substring(6)), toInt(s.substring(3, 5)), toInt(s.substring(0, 2)));
    return d;
  }

  // Conversion String en temps (exprimé en nombre de minutes depuis minuit)
  public static int toHeure(String s) {
    int h = Heure.HNULL;
    if (s == null) return h;
    if (s.matches("\\d{4}")) {
      h = toInt(s.substring(0, 2)) * 60;
      h += toInt(s.substring(2));
    } else if (s.matches("\\d{2}.\\d{2}")) {
      h = toInt(s.substring(0, 2)) * 60;
      h += toInt(s.substring(3));
    }
    return h;
  }

  // Crée un String contenant i espaces blancs
  public static String blanks(int i) {
    StringBuilder b = new StringBuilder();
    while (i-- > 0) b.append(' ');
    return b.toString();
  }

  // Aligne un String suivant le type t sur une ligne de length caractères
  public static String align(String s, Type t, int length) {
    if (s == null) return blanks(length);
    int l = length - s.length();
    if (l <= 0) return s.substring(0, length);
    if (t.center()) {
      int lg = l / 2;
      return blanks(lg) + s + blanks(l - lg);
    }
    return t.left() ? s + blanks(l) : blanks(l) + s;
  }

  // Supprime la chaîne f du String s si s commence par f
  public static String removeFirst(String s, String f) {
    if (s == null)        return "";
    if (!s.startsWith(f)) return s;
    int l = f.length();
    return s.length() == l ? "" : s.substring(l);
  }

  // Supprime les espaces blancs à gauche
  public static String lTrim(String s) {
    if (s == null || s.isEmpty()) return "";
    StringBuilder b = new StringBuilder(s);
    while (b.length() > 0 && b.charAt(0) == ' ') b.deleteCharAt(0);
    return b.toString();
  }

  // Supprime les espaces blancs à droite
  public static String rTrim(String s) {
    if (s == null || s.isEmpty()) return "";
    StringBuilder b = new StringBuilder(s);
    while (b.length() > 0 && b.charAt(b.length() - 1) == ' ') b.deleteCharAt(b.length() - 1);
    return b.toString();
  }

  // Supprime les espaces blancs à gauche et à droite
  public static String trim(String s) {
    return lTrim(rTrim(s));
  }

  // Convertit une tableau en chaîne de caractères
  public static String toString(String[] a) {
    if (a == null || a.length == 0) return "";
    StringBuilder b = new StringBuilder();
    for (String s: a) {
      if (b.length() != 0) b.append(' ');
      b.append(s);
    }
    return b.toString();
  }

}
