package task.util.type;

import java.util.Calendar;

/**
 * La classe <code>Heure</code> décrit une heure dans le jour (au format 24h) et la minute d'heure (de 0 à 59)<br />
 * Ainsi, une heure est comprise entre 00:00 et 23:59 (La date nulle étant identifiée comme la chaîne "-"). <br />
 * En interne, C'est le nombre de minutes du jour qui est utilisé (la division par 60 permettant de récupérer l'heure, et le reste de la division permettant de récupérer la minute d'heure).
 * @author Benjamin VAUDOUR
 * @since 1.0
 */
public final class Heure implements Comparable<Heure> {

  private static final String d2 = "\\d{2}";
  private static final String d4 = "\\d{4}";
  private static final String sp = ":";

  private static final String[] REGEX = {d4, d2 + sp + d2, "-"};

  /**
   * Nombre de minutes pour une heure nulle (non valide)
   */
  public static final int HNULL = -1;

  private final int _h;

  /**
   * Heure nulle
   */
  public static final Heure NONE = new Heure(HNULL);

  /**
   * Heure actuelle
   * @return Heure actuelle
   */
  public static Heure today() {
    Calendar c = Calendar.getInstance();
    return new Heure(c.get(Calendar.HOUR_OF_DAY) * 60 + c.get(Calendar.MINUTE));
  }

  /**
   * Récupère une heure à partir d'une chaîne de caractères
   * @param s Chaîne de caractères à convertir
   * @return Heure convertie si la chaîne correspond à un format d'heure valide (hh:mm ou hhmm), Heure nulle sinon
   */
  public static Heure getInstance(String s) {
    return getInstance(Parser.toHeure(s));
  }

  /**
   * Récupère une heure à partir du numéro de minutes dans le jour
   * @param h Numéro de minutes du jour
   * @return Heure valide si h valide, Heure nulle sinon
   */
  public static Heure getInstance(int h) {
    return isValid(h) ? new Heure(h) : NONE;
  }

  /**
   * Vérifie si une chaîne de caractères identifie une heure
   * @param s Chaîne à analyser
   * @return Vraie si la chaîne est valide (ou == "-"), faux sinon
   */
  public static boolean isMatched(String s) {
    if (s == null) return false;
    for (String e : REGEX)
      if (s.matches(e)) return true;
    return false;
  }

  private Heure(int h) {
    _h = h;
  }

  /**
   * Copie l'heure
   * @return Heure copiée
   */
  public Heure copy() {
    return new Heure(_h);
  }

  /**
   * Vérifie si l'heure est valide
   * @return true si non valide, false sinon
   */
  public boolean isNull() {
    return _h == HNULL;
  }

  /**
   * Récupère une chaîne de caractères pour la sortie fichier (format hhmm (ou "-" pour l'heure nulle))
   * @return Chaîne de caractères correspondante
   */
  public String in() {
    return in(new StringBuilder()).toString();
  }

  /**
   * Récupère une chaîne de caractères pour la sortie fichier (format hhmm (ou "-" pour l'heure nulle))
   * @param b Chaîne de caractères à modifier
   * @return Chaîne de caractères correspondante
   */
  public StringBuilder in(StringBuilder b) {
    return mkstring(b, "");
  }

  /**
   * Récupère une chaîne de caractères pour la sortie terminal (format hh:mm (ou "-" pour l'heure nulle))
   * @return Chaîne de caractères correspondante
   */
  public String out() {
    return out(new StringBuilder()).toString();
  }

  /**
   * Récupère une chaîne de caractères pour la sortie terminal (format hh:mm (ou "-" pour l'heure nulle))
   * @param b Chaîne de caractères à modifier
   * @return Chaîne de caractères correspondante
   */
  public StringBuilder out(StringBuilder b) {
    return mkstring(b, ":");
  }

  /**
   * Convertit l'heure en chaîne de caractères
   * @return Heure au format hh:mm (ou "-" si heure nulle)
   */
  @Override
  public String toString() {
    return out();
  }

  /**
   * Ajoute une certaine durée à une heure
   * @param r Durée à ajouter
   * @return Heure calculée
   */
  public Heure add(Recurrence r) {
    return add(r, 1);
  }

  /**
   * Incrémente une certaine durée à une heure
   * @param r Durée à ajouter
   * @param sign Signe de l'incrément (1 si positif, -1 sinon)
   * @return Heure calculée
   */
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

  /**
   * Calcule la durée entre deux heures
   * @param h Heure de soustraction
   * @return Durée en nombre de minutes
   * @see task.util.type.Recurrence
   */
  public Recurrence substract(Heure h) {
    Recurrence.Unit u = Recurrence.Unit.MIN;
    if (isNull()) return h.isNull() ? Recurrence.NONE : Recurrence.getInstance(Integer.MAX_VALUE, u);
    if (h.isNull()) return Recurrence.getInstance(Integer.MIN_VALUE, u);
    int d = _h - h._h;
    return Recurrence.getInstance(d, u);
  }

  /**
   * Compare l'heure en cours avec une heure donnée
   * @param h Heure à analyser
   * @return 0 si les heures sont égales, -1 si l'heure en cours précède l'heure donnée, 1 sinon
   */
  @Override
  public int compareTo(Heure h) {
    return Integer.compare(_h, h._h);
  }

  /**
   * Vérifie si deux heures sont égales
   * @param h Heure à analyser
   * @return true si égales, false sinon
   */
  public boolean eq(Heure h) {
    return compareTo(h) == 0;
  }
  /**
   * Vérifie si deux heures sont différentes
   * @param h Heure à analyser
   * @return true si différentes, false sinon
   */
  public boolean ne(Heure h) {
    return compareTo(h) != 0;
  }
  /**
   * Vérifie si l'heure en cours précède strictement une heure donnée
   * @param h Heure à analyser
   * @return true si strictement inférieure, false sinon
   */
  public boolean lt(Heure h) {
    return compareTo(h) < 0;
  }
  /**
   * Vérifie si l'heure en cours succède strictement une heure donnée
   * @param h Heure à analyser
   * @return true si strictement supérieure, false sinon
   */
  public boolean gt(Heure h) {
    return compareTo(h) > 0;
  }
  /**
   * Vérifie si l'heure en cours précède une heure donnée
   * @param h Heure à analyser
   * @return true si inférieure, false sinon
   */
  public boolean le(Heure h) {
    return compareTo(h) <= 0;
  }
  /**
   * Vérifie si l'heure en cours succède une heure donnée
   * @param h Heure à analyser
   * @return true si supérieure, false sinon
   */
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
