package task.util.action;

import task.util.generic.T2;
import task.util.type.Parser;
import task.util.type.Type;

import java.util.*;

/**
 * La classe <code>Action</code> permet d'analyser et de gérer les arguments en entrée.
 * @author Benjamin VAUDOUR
 * @since 1.0
 */
public final class Action {

  /**
   * Aucune action
   */
  public static final Action NONE        = new Action(ActionType.NONE);
  /**
   * Action "Aide sur l'application"
   */
  public static final Action HELP        = new Action(ActionType.HELP);
  /**
   * Action "passage en mode interactif"
   */
  public static final Action SHELL       = new Action(ActionType.SHELL);
  /**
   * Action "Sortir du mode interactif"
   */
  public static final Action EXIT        = new Action(ActionType.EXIT);
  /**
   * Action "Voir toutes les tâches"
   */
  public static final Action VIEWALL     = new Action(ActionType.VIEWALL);
  /**
   * Action "Voir toutes les tâches à l'horizon '1 mois'"
   */
  public static final Action VIEWPARTIAL = new Action(ActionType.VIEWPARTIAL);

  private final ActionType          _t;
  private final Set<Integer> _i;
  private final Map<Option, String> _o;

  private Action(ActionType t, Set<Integer> i, Map<Option, String> o) {
    _t = t;
    _i = i;
    _o = o;
  }

  private Action(ActionType t) {
    this(t, new TreeSet<Integer>(), new HashMap<Option, String>());
  }

  private Action(ActionType t, Set<Integer> i) {
    this(t, i, new HashMap<Option, String>());
  }

  private Action(ActionType t , Map<Option, String> o) {
    this(t, new TreeSet<Integer>(), o);
  }

  /**
   * Analyse une série d'arguments pour connaître l'action à effectuer
   * @param s Liste d'arguments en entrée
   * @return Action résultante
   */
  public static Action getInstance(String s) {
    if (s == null) return VIEWPARTIAL;
    Scanner sc = new Scanner(s);
    if (!sc.hasNext()) return VIEWPARTIAL;
    ActionType t = ActionType.getInstance(sc.next());
    switch (t) {
      case HELP : return sc.hasNext() ? NONE : HELP;
      case SHELL: return sc.hasNext() ? NONE : SHELL;
      case EXIT : return sc.hasNext() ? NONE : EXIT;
      case VIEW : return getView(sc);
      case CLO  :
      case DEL  : return getSuppr(sc, t);
      case ADD  : return getAdd(sc);
      case MOD  :
      case DUP  : return getMod(sc, t);
    }
    return NONE;
  }

  /**
   * Récupère le type d'action à effectuer
   * @return Type d'action à effectuer
   */
  public ActionType type() {
    return _t;
  }

  /**
   * Récupère la liste des ID de tâches sur lesquels effectuer l'action
   * @return Liste d'ID
   */
  public Set<Integer> ids() {
    return _i;
  }

  /**
   * Récupère la liste d'options à appliquer
   * @return Liste d'options et valeurs associées
   * @see task.util.action.Option
   */
  public Map<Option, String> options() {
    return _o;
  }

  private static Action getView(Scanner sc) {
    if (!sc.hasNext()) return VIEWPARTIAL;
    String s = sc.next();
    if (sc.hasNext()) return NONE;
    if (s.equals("all")) return VIEWALL;
    Set<Integer> ids = getIds(s);
    return ids.isEmpty() ? NONE : new Action(ActionType.VIEW, ids);
  }

  private static Action getSuppr(Scanner sc, ActionType t) {
    Set<Integer> ids = getIds(sc);
    return (ids.isEmpty() || sc.hasNext()) ? NONE : new Action(t, ids);
  }

  private static Action getAdd(Scanner sc) {
    Map<Option, String> m = getOptions(sc, ActionType.ADD);
    return m.isEmpty() ? NONE : new Action(ActionType.ADD, m);
  }

  private static Action getMod(Scanner sc, ActionType t) {
    Set<Integer> ids = getIds(sc);
    if (ids.isEmpty()) return NONE;
    Map<Option, String> m = getOptions(sc, t);
    return (m.isEmpty() && t == ActionType.MOD) ? NONE : new Action(t, ids, m);
  }

  private static Set<Integer> getIds(Scanner sc) {
    return sc.hasNext() ? getIds(sc.next()) : new TreeSet<Integer>();
  }

  private static Set<Integer> getIds(String s) {
    Set<Integer> out = new TreeSet<Integer>();
    if (!s.matches("\\d+(-\\d+)?(,\\d+(-\\d+)?)*")) return out;
    String[] l = s.split(",");
    for (String e : l) {
      String[] r = e.split("-");
      switch (r.length) {
        case 1:
          out.add(Parser.toInt(r[0]));
          break;
        case 2:
          int i1 = Parser.toInt(r[0]), i2 = Parser.toInt(r[1]);
          int i = Math.min(i1, i2), end = Math.max(i1, i2);
          while (i <= end) {
            out.add(i);
            ++i;
          }
          break;
        default:
          out.clear();
          return out;
      }
    }
    return out;
  }

  private static Map<Option, String> getOptions(Scanner sc, ActionType t) {
    Set<Option> l = t.options();
    Map<Option, String> m = new HashMap<Option, String>();
    Stack<T2<Option, String>> pile = new Stack<T2<Option, String>>();
    while (sc.hasNext()) {
      T2<Option, String> p = new T2<Option, String>();
      if (!nextOption(sc, pile, p, l)) {
        m.clear();
        return m;
      }
      while (!pile.isEmpty()) {
        T2<Option, String> pt = pile.pop();
        m.put(pt.e1, pt.e2);
      }
      if (p.e1 != null) pile.push(p);
    }
    if (!pile.isEmpty()) {
      T2<Option, String> p = pile.pop();
      if (p.e1.type().isMatched(p.e2))
        m.put(p.e1, p.e2);
      else
        m.clear();
    }
    return m;
  }

  private static boolean nextOption(Scanner sc, Stack<T2<Option, String>> oldPair, T2<Option, String> newPair, Set<Option> l) {
    String s = sc.next();
    Option c = Option.getInstance(s);
    String v = c.next(s);
    T2<Option, String> p = oldPair.isEmpty() ? null : oldPair.pop();
    if (p != null) {
      Type t = p.e1.type();
      switch (t) {
        case STR:
          if (c == Option.NONE) {
            newPair.set(p.e1, p.e2.isEmpty() ? s : p.e2 + " " + s);
            return true;
          } else if (!t.isMatched(p.e2))
            return false;
          oldPair.push(p);
          break;
        default:
          if (!t.isMatched(v)) return false;
          p.e2 = v;
          oldPair.push(p);
          return true;
      }
    }
    if (!l.contains(c)) return false;
    switch (c.type()) {
      case STR:
        newPair.set(c, v);
        return true;
      case NONE:
        if (!v.isEmpty()) return false;
        p = new T2<Option, String>(c, v);
        oldPair.push(p);
        return true;
    }
    if (v.isEmpty()) {
      newPair.set(c, v);
      return true;
    }
    if (!c.type().isMatched(v)) return false;
    oldPair.push(new T2<Option, String>(c, v));
    return true;
  }

}
