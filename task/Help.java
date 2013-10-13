package task;

import task.util.action.ActionType;
import task.util.action.Option;
import task.util.color.*;

/**
 * La classe <code>Help</code> regroupe l'ensemble de méthodes permettant d'afficher l'aide
 * @author Benjamin VAUDOUR
 * @since 1.0
 */
public class Help {

  /**
   * Affiche l'aide
   * @param shell si true, affiche l'aide en mode interactif, sinon, affiche l'aide en mode non interactif
   */
  public static void print(boolean shell) {
    printUsage();
    printActions(shell);
    printOptions();
  }

  private static void printUsage() {
    System.out.println(usage());
    System.out.println();
  }

  private static void printActions(boolean shell) {
    StringBuilder b = new StringBuilder();
    ActionType[] act = shell ? ActionType.CSHELL : ActionType.CNORM;
    for (ActionType a : act) {
      if (b.length() > 0) b.append(", ");
      b.append(a.code());
    }
    b.insert(0, "Actions disponibles : ");
    System.out.println(headerSection(b));

    for (ActionType a : act) {
      b = new StringBuilder(a.code());
      switch (a.mustId()) {
        case 1: b.append(" {<id>}")    ; break;
        case 0: b.append(" [{id}|all]"); break;
      }
      switch (a.mustOption()) {
        case 1: b.append(" {<options> <arg>}")  ; break;
        case 0: b.append(" [{<options> <arg>}]"); break;
      }
      System.out.println(code(b));
      System.out.println(description(new StringBuilder(a.help())));
    }
    System.out.println();
  }

  private static void printOptions() {
    StringBuilder b = new StringBuilder();
    for (Option o : Option.CODES) {
      if (b.length() > 0) b.append(", ");
      b.append(o.code());
    }
    b.insert(0, "Options disponibles : ");
    System.out.println(headerSection(b));

    for (Option o : Option.CODES) {
      b = new StringBuilder(o.code());
      switch (o.type()) {
        case STR: b.append(" string")                          ; break;
        case DAT: b.append(" (yyyymmdd|yyyy.mm.dd|dd.mm.yyyy)"); break;
        case HRE: b.append(" (hhmm|hh.mm)")                    ; break;
        case REC: b.append(" \\d+(y|m|w|d|h|')")               ; break;
        case PRI: b.append(" (H|M|L|-)")                       ; break;
      }
      System.out.println(code(b));
      System.out.println(description(new StringBuilder(o.help())));
    }
    System.out.println();
  }

  private static String usage() {
    StringBuilder b = new StringBuilder();
    b.append(Ansi.BUNDERLINE.format("Usage :"));
    b.append(Ansi.BOLD.format(" tache <action> [{<id>}] [{<option> <arg>}]"));
    return b.toString();
  }

  private static String headerSection(StringBuilder b) {
    return Ansi.bold(Couleur.LCYAN).format(b);
  }

  private static String code(StringBuilder b) {
    b.insert(0, "\t");
    return Ansi.bold(Couleur.LYELLOW).format(b);
  }

  private static String description(StringBuilder b) {
    b.insert(0, "\t\t");
    return b.toString();
  }

}
