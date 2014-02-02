package task.util.type;

/**
 * La classe <code>Récurrence</code> définit la récurrence d'une tâche (pour une date), ou bien une durée (dans le cas d'une heure).<br />
 * Elle est constituée d'un nombre, ainsi qu'une unité (année, mois, etc.).
 * @author Benjamin VAUDOUR
 * @since 1.0
 * @see Recurrence.Unit
 */
public final class Recurrence {

  /**
   * L'enum <code>Unit</code> décrit la liste des unités autorisées pour la classe <code>Récurrence</code> :<br />
   * <ul>
   * <li><b>YEAR</b> : Année (identifiée par le caractère y)</li>
   * <li><b>MONTH</b> : Mois (identifié par le caractère m)</li>
   * <li><b>WEEK</b> : Semaine (identifiée par le caractère w)</li>
   * <li><b>DAY</b> : jour (identifié par le caractère d)</li>
   * <li><b>HOUR</b> : Heure (identifiée par le caractère h)</li>
   * <li><b>MIN</b> : Minute (identifiée par le caractère ' pour ne pas confondre avec le mois)</li>
   * <li><b>NONE</b> : Unité non valide</li>
   * </ul>
   * @author Benjamin VAUDOUR
   * @since 1.0
   */
  public static enum Unit {

    YEAR('y'),
    MONTH('m'),
    WEEK('w'),
    DAY('d'),
    HOUR('h'),
    MIN('\''),
    NONE('-');

    /**
     * Expression régulière permettant d'identifier une unité valide
     */
    public static final String REGEX = "(Y|y|M|m|W|w|D|d|H|h|')";

    private static final Unit[] _l = { YEAR, MONTH, WEEK, DAY, HOUR, MIN };
    private static final String _aut = "ymwdh'";

    private final char _c;

    private Unit(char c) {
      _c = c;
    }

    /**
     * Récupère une unité correspondant à un caractère
     * @param c Caractère à analyser
     * @return Unité correspondante
     */
    public static Unit getInstance(char c) {
      int i = _aut.indexOf(c);
      return i < 0 ? NONE : _l[i];
    }

    /**
     * Convertit l'unité en chaîne de caractères
     * @return Résultat de la conversion
     */
    @Override
    public String toString() {
      return String.valueOf(_c);
    }

  }

  private static final String REGEX = "\\d+" + Unit.REGEX;

  /**
   * Récurrence nulle
   */
  public static final Recurrence NONE = new Recurrence(0, Unit.NONE);

  private final int  _v;
  private final Unit _u;

  private Recurrence(int v, Unit u) {
    _v = v;
    _u = u;
  }

  /**
   * Récupère la valeur de la récurrence
   * @return Valeur
   */
  public int value() {
    return _v;
  }

  /**
   * Récupère l'unité de la récurrence
   * @return Unité
   */
  public Unit unit() {
    return _u;
  }

  /**
   * Vérifie si la récurrence est valide
   * @return true si récurrence nulle, false sinon
   */
  public boolean isNull() {
    return _v == 0;
  }

  /**
   * Vérifie l'égalité de deux récurrences
   * @param r Récurrence à comparer
   * @return true si égales, false sinon
   */
  public boolean eq(Recurrence r) {
    return r != null && _v == r._v && _u == r._u;
  }

  /**
   * Vérifie que deux récurrences sont différentes
   * @param r Récurrence à comparer
   * @return true si différentes, false sinon
   */
  public boolean ne(Recurrence r) {
    return !eq(r);
  }

  /**
   * Construit une récurrence
   * @param v Valeur de la récurrence
   * @param u Unité de la récurrence
   * @return La récurrence générée
   */
  public static Recurrence getInstance(int v, Unit u) {
    return (v != 0 && u != Unit.NONE) ? new Recurrence(v, u) : NONE;
  }

  /**
   * Construit une récurrence
   * @param s Chaîne à convertir
   * @param sign Signe de la valeur de la récurrence
   * @return Récurrence nulle si la chaîne n'est pas au format adéquat (ie. un nombre suivi d'un caractère identifiant l'unité), récurrence convertie sinon
   */
  public static Recurrence getInstance(String s, int sign) {
    if (s == null || !s.matches(REGEX)) return NONE;
    int l = s.length() - 1;
    return getInstance(Parser.toInt(s.substring(0, l)) * sign, Unit.getInstance(s.charAt(l)));
  }

  /**
   * Construit une récurrence
   * @param s Chaîne à convertir
   * @return Récurrence nulle si la chaîne n'est pas au format adéquat (ie. un nombre suivi d'un caractère identifiant l'unité), récurrence convertie sinon
   */
  public static Recurrence getInstance(String s) {
    return getInstance(s, 1);
  }

  public static boolean isMatched(String s) {
    return s != null && (s.equals("-") || s.matches(REGEX));
  }

  /**
   * Convertit la récurrence en chaîne de caractères
   * @return Chaîne correspondante
   */
  @Override
  public String toString() {
    return (isNull()) ? "-" : String.valueOf(_v) + _u;
  }

}
