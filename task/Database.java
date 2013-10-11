package task;

import task.util.action.Option;
import task.util.type.*;
import task.util.type.Date;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: benjamin
 * Date: 07/10/13
 * Time: 09:14
 * To change this template use File | Settings | File Templates.
 */
public final class Database {

  private final Map<Integer, Tache>        taches = new TreeMap<Integer, Tache>();
  private final Map<Integer, Tache>        recurs = new TreeMap<Integer, Tache>();
  private final Map<Integer, Set<Integer>> ids    = new TreeMap<Integer, Set<Integer>>();

  private int lid = 0, lrid = Tache.RECUR;

  public void clear() {
    taches.clear();
    recurs.clear();
    ids.clear();
  }

  public Tache get(int id) {
    return table(id).get(id);
  }

  public Tache pop(int id) {
    Map<Integer, Tache> m = table(id);
    Tache t = m.remove(id);
    if (t == null) return t;
    if (t.isTache()) {
      if (t.rid() != 0) ids(t.rid()).remove(id);
      if (id == lid) {
        Set<Integer> l = m.keySet();
        do {
          --lid;
        } while (l.contains(lid));
      }
    } else if (id == lrid) {
      Set<Integer> l = m.keySet();
      do {
        --lrid;
      } while (l.contains(lrid));
    }
    return t;
  }

  public Set<Tache> popAll(int rid) {
    Set<Tache> out = new LinkedHashSet<Tache>();
    Set<Integer> l = new TreeSet<Integer>(ids(rid));
    for (int id : l) out.add(pop(id));
    pop(rid);
    return out;
  }

  public boolean push(Tache t) {
    if (t == null) return false;
    int id = t.id();
    if (id == 0) return false;
    Map<Integer, Tache> m = table(id);
    if (m.containsKey(id)) return false;
    m.put(id, t);
    if (t.isTache()) {
      if (t.rid() != 0) ids(t.rid()).add(id);
      if (id > lid) lid = id;
    } else if (id > lrid) {
      lrid = id;
    }
    return true;
  }

  public int nextId() {
    return lid + 1;
  }

  public int nextRid() {
    int n = Tache.RECUR;
    while (n++ <= lrid) {
      if (!recurs.containsKey(n)) return n;
    }
    return n;
  }

  public Set<Integer> getIds() {
    return new TreeSet<Integer>(taches.keySet());
  }

  public Set<Integer> getIds(Set<Integer> ids) {
    Set<Integer> out = new TreeSet<Integer>();
    Set<Integer> l = getIds();
    for (int i : ids)
      if (l.contains(i)) out.add(i);
    return out;
  }

  public Set<Integer> getIds(int rid) {
    Set<Integer> out = new TreeSet<Integer>();
    if (ids.containsKey(rid)) out.addAll(ids(rid));
    return out;
  }

  public Set<Integer> getIds(Date dMax) {
    Set<Integer> out = new TreeSet<Integer>();
    for (int i : getIds()) {
      Date d = get(i).date();
      if (!d.isNull() && d.le(dMax)) out.add(i);
    }
    return out;
  }

  public Set<Integer> getNotDeletedIds(Set<Integer> ids) {
    Set<Integer> out = new TreeSet<Integer>();
    ids = getIds(ids);
    for (int i : ids)
      if (!get(i).isDeleted()) out.add(i);
    return out;
  }

  public Set<Integer> getRids() {
    return new TreeSet<Integer>(recurs.keySet());
  }

  public Set<Integer> getRids(Set<Integer> rids) {
    Set<Integer> out = new TreeSet<Integer>();
    Set<Integer> l = getRids();
    for (int i : rids)
      if (l.contains(i)) out.add(i);
    return out;
  }

  public void sort() {
    int s = taches.size();
    if (s < 2) return;
    Integer[] l = getIds().toArray(new Integer[s]);
    int i = 1;
    while (i < s) {
      int j = i + 1;
      while (i > 0 && get(l[i]).before(get(l[i - 1])))
        swap(l[i], l[--i]);
      i = j;
    }
  }

  public boolean resequence() {
    if (lid == 0) return true;
    Set<Integer> l = getIds();
    int n = 0;
    for (int id: l)
      if (!modify(id, ++n)) return false;
    lid = n;
    return true;
  }

