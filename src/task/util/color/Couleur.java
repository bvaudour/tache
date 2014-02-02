package task.util.color;

/**
 * L'enum <code>Couleur</code> définit les couleurs d'arrière-plan et de police pour la sortie terminal :<br />
 * <ul>
 * <li><b>NONE</b> : Couleur par défaut</li>
 * <li><b>NORMAL</b> : Effet standard</li>
 * <li><b>BLACK</b> : Noir</li>
 * <li><b>RED</b> : Rouge foncé</li>
 * <li><b>GREEN</b> : Vert foncé</li>
 * <li><b>YELLOW</b> : Orange</li>
 * <li><b>BLUE</b> : Bleu foncé</li>
 * <li><b>MAJENTA</b> : Majenta foncé</li>
 * <li><b>CYAN</b> : Cyan foncé</li>
 * <li><b>WHITE</b> : Gris</li>
 * <li><b>LRED</b> : Rouge clair</li>
 * <li><b>LGREEN</b> : Vert clair</li>
 * <li><b>LYELLOW</b> : Jaune</li>
 * <li><b>LBLUE</b> : Bleu clair</li>
 * <li><b>LMAJENTA</b> : Majenta</li>
 * <li><b>LCYAN</b> : Cyan</li>
 * <li><b>LWHITE</b> : Blanc</li>
 * </ul>
 * @author Benjamin VAUDOUR
 * @since 1.0
 */
public enum Couleur {

  NONE(-1),
  BLACK(232),
  RED(1),
  GREEN(2),
  YELLOW(3),
  BLUE(4),
  MAJENTA(5),
  CYAN(6),
  WHITE(7),
  LBLACK(8),
  LRED(9),
  LGREEN(10),
  LYELLOW(11),
  LBLUE(12),
  LMAJENTA(13),
  LCYAN(14),
  LWHITE(15);

  private final int	_i;

  private Couleur(int i) {
    _i = i;
  }

  /**
   * Sort la valeur ANSI de la couleur de police
   * @return Valeur sous forme de chaîne de caractères
   */
  public String font() {
    return (this == NONE) ? "" : "38;5;" + _i;
  }

  /**
   * Sort la valeur ANSI de la couleur d'arrière-plan
   * @return Valeur sous forme de chaîne de caractères
   */
  public String back() {
    return (this == NONE) ? "" : "48;5;" + _i;
  }

}
