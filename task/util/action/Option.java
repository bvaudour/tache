package task.util.action;

import task.Tache;
import task.util.type.*;

import java.util.HashSet;
import java.util.Set;

/**
 * L'enum <code>Option</code> définit les clés identifiant les champs des tâches pour la modification/l'ajout<br />
 * <ul>
 * <li><b>CAT</b> : Définit la catégorie</li>
 * <li><b>PRI</b> : Définit la priorité</li>
 * <li><b>DAT</b> : Définit la date d'échéance</li>
 * <li><b>BEG</b> : Définit l'heure de début</li>
 * <li><b>END</b> : Définit l'heure de fin</li>
 * <li><b>REC</b> : Définit la récurrence</li>
 * <li><b>TAC</b> : Définit la description</li>
 * <li><b>IDAT</b> : permet d'incrémenter la date d'échéance d'une certaine période</li>
 * <li><b>IBEG</b> : permet d'incrémenter l'heure de début d'une certaine durée</li>
 * <li><b>IEND</b> : permet d'incrémenter l'heure de fin d'une certaine durée</li>
 * <li><b>DDAT</b> : permet de décrémenter la date d'échéance d'une certaine période</li>
 * <li><b>DBEG</b> : permet de décrémenter l'heure de début d'une certaine durée</li>
 * <li><b>DEND</b> : permet de décrémenter l'heure de fin d'une certaine durée</li>
 * <li><b>RDAT</b> : permet de supprimer la date d'échéance</li>
 * <li><b>RBEG</b> : permet de supprimer l'heure de début</li>
 * <li><b>REND</b> : permet de supprimer l'heure de fin</li>
 * <li><b>REND</b> : permet de supprimer la récurrence</li>
 * <li><b>NONE</b> : Option  non valide</li>
 * </ul>
 * @author Benjamin VAUDOUR
 * @since 1.0
 */
public enum Option {

  CAT("c:",   Type.STR, "Catégorie"),
  PRI("p:",   Type.PRI, "Priorité"),
  DAT("d:",   Type.DAT, "Date"),
  BEG("b:",   Type.HRE, "Début"),
  END("e:",   Type.HRE, "Fin"),
  REC("r:",   Type.REC, "Récurrence"),
  TAC("n:",   Type.STR, "Description"),
  IDAT("+d:", Type.REC, "Date (incrémentation)"),
  IBEG("+b:", Type.REC, "Début (incrémentation)"),
  IEND("+e:", Type.REC, "Fin (incrémentation)"),
  DDAT("-d:", Type.REC, "Date (décrémentation)"),
  DBEG("-b:", Type.REC, "Début (décrémentation)"),
  DEND("-e:", Type.REC, "Fin (décrémentation)"),
  RDAT("-d",  Type.NONE, "Date (Suppression)"),
  RBEG("-b",  Type.NONE, "Début (Suppression)"),
  REND("-e",  Type.NONE, "Fin (Suppression)"),
  RREC("-r",  Type.NONE, "Récurrence (Suppression)"),
  NONE("",    Type.NONE, "");

  /**
   * Liste des options valides
   */
  public static final Option[] CODES = {CAT, PRI, DAT, BEG, END, REC, TAC, IDAT, IBEG, IEND, DDAT, DBEG, DEND, RDAT, RBEG, REND, RREC};

  /**
   * Récupère la liste des options de base (mode ajout)
   * Liste d'options
   */
  public static Set<Option> standard() {
    Set<Option> l = new HashSet<Option>();
    l.add(CAT);
    l.add(PRI);
    l.add(DAT);
    l.add(BEG);
    l.add(END);
    l.add(REC);
    l.add(TAC);
    return l;
  }

  /**
   * Récupère la liste d'options étendues (mode duplication/modification)
   * Liste d'options
   */
  public static Set<Option> extended() {
    Set<Option> l = standard();
    l.add(IDAT);
    l.add(IBEG);
    l.add(IEND);
    l.add(DDAT);
    l.add(DBEG);
    l.add(DEND);
    l.add(RDAT);
    l.add(RBEG);
    l.add(REND);
    l.add(RREC);
    return l;
  }

  private final String _code;
  private final Type   _type;
  private final String _help;

  private Option(String c, Type t, String h) {
    _code = c;
    _type = t;
    _help = h;
  }

  /**
   * Récupère le code de l'option
   * @return Code de l'option
   */
  public String code() {
    return _code;
  }

  /**
   * Récupère le type de valeur attendu pour l'option
   * @return Type de valeur
   * @see task.util.type.Type
   */
  public Type type() {
    return _type;
  }

  /**
   * Récupère la description de l'option
   * @return Description de l'option
   */
  public String help() {
    return _help;
  }

  /**
   * Récupère la liste des arguments encore à traiter
   * @param s Ligne de commande comprenant le nom de l'option et les arguments suivants
   * @return Liste des arguments expurgée du nom de l'option
   */
  public String next(String s) {
    s = Parser.removeFirst(s, _code);
    return Parser.lTrim(s);
  }

  /**
   * Récupère la prochaine option parmi les arguments
   * @param s Liste d'arguments d'entrée
   * @return Option correspondante
   */
  public static Option getInstance(String s) {
    for (Option c: CODES)
      if (s.startsWith(c.code())) return c;
    return NONE;
  }

  /**
   * Modifie le champ requis d'une tâche
   * @param t Tâche à modifier
   * @param s Nouvelle valeur du champ à modifier
   * @return true si le champ requis a été modifié, false sinon
   */
  public boolean set(Tache t, String s) {
    int i = 1;
    switch (this) {
      case CAT:  t.categorie(s)                           ; break;
      case PRI:  t.priority(Priorite.getInstance(s))      ; break;
      case DAT:  t.date(Date.getInstance(s))              ; break;
      case BEG:  t.begin(Heure.getInstance(s))            ; break;
      case END:  t.end(Heure.getInstance(s))              ; break;
      case REC:  t.recurrence(Recurrence.getInstance(s))  ; break;
      case TAC:  t.description(s)                         ; break;
      case DDAT: i = -1;
      case IDAT: t.incrDate(Recurrence.getInstance(s, i)) ; break;
      case DBEG: i = -1;
      case IBEG: t.incrBegin(Recurrence.getInstance(s, i)); break;
      case DEND: i = -1;
      case IEND: t.incrEnd(Recurrence.getInstance(s, i))  ; break;
      case RDAT: t.date(Date.NONE)                        ; break;
      case RBEG: t.begin(Heure.NONE)                      ; break;
      case REND: t.end(Heure.NONE)                        ; break;
      case RREC: t.recurrence(Recurrence.NONE)            ; break;
      default:   return false;
    }
    return true;
  }

}