  public int addLackTasks(int rid, Date d0) {
    Tache tR = get(rid);
    if (tR == null) return 0;
    Recurrence r = tR.recurrence();
    Date dR = tR.date().copy();
    while (tR.date().lt(d0)) tR.incrDate(r);
    Set<Integer> l = getIds(rid);
    Date dL = dR.copy();
    int out = 0, n = 0;
    for (int id : l) {
      Tache t = get(id);
      Date d = t.date();
      while (dL.le(d)) dL = dL.add(r);
      if (!t.isDeleted() && d.ge(d0)) ++n;
    }
    while (n < 3) {
      Tache t = new Tache(tR);
      t.id(nextId());
      t.date(dL);
      if (push(t)) {
        ++out;
        if (dL.ge(d0)) ++n;
      }
      dL = dL.add(r);
    }
    return out;
  }

  public void addLackTasks(Date d0) {
    for (int rid : getRids()) addLackTasks(rid, d0);
  }

  public Set<Tache> add(Map<Option, String> m) {
    Set<Tache> out = new LinkedHashSet<Tache>();
    Tache t = Tache.build(new Tache(), m);
    int id = t.recurrence().isNull() ? nextId() : nextRid();
    t.id(id);
    t.repairRid();
    if (!push(t)) return out;
    if (t.isTache()) {
      out.add(t);
    } else {
      addLackTasks(id, Date.today());
      for (int i: getIds(id)) out.add(get(i));
    }
    return out;
  }

  public Set<Tache> close(Set<Integer> ids) {
    Set<Tache> out = new LinkedHashSet<Tache>();
    ids = getIds(ids);
    for (int id : ids) {
      Tache t = get(id);
      if (t.isDeleted()) continue;
      t.delete();
      out.add(t);
    }
    return out;
  }

  public Tache modify(int id, Map<Option, String> m) {
    Tache tO = get(id);
    if (tO == null) return tO;
    Tache tN = Tache.buildFrom(tO, m);
    if (tO.recurrence().isNull()) {
      if (tN.recurrence().isNull()) {
        tN.id(id);
        taches.put(id, tN);
      } else {
        pop(id);
        tN.id(nextRid());
        push(tN.repairRid());
      }
    } else {
      tO.delete();
      if (tO.recurrence().eq(tN.recurrence())) {
        tN.id(nextId());
        push(tN);
      } else {
        int idN = tN.recurrence().isNull() ? nextId() : nextRid();
        tN.id(idN);
        push(tN.repairRid());
      }
    }
    return tO;
  }

  public Set<Tache> modifyAll(int id, Map<Option, String> m) {
    Set<Tache> out = new LinkedHashSet<Tache>();
    Tache tO = get(id);
    if (tO == null) return out;
    Tache tR = get(tO.rid());
    if (tR == null) return out;
    out = popAll(tR.id());
    Tache tN = Tache.buildFrom(tO, m);
    int idN = tN.recurrence().isNull() ? nextId() : nextRid();
    tN.id(idN);
    push(tN.repairRid());
    return out;
  }

  private Map<Integer, Tache> table(int id) {
    return Tache.isTache(id) ? taches : recurs;
  }

  private Set<Integer> ids(int rid) {
    Set<Integer> out = ids.get(rid);
    if (out == null) {
      out = new TreeSet<Integer>();
      ids.put(rid, out);
    }
    return out;
  }

  private void swap(int i1, int i2) {
    Tache t1 = get(i2), t2 = get(i1);
    t1.id(i1);
    t2.id(i2);
    taches.put(i1, t1);
    taches.put(i2, t2);
    int rid1 = t1.rid(), rid2 = t2.rid();
    if (rid1 == rid2) return;
    if (rid1 != 0) {
      Set<Integer> l = ids(rid1);
      l.remove(i2);
      l.add(i1);
    }
    if (rid2 != 0) {
      Set<Integer> l = ids(rid2);
      l.remove(i1);
      l.add(i2);
    }
  }

  private boolean modify(int oldId, int newId) {
    if (oldId == newId)            return true;
    if (taches.containsKey(newId)) return false;
    Tache t = taches.remove(oldId);
    t.id(newId);
    taches.put(newId, t);
    int rid = t.rid();
    if (rid == 0) return true;
    Set<Integer> l = ids(rid);
    l.remove(oldId);
    l.add(newId);
    return true;
  }

}
