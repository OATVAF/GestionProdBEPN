/**
 * 
 */
package data;

/**
 * @author sylvain_christine
 *
 */

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

/**
 * @author sylvain_christine
 *
 */
@Entity
public class Formateur implements Serializable {
    private static final long serialVersionUID = 1L;
 
    @Id @GeneratedValue
    private long id;
 
    public String nom;
    public String prenom;
    public List<String> qualif;
 
    public Formateur() {
    }
 
    public Formateur(String nom, String prenom) {
        this.nom = nom;
        this.prenom  = prenom;
    }
 
    public Long getId() {
        return id;
    }
 
    public String getNom() {
         return nom;
    }
 
    public String getPrenom() {
         return prenom;
    }
 
    @Override
    public String toString() {
        return String.format("%-10s", nom);
    }
}
