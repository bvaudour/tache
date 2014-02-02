package task.util.generic;

/**
 * La classe <code>T3</code> définit un triplé de valeurs.
 * Elle est utilisée en interne pour la définition d'une date
 * @author Benjamin VAUDOUR
 * @since 1.0
 * @see task.util.type.Date
 */
public class T3<E1, E2, E3> {

  /**
   * Première valeur
   */
  public E1 e1;
  /**
   * Deuxième valeur
   */
  public E2 e2;
  /**
   * Troisième valeur
   */
  public E3 e3;

  /**
   * Constructeur par défaut
   */
  public T3() {
    this(null, null, null);
  }

  /**
   * Constructeur par copie
   * @param t Triplé à copier
   */
  public T3(T3<E1, E2, E3> t) {
    this(t.e1, t.e2, t.e3);
  }

  /**
   * Constructeur initialisé
   * @param e1 Première valeur
   * @param e2 Deuxième valeur
   * @param e3 Troisième valeur
   */
  public T3(E1 e1, E2 e2, E3 e3) {
    set(e1, e2, e3);
  }

  /**
   * Modifie les valeurs du triplé
   * @param e1 Première valeur
   * @param e2 Deuxième valeur
   * @param e3 Troisième valeur
   * @return Triplé modifié
   */
  public T3<E1, E2, E3> set(E1 e1, E2 e2, E3 e3) {
    this.e1 = e1;
    this.e2 = e2;
    this.e3 = e3;
    return this;
  }

  /**
   * Copie les valeurs d'un triplé
   * @param t Triplé à copier
   * @return Triplé modifié
   */
  public T3<E1, E2, E3> set(T3<E1, E2, E3> t) {
    return set(t.e1, t.e2, t.e3);
  }

  /**
   * Modifie les valeurs de la triplé
   * @param t Triplé à modifier
   * @param e1 Première valeur
   * @param e3 Troisième valeur
   * @return Triplé modifié
   */
  public static <E1, E2, E3> T3<E1, E2, E3> set(T3<E1, E2, E3> t, E1 e1, E2 e2, E3 e3) {
    return t == null ? new T3<E1, E2, E3>(e1, e2, e3) : t.set(e1, e2, e3);
  }

}
