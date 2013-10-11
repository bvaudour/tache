package task;

import task.util.action.Option;
import task.util.type.*;

import java.util.Map;

/**
 * La classe <code>Tache</code> représente la structure d'une tâche :
 * <ul>
 * <dd>id : Identifiant unique de la tâche. Si id > Tache.RECUR, il s'agit du template d'une tâche récurrente</dd>
 * <dd>rid : Identifiant du template de référence pour une tâche récurrente</dd>
 * <dd>categorie : Catégorie de la tâche</dd>
 * <dd>priority : Priorité de la tâche</dd>
 * <dd>date : Date d'échéance de la tâche</dd>
 * <dd>begin : Heure de début de la tâche</dd>
 * <dd>end : Heure de fin de la tâche</dd>
 * <dd>recurrence : Récurrence de la tâche</dd>
 * <dd>name : Description de la tâche</dd>
 * <dd>del : Marqueur de suppression de la tâche</dd>
 * </ul>
 * @author Benjamin VAUDOUR
 * @since JDK1.7
 * @see task.Champ
 */
public final class Tache implements Comparable<Tache> {

  /**
   * Définit la frontière entre les ids de récurrence et les ids de tâches
   */
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

  /**
   * Constructeur par défaut
   */
  public Tache() {}

  /**
   * Constructeur par copie
   */
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

  /**
   * Crée une nouvelle tâche à partir d'une chaîne de caractères
   * @param s
   *   Chaîne de caractère dont les différents champs de la tâche sont séparées par des tabulations
   * @see task.Champ
   */
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

  /**
   * Récupère l'ID de la tâche
   * @return ID de la tâche
   */
  public int        id() {
    return _id;
  }
  /**
   * Récupère l'ID de la tâche récurrente
   * @return ID de la tâche récurrente
   */
  public int        rid() {
    return _rid;
  }
  /**
   * Récupère la catégorie de la tâche
   * @return Catégorie de la tâche
   */
  public String     categorie() {
    return _categorie;
  }
  /**
   * Récupère la priorité de la tâche
   * @return ID de la tâche
   * @see task.util.type.Priorite
   */
  public Priorite   priority() {
    return _priority;
  }
  /**
   * Récupère la date d'échéance de la tâche
   * @return Date d'échéance de la tâche
   * @see task.util.type.Date
   */
  public Date       date() {
    return _date;
  }
  /**
   * Récupère l'heure de début de la tâche
   * @return Heure de début de la tâche
   * @see task.util.type.Heure
   */
  public Heure      begin() {
    return _begin;
  }
  /**
   * Récupère l'heure de fin de la tâche
   * @return Heure de fin de la tâche
   * @see task.util.type.Heure
   */
  public Heure      end() {
    return _end;
  }
  /**
   * Récupère la récurrence de la tâche
   * @return Récurrence de la tâche
   * @see task.util.type.Recurrence
   */
  public Recurrence recurrence() {
    return _recurrence;
  }
  /**
   * Récupère la description de la tâche
   * @return Description de la tâche
   */
  public String     description() {
    return _name;
  }
  /**
   * Récupère le marqueur de supprresion de la tâche
   * @return true si la tâche est supprimée, false sinon
   */
  public boolean    isDeleted() {
    return _del == Suppression.YES;
  }

