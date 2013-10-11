package task.util.action;

import task.Tache;
import task.util.type.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: benjamin
 * Date: 06/10/13
 * Time: 19:18
 * To change this template use File | Settings | File Templates.
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

  public static final Option[] CODES = {CAT, PRI, DAT, BEG, END, REC, TAC, IDAT, IBEG, IEND, DDAT, DBEG, DEND, RDAT, RBEG, REND, RREC};

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

  public String code() {
    return _code;
  }

  public Type type() {
    return _type;
  }

  public String help() {
    return _help;
  }

  public String next(String s) {
    s = Parser.removeFirst(s, _code);
    return Parser.lTrim(s);
  }

  public static Option getInstance(String s) {
    for (Option c: CODES)
      if (s.startsWith(c.code())) return c;
    return NONE;
  }

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
