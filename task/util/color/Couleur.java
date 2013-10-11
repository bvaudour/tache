package task.util.color;

/**
 * Created with IntelliJ IDEA.
 * User: benjamin
 * Date: 22/09/13
 * Time: 17:22
 * To change this template use File | Settings | File Templates.
 */
public enum Couleur {

  NONE(-1),
  BLACK(232),
  RED(1),
  GREEN(2),
  YELLOW(3),
  BLUE(4),
  MAJENTA(5),
  CYAN(6),
  WHITE(7),
  LBLACK(8),
  LRED(9),
  LGREEN(10),
  LYELLOW(11),
  LBLUE(12),
  LMAJENTA(13),
  LCYAN(14),
  LWHITE(15);

  private final int	_i;

  private Couleur(int i) {
    _i = i;
  }

  public String font() {
    return (this == NONE) ? "" : "38;5;" + _i;
  }

  public String back() {
    return (this == NONE) ? "" : "48;5;" + _i;
  }

}
