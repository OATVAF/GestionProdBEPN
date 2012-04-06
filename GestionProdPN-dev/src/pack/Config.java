package pack;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import javax.swing.JOptionPane;

public enum Config {
	
	INSTANCE;
	
	private Properties appProp = new Properties();
	private Properties cfgProp = new Properties();
	private static final String  appConfigFile = "dataSystem/app.properties";
	private static final String  cfgConfigFile = "dataSystem/cfg.properties";

	private Config() {
		System.out.println("[Config()] : Reading "+appConfigFile+" !");
		try {
			appProp.load(new FileInputStream(appConfigFile));
		} catch (FileNotFoundException e1) {
			System.out.println("[ERR]Config File " + cfgConfigFile + " Not Found for reading");
			JOptionPane.showMessageDialog(null, "Fichier de configuration " + appConfigFile +
					"\nnon trouvé pour lecture!", "Erreur", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			System.out.println("[ERR]Config Read Error" + cfgConfigFile);
			JOptionPane.showMessageDialog(null, "Erreur de lecture de du fichier de configuration !",
					"Erreur", JOptionPane.ERROR_MESSAGE);
		}
		//System.out.println(prop.toString());
		System.out.println("[Config()] : Reading "+cfgConfigFile+" !");
		try {
			cfgProp.load(new FileInputStream(cfgConfigFile));
		} catch (FileNotFoundException e1) {
			System.out.println("[ERR]Config File " + cfgConfigFile + " Not Found for reading");
			JOptionPane.showMessageDialog(null, "Fichier de configuration " + cfgConfigFile +
					"\nnon trouvé pour lecture!", "Erreur", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			System.out.println("[ERR]Config Read Error" + cfgConfigFile);
			JOptionPane.showMessageDialog(null, "Erreur de lecture de du fichier de configuration !",
					"Erreur", JOptionPane.ERROR_MESSAGE);
		}

	}
	
	public static void Write() {
		try {
			INSTANCE.cfgProp.store(new FileOutputStream(cfgConfigFile), null);
		} catch (FileNotFoundException e) {
			System.out.println("[ERR]Config File Not Found for writing");
			JOptionPane.showMessageDialog(null, "Fichier de configuration " + cfgConfigFile +
					"\nnon trouvé pour écriture!", "Erreur", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			System.out.println("[ERR]Config Write Error");
			JOptionPane.showMessageDialog(null, "Erreur d'écriture du fichier de configuration !",
					"Erreur", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static String get(String param) {
		if (INSTANCE.cfgProp.containsKey(param)) {
			return INSTANCE.cfgProp.getProperty(param);
		}
		else {
			return INSTANCE.appProp.getProperty(param); 
		}
	}
	public static int getI(String param) {
		if (INSTANCE.cfgProp.containsKey(param)) {
			return Integer.parseInt(INSTANCE.cfgProp.getProperty(param));
		}
		else {
			return Integer.parseInt(INSTANCE.appProp.getProperty(param)); 
		}
	}
	public static boolean getB(String param) {
		if (INSTANCE.cfgProp.containsKey(param)) {
			return INSTANCE.cfgProp.getProperty(param).equalsIgnoreCase("true");
		}
		else {
			return INSTANCE.appProp.getProperty(param).equalsIgnoreCase("true"); 
		}
	}
	public static void set(String param, String val) {
		INSTANCE.cfgProp.setProperty(param,val);
	}
	public static URL getRes(String param) {
		return Config.class.getResource("/res/"+param);
	}
}
