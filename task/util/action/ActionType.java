package task.util.action;

import task.util.type.Parser;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: benjamin
 * Date: 06/10/13
 * Time: 19:22
 * To change this template use File | Settings | File Templates.
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

  public static final ActionType[] CODES  = {SHELL, EXIT, ADD, MOD, DUP, CLO, DEL, HELP, VIEW};
  public static final ActionType[] CSHELL = {EXIT, ADD, MOD, DUP, CLO, DEL, HELP, VIEW};
  public static final ActionType[] CNORM  = {SHELL, ADD, MOD, DUP, CLO, DEL, HELP, VIEW};


  private final String _code;
  private final String _help;

  private ActionType(String c, String h) {
    _code = c;
    _help = h;
  }

  public String code() {
    return _code;
  }

  public String help() {
    return _help;
  }

  public String next(String s) {
    s = Parser.removeFirst(s, _code);
    return Parser.lTrim(s);
  }

  public Set<Option> options() {
    switch (this) {
      case MOD:
      case DUP: return Option.extended();
      case ADD: return Option.standard();
    }
    return new HashSet<Option>();
  }

  // 0 : id facultatif; 1 : id obligatoire; -1 ; id non utilisé
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

  // 0 : options facultatives; 1 : options obligatoires; -1 ; options non utilisées
  public int mustOption() {
    switch (this) {
      case DUP: return 0;
      case ADD:
      case MOD: return 1;
      default:  return -1;
    }
  }

  public static ActionType getInstance(String s) {
    if (s == null) return NONE;
    for (ActionType c: CODES)
      if (s.equals(c.code())) return c;
    return NONE;
  }

}
