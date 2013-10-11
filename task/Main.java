package task;

import task.util.type.Parser;

/**
 * Created with IntelliJ IDEA.
 * User: benjamin
 * Date: 06/10/13
 * Time: 15:22
 * To change this template use File | Settings | File Templates.
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
