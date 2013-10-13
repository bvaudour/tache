package task.util.type;

import task.util.generic.T3;

import java.util.Calendar;

/**
 * La classe <code>Date</code> définit le type d'échéance d'une tâche<br />
 * @author Benjamin VAUDOUR
 * @since 1.0
 */
public final class Date implements Comparable<Date> {

  private static final String d2 = "\\d{2}";
  private static final String d4 = "\\d{4}";
  private static final String d8 = "\\d{8}";
  private static final String sp = "\\.";

  private static final String[] REGEX = {d8, d4 + sp + d2 + sp + d2, d2 + sp + d2 + sp + d4, "-"};

  /**
   * Récupère la définition d'une date nulle
   * @return Triplé de valeur représentant la date nulle
   */
  public static T3<Integer, Integer, Integer> DNULL() {
    return new T3<Integer, Integer, Integer>(0, 0, 0);
  }

  private final T3<Integer, Integer, Integer> _d = DNULL();

  /**
   * Date nulle
   */
  public static final Date NONE = new Date(DNULL());

  /**
   * Date du jour
   * @return Date du jour
   */
  public static Date today() {
    Calendar c = Calendar.getInstance();
    return new Date(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
  }

  /**
   * Récupère une date à partir d'une chaîne de caractères
   * @param s Chaîne à convertir
   * @return Date correspondante
   */
  public static Date getInstance(String s) {
    return getInstance(Parser.toDate(s));
  }

  /**
   * Récupère une date à partir d'un triplé d'entiers
   * @param d Triplé à convertir
   * @return Date correspondante
   */
  public static Date getInstance(T3<Integer, Integer, Integer> d) {
    return d == null ? NONE : getInstance(d.e1, d.e2, d.e3);
  }

  /**
   * Récupère une date à partir de l'année, le mois et le jour
   * @param y Année
   * @param m Mois
   * @param d Jour du mois
   * @return Date correspondante
   */
  public static Date getInstance(int y, int m, int d) {
    return isDate(y, m, d) ? new Date(y, m, d) : NONE;
  }

  private Date(int y, int m, int d) {
    _d.set(y, m, d);
  }

  private Date(T3<Integer, Integer, Integer> d) {
    this(d.e1, d.e2, d.e3);
  }

  /**
   * Vérifie si un entier définit une année valide
   * @param y Année
   * @return true si c'est une année, false sinon
   */
  public static boolean isYear(int y) {
    return y > 999 && y < 10000;
  }

  /**
   * Vérifie si un entier définit un mois valide
   * @param m Mois
   * @return true si c'est un mois, false sinon
   */
  public static boolean isMonth(int m) {
    return m > 0 && m < 13;
  }

  /**
   * Vérifie si 3 entiers définissent une date valide
   * @param y Année
   * @param m Mois
   * @param d Jour du mois
   * @return true si c'est une date valide, false sinon
   */
  public static boolean isDate(int y, int m, int d) {
    return isYear(y) && isMonth(m) && d > 0 && d <= daysInMonth(y, m);
  }

  /**
   * Vérifie si 3 entiers définissent une date valide
   * @param d Triplé d'entiers
   * @return true si c'est une date valide, false sinon
   */
  public static boolean isDate(T3<Integer, Integer, Integer> d) {
    return d != null && isDate(d.e1, d.e2, d.e3);
  }

  /**
   * Vérifie si une année est bissextile
   * @param y Année à tester
   * @return true si c'est une année bissextile, false sinon
   */
  public static boolean isBissextil(int y) {
    return y % 4 == 0 && !(y % 100 == 0 && y % 400 != 0);
  }

  /**
   * Récupère le nombre de jours d'un mois donné
   * @param y Année
   * @param m Mois
   * @return Nombre de jour dans le mois
   */
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

  /**
   * Récupère le n° de jour de la semaine (de 0 à 6, la semaine commençant un dimanche)
   * @param y Année
   * @param m Mois
   * @param d Jour du mois
   * @return N° de jour de la semaine
   */
  public static int dayOfWeek(int y, int m, int d) {
    int z = (m < 3) ? (y - 1) : y;
    int w = (m < 3) ? 0 : -2;
    return ((23 * m / 9 + d + 4 + y + (z / 4) - (z / 100) + (z / 400) + w) % 7);
  }

  /**
   * Récupère le n° de jour de la semaine (de 0 à 6, la semaine commençant un dimanche)
   * @param d Triplé d'entiers
   * @return N° de jour de la semaine
   */
  public static int dayOfWeek(T3<Integer, Integer, Integer> d) {
    return dayOfWeek(d.e1, d.e2, d.e3);
  }

  /**
   * Récupère le dernier jour du mois d'une date quelconque
   * @param y Année
   * @param m Mois
   * @param d Jour du mois
   * @return Dernier jour du mois (sous forme de triplé d'entiers représentant l'année, le mois et le jour)
   */
  public static T3<Integer, Integer, Integer> lastDom(int y, int m, int d) {
    T3<Integer, Integer, Integer> out = new T3<Integer, Integer, Integer>(y, m, d);
    lastDom(out);
    return out;
  }

  /**
   * Passe au dernier jour du mois
   * @param d Date à modifier
   * @return true si date valide, false sinon
   */
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

  /**
   * Passe au dernier jour de la semaine (le dimanche)
   * @param d Date à modifier
   * @return true si date valide, false sinon
   */
  public static boolean lastDow(T3<Integer, Integer, Integer> d) {
    if (!format(d)) return false;
    d.e3 += (7 - dayOfWeek(d));
    return format(d);
  }

  /**
   * Formate une date de façon à ce qu'elle soit valide : <br />
   * <ul>
   * <li>L'année doit contenir 4 chiffres</li>
   * <li>Le mois doit être compris entre 1 et 12</li>
   * <li>Le jour du mois doit être compris entre 1 et le nombre de jours dans le mois</li>
   * </ul>
   * @param y Année
   * @param m Mois
   * @param d Jour du mois
   * @return Date formatée
   */
  public static T3<Integer, Integer, Integer> format(int y, int m, int d) {
    T3<Integer, Integer, Integer> out = new T3<Integer, Integer, Integer>(y, m, d);
    format(out);
    return out;
  }

  /**
   * Modifie une date de façon à ce qu'elle soit valide
   * @param d Date modifiée
   * @return true si la date modifiée est valide, false sinon
   */
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

  /**
   * Formate le mois
   * @param d Date à modifier
   * @return true si le formatage s'est effectué dans encombre, false sinon
   */
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

  /**
   * Copie la date
   * @return Date copiée
   */
  public Date copy() {
    return new Date(_d);
  }

  /**
   * Vérifie si une date est valide
   * @return true si date = 0000.00.00, false sinon
   */
  public boolean isNull() {
    return !isDate(_d);
  }

  /**
   * Vérifie si une chaîne de caractère peut être convertie en date
   * @param s Chaîne à analyser
   * @return true si la chaîne correspond à une date, false sinon
   */
  public static boolean isMatched(String s) {
    if (s == null) return false;
    for (String e: REGEX)
      if (s.matches(e)) return true;
    return false;
  }

  /**
   * Convertit une date en chaîne de caractères pour sortie fichier (de la forme yyyymmdd)
   * @return Chaîne de caractères correspondante
   */
  public String in() {
    return in(new StringBuilder()).toString();
  }

  /**
   * Convertit une date en chaîne de caractères pour sortie fichier (de la forme yyyymmdd)
   * @param b Chaîne de caractères à modifier
   * @return Chaîne de caractères modifiée
   */
  public StringBuilder in(StringBuilder b) {
    return mkstring(b, "");
  }

  /**
   * Convertit une date en chaîne de caractères pour sortie terminal (de la forme yyyy.mm.dd)
   * @return Chaîne de caractères correspondante
   */
  public String out() {
    return out(new StringBuilder()).toString();
  }

  /**
   * Convertit une date en chaîne de caractères pour sortie terminal (de la forme yyyy.mm.dd)
   * @param b Chaîne de caractères à modifier
   * @return Chaîne de caractères modifiée
   */
  public StringBuilder out(StringBuilder b) {
    return mkstring(b, ".");
  }

  /**
   * Ajoute une certaine période à une date
   * @param r Période à ajouter
   * @return Date calculée
   * @see task.util.type.Recurrence
   */
  public Date add(Recurrence r) {
    return add(r, 1);
  }

  /**
   * Incrémente une date d'une certaine période
   * @param r Période à incrémenter
   * @param sign Signe de l'incrémentation (1 pour l'incrémentation, -1 pour la décrémentation)
   * @return Date calculée
   * @see task.util.type.Recurrence
   */
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

  /**
   * Compare 2 dates<br />
   * <i>Nota : une date nulle est considérée comme infinie</i>
   * @param d Date à analyser
   * @return 0 si les deux dates sont égales, -1 si la date en cours précède la date à analyser, 1 sinon
   */
  @Override
  public int compareTo(Date d) {
    if (isNull()) return (d.isNull()) ? 0 : 1;
    if (d.isNull()) return -1;
    int c = Integer.compare(_d.e1, d._d.e1);
    if (c != 0) return c;
    c = Integer.compare(_d.e2, d._d.e2);
    return c != 0 ? c : Integer.compare(_d.e3, d._d.e3);
  }

  /**
   * Vérifie que 2 dates sont égales
   * @param d Date à analyser
   * @return true si égales, false sinon
   */
  public boolean eq(Date d) {
    return compareTo(d) == 0;
  }
  /**
   * Vérifie que 2 dates sont différentes
   * @param d Date à analyser
   * @return true si différentes, false sinon
   */
  public boolean ne(Date d) {
    return compareTo(d) != 0;
  }
  /**
   * Vérifie si la date précède strictement une date donnée
   * @param d Date à analyser
   * @return true si strictement inférieure, false sinon
   */
  public boolean lt(Date d) {
    return compareTo(d) < 0;
  }
  /**
   * Vérifie si la date succède strictement une date donnée
   * @param d Date à analyser
   * @return true si strictement supérieure, false sinon
   */
  public boolean gt(Date d) {
    return compareTo(d) > 0;
  }
  /**
   * Vérifie si la date précède une date donnée
   * @param d Date à analyser
   * @return true si sinférieure, false sinon
   */
  public boolean le(Date d) {
    return compareTo(d) <= 0;
  }
  /**
   * Vérifie si la date succède une date donnée
   * @param d Date à analyser
   * @return true si supérieure, false sinon
   */
  public boolean ge(Date d) {
    return compareTo(d) >= 0;
  }

  /**
   * Convertit la date en chaîne de caractères
   * @return Chaîne de caractères (de la forme yyyy.mm.dd)
   */
  @Override
  public String toString() {
    return out();
  }

  /**
   * Calcule le nombre de jours dans le mois
   * @return Nombre de jours dans le mois, 0 si date nulle
   */
  public int daysInMonth() {
    return daysInMonth(_d.e1, _d.e2);
  }

  /**
   * Calcule le jour de la semaine
   * @return Nombre de 0 à 6, 0 étant dimanche
   */
  public int dayOfWeek() {
    return dayOfWeek(_d);
  }

  /**
   * Calcule le dernier jour de la semaine en cours (un dimanche)
   * @return Date pointant vers le dernier jour de la semaine
   */
  public Date lastDow() {
    return Date.getInstance(lastDow(_d.e1, _d.e2, _d.e3));
  }

  /**
   * Calcule le dernier jour de la du mois en cours
   * @return Date pointant vers le dernier jour du mois
   */
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
