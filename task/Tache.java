package task;

import task.util.action.Option;
import task.util.type.*;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: benjamin
 * Date: 06/10/13
 * Time: 18:54
 * To change this template use File | Settings | File Templates.
 */
public final class Tache implements Comparable<Tache> {

  // Définit la frontière entre les ids de récurrence et les ids de tâches
  public static final int RECUR = 1000;

  private int         _id         = 0;
  private int         _rid        = 0;
  private String      _categorie  = "";
  private Priorite    _priority   = Priorite.NONE;
  private Date        _date       = Date.NONE;
  private Heure       _begin      = Heure.NONE;
  private Heure       _end        = Heure.NONE;
  private Recurrence  _recurrence = Recurrence.NONE;
  private String      _name       = "";
  private Suppression _del        = Suppression.NO;

  public Tache() {}

  public Tache(Tache t) {
    if (t == null) return;
    id(t.id());
    rid(t.rid());
    categorie(t.categorie());
    priority(t.priority());
    date(t.date());
    begin(t.begin());
    end(t.end());
    recurrence(t.recurrence());
    description(t.description());
    setDeleted(t.isDeleted());
  }

  public Tache(String s) {
    if (s == null) return;
    String[] t = s.split("\\t");
    if (t.length != Champ.IN.length) return;
    for (int i = 0; i < t.length; ++i)
      set(t[i], Champ.IN[i]);
  }

  private void set(String s, Champ c) {
    switch (c) {
      case IDT:
        id(Parser.toInt(s));
        break;
      case IDR:
        rid(Parser.toInt(s));
        break;
      case CAT:
        categorie(s);
        break;
      case PRI:
        priority(Priorite.getInstance(s));
        break;
      case DAT:
        date(Date.getInstance(s));
        break;
      case BEG:
        begin(Heure.getInstance(s));
        break;
      case END:
        end(Heure.getInstance(s));
        break;
      case REC:
        recurrence(Recurrence.getInstance(s));
        break;
      case TAC:
        description(s);
        break;
      case DEL:
        setDeleted(Suppression.getInstance(s));
        break;
    }
  }

  public int        id() {
    return _id;
  }
  public int        rid() {
    return _rid;
  }
  public String     categorie() {
    return _categorie;
  }
  public Priorite   priority() {
    return _priority;
  }
  public Date       date() {
    return _date;
  }
  public Heure      begin() {
    return _begin;
  }
  public Heure      end() {
    return _end;
  }
  public Recurrence recurrence() {
    return _recurrence;
  }
  public String     description() {
    return _name;
  }
  public boolean    isDeleted() {
    return _del == Suppression.YES;
  }

  public int        id(int id) {
    _id = id;
    return _id;
  }
  public int        rid(int rid) {
    _rid = rid;
    return rid;
  }
  public String     categorie(String c) {
    _categorie = c;
    return _categorie;
  }
  public Priorite   priority(Priorite p) {
    _priority = p;
    return _priority;
  }
  public Date       date(Date d) {
    _date = d;
    return _date;
  }
  public Date       incrDate(Recurrence r) {
    return date(_date.add(r));
  }
  public Heure      begin(Heure h) {
    _begin = h;
    return _begin;
  }
  public Heure      incrBegin(Recurrence r) {
    return begin(_begin.add(r));
  }
  public Heure      end(Heure h) {
    _end = h;
    return _end;
  }
  public Heure      incrEnd(Recurrence r) {
    return end(_end.add(r));
  }
  public Recurrence recurrence(Recurrence r) {
    _recurrence = r;
    return _recurrence;
  }
  public String     description(String s) {
    _name = s;
    return _name;
  }
  public boolean    setDeleted(Suppression d) {
    _del = d;
    return _del == Suppression.YES;
  }
  public boolean    setDeleted(boolean b) {
    return setDeleted(Suppression.getInstance(b));
  }
  public boolean    delete() {
    return setDeleted(Suppression.YES);
  }
  public boolean    undelete() {
    return setDeleted(Suppression.NO);
  }

  public String in() {
    return in(new StringBuilder()).toString();
  }
  public String out() {
    return out(new StringBuilder()).toString();
  }

  public StringBuilder in(StringBuilder b) {
    for (Champ c : Champ.IN) in(b, c);
    return b;
  }
  public StringBuilder out(StringBuilder b) {
    for (Champ c : Champ.OUT) out(b, c);
    return b;
  }

  private StringBuilder in(StringBuilder b, Champ c) {
    String s = "";
    switch (c) {
      case IDT:
        s = String.valueOf(_id);
        break;
      case IDR:
        s = String.valueOf(_rid);
        break;
      case CAT:
        s = _categorie;
        break;
      case PRI:
        s = _priority.toString();
        break;
      case DAT:
        s = _date.in();
        break;
      case BEG:
        s = _begin.in();
        break;
      case END:
        s = _end.in();
        break;
      case REC:
        s = _recurrence.toString();
        break;
      case TAC:
        s = _name;
        break;
      case DEL:
        s = _del.in();
        break;
    }
    return c.in(b, s);
  }
  private StringBuilder out(StringBuilder b, Champ c) {
    String s = "";
    switch (c) {
      case IDT:
        s = String.valueOf(_id);
        break;
      case IDR:
        s = String.valueOf(_rid);
        break;
      case CAT:
        s = _categorie;
        break;
      case PRI:
        s = _priority.toString();
        break;
      case DAT:
        s = _date.out();
        break;
      case BEG:
        s = _begin.out();
        break;
      case END:
        s = _end.out();
        break;
      case REC:
        s = _recurrence.toString();
        break;
      case TAC:
        s = _name;
        break;
      case DEL:
        s = _del.out();
        break;
    }
    return c.out(b, s);
  }

