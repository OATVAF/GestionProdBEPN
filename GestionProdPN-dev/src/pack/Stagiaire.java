package pack;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * classe contenant toutes les informations sur un stagiaire
 * @author BERON Jean-Sebastien
 *
 */
public class Stagiaire implements Serializable{
	
	private static final long serialVersionUID = -7997348317614955828L;
	
	//attirbuts
	private String matricule;
	private String codeStage;
	private String dateDebStage;
	private String dateFinStage;
	private String nom;
	private String prenom;
	private String spe;
	private String comment;
	
	private static SimpleDateFormat fmt1 = new SimpleDateFormat("dd/MM/yyyy");
	private static SimpleDateFormat fmt2 = new SimpleDateFormat("dd/MM/yy");

	/**
	 * constructeur
	 * @param unMat
	 * @param unCodeStage
	 * @param dateDeb
	 * @param datefin
	 * @param unNom
	 * @param unprenom
	 * @param uneSpe
	 * @param unSecteur
	 */
	public Stagiaire(String unMat,String unCodeStage ,String dateDeb, String dateFin,
						String unNom,String unprenom,String uneSpe,String unSecteur){
		
		this.matricule = unMat;
		this.codeStage = unCodeStage;
		this.dateDebStage = fmt1.format(convDate(dateDeb));
		this.dateFinStage = fmt1.format(convDate(dateFin));
		this.nom = unNom;
		this.prenom = unprenom;
		this.spe = uneSpe;
		this.comment = unSecteur;
		
	}//fin Stagiaire()
		
	/**
	 * getter de matricule
	 * @return
	 */
	public String getMatricule() {
		return matricule;
	}
	
	/**
	 * getter de codestage
	 * @return
	 */
	public String getCodeStage() {
		return codeStage;
	}

	/**
	 * getter de dateDebStage
	 * @return
	 */
	public String getDateDebStage() {
		return dateDebStage;
	}
	
	/**
	 * getter de dataFinStage
	 * @return
	 */
	public String getDateFinStage() {
		return dateFinStage;
	}

	/**
	 * getter de nom
	 * @return
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * getter de prenom
	 * @return
	 */
	public String getPrenom() {
		return prenom;
	}
	
	/**
	 * getter de spé
	 * @return
	 */
	public String getSpe() {
		return spe;
	}

	/**
	 * getter de comment
	 * @return
	 */
	public String getComment() {
		return comment;
	}
	
	/**
	 * fonction renvoyant des info sur le stagiaire en fonction d'un entier.
	 * fonction utilisée pour la creation des pdfs
	 * @param index
	 * @return
	 */
	public String getInfo(int index){
		String info = " ";
		//traitement du retour en fonction de l'index
		switch (index) {
		case 0:
			info = nom;
			break;
		case 1:
			info = prenom;
			break;
		case 2:
			info = matricule;
			break;
		case 3:
			info = spe;
			break;
		case 4:
			info = comment;
			// SG if (info.length()>12) {
			// SG	info = info.substring(0,12);
			// SG }
			break;
		default:
			break;
		}
		//retour
		return info;
		
	}//fin getInfo()
	
	private Date convDate(String D) {
		Date r = null;
		try {
			if (D.length() > 9) {
				r = fmt1.parse(D);
			}
			else {
				r = fmt2.parse(D);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return r;
	}

	/**
	 * fonction retournant la date de debut de stage en format Date
	 * @return
	 */
	public Date getDateDeb(){
		return convDate(dateDebStage);
	}//fin getDateDeb()
	
	/**
	 * fonction retournant la date de fin de stage en format Date
	 * @return
	 */
	public Date getDateFin(){
		return convDate(dateFinStage);
		
	}//fin getDateFin()

	public void setNom(String aValue) {
		nom = aValue;
	}

	public void setPrenom(String aValue) {
		prenom = aValue;
	}

	public void setMatricule(String aValue) {
		matricule = aValue;
	}
	
	public void setSpe(String aValue) {
		spe = aValue;
	}

	public void setSecteur(String aValue) {
		comment = aValue;
	}

}//fin class
