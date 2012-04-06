package data;

import jxl.Cell;

public class StgMail {

	public String Nom;
	public String Prenom;
	public String Matricule;
	public String Fct;
	public String Email;
	public String NumStage;
	public String Site;
	public int n;
	
	public StgMail(Cell[] c, int i) {
		Nom       = c[0].getContents();
		Prenom    = c[1].getContents();
		Matricule = c[2].getContents();
		Fct       = c[3].getContents();
		Email     = c[4].getContents();
		NumStage  = c[5].getContents();
		Site      = c[6].getContents();
		n = i;
	}
	public String toString() {
		return Nom + "\t" + Prenom + "\t" + Matricule + "\t" + Fct + "\t" + Email + "\t" + NumStage + "\t" + Site;
	}
}
