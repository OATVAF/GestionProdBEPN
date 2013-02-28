package pack;
import java.io.Serializable;
import java.util.Comparator;


/**
 * classe contenatn toutes les informations sur un module
 * @author BERON Jean-Sebastien
 *
 */
public class Module implements Serializable, Comparable<Module> {
	
	private static final long serialVersionUID = -1234129121873518254L;

	private Long id;

	//attributs
	private Stage stage;
	private String codeStage;
	private String date;
	private String libelle;
	private String heureDebut;
	private String heureFin;
	private String salle;
	private String nomLeader;
	private String nomAide;
	private String nomIntervenant;
	private String compagnie;

	/**
	 * constructeur
	 * @param codStg
	 * @param libelle
	 * @param ladate
	 * @param Hdebut
	 * @param Hfin
	 */
	public Module(Long sId,String codStg, String libelle, String ladate, String Hdebut, String Hfin){
		this.id = sId;
		this.stage = null;
		this.codeStage = codStg;
		this.date = ladate;
		this.libelle = libelle;
		this.heureDebut = Hdebut;
		this.heureFin = Hfin;
		this.setSalle("");
		this.setNomLeader("");
		this.setNomAide("");
		this.setNomIntervenant("");
	}
	
	public long getId() {
		return id;
	}
	public void setCodeStage(String codeStage) {
		this.codeStage = codeStage;
	}

	/**
	 * setter de stage
	 * @param stage
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	/**
	 * getter de stage
	 * @return stage
	 */
	public Stage getStage() {
		return stage;
	}
	
	/**
	 * getter de codeStage
	 * @return codeStage
	 */

	public String getCodeStage() {
		return codeStage;
	}

	/**
	 * getter de date
	 * @return date
	 */
	public String getDate() {
		return date;
	}
	
	/**
	 * getter de libelle
	 * @return libelle
	 */
	public String getLibelle() {
		return libelle;
	}

	/**
	 * setter de libelle
	 * @para; libelle
	 */
	public void setLibelle(String lib) {
		this.libelle = lib;
	}

	/**
	 * getter de heureDebut
	 * @return heuredebut
	 */
	public String getHeureDebut() {
		return heureDebut;
	}
	
	public void setHeureDebut(String heureDebut) {
		this.heureDebut=heureDebut;
	}

	/**
	 * getter de  heureFin
	 * @return heurefin
	 */
	public String getHeureFin() {
		return heureFin;
	}
	
	public void setHeureFin(String heureFin) {
		this.heureFin=heureFin;
	}

	/**
	 * setter de salle
	 * @param newsalle
	 */
	public void setSalle(String newsalle) {
		this.salle = newsalle;
	}

	/**
	 * getter de salle
	 * @return salle
	 */
	public String getSalle() {
		return salle;
	}

	/**
	 * setter de nomLeader
	 * @param nomLeader
	 */
	public void setNomLeader(String nomLeader) {
		this.nomLeader = nomLeader;
		if (this.stage != null) {
			this.stage.setLeader(nomLeader);
		}
	}
	
	/**
	 * getter de nomLeader
	 * @return nomLeader
	 */
	public String getNomLeader() {
		return nomLeader;
	}

	/**
	 * setter de nomAide
	 * @param nomAide
	 */
	public void setNomAide(String nomAide) {
		this.nomAide = nomAide;
	}

	/**
	 * getter de nomAide
	 * @return nomaide
	 */
	public String getNomAide() {
		return nomAide;
	}
	
	/**
	 * setter de nomIntervenant
	 * @param nomIntervenant
	 */
	public void setNomIntervenant(String nomIntervenant) {
		this.nomIntervenant = nomIntervenant;
	}

	/**
	 * getter de nomIntervenant
	 * @return nomIntervenant
	 */
	public String getNomIntervenant() {
		return nomIntervenant;
	}

	/**
	 * retourne le nombre de minutes de l'heure de debut
	 * @return nbmin
	 */
	public int getnbMin(){
		//recuperation de l'heure
		String heureDeb = getHeureDebut();
		int nbmin;
		nbmin = Integer.parseInt(heureDeb.substring(0, 2))*60 + Integer.parseInt(heureDeb.substring(3, 5));
		//retour
		return nbmin;
	}//fin getnbMin()

	public String getCompagnie() {
		return compagnie;
	}

	public void setCompagnie(String compagnie) {
		this.compagnie = compagnie;
	}

	public int compareTo(Module other) {
		return (this.getnbMin() - other.getnbMin());
	}

	public String toString() {
		return codeStage+"."+libelle+"."+heureDebut+"-"+heureFin;
	}
}//fin class

/**
 * Comparateur de Modules selon l'heure de d�but de stage
 */
class ModuleStartComparator implements Comparator<Module> {
    public int compare(Module mod1, Module mod2) {
        return mod1.getnbMin() - mod2.getnbMin();
    }
}