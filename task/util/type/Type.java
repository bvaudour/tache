package task.util.type;

/**
 * L'enum <code>Type</code> définit les propriétés des différents types de champs d'une tâche (notamment l'alignement de la colonne) :<br />
 * <ul>
 * <li><b>STR</b> : Chaîne de caractères normale (alignée à gauche)</li>
 * <li><b>INT</b> : Nombre entier (aligné à droite)</li>
 * <li><b>PRI</b> : Priorité (centrée)</li>
 * <li><b>DAT</b> : Échéance (centrée)</li>
 * <li><b>HRE</b> : Heure d'exécution (centrée)</li>
 * <li><b>REC</b> : Récurrence (centrée)</li>
 * <li><b>DEL</b> : Marqueur de suppression (centrée mais actuellement non utilisé)</li>
 * <li><b>NONE</b> : Chaîne vide</li>
 * </ul>
 * @author Benjamin VAUDOUR
 * @since 1.0
 * @see task.Champ
 */
public enum Type {

  STR('L'),
  INT('R'),
  PRI('C'),
  DAT('C'),
  HRE('C'),
  REC('C'),
  DEL('C'),
  NONE('L');

  private final char _align;

  private Type(char c) {
    _align = c;
  }

  /**
   * Vérifie si le type est par défaut centré
   * @return true si une chaîne de ce type doit être centrée, false sinon
   */
  public boolean center() {
    return _align == 'C';
  }

  /**
   * Vérifie si le type est par défaut aligné à gauche
   * @return true si une chaîne de ce type doit être alignée à gauche, false sinon
   */
  public boolean left() {
    return _align == 'L';
  }

  /**
   * Vérifie si le type est par défaut aligné à droite
   * @return true si une chaîne de ce type doit être alignée à gauche, false sinon
   */
  public boolean right() {
    return _align == 'R';
  }

  /**
   * Vérifie si une chaîne de caractère est bien du type défini
   * @return true si une chaîne correspond, false sinon
   */
  public boolean isMatched(String s) {
    if (s == null) return false;
    switch (this) {
      case NONE: return s.isEmpty();
      case STR: return !s.isEmpty();
      case INT: return s.matches("\\d+");
      case PRI: return s.matches(Priorite.REGEX);
      case DAT: return Date.isMatched(s);
      case HRE: return Heure.isMatched(s);
      case REC: return Recurrence.isMatched(s);
      case DEL: return s.matches(Suppression.REGEX);
    }
    return false;
  }

}
