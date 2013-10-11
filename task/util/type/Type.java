package task.util.type;

/**
 * Created with IntelliJ IDEA.
 * User: benjamin
 * Date: 06/10/13
 * Time: 15:32
 * To change this template use File | Settings | File Templates.
 */
public enum Type {

  STR('L'),
  INT('R'),
  PRI('C'),
  DAT('C'),
  HRE('C'),
  REC('C'),
  DEL('C'),
  NONE('L');

  private final char _align;

  private Type(char c) {
    _align = c;
  }

  public boolean center() {
    return _align == 'C';
  }

  public boolean left() {
    return _align == 'L';
  }

  public boolean right() {
    return _align == 'R';
  }

  public boolean isMatched(String s) {
    if (s == null) return false;
    switch (this) {
      case NONE: return s.isEmpty();
      case STR: return !s.isEmpty();
      case INT: return s.matches("\\d+");
      case PRI: return s.matches(Priorite.REGEX);
      case DAT: return Date.isMatched(s);
      case HRE: return Heure.isMatched(s);
      case REC: return Recurrence.isMatched(s);
      case DEL: return s.matches(Suppression.REGEX);
    }
    return false;
  }

}
