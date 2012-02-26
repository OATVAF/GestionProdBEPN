package pack;

import java.util.List;

import javax.persistence.*;

import ui.Config;
import data.DB;
import data.Formateur;
import data.Import;
import data.Stage;
import data.Module;

/**
 * classe Main (debut du programme)
 * @author BERON Jean-Sébastien
 */

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//construction de la fenetre principale
		//new FenetrePrincipale();
		
		Config.Read();
		DB.Open();
		System.out.println(Config.get("app.version"));
		
		DB.Purge();
		Import.ImportDelia();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Import.ImportPNC();
		
		DB.Close();
		Config.Write();
		
	}//fin main()

}//fin class