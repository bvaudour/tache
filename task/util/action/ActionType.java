package task.util.action;

import task.util.type.Parser;

import java.util.HashSet;
import java.util.Set;

/**
 * L'enum <code>ActionType</code> définit le type d'actions gérées par l'application :<br />
 * <ul>
 * <li><b>SHELL</b> : Permet de passer au mode interactif</li>
 * <li><b>EXIT</b> : Permet de sortir du mode interactif</li>
 * <li><b>ADD</b> : Permet d'ajouter une tâche</li>
 * <li><b>MOD</b> : Permet de modifier une tâche</li>
 * <li><b>DUP</b> : Permet de dupliquer une tâche</li>
 * <li><b>CLO</b> : Permet de clôturer une tâche</li>
 * <li><b>DEL</b> : Permet de supprimer définitivement une tâche (confirmation demandée)</li>
 * <li><b>HELP</b> : Permet de visualiser l'aide</li>
 * <li><b>VIEW</b> : Permet de visualiser des tâches selon une liste d'ID fournie en entrée</li>
 * <li><b>VIEWALL</b> : Permet de visualiser toutes les tâches non clôturées</li>
 * <li><b>VIEWPARTIAL</b> : Permet de visualiser toutes les tâches non clôturées dont la date d'échéance est inférieure à un mois</li>
 * <li><b>SHELL</b> : Action nulle</li>
 * </ul>
 * @author Benjamin VAUDOUR
 * @since 1.0
 */
public enum ActionType {

  SHELL("shell", "Passe en mode shell"),
  EXIT("exit", "Sort du mode shell"),
  ADD("add", "Ajoute une tâche"),
  MOD("mod", "Modifie une(des) tâche(s)"),
  DUP("dup", "Duplique une(des) tâches(s)"),
  CLO("clo", "Clôture une(des) tâches(s)"),
  DEL("del", "Supprime une(des) tâches(s)"),
  HELP("help", "Affiche l'aide"),
  VIEW("view", "Affiche une(des) tâches(s)"),
  VIEWALL("view", ""),
  VIEWPARTIAL("view", ""),
  NONE("", "");

  /**
   * Liste des types d'action valides
   */
  public static final ActionType[] CODES  = {SHELL, EXIT, ADD, MOD, DUP, CLO, DEL, HELP, VIEW};
  /**
   * Liste des types d'action valides en mode interactif
   */
  public static final ActionType[] CSHELL = {EXIT, ADD, MOD, DUP, CLO, DEL, HELP, VIEW};
  /**
   * Liste des types d'action valides en mode normal
   */
  public static final ActionType[] CNORM  = {SHELL, ADD, MOD, DUP, CLO, DEL, HELP, VIEW};


  private final String _code;
  private final String _help;

  private ActionType(String c, String h) {
    _code = c;
    _help = h;
  }

  /**
   * Récupère le nom de l'action
   * @return Nom de l'action
   */
  public String code() {
    return _code;
  }

  /**
   * Récupère la description de l'action
   * @return Description de l'action
   */
  public String help() {
    return _help;
  }

  /**
   * Récupère la liste des arguments de l'action
   * @param s Ligne de commande comprenant le nom de l'action et les arguments
   * @return Liste des arguments expurgée du nom de l'action
   */
  public String next(String s) {
    s = Parser.removeFirst(s, _code);
    return Parser.lTrim(s);
  }

  /**
   * Récupère la liste des options autorisées pour l'action
   * @return Liste des options autorisées
   * @see task.util.action.Option
   */
  public Set<Option> options() {
    switch (this) {
      case MOD:
      case DUP: return Option.extended();
      case ADD: return Option.standard();
    }
    return new HashSet<Option>();
  }

  /**
   * Indique si l'action requiert une liste d'ID parmi les arguments
   * @return 1 si liste d'ID obligatoire, 0 si facultative, -1 si interdite
   */
  public int mustId() {
    switch (this) {
      case VIEW: return 0;
      case MOD:
      case DUP:
      case CLO:
      case DEL:  return 1;
      default:   return -1;
    }
  }

  /**
   * Indique si l'action requiert une liste d'options parmi les arguments
   * @return 1 si liste d'options obligatoire, 0 si facultative, -1 si interdite
   */
  public int mustOption() {
    switch (this) {
      case DUP: return 0;
      case ADD:
      case MOD: return 1;
      default:  return -1;
    }
  }

  /**
   * Récupère le type d'action à partir d'une commande saisie
   * @param s Liste d'arguments d'entrée
   * @return Type d'action correspondante
   */
  public static ActionType getInstance(String s) {
    if (s == null) return NONE;
    for (ActionType c: CODES)
      if (s.equals(c.code())) return c;
    return NONE;
  }

}
