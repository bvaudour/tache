package task;

import task.i18n.Messages;
import task.util.type.Parser;
import task.util.type.Type;

/**
 * L'enum <code>Champ</code> décrit les différents types de champs d'une tâche :<br />
 * <ul>
 * <li><b>IDT</b> : ID de tâche</li>
 * <li><b>IDR</b> : ID de tâche de référence (tâche récurrente)</li>
 * <li><b>CAT</b> : Catégorie de la tâche</li>
 * <li><b>PRI</b> : Priorité de la tâche</li>
 * <li><b>DAT</b> : Échéance de la tâche</li>
 * <li><b>BEG</b> : Heure de début de la tâche</li>
 * <li><b>END</b> : Heure de fin de la tâche</li>
 * <li><b>REC</b> : Récurrence de la tâche</li>
 * <li><b>TAC</b> : Description de la tâche</li>
 * <li><b>DEL</b> : Marqueur de suppression de la tâche</li>
 * </ul>
 * @author Benjamin VAUDOUR
 * @since 1.0
 */
public enum Champ {

  IDT(Messages.getString("Champ.id"), 2, Type.INT), //$NON-NLS-1$
  IDR(Messages.getString("Champ.rid"), 4, Type.INT), //$NON-NLS-1$
  CAT(Messages.getString("Champ.categorie"), 15, Type.STR), //$NON-NLS-1$
  PRI(Messages.getString("Champ.priority"), 3, Type.PRI), //$NON-NLS-1$
  DAT(Messages.getString("Champ.date"), 10, Type.DAT), //$NON-NLS-1$
  BEG(Messages.getString("Champ.begin"), 5, Type.HRE), //$NON-NLS-1$
  END(Messages.getString("Champ.end"), 5, Type.HRE), //$NON-NLS-1$
  REC(Messages.getString("Champ.recurrence"), 3, Type.REC), //$NON-NLS-1$
  TAC(Messages.getString("Champ.description"), 30, Type.STR), //$NON-NLS-1$
  DEL(Messages.getString("Champ.deleted"), 3, Type.DEL); //$NON-NLS-1$

  /**
   * Liste des Champs autorisés en entrée (extraction fichier)
   */
  public static final Champ[] IN  = { IDT, IDR, CAT, PRI, DAT, BEG, END, REC, TAC, DEL };
  /**
   * Liste des Champs autorisés en sortie terminal
   */
  public static final Champ[] OUT = { IDT, CAT, PRI, DAT, BEG, END, REC, TAC };

  private final String _name;
  private final int    _size;
  private final Type   _type;

  private Champ(String n, int s, Type t) {
    _name = n;
    _size = s;
    _type = t;
  }

  /**
   * Formatage du champ pour sortie console
   * @param b Chaîne à modifier
   * @param s Chaîne à ajouter
   * @return Chaîne modifiée
   */
  public StringBuilder out(StringBuilder b, String s) {
    if (b.length() != 0) b.append(" "); //$NON-NLS-1$
    return b.append(Parser.align(s, _type, _size));
  }

  private StringBuilder _title(StringBuilder b) {
    return out(b, _name);
  }

  /**
   * Formatage du champ pour sortie fichier
   * @param b Chaîne à modifier
   * @param s Chaîne à ajouter
   * @return Chaîne modifiée
   */
  public StringBuilder in(StringBuilder b, String s) {
    if (b.length() != 0) b.append("\t"); //$NON-NLS-1$
    return b.append(s);
  }

  /**
   * Formatage de l'en-tête
   * @return En-tête pour sortie console
   */
  public static StringBuilder title() {
    StringBuilder b = new StringBuilder();
    for (Champ c : OUT) c._title(b);
    return b;
  }

}
