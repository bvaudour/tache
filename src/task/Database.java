package task;

import task.util.action.Option;
import task.util.type.*;
import task.util.type.Date;

import java.util.*;

/**
 * La classe <code>Database</code> définit la base de données des tâches. Elle contient les tables suivantes :
 * <br></br>
 * <ul>
 * <li>La liste des tâches de référence aux tâches récurrentes indexées par leur ID</li>
 * <li>La liste des tâches indexées par leur ID</li>
 * <li>La liste des ID de tâches récurrentes par ID de référence</li>
 * </ul>
 * @author Benjamin VAUDOUR
 * @since 1.0
 * @see task.Tache
 */
public final class Database {

  private final Map<Integer, Tache>        taches = new TreeMap<Integer, Tache>();
  private final Map<Integer, Tache>        recurs = new TreeMap<Integer, Tache>();
  private final Map<Integer, Set<Integer>> ids    = new TreeMap<Integer, Set<Integer>>();

  private int lid = 0, lrid = Tache.RECUR;

  /**
   * Efface la base de donnée
   */
  public void clear() {
    taches.clear();
    recurs.clear();
    ids.clear();
  }

  /**
   * Récupère une tâche à partir de son ID
   * @param id ID de la tâche à récupérer
   * @return null si l'ID n'est pas trouvé dans la BDD, une tâche standard s'il s'agit d'un ID de tâche, une tâche de référence sinon
   */
  public Tache get(int id) {
    return table(id).get(id);
  }

  /**
   * Supprime une tâche
   * @param id ID de la tâche (ou tâche de référence) à supprimer
   * @return La tâche supprimée (null si l'ID n'a pas été trouvé)
   */
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

  /**
   * Supprime toutes les tâches récurrentes d'une tâche de référence
   * @param rid ID de la tâche de référence à supprimer
   * @return Liste des tâches supprimées
   */
  public Set<Tache> popAll(int rid) {
    Set<Tache> out = new LinkedHashSet<Tache>();
    Set<Integer> l = new TreeSet<Integer>(ids(rid));
    for (int id : l) out.add(pop(id));
    pop(rid);
    return out;
  }

  /**
   * Ajoute une tâche
   * <i>
   * Nota : L'ID de la tâche à ajouter ne doit pas être présent dans la BDD
   * Pour connaître le premier ID utilisable, utiliser la méthode nextId() (ou nextRid() s'il s'agit d'une tâche de référence)
   * </i>
   * @param t Tâche à ajouter
   * @return true si la tâche a été ajoutée, false sinon (dû à ID déjà présent dans la BDD ou bien tâche invalide)
   * @see #nextId()
   * @see #nextRid()
   */
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

  /**
   * Récupère le premier ID de tâche standard utilisable
   * @return ID utilisable pour une tâche à ajouter (dernier ID listé + 1)
   */
  public int nextId() {
    return lid + 1;
  }

  /**
   * Récupère le premier ID de tâche de référence utilisable
   * @return ID utilisable pour une tâche de référence à ajouter (premier "trou" trouvé)
   */
  public int nextRid() {
    int n = Tache.RECUR;
    while (n++ <= lrid) {
      if (!recurs.containsKey(n)) return n;
    }
    return n;
  }

  /**
   * Récupère la liste des ID de tâches standard
   * @return Liste d'ID
   */
  public Set<Integer> getIds() {
    return new TreeSet<Integer>(taches.keySet());
  }

  /**
   * Récupère la liste des ID de tâches standard parmi une liste d'ID
   * @param ids Liste d'ID à tester
   * @return Liste d'ID
   */
  public Set<Integer> getIds(Set<Integer> ids) {
    Set<Integer> out = new TreeSet<Integer>();
    Set<Integer> l = getIds();
    for (int i : ids)
      if (l.contains(i)) out.add(i);
    return out;
  }

  /**
   * Récupère la liste des ID de tâches récurrentes
   * @param rid ID de la tâche de référence
   * @return Liste d'ID
   */
  public Set<Integer> getIds(int rid) {
    Set<Integer> out = new TreeSet<Integer>();
    if (ids.containsKey(rid)) out.addAll(ids(rid));
    return out;
  }

  /**
   * Récupère la liste des ID de tâches standard dont l'échéance est inférieure à une date donnée
   * @param dMax Échéance maximale des tâches dont on souhaite récupérer l'ID
   * @return Liste d'ID
   */
  public Set<Integer> getIds(Date dMax) {
    Set<Integer> out = new TreeSet<Integer>();
    for (int i : getIds()) {
      Date d = get(i).date();
      if (!d.isNull() && d.le(dMax)) out.add(i);
    }
    return out;
  }

