package pack;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

/**
 * classe technique pour les données de l'affichage
 * @author BERON Jean-Sebastien
 *
 */
public class PasserelleAffichage {
	

	/**
	 * procedure qui permet d'afficher le powerpoint
	 */
	public static void affichagePPT(){
		
		//recherche de l'executable de powerpoint
		Runtime x = Runtime.getRuntime();
		String cheminPptExe = getCheminPptExe();
		cheminPptExe = cheminPptExe.replace("\\", "\\\\");
		//chemin du powerpoint
		String cheminPpt = "dataImport\\TVAFFPPT.ppt";
		//execution du diaporama
		try {
			x.exec("\""+cheminPptExe+"\" /s \""+cheminPpt+"\"");
		} catch (IOException e1) {
			//boite de dialogue d'erreur
			JOptionPane.showMessageDialog(null, "soit le chemin vers powerpnt.exe est incorrect ! soit le ppt n'est pas nommé TVAFFPPT.ppt !", "Erreur", JOptionPane.ERROR_MESSAGE);
		}
	}//fin affichagePPT()
	
	/**
	 * retourne le chemin absolu vers powerpnt.exe
	 * @return
	 */
	public static String getCheminPptExe(){
		String chemin = "";
		
		try {			
			//lecture du fichier
			FileReader fichier;
			fichier = new FileReader("dataSystem\\dataAff.txt");
			BufferedReader reader = new BufferedReader(fichier);
			String ligne;
			String chaine;
			while ((ligne = reader.readLine()) != null){
				chaine = "";
				for (int i = 0; i < ligne.length(); i++) {
					if(! ligne.substring(i, i+1).equalsIgnoreCase("/")){
						chaine = chaine + ligne.substring(i, i+1);
					}else{
						if(chaine.equalsIgnoreCase("cheminpptexe")){
							chemin = ligne.substring(i+1, ligne.length()-1);
						}
					}//finsi
				}//fin pour
			}//fin tantque
			reader.close();//fermeture du lecteur
			
		} catch (FileNotFoundException e) {
			//boite de dialogue d'erreur
			JOptionPane.showMessageDialog(null, "<html>Fichier non trouvé" +
					"<br/>dataSystem\\dataAff.txt</html>", "Erreur", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			//boite de dialogue d'erreur
			JOptionPane.showMessageDialog(null, "<html>probleme de lecture" +
					"<br/>dataSystem\\dataAff.txt/html>", "Erreur", JOptionPane.ERROR_MESSAGE);
		}
		
		//retour
		return chemin;
	}//fin getCheminPptExe()
	
	public static String getAppConfig(String param) {
		String val = "";
		Properties prop = new Properties();

		//ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = PasserelleAffichage.class.getResourceAsStream("/res/app.config");
		//InputStream input = classLoader.getResourceAsStream("/res/app.config");
		//InputStream input2 = PasserelleAffichage.class.getResourceAsStream("/res/app.config");
		//ResourceBundle.getBundle();
		try {
		   	prop.load(input);
		} catch (IOException e) {
			System.out.println("ERR");
		}
		val = prop.getProperty("aff."+param);

		return val;
	}
	/**
	 * retourne le message de l'entete
	 * @return
	 */
	public static String getHeaderMsg(){
		String headerMsg = "";
		
		try {
			//lecture du fichier
			FileReader fichier;
			fichier = new FileReader("dataSystem\\dataAff.txt");
			BufferedReader reader = new BufferedReader(fichier);
			String ligne;
			String chaine;
			while ((ligne = reader.readLine()) != null){
				chaine = "";
				for (int i = 0; i < ligne.length(); i++) {
					if(! ligne.substring(i, i+1).equalsIgnoreCase("/")){
						chaine = chaine + ligne.substring(i, i+1);
					}else{
						if(chaine.equalsIgnoreCase("header")){
							headerMsg = ligne.substring(i+1, ligne.length()-1);
						}//finsi
					}//finsi
				}//finpour
			}//fin tantque
			reader.close();
			
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "<html>Fichier non trouvé" +
					"<br>dataSystem\\dataAff.txt</html>", "Erreur", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "<html>probleme de lecture" +
					"<br>dataSystem\\dataAff.txt/html>", "Erreur", JOptionPane.ERROR_MESSAGE);
		}
		//retour
		return headerMsg;
	}//fin getHeaderMsg()
	
	/**
	 * retourne le texte du message défilant
	 * @return
	 */
	public static String getFooterMsg(){
		String footerMsg = "";
		
		try {
			FileReader fichier;
			fichier = new FileReader("dataSystem\\dataAff.txt");
			BufferedReader reader = new BufferedReader(fichier);
			String ligne;
			String chaine;
			while ((ligne = reader.readLine()) != null){
				chaine = "";
				for (int i = 0; i < ligne.length(); i++) {
					if(! ligne.substring(i, i+1).equalsIgnoreCase("/")){
						chaine = chaine + ligne.substring(i, i+1);
					}else{
						if(chaine.equalsIgnoreCase("footer")){
							footerMsg = ligne.substring(i+1, ligne.length()-1);
						}
					}
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "<html>Fichier non trouvé" +
					"<br/>dataSystem\\dataAff.txt</html>", "Erreur", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "<html>probleme de lecture" +
					"<br/>dataSystem\\dataAff.txt/html>", "Erreur", JOptionPane.ERROR_MESSAGE);
		}
		//retour
		return footerMsg;
	}//fin getFooterMsg()
	
	/**
	 * procedure de sauvegarde des parametres d'affichage
	 * @param header
	 * @param footer
	 * @param chemin
	 */
	public static void ecritureTextAff(String header,String footer, String chemin){
		
		//les parametres ne doivent pas etre nuls
		if(header.equalsIgnoreCase("")){
			header = " ";
		}
		if(footer.equalsIgnoreCase("")){
			footer = " ";
		}
		if(chemin.equalsIgnoreCase("")){
			chemin = " ";
		}
		//acces au fichier
		try {
			FileWriter fichier = new FileWriter("dataSystem\\dataAff.txt");
			PrintWriter printer = new PrintWriter(fichier);
			//ecriture
			printer.println("header/"+header+"/");
			printer.println("footer/"+footer+"/");
			printer.print("cheminpptexe/"+chemin+"/");
			printer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}//fin ecritureTextAff()

}//fin class
