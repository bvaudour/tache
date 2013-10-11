package task.util.color;

/**
 * Created with IntelliJ IDEA.
 * User: benjamin
 * Date: 22/09/13
 * Time: 17:48
 * To change this template use File | Settings | File Templates.
 */
public enum Effet {
  NONE(-1),
  NORMAL(0),
  BOLD(1),
  ITALIC(3),
  UNDERLINE(4),
  BLINK(5),
  INVERTED(7);

  private final int _i;

  private Effet(int i) {
    _i = i;
  }

  public String value() {
    return (this == NONE) ? "" : String.valueOf(_i);
  }
}
