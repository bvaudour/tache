package task.util.type;

/**
 * L'enum <code>Priorite</code> définit la priorité d'une tâche :<br />
 * <ul>
 * <li><b>HIGH</b> : Priorité la plus importante</li>
 * <li><b>MEDIUM</b> : Priorité moyenne</li>
 * <li><b>LOW</b> : Priorité faible</li>
 * <li><b>NONE</b> : Aucune priorité</li>
 * </ul>
 * @author Benjamin VAUDOUR
 * @since 1.0
 */
public enum Priorite {

  HIGH('H', 0),
  MEDIUM('M', 1),
  LOW('L', 2),
  NONE('-', 3);

  /**
   * Expression régulière permettant d'identifier un format de priorité valide
   */
  public static final String REGEX = "(H|h|M|m|L|l|-)";

  private static final Priorite[] _l = { HIGH, MEDIUM, LOW };

  private final char _c;
  private final int  _i;

  private Priorite(char c, int i) {
    _c = c;
    _i = i;
  }

  /**
   * Convertit un caractère en priorité
   * @param c Caractère à convertir
   * @return Priorité correspondante (Priorité nulle si aucune correspondance)
   */
  public static Priorite getInstance(char c) {
    c = Character.toUpperCase(c);
    for (Priorite p : _l)
      if (p._c == c) return p;
    return NONE;
  }

  /**
   * Convertit une chaîne de caractères en priorité
   * @param s Chaîne de caractères à convertir
   * @return Priorité correspondante (Priorité nulle si aucune correspondance)
   */
  public static Priorite getInstance(String s) {
    return (s != null && s.length() == 1) ? getInstance(s.charAt(0)) : NONE;
  }

  /**
   * Compare deux priorités
   * @param p1 priorité 1
   * @param p2 priorité 2
   * @return 0 si les priorités sont identiques, -1 si p1 prioritaire sur p2, 1 sinon
   */
  public static int compare(Priorite p1, Priorite p2) {
    return Integer.compare(p1._i, p2._i);
  }

  /**
   * Convertit la priorité en chaîne de caractères
   * @return Chaîne correspondante
   */
  @Override
  public String toString() {
    return String.valueOf(_c);
  }

}
