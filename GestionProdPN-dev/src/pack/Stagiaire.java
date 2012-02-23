package pack;
import java.io.Serializable;
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
	public Stagiaire(String unMat,String unCodeStage ,String dateDeb,String datefin,
						String unNom,String unprenom,String uneSpe,String unSecteur){
		
		this.matricule = unMat;
		this.codeStage = unCodeStage;
		this.dateDebStage = dateDeb;
		this.dateFinStage = datefin;
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
			if (info.length()>12) {
				info = info.substring(0,12);
			}
			break;
		default:
			break;
		}
		//retour
		return info;
		
	}//fin getInfo()
	
	/**
	 * fonction retournant la date de debut de stage en format Date
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public Date getDateDeb(){
		//recuperation du jour, du mois, de l'année
		int year = Integer.parseInt(dateDebStage.substring(6, 10))-1900;
		int mois = Integer.parseInt(dateDebStage.substring(3, 5))-1;
		int day = Integer.parseInt(dateDebStage.substring(0, 2));
		//creation de la date
		Date date = new Date(year,mois,day);
		//retour
		return date;
	}//fin getDateDeb()
	
	/**
	 * fonction retournant la date de fin de stage en format Date
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public Date getDateFin(){
		//recuperation du jour, du mois, de l'année
		int year = Integer.parseInt(dateFinStage.substring(6, 10))-1900;
		int mois = Integer.parseInt(dateFinStage.substring(3, 5))-1;
		int day = Integer.parseInt(dateFinStage.substring(0, 2));
		//creation de la date
		Date date = new Date(year,mois,day);
		//retour
		return date;
		
	}//fin getDateFin()

}//fin class