  /**
   * Récupère la liste des ID de tâches standard non supprimées parmi une liste d'ID
   * @param ids Liste d'ID à tester
   * @return Liste d'ID
   */
  public Set<Integer> getNotDeletedIds(Set<Integer> ids) {
    Set<Integer> out = new TreeSet<Integer>();
    ids = getIds(ids);
    for (int i : ids)
      if (!get(i).isDeleted()) out.add(i);
    return out;
  }

  /**
   * Récupère la liste des ID de tâches de référence
   * @return Liste d'ID de référence
   */
  public Set<Integer> getRids() {
    return new TreeSet<Integer>(recurs.keySet());
  }

  /**
   * Récupère la liste des ID de tâches de référence parmi une liste d'ID
   * @param rids Liste d'ID de référence à tester
   * @return Liste d'ID de référence
   */
  public Set<Integer> getRids(Set<Integer> rids) {
    Set<Integer> out = new TreeSet<Integer>();
    Set<Integer> l = getRids();
    for (int i : rids)
      if (l.contains(i)) out.add(i);
    return out;
  }

  /**
   * Trie la liste des tâches standard suivant l'ordre naturel
   * @see task.Tache#compareTo(Tache)
   */
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

  /**
   * Réordonnance les ID de tâches afin de supprimer les "trous"
   * <i>Nota : Pour que la méthode fonctionne, la BDD doit être préalablement triée</i>
   * @see #sort()
   */
  public boolean resequence() {
    if (lid == 0) return true;
    Set<Integer> l = getIds();
    int n = 0;
    for (int id: l)
      if (!modify(id, ++n)) return false;
    lid = n;
    return true;
  }

  /**
   * Ajoute les tâches récurrentes manquantes à partir d'une date donnée
   * Après exécution de la méthode :
   * <ul>
   * <li>la tâche de référence possède une échéance supérieure à la date d'entrée,</li>
   * <li>la BDD contient toutes les tâches récurrentes (supprimées ou non) entre l'ancienne date de référence et la nouvelle</li>
   * <li>La BDD contient au moins 3 tâches récurrentes (non supprimées) à la tâche de référence, avec une date d'échéance supérieure à la date d'entrée</li>
   * @param rid ID de la tâche de référence à traiter
   * @param d0 Date minimum de la future échéance de la tâche de référence
   * @return Nombre de tâches récurrentes manquantes ajoutées
   */
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

  /**
   * Ajoute toutes les tâches récurrentes manquantes de toutes les tâches de référence à partir d'une date donnée
   * @param d0 Date minimum de la future échéance de la tâche de référence
   * @see #addLackTasks(int, task.util.type.Date)
   */
  public void addLackTasks(Date d0) {
    for (int rid : getRids()) addLackTasks(rid, d0);
  }

  /**
   * Ajoute une tâche à partir d'un template de champs
   * <ul>
   * <li>Si la tâche est une tâche récurrente, celle-ci est ajoutée dans la table des tâches de référence, et les tâches manquantes sont créées.</li>
   * <li>Sinon, la tâche est directement ajoutée dans la table des tâches standard.</li>
   * </ul>
   * @param m Liste des champs (Clé : Type de champ/Type de valeur; Valeur : chaîne de caractère)
   * @return Liste des tâches créées
   * @see task.util.action.Option
   * @see task.Tache#build(Tache, java.util.Map)
   */
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

  /**
   * Clôture une(des) tâche(s)
   * @param ids Liste des ID de tâches à clôturer
   * @return Liste des tâches clôturées
   */
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

  /**
   * Modifie une tâche à partir d'un template
   * @param id ID de la tâche à modifier
   * @param m Liste des champs (Clé : Type de champ/Type de valeur; Valeur : chaîne de caractère)
   * @return Tâche modifiée
   * @see task.util.action.Option
   * @see task.Tache#buildFrom(Tache, java.util.Map)
   */
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

  /**
   * Modifie toutes les tâches récurrentes associées à une même tâche de référence
   * @param id ID de la tâche récurrente à modifier servante de base à la tâche de référence
   * @param m Liste des champs (Clé : Type de champ/Type de valeur; Valeur : chaîne de caractère)
   * @return Liste des tâches récurrentes modifiées
   * @see task.util.action.Option
   * @see task.Tache#buildFrom(Tache, java.util.Map)
   */
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
