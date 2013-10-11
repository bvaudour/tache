package task;

import task.util.action.Action;
import task.util.action.Option;
import task.util.color.Ansi;
import task.util.color.Couleur;
import task.util.type.Date;
import task.util.type.Heure;
import task.util.type.Recurrence;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: benjamin
 * Date: 07/10/13
 * Time: 11:52
 * To change this template use File | Settings | File Templates.
 */
public class Controleur {

  public static final String DEFAULTFILE = ".tache.lst";

  private final String   _path;
  private final Database _db    = new Database();
  private       boolean  _shell = false;
  private       boolean  _open  = false;

  private static String defaultPath() {
    StringBuilder b = new StringBuilder(System.getProperty("user.home"));
    b.append(File.separator).append(DEFAULTFILE);
    return b.toString();
  }

  public Controleur(String path) {
    _path = path;
  }

  public Controleur() {
    this(defaultPath());
  }

  public boolean open() {
    File f = new File(_path);
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
    }
    if (!_open && !open()) {
      System.out.println(Ansi.bold(Couleur.LRED).format("Impossible d'ouvrir le fichier !"));
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
    }
  }

  public void help() {
    Help.print(_shell);
  }

  public void shell() {
    if (_shell) {
      error();
      return;
    }
    forceShell();
  }

  public void viewAll() {
    view(_db.getIds());
  }

  public void viewPartial() {
    view(_db.getIds(Date.today().add(Recurrence.getInstance(1, Recurrence.Unit.MONTH))));
  }

  public void forceShell() {
    _shell = true;
    while (_shell) execute(_getShell("> "));
  }

  public void exitShell() {
    _shell = false;
  }

  private void error() {
    System.out.println(Ansi.bold(Couleur.LRED).format("Syntaxe incorrecte !\n"));
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
      System.out.println(Ansi.normal(Couleur.YELLOW).format("Aucune tâche"));
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
      Ansi a;
      if (t.isExpired(d0, h0)) {
        a = Ansi.blink(Couleur.RED, back);
      } else if (t.date().eq(d0)) {
        a = Ansi.bold(Couleur.LRED, back);
      } else if (t.date().le(dow)) {
        a = Ansi.bold(Couleur.LYELLOW, back);
      } else if (t.date().le(d7)) {
        a = Ansi.bold(Couleur.LGREEN, back);
      } else if (t.date().le(dom)) {
        a = Ansi.normal(Couleur.LGREEN, back);
      } else if (!t.date().isNull()) {
        a = Ansi.normal(Couleur.NONE, back);
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
    _printCount(ids.size(), "affichée");
  }

  private void add(Map<Option, String> m) {
    Set<Tache> l = _db.add(m);
    String action = "ajoutée";
    if (l.isEmpty()) {
      _printCount(0, action);
      return;
    }
    _db.sort();
    _db.resequence();
    for (Tache t: l) _printName(t, action);
    _printCount(l.size(), action);
  }

  private void close(Set<Integer> ids) {
    Set<Tache> l = _db.close(ids);
    String action = "clôturée";
    if (l.isEmpty()) {
      _printCount(0, action);
      return;
    }
    for (Tache t: l) _printName(t, action);
    _printCount(l.size(), action);
    _db.addLackTasks(Date.today());
    _db.sort();
    _db.resequence();
  }

  private void delete(Set<Integer> ids) {
    String action = "supprimée";
    String[] qst = {"o", "n"};
    ids = _db.getNotDeletedIds(ids);
    int count = 0;
    for (int id : ids) {
      Tache t = _db.get(id);
      if (t == null) continue;
      StringBuilder b = new StringBuilder("Voulez-vous supprimer ");
      if (t.rid() != 0) b.append("toutes les tâches récurrentes de ");
      b.append("la tâche");
      _name(b, t);
      b.append('?');
      if (_question(b.toString(), qst) != 0) continue;
      if (t.rid() == 0) {
        _printName(_db.pop(id), action);
        ++count;
      } else {
        Set<Tache> lT = _db.popAll(t.rid());
        for (Tache tS : lT) _printName(tS, action);
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
    String[] qst = {"o", "n"};
    String action = "modifiée";
    ids = _db.getNotDeletedIds(ids);
    Set<Integer> traites = new HashSet<Integer>();
    for (int id: ids) {
      if (traites.contains(id)) continue;
      traites.add(id);
      Tache t = _db.get(id);
      if (t == null) continue;
      boolean all = false;
      if (t.rid() != 0) {
        StringBuilder b = new StringBuilder("Voulez-vous modifier toutes les tâches récurrentes de la tâche");
        _name(b, t).append('?');
        all = _question(b.toString(), qst) == 0;
      }
      if (all) {
        Set<Tache> l = _db.modifyAll(t.id(), m);
        for (Tache tM : l) {
          _printName(tM, action);
          ids.add(tM.id());
          ++count;
        }
      } else {
        _printName(_db.modify(t.id(), m), action);
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
    String action = "dupliquée";
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
    for (Tache t: l) _printName(t, action);
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

  private static StringBuilder _name(StringBuilder b, Tache t) {
    b.append(' ').append(t.id());
    b.append(" '").append(t.description()). append("' ");
    return b;
  }

  private static void _printName(Tache t, String action) {
    StringBuilder b = new StringBuilder("Tâche");
    System.out.println(Ansi.BOLD.format(_name(b, t).append(action)));
  }

  private static void _printCount(int i, String action) {
    StringBuilder b = new StringBuilder();
    if (i == 0)
      b.append("Aucune");
    else
      b.append(i);
    if (i > 1)
      b.append(" tâches ").append(action).append('s');
    else
      b.append(" tâche ").append(action);
    System.out.println(Ansi.normal(Couleur.YELLOW).format(b));
  }

  private static int _question(String s, String[] rep) {
    @SuppressWarnings("resource")
    Scanner sc = new Scanner(System.in);
    StringBuilder b = new StringBuilder();
    for (String r : rep) {
      if (b.length() != 0) b.append('/');
      b.append(r);
    }
    b.insert(0, " (").append(") ");
    while (true) {
      System.out.print(Ansi.BOLD.format(s) + Ansi.bold(Couleur.LYELLOW).format(b));
      String sRep = sc.nextLine().toLowerCase();
      for (int i = 0; i < rep.length; ++i)
        if (sRep.equals(rep[i].toLowerCase())) return i;
      System.out.println(Ansi.normal(Couleur.LRED).format("Réponse invalide !"));
    }
  }

}
