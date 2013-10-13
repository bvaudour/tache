package task.util.type;

/**
 * L'enum <code>Suppression</code> définit le marqueur de suppression d'une tâche :<br />
 * <ul>
 * <li><b>YES</b> : Tâche clôturée</li>
 * <li><b>NO</b> : Tâche non clôturée</li>
 * </ul>
 * @author Benjamin VAUDOUR
 * @since 1.0
 */
public enum Suppression {

  YES(true),
  NO(false);

  /**
   * Expression régulière permettant d'identifier un format correct pour le marqueur de suppression
   */
  public static final String REGEX = "(X|x|0|1|-)";

  private final boolean _b;

  private Suppression(boolean b) {
    _b = b;
  }

  /**
   * Affiche le marqueur pour la sortie fichier
   * @return 1 si tâche clôturée, 0 sinon
   */
  public String in() {
    return (_b) ? "1" : "0";
  }

  /**
   * Affiche le marqueur pour la sortie terminal (actuellement non utilisé)
   * @return X si tâche clôturée, blanc sinon
   */
  public String out() {
    return (_b) ? "X" : " ";
  }

  /**
   * Récupère le marqueur correspondant
   * @param b true si marqueur YES, false sinon
   * @return Marqueur
   */
  public static Suppression getInstance(boolean b) {
    return (b) ? YES : NO;
  }

  /**
   * Récupère le marqueur de suppression
   * @param c Caractère identifiant le marqueur
   * @return Marqueur correspondant
   */
  public static Suppression getInstance(char c) {
    c = Character.toUpperCase(c);
    return getInstance(c == '1' || c == 'X');
  }

  /**
   * Récupère le marqueur de suppression
   * @param s Chaîne à analyser
   * @return Marqueur correspondant
   */
  public static Suppression getInstance(String s) {
    return (s != null && s.length() == 1) ? getInstance(s.charAt(0)) : NO;
  }

  /**
   * Affichage du marqueur
   * @return 1 si tâche clôturée, 0 sinon
   */
  @Override
  public String toString() {
    return in();
  }

}
