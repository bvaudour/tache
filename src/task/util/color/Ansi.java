package task.util.color;

import java.util.HashSet;
import java.util.Set;

/**
 * La classe <code>Ansi</code> permet le formatage de la sortie terminal.
 * <i>Nota : Seul un terminal 256 couleurs est supporté actuellement</i>
 * @author Benjamin VAUDOUR
 * @since 1.0
 * @see task.Champ
 */
public final class Ansi {

  /**
   * Utilisé à des fins de test. Active ou non le formatage de la sortie terminal
   */
  public static final boolean FORMAT = true;

  /**
   * Code de fin de formatage
   */
  public static final Ansi END        = new Ansi(Couleur.NONE);
  /**
   * Effet normal
   */
  public static final Ansi NORMAL     = new Ansi(Effet.NORMAL);
  /**
   * Code gras
   */
  public static final Ansi BOLD       = new Ansi(Effet.BOLD);
  /**
   * Code souligné
   */
  public static final Ansi UNDERLINE  = new Ansi(Effet.UNDERLINE);
  /**
   * Code italique
   */
  public static final Ansi ITALIC     = new Ansi(Effet.ITALIC);
  /**
   * Code clignotant
   */
  public static final Ansi BLINK      = new Ansi(Effet.BLINK);
  /**
   * Code couleurs inversées
   */
  public static final Ansi INVERTED   = new Ansi(Effet.INVERTED);
  /**
   * Code gras-souligné
   */
  public static final Ansi BUNDERLINE = new Ansi(Effet.BOLD, Effet.UNDERLINE);
  /**
   * Code gras-italique
   */
  public static final Ansi BITALIC    = new Ansi(Effet.BOLD, Effet.ITALIC);
  /**
   * Code gras-italique
   */
  public static final Ansi BBLINK     = new Ansi(Effet.BOLD, Effet.BLINK);

  private final Couleur    _f;
  private final Couleur    _b;
  private final Set<Effet> _e = new HashSet<Effet>();

  private Ansi(Couleur c1, Couleur c2, Set<Effet> l) {
    _f = c1;
    _b = c2;
    for (Effet e : l) _e.add(e);
  }

  /**
   * Constructeur complet
   * @param c1 Couleur de police
   * @param c2 Couleur d'arrière-plan
   * @param e1 Premier effet
   * @param e2 Deuxième effet
   */
  public Ansi(Couleur c1, Couleur c2, Effet e1, Effet e2) {
    _f = c1;
    _b = c2;
    _add(e1, e2);
  }

  /**
   * Constructeur 2 couleurs/1 effet
   * @param c1 Couleur de police
   * @param c2 Couleur d'arrière-plan
   * @param e Effet
   */
  public Ansi(Couleur c1, Couleur c2, Effet e) {
    this(c1, c2, e, Effet.NONE);
  }

  /**
   * Constructeur 2 couleurs
   * @param c1 Couleur de police
   * @param c2 Couleur d'arrière-plan
   */
  public Ansi(Couleur c1, Couleur c2) {
    this(c1, c2, Effet.NONE);
  }

  /**
   * Constructeur 1 couleur/2 effets
   * @param c Couleur de police
   * @param e1 Premier effet
   * @param e2 Deuxième effet
   */
  public Ansi(Couleur c, Effet e1, Effet e2) {
    this(c, Couleur.NONE, e1, e2);
  }

  /**
   * Constructeur 1 couleur/1 effet
   * @param c Couleur de police
   * @param e Effet
   */
  public Ansi(Couleur c, Effet e) {
    this(c, Couleur.NONE, e);
  }

  /**
   * Constructeur 1 couleur
   * @param c Couleur de police
   */
  public Ansi(Couleur c) {
    this(c, Couleur.NONE);
  }

  /**
   * Constructeur 2 effets
   * @param e1 Premier effet
   * @param e2 Deuxième effet
   */
  public Ansi(Effet e1, Effet e2) {
    this(Couleur.NONE, e1, e2);
  }

  /**
   * Constructeur 1 effet
   * @param e Effet
   */
  public Ansi(Effet e) {
    this(Couleur.NONE, e);
  }

  private void _add(Effet e) {
    if (e != Effet.NONE) _e.add(e);
  }

  private void _add(Effet e1, Effet e2) {
    _add(e1);
    _add(e2);
  }

  /**
   * Applique 2 couleurs à un effet
   * @param c1 Couleur de police
   * @param c2 Couleur d'arrière-plan
   * @return Nouveau code Ansi
   */
  public Ansi color(Couleur c1, Couleur c2) {
    return new Ansi(c1, c2, _e);
  }

  /**
   * Applique 1 couleur à un effet
   * @param c Couleur de police
   * @return Nouveau code Ansi
   */
  public Ansi font(Couleur c) {
    return color(c, _b);
  }

  /**
   * Applique 1 couleur à un effet
   * @param c Couleur d'arrière-plan
   * @return Nouveau code Ansi
   */
  public Ansi back(Couleur c) {
    return color(_f, c);
  }

  private static void _addStr(StringBuilder b, String s) {
    if (s.isEmpty()) return;
    if (b.length() != 0) b.append(";");
    b.append(s);
  }

  /**
   * Récupère la valeur du code Ansi
   * @return Code au format chaîne de caractères
   */
  @Override
  public String toString() {
    StringBuilder b = new StringBuilder();
    for (Effet e : _e) _addStr(b, e.value());
    _addStr(b, _f.font());
    _addStr(b, _b.back());
    return b.insert(0, "\033[").append("m").toString();
  }

