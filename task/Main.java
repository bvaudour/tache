package task;

import task.util.type.Parser;

/**
 * Created with IntelliJ IDEA.
 * User: benjamin
 * Date: 06/10/13
 * Time: 15:22
 */
public class Main {

  public static void main(String[] args) {
    boolean test = false;
    String[] sT = {};
    String s = test ? Parser.toString(sT) : Parser.toString(args);
    Controleur c = new Controleur();
    c.execute(s);
    c.close();
  }
}
