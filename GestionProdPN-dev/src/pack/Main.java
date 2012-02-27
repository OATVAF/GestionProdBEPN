package pack;

import java.awt.EventQueue;
import java.util.List;

import javax.persistence.*;

import ui.Config;
import ui.MainWin;
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
		/**
		 * Launch the application.
		 */
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWin window = new MainWin();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}	
		});

		//construction de la fenetre principale
		//new FenetrePrincipale();		
	}//fin main()

}//fin class