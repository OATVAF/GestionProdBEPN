/**
 * 
 */
package data;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import data.Module;
import data.Stagiaire;

/**
 * @author sylvain_christine
 *
 */
@Entity
public class Stage implements Serializable {
    private static final long serialVersionUID = 2L;

    @Id @GeneratedValue
    private long id;
 
    public String code;
    public String type;
    public String avion;
    @Temporal(TemporalType.DATE)
    public Date date;
    public int maxiPresent;
    public Formateur leader;
	public List<Module> modules;
	public List<Stagiaire> stagiaires;
 
    public Stage() {
    }
 
    public Stage(String code, String type, String avion, Date date) {
        this.code = code;
        this.type  = type;
        this.avion = avion;
        this.date = date;
        this.modules = new ArrayList<Module>();
        this.stagiaires = new ArrayList<Stagiaire>();
    }
 
    public Long getId() 	{ return id; }
 
    public String getCode() { return code; }
  
    public List<Module> getModules() { return modules; }
    
	public Module getModule(String nom) {
		//TypedQuery<Long> q = em.createQuery("SELECT COUNT(m) FROM Stage s JOIN modules m WHERE s.code = :code AND m.nom = :nom", Long.class);
	    //return (q.setParameter("code",s.code).setParameter("nom",nom).getSingleResult() > 0);
		for (Module m : this.modules) {
			if (m.nom.equals(nom)) {
				return m;
			}
		}
		return null;
	}
	public Module getModule(String nom, Time hd, Time hf) {
		try {
			String fq = "";
			TypedQuery<Module> q = DB.em.createQuery("SELECT m FROM Stage s JOIN s.modules m WHERE" +
				" s.code = :code" + 
				" AND m.nom = :nom" +
				" AND m.debut = :hd" +
				" AND m.fin = :hf ", Module.class);
			q.setParameter("nom", nom);
			q.setParameter("code", code);
			q.setParameter("hd", hd);
			q.setParameter("hf", hf);
			List<Module> ml = q.getResultList();
			if (ml.size() > 1) {
				System.out.println("[ERR] Module not unique! ");
			}
	    	return q.getSingleResult();
		}
		catch (NoResultException e) {
			return null;
		}
		catch (NonUniqueResultException e) {
			System.out.println("[ERR] Module not unique! ");
		}
		return null;
	}

	public void addModule(Module m) {
	    DB.em.getTransaction().begin();
	    DB.em.persist(m);
	    modules.add(m);
	    DB.em.getTransaction().commit();
		System.out.println("[DB] " + this.toString()+ " addModule  " + m.toString());
	}
	
    @Override
    public String toString() {
        return String.format("[%d]  %-10s/%-10s : %10s)", id, code, type, date);
    }

	public void addStagiaire(Stagiaire stg) {
	    DB.em.getTransaction().begin();
	    DB.em.persist(stg);
	    stagiaires.add(stg);
	    DB.em.getTransaction().commit();
		System.out.println("[DB] " + this.toString()+ " addStagiaire " + stg.toString());
	}

}
