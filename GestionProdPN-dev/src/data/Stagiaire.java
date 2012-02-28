package data;

import java.io.Serializable;
import java.sql.Date;
import java.util.Calendar;

import javax.jdo.annotations.PrimaryKey;
import javax.persistence.*;

/**
 * classe contenant toutes les informations sur un stagiaire
 * @author BERON Jean-Sebastien
 *
 */
@Entity
public class Stagiaire implements Serializable{
	
	private static final long serialVersionUID = -7997348317614955828L;
 
    @Id @GeneratedValue
	private int id;
   
 
	//attirbuts
    public int matricule;
    public String codeStage;
    public Stage stage;
	public Date  dateDebStage;
	public Date  dateFinStage;
	public String nom;
	public String prenom;
	public String spe;
	public String comment;
	
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
	public Stagiaire(int unMat, String unCodeStage,
						Date dateDeb, Date dateFin,
						String unNom, String unprenom,
						String uneSpe, String unSecteur){
		
		this.matricule = unMat;
		this.codeStage = unCodeStage;
		this.dateDebStage = dateDeb;
		this.dateFinStage = dateFin;
		this.nom = unNom;
		this.prenom = unprenom;
		this.spe = uneSpe;
		this.comment = unSecteur;
		
	}//fin Stagiaire()
	
	/**
	 * getter de matricule
	 * @return
	 */
	public int getMatricule() {
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
	public Date getDateDebStage() {
		return dateDebStage;
	}
	
	/**
	 * getter de dataFinStage
	 * @return
	 */
	public Date getDateFinStage() {
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
			info = ""+matricule;
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
	
	/**
	 * fonction retournant la date de debut de stage en format Date
	 * @return
	 */
	public Date getDateDeb(){
		return dateDebStage;
	}//fin getDateDeb()
	
	/**
	 * fonction retournant la date de fin de stage en format Date
	 * @return
	 */
	public Date getDateFin(){
		return dateFinStage;
		
	}//fin getDateFin()
	
    @Override
    public String toString() {
        return String.format("[%8d]  %-10s/%-10s : %10s-%10s)", matricule, codeStage, nom, dateDebStage, dateFinStage);
    }

	public void setStage(Stage stage2) {
		stage = stage2;
	}


}//fin class
