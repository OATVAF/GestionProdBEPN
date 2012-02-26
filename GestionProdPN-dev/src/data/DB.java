package data;

import java.sql.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

public class DB {
	
	static public EntityManagerFactory emf;
	static public EntityManager em;
	
	public DB() {
		
	}
	
	static public void Open() {
		// Open a database connection
	    // (create a new database if it doesn't exist yet):
		try {
			emf = Persistence.createEntityManagerFactory("$objectdb/db/points.odb");
			em = emf.createEntityManager();
		} catch (PersistenceException e) {
			System.out.println("[ERR] Database not free ");
		}
		
	}
	
	
	/*
    em.getTransaction().begin();
    
    Formateur f1 = new Formateur("Motton","Sandrine");
    Formateur f2 = new Formateur("Boissy","JŽr™me");
    em.persist(f1);
    em.persist(f2);
    for (int i = 1; i < 5; i++) {
        Stage p = new Stage("SMG", "GENE"+i, "2012-02-0"+i);
        for (int j = 0; j< 4; j++) {
        	Module m = new Module("MOD"+j, "1"+j+":00:00", "1"+j+":45:00");
        	m.leader = f1;
        	m.aide = f2;
        	//em.persist(m);
        	p.modules.add(m);
        }
        em.persist(p);
    }
    
    em.getTransaction().commit();
	*/
	/*
    // Find the number of Point objects in the database:
    Query q1 = em.createQuery("SELECT COUNT(s) FROM Stage s");
    System.out.println("Total Points: " + q1.getSingleResult());

    // Find the average X value:
    Query q2 = em.createQuery("SELECT AVG(s.id) FROM Stage s");
    System.out.println("Average X: " + q2.getSingleResult());

    // Retrieve all the Point objects from the database:
    TypedQuery<Stage> query =
    		//em.createQuery("SELECT s FROM Stage s WHERE s.nom LIKE '%2'", Stage.class);
    		em.createQuery("SELECT s FROM Stage s WHERE s.date <= {d '2012-02-02'}", Stage.class);
    List<Stage> results = query.getResultList();
    for (Stage p : results) {
        System.out.println(p); 
    }
    
    TypedQuery<Formateur> query1 =
    		//em.createQuery("SELECT s FROM Stage s WHERE s.nom LIKE '%2'", Stage.class);
    		em.createQuery("SELECT f FROM Formateur f", Formateur.class);
    List<Formateur> results1 = query1.getResultList();
    for (Formateur p : results1) {
        System.out.println(p); 
    }
  	*/
	
	public static void Close() {
		em.close();
		emf.close();
	}
	
	public static void Purge() {
	    DB.em.getTransaction().begin();
		try {
			int c1 = em.createQuery("DELETE FROM Stage").executeUpdate();
			int c2 = em.createQuery("DELETE FROM Module").executeUpdate();
			int c3 = em.createQuery("DELETE FROM Stagiaire").executeUpdate();
	        System.out.println("[INFO] deleted "+c1+" stages and "+c2+" modules"); 
		}
		catch(PersistenceException e) {
		    	
		}
	    DB.em.getTransaction().commit();		
	}
	
	public static boolean hasStage(String code) {
		TypedQuery<Long> q = em.createQuery("SELECT COUNT(s) FROM Stage s WHERE s.code = :code", Long.class);
	    return (q.setParameter("code",code).getSingleResult() > 0);
	}
	
	public static boolean hasFormateur(String nom) {
		TypedQuery<Long> q = em.createQuery("SELECT COUNT(f) FROM Formateur f WHERE f.nom = :nom", Long.class);
	    return (q.setParameter("nom",nom).getSingleResult() > 0);
	}

	public static Stage addStage(Stage s) {
	    DB.em.getTransaction().begin();
	    DB.em.persist(s);
	    DB.em.getTransaction().commit();		
		System.out.println("[DB] new stage: " + s.toString());
	    return s;
	}
	
	public static Stage newStage(String code, String type, String avion, Date date) {
	    DB.em.getTransaction().begin();
	    Stage s = new Stage(code, type, avion, date);
	    DB.em.persist(s);
	    DB.em.getTransaction().commit();		
		System.out.println("[DB] new stage: " + s.toString());
	    return s;
	}
	
	public static Stage getStage(String code) {
		Stage res = null;
		try {
			TypedQuery<Stage> q = em.createQuery("SELECT s FROM Stage s WHERE s.code = :code", Stage.class);
			res = q.setParameter("code",code).getSingleResult();
		}
		catch (NoResultException e) {
			return null;
		}
		return res;
	}
	public static Stage getStage(String code, Date date) {
		Stage res = null;
		try {
			TypedQuery<Stage> q = em.createQuery("SELECT s FROM Stage s WHERE s.code = :code AND s.date = :date", Stage.class);
			q.setParameter("code",code);
			q.setParameter("date",date);
			res = q.getSingleResult();
		}
		catch (NoResultException e) {
			return null;
		}
		return res;
	}
	//public static Stage getStage(Stagiaire stg) {
	public static Stage getStage(String code, Date dateD, Date dateF) {
		Stage res = null;
		try {
			System.out.println("[INFO] Looking for stage with "+code + " " +dateD + "<"+dateF);
			TypedQuery<Stage> q = em.createQuery("SELECT s FROM Stage s " +
					"WHERE s.code = :code AND s.date >= :dateD AND s.date <= :dateF", Stage.class);
					//"WHERE s.code = :code AND s.date >= {d '2012-02-21'} AND s.date <= {d '2012-02-23'}", Stage.class);
			q.setParameter("code",  code);
			q.setParameter("dateD", dateD);
			q.setParameter("dateF", dateF);
			res = q.getSingleResult();
		}
		catch (NoResultException e) {
			return null;
		}
		catch (NonUniqueResultException e) {
			System.out.println("[ERR] Stage not unique for stg! ");
		}
		return res;
	}

	public static Formateur newFormateur(String nom, String prenom) {
	    DB.em.getTransaction().begin();
	    Formateur f = new Formateur(nom, prenom);
	    DB.em.persist(f);
	    DB.em.getTransaction().commit();
	    return f;
	}

	public static Formateur getFormateur(String nom) {
		Formateur res = null;
		try {
			TypedQuery<Formateur> q = em.createQuery("SELECT f FROM Formateur f WHERE f.nom = :nom", Formateur.class);
			res = q.setParameter("nom",nom).getSingleResult();
		}
		catch (NoResultException e) {
			return null;
		}
		return res;
	}
	public static Stagiaire getStagiaire(long mat) {
		Stagiaire res = null;
		try {
			TypedQuery<Stagiaire> q = em.createQuery("SELECT s FROM Stagiaire s WHERE s.matricule = :mat", Stagiaire.class);
			res = q.setParameter("mat",mat).getSingleResult();
		}
		catch (NoResultException e) {
			return null;
		}
		return res;
	}
	/*
	SELECT s.code, s.date, m.nom, m.debut, m.stage
	FROM Module m
	JOIN m.stage s
	WHERE m.debut > :t
	AND m.nom = "FF"
	ORDER BY m.debut
	*/
}