  /**
   * Formate une chaîne de caractère avec le code ANSI
   * @param b Chaîne de caractère à formater
   * @return Chaîne formatée
   */
  public String format(StringBuilder b) {
    return FORMAT ? b.insert(0, this).append(END).toString() : b.toString();
  }

  /**
   * Formate une chaîne de caractère avec le code ANSI
   * @param s Chaîne de caractère à formater
   * @return Chaîne formatée
   */
  public String format(String s) {
    return format(new StringBuilder(s));
  }

  /**
   * Applique 2 couleurs à un effet normal
   * @param c1 Couleur de police
   * @param c2 Couleur d'arrière-plan
   * @return Code ANSI résultant
   */
  public static Ansi normal(Couleur c1, Couleur c2) {
    return END.color(c1, c2);
  }
  /**
   * Applique 1 couleur à un effet normal
   * @param c Couleur de police
   * @return Code ANSI résultant
   */
  public static Ansi normal(Couleur c) {
    return END.font(c);
  }
  /**
   * Applique 2 couleurs à un effet gras
   * @param c1 Couleur de police
   * @param c2 Couleur d'arrière-plan
   * @return Code ANSI résultant
   */
  public static Ansi bold(Couleur c1, Couleur c2) {
    return BOLD.color(c1, c2);
  }
  /**
   * Applique 1 couleur à un effet gras
   * @param c Couleur de police
   * @return Code ANSI résultant
   */
  public static Ansi bold(Couleur c) {
    return BOLD.font(c);
  }
  /**
   * Applique 2 couleurs à un effet souligné
   * @param c1 Couleur de police
   * @param c2 Couleur d'arrière-plan
   * @return Code ANSI résultant
   */
  public static Ansi underline(Couleur c1, Couleur c2) {
    return UNDERLINE.color(c1, c2);
  }
  /**
   * Applique 1 couleur à un effet souligné
   * @param c Couleur de police
   * @return Code ANSI résultant
   */
  public static Ansi underline(Couleur c) {
    return UNDERLINE.font(c);
  }
  /**
   * Applique 2 couleurs à un effet italique
   * @param c1 Couleur de police
   * @param c2 Couleur d'arrière-plan
   * @return Code ANSI résultant
   */
  public static Ansi italic(Couleur c1, Couleur c2) {
    return ITALIC.color(c1, c2);
  }
  /**
   * Applique 1 couleur à un effet italique
   * @param c Couleur de police
   * @return Code ANSI résultant
   */
  public static Ansi italic(Couleur c) {
    return ITALIC.font(c);
  }
  /**
   * Applique 2 couleurs à un effet clignotant
   * @param c1 Couleur de police
   * @param c2 Couleur d'arrière-plan
   * @return Code ANSI résultant
   */
  public static Ansi blink(Couleur c1, Couleur c2) {
    return BLINK.color(c1, c2);
  }
  /**
   * Applique 1 couleur à un effet clignotant
   * @param c Couleur de police
   * @return Code ANSI résultant
   */
  public static Ansi blink(Couleur c) {
    return BLINK.font(c);
  }
  /**
   * Applique 2 couleurs à un effet inversé
   * @param c1 Couleur de police
   * @param c2 Couleur d'arrière-plan
   * @return Code ANSI résultant
   */
  public static Ansi inverted(Couleur c1, Couleur c2) {
    return INVERTED.color(c1, c2);
  }
  /**
   * Applique 1 couleur à un effet inversé
   * @param c Couleur de police
   * @return Code ANSI résultant
   */
  public static Ansi inverted(Couleur c) {
    return INVERTED.font(c);
  }
  /**
   * Applique 2 couleurs à un effet gras-clignotant
   * @param c1 Couleur de police
   * @param c2 Couleur d'arrière-plan
   * @return Code ANSI résultant
   */
  public static Ansi bblink(Couleur c1, Couleur c2) {
    return BBLINK.color(c1, c2);
  }
  /**
   * Applique 1 couleur à un effet grase-clignotant
   * @param c Couleur de police
   * @return Code ANSI résultant
   */
  public static Ansi bblink(Couleur c) {
    return BBLINK.font(c);
  }
  /**
   * Applique 2 couleurs à un effet gras-souligné
   * @param c1 Couleur de police
   * @param c2 Couleur d'arrière-plan
   * @return Code ANSI résultant
   */
  public static Ansi bunderline(Couleur c1, Couleur c2) {
    return BUNDERLINE.color(c1, c2);
  }
  /**
   * Applique 1 couleur à un effet gras-souligné
   * @param c Couleur de police
   * @return Code ANSI résultant
   */
  public static Ansi bunderline(Couleur c) {
    return BUNDERLINE.font(c);
  }
  /**
   * Applique 2 couleurs à un effet gras-italique
   * @param c1 Couleur de police
   * @param c2 Couleur d'arrière-plan
   * @return Code ANSI résultant
   */
  public static Ansi bitalic(Couleur c1, Couleur c2) {
    return BITALIC.color(c1, c2);
  }
  /**
   * Applique 1 couleur à un effet gras-italique
   * @param c Couleur de police
   * @return Code ANSI résultant
   */
  public static Ansi bitalic(Couleur c) {
    return BITALIC.font(c);
  }

}
