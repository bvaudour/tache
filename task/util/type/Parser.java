package task.util.type;

import task.util.generic.T3;

/**
 * La classe <code>Parser</code> regroupe un ensemble de méthodes permettant de convertir des données d'un type à un autre
 * @author Benjamin VAUDOUR
 * @since 1.0
 */
public class Parser {

  /**
   * Convertit un chaîne de caractères en entier
   * @param s Chaîne à convertir
   * @return Entier correspondant (0 si la chaîne ne correspond pas à un format d'entier)
   */
  public static int toInt(String s) {
    return (s != null && s.matches("\\d+")) ? Integer.valueOf(s) : 0;
  }

  /**
   * Convertit un entier en chaîne numérique contenant un nombre de chiffres donnés
   * @param b Chaîne à modifier
   * @param i Entier à convertir
   * @param nbDigits Nombre de chiffres que doit contenir la chaîne
   * @return Chaîne modifiée
   */
  public static StringBuilder formatInt(StringBuilder b, int i, int nbDigits) {
    int p10 = 1;
    while (--nbDigits > 0) {
      p10 *= 10;
      if (i < p10) b.append(0);
    }
    return b.append(i);
  }

  /**
   * Convertit une chaîne de caractère en triplé d'entiers correspondants à une date
   * @param s Chaîne à convertir
   * @return Triplé d'entiers contenant l'année, le mois et le jour du mois (Triplé de date nulle si chaîne au mauvais format)
   */
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

  /**
   * Convertit une chaîne de caractère en nombre de minutes de jour
   * @param s Chaîne à convertir
   * @return -1 si la chaîne ne correspond pas à un format d'heure (hh:mm ou hhmm), nombre de minutes du jour sinon
   */
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

  /**
   * Crée une chaîne de caractères contenant un nombre défini d'espaces
   * @param i Nombre d'espaces que doit contenir la chaîne
   * @return Chaîne contenant le nombre d'espaces requis
   */
  public static String blanks(int i) {
    StringBuilder b = new StringBuilder();
    while (i-- > 0) b.append(' ');
    return b.toString();
  }

  /**
   * Aligne une chaîne de caractères sur une ligne d'une certaine longueur
   * @param s Chaîne à aligner
   * @param t Type de valeur qu'est supposé représenter la chaîne (permet de décider si l'alignement se fait à gauche, à droite ou centré)
   * @param length Longueur de la ligne en nombre de caractères
   * @return Chaîne alignée sur la ligne
   * @see task.util.type.Type
   */
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

  /**
   * Supprime un motif de début d'une chaîne de caractères
   * @param s Chaîne de caractères à modifier
   * @param f Motif de début
   * @return La chaîne modifiée si la chaîne donnée contient bien le motif donné au début, la chaîne non modifiée sinon
   */
  public static String removeFirst(String s, String f) {
    if (s == null)        return "";
    if (!s.startsWith(f)) return s;
    int l = f.length();
    return s.length() == l ? "" : s.substring(l);
  }

  /**
   * Supprime les espaces à gauche
   * @param s Chaîne à modifier
   * @return Chaîne sans les espaces à gauche
   */
  public static String lTrim(String s) {
    if (s == null || s.isEmpty()) return "";
    StringBuilder b = new StringBuilder(s);
    while (b.length() > 0 && b.charAt(0) == ' ') b.deleteCharAt(0);
    return b.toString();
  }

  /**
   * Supprime les espaces à droite
   * @param s Chaîne à modifier
   * @return Chaîne sans les espaces à droite
   */
  public static String rTrim(String s) {
    if (s == null || s.isEmpty()) return "";
    StringBuilder b = new StringBuilder(s);
    while (b.length() > 0 && b.charAt(b.length() - 1) == ' ') b.deleteCharAt(b.length() - 1);
    return b.toString();
  }

  /**
   * Supprime les espaces à gauche et à droite
   * @param s Chaîne à modifier
   * @return Chaîne sans les espaces inutiles
   */
  public static String trim(String s) {
    return lTrim(rTrim(s));
  }

  /**
   * Convertit un tableau en chaîne de caractères
   * @param a Tableau de chaînes de caractères
   * @return Chaîne contenant les entrées du tableau séparées par des espaces
   */
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