  /**
   * Modifie l'ID de la tâche
   * @param id
   *   Nouvel ID de la tâche
   * @return ID modifié
   */
  public int        id(int id) {
    _id = id;
    return _id;
  }
  /**
   * Modifie l'ID de la tâche de référence
   * @param rid
   *   Nouvel ID de la tâche de référence
   * @return ID de référence modifié
   */
  public int        rid(int rid) {
    _rid = rid;
    return rid;
  }
  /**
   * Modifie la catégorie de la tâche
   * @param c
   *   Nouvelle catégorie de la tâche
   * @return Catégorie modifiée
   */
  public String     categorie(String c) {
    _categorie = c;
    return _categorie;
  }
  /**
   * Modifie la priorité de la tâche
   * @param p
   *   Nouvelle priorité de la tâche
   * @return Priorité modifiée
   */
  public Priorite   priority(Priorite p) {
    _priority = p;
    return _priority;
  }
  /**
   * Modifie la date de la tâche
   * @param d
   *   Nouvelle date de la tâche
   * @return Date modifiée
   */
  public Date       date(Date d) {
    _date = d;
    return _date;
  }
  /**
   * Incrémente la date de la tâche d'un certain temps
   * @param r
   *   Temps à ajouter/supprimer à la date
   * @return Date modifiée
   */
  public Date       incrDate(Recurrence r) {
    return date(_date.add(r));
  }
  /**
   * Modifie l'heure de début de la tâche
   * @param h
   *   Nouvelle heure de début de la tâche
   * @return Heure modifiée
   */
  public Heure      begin(Heure h) {
    _begin = h;
    return _begin;
  }
  /**
   * Incrémente l'heure de début de la tâche d'un certain temps
   * @param r
   *   Temps à ajouter/supprimer à l'heure de début
   * @return Heure modifiée
   */
  public Heure      incrBegin(Recurrence r) {
    return begin(_begin.add(r));
  }
  /**
   * Modifie l'heure de fin de la tâche
   * @param h
   *   Nouvelle heure de fin de la tâche
   * @return Heure modifiée
   */
  public Heure      end(Heure h) {
    _end = h;
    return _end;
  }
  /**
   * Incrémente l'heure de fin de la tâche d'un certain temps
   * @param r
   *   Temps à ajouter/supprimer à l'heure de fin
   * @return Heure modifiée
   */
  public Heure      incrEnd(Recurrence r) {
    return end(_end.add(r));
  }
  /**
   * Modifie la récurrence de la tâche
   * @param r
   *   Nouvelle récurrence de la tâche
   * @return Récurrence modifiée
   */
  public Recurrence recurrence(Recurrence r) {
    _recurrence = r;
    return _recurrence;
  }
  /**
   * Modifie la description de la tâche
   * @param s
   *   Nouvelle description de la tâche
   * @return Description modifiée
   */
  public String     description(String s) {
    _name = s;
    return _name;
  }
  /**
   * Modifie le marqueur de suppression de la tâche
   * @param d
   *   Marqueur de suppression de la tâche
   * @return true si tâche supprimée, false sinon
   * @see task.util.type.Suppression
   */
  public boolean    setDeleted(Suppression d) {
    _del = d;
    return _del == Suppression.YES;
  }
  /**
   * Modifie le marqueur de suppression de la tâche
   * @param b
   *   Marqueur de suppression de la tâche
   * @return true si tâche supprimée, false sinon
   */
  public boolean    setDeleted(boolean b) {
    return setDeleted(Suppression.getInstance(b));
  }
  /**
   * Supprime la tâche
   * @return true
   */
  public boolean    delete() {
    return setDeleted(Suppression.YES);
  }
  /**
   * Annule la suppression
   * @return false
   */
  public boolean    undelete() {
    return setDeleted(Suppression.NO);
  }

  /**
   * Affiche la tâche sous forme de chaîne de caractère compatible avec le format du fichier d'entrée
   * @return Tâche au format fichier
   */
  public String in() {
    return in(new StringBuilder()).toString();
  }
  /**
   * Affiche la tâche sous forme de chaîne de caractère "humainement" lisible (pour la sortie terminal)
   * @return Tâche au format sortie terminal
   */
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

  /**
   * Utilisée à des fins de tests (mode debug)
   * @return Champs de la tâche sous forme de liste de champs séparés par des virgules
   */
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

  /**
   * Compare la tâche avec une autre (pour le tri)
   * @param t
   *   Tâche à comparer
   * @return -1 si tâche courante à placer avant, 0 si pas d'importance, 1 si à placer après
   */
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