  @Override
  public String toString() {
    StringBuilder b = new StringBuilder("( ");
    b.append(_id).append(", ");
    b.append(_rid).append(", ");
    b.append(_categorie).append(", ");
    b.append(_priority).append(", ");
    b.append(_date).append(", ");
    b.append(_begin).append(", ");
    b.append(_end).append(", ");
    b.append(_recurrence).append(", ");
    b.append(_name).append(", ");
    b.append(_del).append(" )");
    return b.toString();
  }

  @Override
  public int compareTo(Tache t) {
    int c = _date.compareTo(t._date);
    if (c != 0) return c;
    boolean b1 = _begin.isNull(), b2 = t._begin.isNull();
    if (b1)
      c = b2 ? 0 : 1;
    else
      c = b2 ? -1 : _begin.compareTo(t._begin);
    if (c != 0) return c;
    b1 = _end.isNull();
    b2 = t._end.isNull();
    if (b1)
      c = b2 ? 0 : -1;
    else
      c = b2 ? 1 : _end.compareTo(t._end);
    if (c != 0) return c;
    /*
    if (_begin.isNull())   return t._begin.isNull() ? 0 : 1;
    if (t._begin.isNull()) return -1;
    c = _begin.compareTo(t._begin);
    if (c != 0) return c;
    c = _end.compareTo(t._end);
    if (c != 0) return c;
    */
    return Priorite.compare(_priority, t._priority);
  }

  public boolean before(Tache t) {
    return compareTo(t) < 0;
  }

  public boolean after(Tache t) {
    return compareTo(t) > 0;
  }

  public static boolean isTache(int id) {
    return id < RECUR;
  }

  public boolean isTache() {
    return isTache(_id);
  }

  public boolean isExpired(Date d0, Heure h0) {
    if (_date.isNull()) return false;
    switch (_date.compareTo(d0)) {
      case -1: return true;
      case 1 : return false;
    }
    if (_begin.isNull() || _begin.ge(h0)) return false;
    return _end.isNull() || _end.lt(h0);
  }

  public boolean isValid(Date d0, Heure h0) {
    return isTache() ? !isObsolete(d0, h0) : !isDeleted();
  }

  public boolean isObsolete(Date d0, Heure h0) {
    return isDeleted() && (_date.isNull() || isExpired(d0, h0));
  }

  public  Tache repairHour() {
    if (_date.isNull()) {
      if (!_begin.isNull()) begin(Heure.NONE);
      if (!_end.isNull())   end(Heure.NONE);
    } else if (!_end.isNull()) {
      if (_begin.isNull() || _end.lt(begin()))
        end(Heure.NONE);
    }
    return this;
  }
  public Tache repairRecur() {
    if (_date.isNull() && !_recurrence.isNull())
      recurrence(Recurrence.NONE);
    return this;
  }
  public Tache repairRid() {
    if (isTache()) {
      if (_recurrence.isNull())
        rid(0);
      else if (_rid == 0)
        recurrence(Recurrence.NONE);
    } else {
      if (_recurrence.isNull())
        delete();
      else if (_rid == 0)
        rid(_id);
    }
    return this;
  }

  public Tache repair() {
    repairHour();
    repairRecur();
    return repairRid();
  }

  private static Tache set(Tache t, Map<Option, String> m) {
    for (Option o : m.keySet()) o.set(t, m.get(o));
    return t;
  }

  private static boolean beginModified(Map<Option, String> m) {
    if (m.containsKey(Option.BEG))  return true;
    if (m.containsKey(Option.IBEG)) return true;
    if (m.containsKey(Option.DBEG)) return true;
    if (m.containsKey(Option.RBEG)) return true;
    return false;
  }

  private static boolean endModified(Map<Option, String> m) {
    if (m.containsKey(Option.END))  return true;
    if (m.containsKey(Option.IEND)) return true;
    if (m.containsKey(Option.DEND)) return true;
    if (m.containsKey(Option.REND)) return true;
    return false;
  }

  public static Tache build(Tache t, Map<Option, String> m) {
    set(t, m);
    t.repairHour();
    t.repairRecur();
    return t;
  }

  public static Tache buildFrom(Tache t, Map<Option, String> m) {
    Tache tN = set(new Tache(t), m);
    if (!tN.begin().isNull() && !tN.end().isNull() && beginModified(m) && !endModified(m)) {
      Recurrence r = tN.begin().substract(t.begin());
      Heure h = t.end().add(r);
      tN.end(h.isNull() ? Heure.getInstance("23:59") : h);
    }
    tN.repairHour();
    tN.repairRecur();
    return tN;
  }

}
