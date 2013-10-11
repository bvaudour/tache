package task.util.type;

/**
 * Created with IntelliJ IDEA.
 * User: benjamin
 * Date: 06/10/13
 * Time: 15:34
 * To change this template use File | Settings | File Templates.
 */
public enum Suppression {

  YES(true),
  NO(false);

  public static final String REGEX = "(X|x|0|1|-)";

  private final boolean _b;

  private Suppression(boolean b) {
    _b = b;
  }

  public String in() {
    return (_b) ? "1" : "0";
  }

  public String out() {
    return (_b) ? "X" : " ";
  }

  public static Suppression getInstance(boolean b) {
    return (b) ? YES : NO;
  }

  public static Suppression getInstance(char c) {
    c = Character.toUpperCase(c);
    return getInstance(c == '1' || c == 'X');
  }

  public static Suppression getInstance(String s) {
    return (s != null && s.length() == 1) ? getInstance(s.charAt(0)) : NO;
  }

  @Override
  public String toString() {
    return in();
  }

}
