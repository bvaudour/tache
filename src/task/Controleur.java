package task;

import task.i18n.Messages;
import task.util.action.Action;
import task.util.action.Option;
import task.util.color.Ansi;
import task.util.color.Couleur;
import task.util.type.Date;
import task.util.type.Heure;
import task.util.type.Priorite;
import task.util.type.Recurrence;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;

/**
 * La classe <code>Controleur</code> permet le contrôle de la BDD des tâches et gère les entrées/sorties (fichier + terminal)
 * @author Benjamin VAUDOUR
 * @since 1.0
 */
public class Controleur {

  /**
   * Nom du fichier des tâches par défaut (pour le moment, le seul utilisable).<br />
   * Le fichier contient, au début, l'ensemble des tâches de références (utilisées pour la création de tâches récurrentes), suivie de la liste de toutes les tâches.<br />
   * Chaque tâche est représentée par une ligne dans le fichier. Une ligne de tâche contient les champs suivants, séparés par des tabulations :
   * <ul>
   * <li><b>ID</b> : Il s'agit de l'ID de la tâche (ou tâche de référence, suivant le rang)</li>
   * <li><b>RID</b> : ID de la tâche de référence pour les tâches récurrentes, 0 sinon</li>
   * <li><b>Catégorie</b> : Catégorie de la tâche</li>
   * <li><b>Priorité</b> : Priorité de la tâche (H(igh), M(edium), L(ow), caractère '-' si aucune priorité définie)</li>
   * <li><b>Date</b> : Échéance de la tâche (au format YYYYMMDD, '-' si aucune échéance)</li>
   * <li><b>Début</b> : Heure de début de la tâche (au format hhmm sur 24h, '-' si non défini)</li>
   * <li><b>Fin</b> : Heure de fin de la tache (format identique à début)</li>
   * <li><b>Récurrence</b> : Récurrence de la tâche (entier suivi de l'unité de récurrence : y(ear), m(onth), w(eek), d(ay), h(our) '(minutes), '-' si tâche non récurrente)</li>
   * <li><b>Description</b> : Description de la tâche</li>
   * <li><b>Suppression</b> : Marqueur de suppression de la tâche (1 si tâche clôturée, 0 sinon)</li>
   * </ul>
   */
  public static final String DEFAULTFILE = Messages.getString("Controleur.file"); //$NON-NLS-1$

  private final String   _path;
  private final Database _db    = new Database();
  private       boolean  _shell = false;
  private       boolean  _open  = false;

  private static String defaultPath() {
    StringBuilder b = new StringBuilder(System.getProperty(Messages.getString("Controleur.home"))); //$NON-NLS-1$
    b.append(File.separator).append(DEFAULTFILE);
    return b.toString();
  }

  /**
   * Constructeur générique
   * @param path Nom du chemin complet du fichier des tâches
   */
  public Controleur(String path) {
    _path = path;
  }

  /**
   * Constructeur par défaut<br />
   * Utilise le chemin du fichier par défaut (dans le répertoire utilisateur)
   * @see #DEFAULTFILE
   */
  public Controleur() {
    this(defaultPath());
  }

