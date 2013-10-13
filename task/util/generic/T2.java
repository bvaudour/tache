package task.util.generic;

/**
 * La classe <code>T2</code> définit une paire de valeurs.
 * Elle est utilisée en interne pour l'analyse de la commande saisie
 * @author Benjamin VAUDOUR
 * @since 1.0
 * @see task.util.action.Action
 */
public class T2<E1, E2> {

  /**
   * Première valeur
   */
  public E1 e1;
  /**
   * Deuxième valeur
   */
  public E2 e2;

  /**
   * Constructeur par défaut
   */
  public T2() {
    this(null, null);
  }

  /**
   * Constructeur par copie
   * @param t Paire à copier
   */
  public T2(T2<E1, E2> t) {
    this(t.e1, t.e2);
  }

  /**
   * Constructeur initialisé
   * @param e1 Première valeur
   * @param e2 Deuxième valeur
   */
  public T2(E1 e1, E2 e2) {
    set(e1, e2);
  }

  /**
   * Modifie les valeurs de la paire
   * @param e1 Première valeur
   * @param e2 Deuxième valeur
   * @return Paire modifiée
   */
  public T2<E1, E2> set(E1 e1, E2 e2) {
    this.e1 = e1;
    this.e2 = e2;
    return this;
  }

  /**
   * Copie les valeurs d'une paire
   * @param t Paire à copier
   * @return Paire modifiée
   */
  public T2<E1, E2> set(T2<E1, E2> t) {
    return set(t.e1, t.e2);
  }

  /**
   * Modifie les valeurs de la paire
   * @param t Paire à modifier
   * @param e1 Première valeur
   * @param e2 Deuxième valeur
   * @return Paire modifiée
   */
  public static <E1, E2> T2<E1, E2> set(T2<E1, E2> t, E1 e1, E2 e2) {
    return t == null ? new T2<E1, E2>(e1, e2) : t.set(e1, e2);
  }

}