  /**
   * Compare la tâche avec une autre (pour le tri)
   * @param t
   *   Tâche à comparer
   * @return true si tâche courante avant, false sinon
   */
  public boolean before(Tache t) {
    return compareTo(t) < 0;
  }

  /**
   * Compare la tâche avec une autre (pour le tri)
   * @param t
   *   Tâche à comparer
   * @return true si tâche courante après, false sinon
   */
  public boolean after(Tache t) {
    return compareTo(t) > 0;
  }

  /**
   * Permet de savoir s'il s'agit d'un ID de tâche ou de tâche de référence (tâches récurrentes)
   * @param id
   *   ID à tester
   * @return true s'il s'agit d'un ID de tâche, false s'il s'agit d'un ID de tâche de référence
   */
  public static boolean isTache(int id) {
    return id < RECUR;
  }

  /**
   * Permet de savoir s'il s'agit d'une tâche ou d'une tâche de référence (tâches récurrentes)
   * @return true s'il s'agit d'une tâche standard, false s'il s'agit d'une tâche de référence
   */
  public boolean isTache() {
    return isTache(_id);
  }

  /**
   * Vérifie que l'échéance de la tâche n'est pas dépassée
   * @param d0
   *   Date de référence
   * @param h0
   *   Heure de référence
   * @return true si la tâche est expirée, false sinon
   */
  public boolean isExpired(Date d0, Heure h0) {
    if (_date.isNull()) return false;
    switch (_date.compareTo(d0)) {
      case -1: return true;
      case 1 : return false;
    }
    if (_begin.isNull() || _begin.ge(h0)) return false;
    return _end.isNull() || _end.lt(h0);
  }

  /**
   * Teste si la tâche est à supprimer
   * @param d0
   *   Date de référence
   * @param h0
   *   Heure de référence
   * @return true si la tâche est à conserver, false sinon
   */
  public boolean isValid(Date d0, Heure h0) {
    return isTache() ? !isObsolete(d0, h0) : !isDeleted();
  }

  /**
   * Teste si la tâche est obsolète
   * @param d0
   *   Date de référence
   * @param h0
   *   Heure de référence
   * @return true si la tâche est obsolète, false sinon
   */
  public boolean isObsolete(Date d0, Heure h0) {
    return isDeleted() && (_date.isNull() || isExpired(d0, h0));
  }

  /**
   * Répare le temps d'exécution de la tâche (heure de début / heure de fin)
   *   = L'heure de fin doit être supérieure à l'heure de début ou nulle 
   * @return Tâche modifiée
   */
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
  /**
   * Répare la récurrence de la tâche
   *   = Une tâche ne peut être récurrente que si la date d'échéance est non nulle 
   * @return Tâche modifiée
   */
  public Tache repairRecur() {
    if (_date.isNull() && !_recurrence.isNull())
      recurrence(Recurrence.NONE);
    return this;
  }
  /**
   * Répare l'ID de la tâche de référence :
   * <ul>
   * <dd>Une tâche non récurrente doit avoir un ID de référence à 0</dd>
   * <dd>Une tâche récurrente doit avoir un ID de référence différent de 0</dd>
   * </ul>
   * @return Tâche modifiée
   */
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

  /**
   * Répare la tâche
   * @return Tâche modifiée
   */
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

  /**
   * Modifie une tâche à partir d'options d'entrée
   * @param t
   *   Tâche à modifier
   * @param m
   *   Liste des champs à modifier (Clé : type de champ/type de valeur; Valeur : nouvelle valeur du champ sous forme de String)
   * @return Tâche modifiée
   */
  public static Tache build(Tache t, Map<Option, String> m) {
    set(t, m);
    t.repairHour();
    t.repairRecur();
    return t;
  }

  /**
   * Crée une tâche à partir d'options d'entrée et d'une tâche de base
   * @param t
   *   Tâche à copier
   * @param m
   *   Liste des champs à modifier (Clé : type de champ/type de valeur; Valeur : nouvelle valeur du champ sous forme de String)
   * @return Nouvelle tâche construite
   */
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
