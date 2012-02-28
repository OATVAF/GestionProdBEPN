package ui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JOptionPane;

public class Config {

	private static Properties prop = new Properties();
	private static final String configFile = "dataSystem/app.properties";
	
	public Config() {

	}
		
	public static void Read() {
		//InputStream input = PasserelleAffichage.class.getResourceAsStream(configFile);		
		try {
		   	prop.load(new FileInputStream(configFile));
		} catch (FileNotFoundException e1) {
			System.out.println("[ERR]Config File Not Found for reading");
			JOptionPane.showMessageDialog(null, "Fichier de configuration " + configFile +
					"\nnon trouvé pour lecture!", "Erreur", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			System.out.println("[ERR]Config Read Error");
			JOptionPane.showMessageDialog(null, "Erreur de lecture de du fichier de configuration !",
					"Erreur", JOptionPane.ERROR_MESSAGE);
		}
		//System.out.println(prop.toString());
	}
	
	public static void Write() {
		try {
			prop.store(new FileOutputStream(configFile), null);
		} catch (FileNotFoundException e) {
			System.out.println("[ERR]Config File Not Found for writing");
			JOptionPane.showMessageDialog(null, "Fichier de configuration " + configFile +
					"\nnon trouvé pour écriture!", "Erreur", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			System.out.println("[ERR]Config Write Error");
			JOptionPane.showMessageDialog(null, "Erreur d'écriture du fichier de configuration !",
					"Erreur", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static String get(String param) {
		return prop.getProperty(param); 
	}

	public static int getI(String param) {
		return Integer.parseInt(prop.getProperty(param));
	}
	public static boolean getB(String param) {
		return prop.getProperty(param).equalsIgnoreCase("true");
	}
	public static void set(String param, String val) {
		prop.setProperty(param,val);
	}
}
