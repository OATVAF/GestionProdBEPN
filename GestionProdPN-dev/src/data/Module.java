package data;

import java.io.Serializable;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.persistence.*;

import data.Formateur;
/**
 * classe contenatn toutes les informations sur un module
 * @author S.Goletto
 *
 */
@Entity
public class Module implements Serializable{
	
	private static final long serialVersionUID = 1L;
    private static DateFormat timeFormat = new SimpleDateFormat("H:mm");

    @Id @GeneratedValue
    private long id;

	//attributs
    public Stage stage; 
    public String nom;
	public Time debut;
	public Time fin;	
	public List<String> moyens;
	public Formateur leader;
	public Formateur aide;	

	public Module() {
        this.moyens = new ArrayList<String>();
	}
	/**
	 * constructeur
	 * @param Nom
	 * @param Hdebut
	 * @param Hfin
	 */
	public Module(Stage stg,String nom, String debut, String fin){
		//this.stage = new Stage();
		this.stage = stg;
		this.nom = nom;
		try {
			this.debut = new Time(timeFormat.parse(debut).getTime());
			this.fin   = new Time(timeFormat.parse(fin).getTime());
		} catch (ParseException e) {
			System.out.println("[ERR] conversion de time:" + debut + "/" + fin);
		}
        this.moyens = new ArrayList<String>();

		//this.debut = Time.valueOf(debut);
		//this.fin = Time.valueOf(fin);
		//this.setSalle("");
		//this.setNomLeader("");
		//this.setNomAide("");
	}
	
	public Stage  getStage()	{ return stage; }
	public String getNom()		{ return nom; }
	public Time   getDebut()	{ return debut; }
	public Time	  getFin()		{ return fin; }
	public String getMoyen()	{ 
		if (moyens != null && moyens.size() >0) { 
			return moyens.get(0);
		} else { 
			return "NILL";
		}}
	public Formateur getLeader(){ return leader; }
	public Formateur getAide()	{ return aide; }
	
	public void setMoyen (String m) {
	    DB.em.getTransaction().begin();
		this.moyens.add(m);
	    DB.em.getTransaction().commit();
		//System.out.println(String.format("[DB] [%s] \\ setMoyen  %s", this.stageId,this.toString()));
	}
	public void setLeader (Formateur f) {
	    DB.em.getTransaction().begin();
	    if (this.leader != null){
	    	System.out.println(String.format("[ERR] dual leader "+ toString()));
	    }
		this.leader = f;
	    DB.em.getTransaction().commit();
		//System.out.println(String.format("[DB] [%s] \\ setLeader %s", this.stageId,this.toString()));
	}
	public void setAide (Formateur f) {
	    DB.em.getTransaction().begin();
	    if (this.aide != null){
	    	System.out.println(String.format("[ERR] dual leader "+ toString()));
	    }
		this.aide = f;
	    DB.em.getTransaction().commit();
		//System.out.println(String.format("[DB] [%s] \\ setAide   %s", this.stageId,this.toString()));
	}

    @Override
    public String toString() {
    	String l="-",a="-";
		if (leader != null) {
			l = leader.toString();
		}
		if (aide != null) {
			a = aide.toString();
		}
		
		return String.format("%-20s %8s-%8s m:%-15s | l:%-10s | a:%-10s)" , nom, 
    				debut, fin, moyens, l, a);
    }
}