  /**
   * Ouvre le fichier des tâches et crée une BDD interne
   * @return true si le fichier est ouvert ou inexistant (lancement de l'application pour la première fois), false si échec ouverture
   * @see task.Database
   */
  public boolean open() {
    File f = new File(_path);

    // Si le fichier n'existe pas, on sort (permet la création à l'initialisation)
    if (!f.exists()) {
      _open = true;
      return true;
    }
    try {
      Scanner sc = new Scanner(f);
      if (_open)
        _db.clear();
      else
        _open = true;
      Date d0    = Date.today();
      Heure h0   = Heure.today();
      while (sc.hasNext()) {
        Tache t = new Tache(sc.nextLine());
        if (t.repair().isValid(d0, h0))
          _db.push(t);
      }
      _db.addLackTasks(d0);
      _db.sort();
      _db.resequence();
      sc.close();
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  /**
   * Enregistre le fichier des tâches
   * @return false si échec enregistrement ou aucune modification apportée, false sinon
   */
  public boolean close() {
    if (!_open) return false;
    try {
      PrintWriter ecr = new PrintWriter(new FileWriter(_path, false));
      Set<Integer> l = _db.getRids();
      for (int i : l) {
        Tache t = _db.get(i);
        if (t == null) continue;
        ecr.println(t.in());
      }
      l = _db.getIds();
      for (int i : l) {
        Tache t = _db.get(i);
        if (t == null) continue;
        ecr.println(t.in());
      }
      ecr.flush();
      ecr.close();
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  /**
   * Exécute une action sur la BDD des tâches
   * @param s commande à exécuter (action suivie ou non d'arguments de commande)
   * @see task.Database
   * @see task.util.action.Action
   */
  public void execute(String s) {
    Action a = Action.getInstance(s);
    switch (a.type()) {
      case HELP:
        help();
        return;
      case SHELL:
        shell();
        return;
      case EXIT:
        exit();
        return;
      case NONE:
        error();
        return;
      default:
      	break;
    }
    if (!_open && !open()) {
      System.out.println(Ansi.bold(Couleur.LRED).format(Messages.getString("Controleur.error_open_file"))); //$NON-NLS-1$
      return;
    }
    switch (a.type()) {
      case VIEW:
        view(a.ids());
        return;
      case VIEWALL:
        viewAll();
        return;
      case VIEWPARTIAL:
        viewPartial();
        return;
      case ADD:
        add(a.options());
        return;
      case CLO:
        close(a.ids());
        return;
      case DEL:
        delete(a.ids());
        return;
      case MOD:
        modify(a.ids(), a.options());
        return;
      case DUP:
        duplicate(a.ids(), a.options());
        return;
      default:
      	break;
    }
  }

  /**
   * Affiche l'aide sur les commandes de base
   * @see task.Help
   */
  public void help() {
    Help.print(_shell);
  }

  /**
   * Passe en mode interactif (affiche erreur si déjà en mode interactif)
   */
  public void shell() {
    if (_shell) {
      error();
      return;
    }
    forceShell();
  }

  /**
   * Affiche la liste de toutes les tâches
   */
  public void viewAll() {
    view(_db.getIds());
  }

  /**
   * Affiche toutes les tâches dont l'échéance est inférieure à un mois
   */
  public void viewPartial() {
    view(_db.getIds(Date.today().add(Recurrence.getInstance(1, Recurrence.Unit.MONTH))));
  }

  /**
   * Passe en mode interactif (sans erreur)
   */
  public void forceShell() {
    _shell = true;
    while (_shell) execute(_getShell(Messages.getString("Controleur.prompt"))); //$NON-NLS-1$
  }

  /**
   * Sort du mode interactif
   */
  public void exitShell() {
    _shell = false;
  }

  private void error() {
    System.out.println(Ansi.bold(Couleur.LRED).format(Messages.getString("Controleur.bad_syntax"))); //$NON-NLS-1$
    help();
  }

  private void exit() {
    if (!_shell) {
      error();
      return;
    }
    exitShell();
  }

  private void view(Set<Integer> ids) {
    ids = _db.getNotDeletedIds(ids);
    System.out.println();

    if (ids.isEmpty()) {
      System.out.println(Ansi.normal(Couleur.YELLOW).format(Messages.getString("Controleur.no_task"))); //$NON-NLS-1$
      return;
    }

    System.out.println(Ansi.BUNDERLINE.format(Champ.title()));

    boolean b   = false;
    Date    d0  = Date.today();
    Heure   h0  = Heure.today();
    Date    dow = d0.lastDow();
    Date    dom = d0.lastDom();
    Date    d7  = d0.add(Recurrence.getInstance(7, Recurrence.Unit.DAY));
    for (int i : ids) {
      Couleur back = b ? Couleur.BLACK : Couleur.NONE;
      b = !b;
      Tache t = _db.get(i);
      boolean p = t.priority() == Priorite.HIGH;
      Ansi a;
      if (t.isExpired(d0, h0)) {
        a = (p) ? Ansi.bblink(Couleur.RED, back) : Ansi.blink(Couleur.RED, back);
      } else if (t.date().eq(d0)) {
        a = (p) ? Ansi.bold(Couleur.LRED, back) : Ansi.normal(Couleur.LRED, back);
      } else if (t.date().le(dow)) {
        a = (p) ? Ansi.bold(Couleur.LYELLOW, back) : Ansi.normal(Couleur.LYELLOW, back);
      } else if (t.date().le(d7)) {
        a = (p) ? Ansi.bold(Couleur.LGREEN, back) : Ansi.normal(Couleur.LGREEN, back);
      } else if (t.date().le(dom)) {
        a = (p) ? Ansi.bold(Couleur.LCYAN, back) : Ansi.normal(Couleur.LCYAN, back);
      } else if (!t.date().isNull()) {
        a = (p) ? Ansi.bold(Couleur.NONE, back) : Ansi.normal(Couleur.NONE, back);
      } else {
        switch (t.priority()) {
          case HIGH:
            a = Ansi.bitalic(Couleur.LRED, back);
            break;
          case MEDIUM:
            a = Ansi.bitalic(Couleur.LYELLOW, back);
            break;
          case LOW:
            a = Ansi.bitalic(Couleur.LGREEN, back);
            break;
          default:
            a = Ansi.italic(Couleur.NONE, back);
            break;
        }
      }
      System.out.println(a.format(t.out()));
    }
    System.out.println();
    _printCount(ids.size(), Messages.getString("Controleur.count_displayed")); //$NON-NLS-1$
  }

  private void add(Map<Option, String> m) {
    Set<Tache> l = _db.add(m);
    String action = Messages.getString("Controleur.count_added"); //$NON-NLS-1$
    String added = Messages.getString("Controleur.added_task"); //$NON-NLS-1$
    if (l.isEmpty()) {
      _printCount(0, action);
      return;
    }
    _db.sort();
    _db.resequence();
    for (Tache t: l) _printName(t, added);
    _printCount(l.size(), action);
  }

  private void close(Set<Integer> ids) {
    Set<Tache> l = _db.close(ids);
    String action = Messages.getString("Controleur.count_closed"); //$NON-NLS-1$
    String closed = Messages.getString("Controleur.closed_task"); //$NON-NLS-1$
    if (l.isEmpty()) {
      _printCount(0, action);
      return;
    }
    for (Tache t: l) _printName(t, closed);
    _printCount(l.size(), action);
    _db.addLackTasks(Date.today());
    _db.sort();
    _db.resequence();
  }

  private void delete(Set<Integer> ids) {
    String action  = Messages.getString("Controleur.count_deleted"); //$NON-NLS-1$
    String deleted = Messages.getString("Controleur.deleted_task");  //$NON-NLS-1$
    String[] qst = {Messages.getString("Controleur.yes"), Messages.getString("Controleur.no")}; //$NON-NLS-1$ //$NON-NLS-2$
    ids = _db.getNotDeletedIds(ids);
    int count = 0;
    for (int id : ids) {
      Tache t = _db.get(id);
      if (t == null) continue;
      String question = (t.rid() != 0) ? Messages.getString("Controleur.question_del_rec") : Messages.getString("Controleur.question_del_task"); //$NON-NLS-1$ //$NON-NLS-2$
      /*
      StringBuilder b = new StringBuilder("Voulez-vous supprimer ");
      if (t.rid() != 0) b.append("toutes les tâches récurrentes de ");
      b.append("la tâche");
      _name(b, t);
      b.append('?');
      */
      if (_question(String.format(question, _taskName(t)), qst) != 0) continue;
      if (t.rid() == 0) {
        _printName(_db.pop(id), deleted);
        ++count;
      } else {
        Set<Tache> lT = _db.popAll(t.rid());
        for (Tache tS : lT) _printName(tS, deleted);
        count += lT.size();
      }
    }
    _printCount(count, action);
    _db.addLackTasks(Date.today());
    _db.sort();
    _db.resequence();
  }

  private void modify(Set<Integer> ids, Map<Option, String> m) {
    int count = 0;
    String[] qst = {Messages.getString("Controleur.yes"), Messages.getString("Controleur.no")}; //$NON-NLS-1$ //$NON-NLS-2$
    String action = Messages.getString("Controleur.count_modified"); //$NON-NLS-1$
    String modified = Messages.getString("Controleur.modified_task");  //$NON-NLS-1$
    ids = _db.getNotDeletedIds(ids);
    Set<Integer> traites = new HashSet<Integer>();
    for (int id: ids) {
      if (traites.contains(id)) continue;
      traites.add(id);
      Tache t = _db.get(id);
      if (t == null) continue;
      boolean all = false;
      if (t.rid() != 0) {
        String question = Messages.getString("Controleur.question_mod_rec"); //$NON-NLS-1$
        all = _question(String.format(question, _taskName(t)), qst) == 0;
      }
      if (all) {
        Set<Tache> l = _db.modifyAll(t.id(), m);
        for (Tache tM : l) {
          _printName(tM, modified);
          ids.add(tM.id());
          ++count;
        }
      } else {
        _printName(_db.modify(t.id(), m), modified);
        ++count;
      }
    }
    _printCount(count, action);
    _db.addLackTasks(Date.today());
    _db.sort();
    _db.resequence();
  }

  private void duplicate(Set<Integer> ids, Map<Option, String> m) {
    int count = 0;
    String action = Messages.getString("Controleur.count_duplicated"); //$NON-NLS-1$
    String dupl   = Messages.getString("Controleur.duplicated_task"); //$NON-NLS-1$
    ids = _db.getNotDeletedIds(ids);
    Set<Tache> l = new LinkedHashSet<Tache>();
    for (int id: ids) {
      Tache tO = _db.get(id);
      Tache tN = Tache.buildFrom(tO, m);
      if (!tO.recurrence().isNull() && !m.containsKey(Option.REC)) tN.recurrence(Recurrence.NONE);
      tN.id(tN.recurrence().isNull() ? _db.nextId() : _db.nextRid());
      if (_db.push(tN.repairRid())) {
        l.add(tO);
        ++count;
      }
    }
    for (Tache t: l) _printName(t, dupl);
    _printCount(count, action);
    _db.addLackTasks(Date.today());
    _db.sort();
    _db.resequence();
  }

  private static String _getShell(String invite) {
    System.out.print(invite);
    @SuppressWarnings("resource")
    Scanner sc = new Scanner(System.in);
    return sc.nextLine();
  }
  
  private static String _taskName(Tache t) {
  	return String.format("%d '%s'", t.id(), t.description()); //$NON-NLS-1$
  }

  private static StringBuilder _name(StringBuilder b, Tache t) {
    b.append(' ').append(t.id());
    b.append(" '").append(t.description()). append("' "); //$NON-NLS-1$ //$NON-NLS-2$
    return b;
  }

  private static void _printName(Tache t, String action) {
  	String msg = String.format(action, _taskName(t));
    System.out.println(Ansi.BOLD.format(msg));
  	/*
    StringBuilder b = new StringBuilder("Tâche");
    System.out.println(Ansi.BOLD.format(_name(b, t).append(action)));
    */
  }

  private static void _printCount(int i, String action) {
  	String msg = String.format(action, i);
  	System.out.println(Ansi.normal(Couleur.YELLOW).format(msg));
    /*
    StringBuilder b = new StringBuilder();
    if (i == 0)
      b.append(Messages.getString("Controleur.noone")); //$NON-NLS-1$
    else
      b.append(i);
    if (i > 1)
      b.append(" tâches ").append(action).append('s');
    else
      b.append(" tâche ").append(action);
    b.append(action);
    System.out.println(Ansi.normal(Couleur.YELLOW).format(b));
    */
  }

  private static int _question(String s, String[] rep) {
    @SuppressWarnings("resource")
    Scanner sc = new Scanner(System.in);
    StringBuilder b = new StringBuilder();
    for (String r : rep) {
      if (b.length() != 0) b.append('/');
      b.append(r);
    }
    b.insert(0, " (").append(") "); //$NON-NLS-1$ //$NON-NLS-2$
    while (true) {
      System.out.print(Ansi.BOLD.format(s) + Ansi.bold(Couleur.LYELLOW).format(b));
      String sRep = sc.nextLine().toLowerCase();
      for (int i = 0; i < rep.length; ++i)
        if (sRep.equals(rep[i].toLowerCase())) return i;
      System.out.println(Ansi.normal(Couleur.LRED).format(Messages.getString("Controleur.bad_answer"))); //$NON-NLS-1$
    }
  }

}
