package task;

import task.util.type.Parser;
import task.util.type.Type;

/**
 * Created with IntelliJ IDEA.
 * User: benjamin
 * Date: 06/10/13
 * Time: 18:50
 * To change this template use File | Settings | File Templates.
 */
public enum Champ {

  IDT("ID", 2, Type.INT),
  IDR("RID", 4, Type.INT),
  CAT("Catégorie", 15, Type.STR),
  PRI("Pri", 3, Type.PRI),
  DAT("Date", 10, Type.DAT),
  BEG("Début", 5, Type.HRE),
  END("Fin", 5, Type.HRE),
  REC("Réc", 3, Type.REC),
  TAC("Description", 30, Type.STR),
  DEL("Sup", 3, Type.DEL);

  public static final Champ[] IN  = { IDT, IDR, CAT, PRI, DAT, BEG, END, REC, TAC, DEL };
  public static final Champ[] OUT = { IDT, CAT, PRI, DAT, BEG, END, REC, TAC };

  private final String _name;
  private final int    _size;
  private final Type   _type;

  private Champ(String n, int s, Type t) {
    _name = n;
    _size = s;
    _type = t;
  }

  // Formatage du champ pour sortie console
  public StringBuilder out(StringBuilder b, String s) {
    if (b.length() != 0) b.append(" ");
    return b.append(Parser.align(s, _type, _size));
  }

  private StringBuilder _title(StringBuilder b) {
    return out(b, _name);
  }

  // Formatage du champ pour sortie fichier
  public StringBuilder in(StringBuilder b, String s) {
    if (b.length() != 0) b.append("\t");
    return b.append(s);
  }

  // Formatage en-tête
  public static StringBuilder title() {
    StringBuilder b = new StringBuilder();
    for (Champ c : OUT) c._title(b);
    return b;
  }

}
