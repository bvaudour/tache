package task.util.color;

/**
 * L'enum <code>Effet</code> définit les effets de police pour la sortie terminal :<br />
 * <ul>
 * <li><b>NONE</b> : Aucun effet</li>
 * <li><b>NORMAL</b> : Effet standard</li>
 * <li><b>BOLD</b> : Gras</li>
 * <li><b>ITALIC</b> : Italique</li>
 * <li><b>UNDERLINE</b> : Souligné</li>
 * <li><b>BLINK</b> : Clignotant</li>
 * <li><b>INVERTED</b> : Couleurs inversées</li>
 * </ul>
 * @author Benjamin VAUDOUR
 * @since 1.0
 */
public enum Effet {
  NONE(-1),
  NORMAL(0),
  BOLD(1),
  ITALIC(3),
  UNDERLINE(4),
  BLINK(5),
  INVERTED(7);

  private final int _i;

  private Effet(int i) {
    _i = i;
  }

  /**
   * Sort la valeur ANSI de l'effet
   * @return Valeur sous forme de chaîne de caractères
   */
  public String value() {
    return (this == NONE) ? "" : String.valueOf(_i);
  }
}
