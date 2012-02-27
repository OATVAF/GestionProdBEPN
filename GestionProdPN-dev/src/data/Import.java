package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JOptionPane;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import ui.Config;
//import data.*;

public class Import {
	
	static private final int ID = 0;
	static private final int NOM_ACT = 1;
	static private final int CLASSE = 2;
	static private final int ACTIVITE = 3;
	static private final int CODE_STG = 4;
	static private final int AVION_STG = 5;
	static private final int NOM_MOD= 22;

	static private final int DATEHEURE_DEB = 29;
	static private final int DATEHEURE_FIN = 30;
	static private final int IS_LEADER = 31;

	private static String deliaFile = null;
	private static int colNum = 0;
	private static String filterStg = null;
	private static boolean filterCan = true;
	private static String pncFile = null;
	private static String pntFile = null;

    private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
    private static DateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
    private static DateFormat timeFormat = new SimpleDateFormat("H:mm");

	public static void ImportDelia() {
		FileReader file;
		
		deliaFile = Config.get("imp.delia");
		colNum = Config.getI("imp.delia.cols");
		filterStg = Config.get("imp.delia.filter.stage");
		filterCan = Config.getB("imp.delia.filter.cancel");

		DB.em.getMetamodel();
		
		try {
			file = new FileReader(deliaFile);
			BufferedReader reader = new BufferedReader(file);
			String str;
			ArrayList<String> tmp = new ArrayList<String>();

			// Ligne 1
			reader.readLine();
			//lecture des chaque ligne jusqu'a la fin du fichier
			while ((str = reader.readLine()) != null) {
				tmp.clear();
				tmp.addAll(Arrays.asList(str.split("\t")));
				//tmp = Arrays.asList(str.split("\t"));
				while (tmp.size() < colNum) {
					System.out.println("BUG");
					str += "\n" + reader.readLine();
					tmp.clear();
					tmp.addAll(Arrays.asList(str.split("\t")));
				}
				//System.out.println(tmp.get(NOM) + " " + tmp.get(CLASSE));
				// codStg : 4
				// libelle : 22
				// ladate : 29 0-10
				// Hdebut : 29 11-16
				// Hfin : 30 11-16
				// Module(String codStg, String libelle, String ladate, String Hdebut, String Hfin){
				// new Module(infoLigne.get(4), infoLigne.get(22), infoLigne.get(29).substring(0, 10)
				// infoLigne.get(29).substring(11,16), infoLigne.get(30).substring(11,16));
				try {

					String code = tmp.get(CODE_STG);
					String type = code.split(" ")[0];
					code = code.replace(" ", "").trim();
					Date date = new Date(dateFormat.parse(tmp.get(DATEHEURE_DEB).substring(0,10)).getTime());
					// Filter 
					if (filterCan && code.contains("Annul")) {
						System.out.println("[INFO] filter can "+code);
						continue;
					}
					if (filterStg.contains(code)) {
						System.out.println("[INFO] filter out "+code);
						continue;
					}
					Stage s = DB.modelStages.getStage(code, date);
					if (s == null) {
						s = DB.modelStages.addStage(code, type, tmp.get(AVION_STG), date);
						//DB.stages.addStage(s);
					}			

					if ( tmp.get(ACTIVITE).equals("ActivitÈ")) {
						String nom = tmp.get(NOM_ACT);
						String classe = tmp.get(CLASSE);
						String nomMod = tmp.get(NOM_MOD);
						String moyen = "";
						boolean leader = false;
						Formateur form = null;
						String debut = tmp.get(DATEHEURE_DEB).substring(11,16);
						String fin   = tmp.get(DATEHEURE_FIN).substring(11,16);
						Time hd, hf;
							hd = new Time(timeFormat.parse(debut).getTime());
							hf = new Time(timeFormat.parse(fin).getTime());
	
						//System.out.println("[INFO] act "+nom+":"+classe);
	
						if(classe.equalsIgnoreCase("salle")) {
							moyen = "Salle " + nom;
						}
						if(classe.equalsIgnoreCase("moyen-bepn")) {
							moyen = nom;
						}
						if(classe.equalsIgnoreCase("instructeur")) {
							form = DB.getFormateur(nom);
							if (form == null) {
								form = DB.newFormateur(nom, "TBD");
							}
							leader = tmp.get(IS_LEADER).equalsIgnoreCase("oui");
						}
						if(classe.equalsIgnoreCase("intervenant")){
							form = DB.getFormateur(nom);
							if (form == null) {
								form = DB.newFormateur(nom, "TBD");
							}
						}
						Module m = s.getModule(nomMod, hd, hf);
						// Set 
						if (m == null) {
							m = new Module(s,nomMod, debut, fin );
						    s.addModule(m);							
						}
						if (moyen != "") {
							m.setMoyen(moyen);
						}
						if (form != null) {
							if(leader) {
								m.setLeader(form);
							}else{
								m.setAide(form);
							}
						}
						//DB.addModule(s, m);
						/*
						Stage s1 = DB.getStage(code);
						Module m1 = s.getModule(nomMod, hd, hf);
						System.out.println("[CHECK] s  " +s  );
						System.out.println("[CHECK] s1 " +s1 );
						System.out.println("[CHECK] m  " +m  );
						System.out.println("[CHECK] m1 " +m1 );	
						*/
					}
					// Check !
				} catch (ParseException e) {
					System.out.println("[ERR] conversion de time");
				}

			}			
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "<html>Fichier delia " + deliaFile +
					"<br>no trouvé</html>", "Erreur", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "<html>Probleme de lecture de" +
					"<br>"+deliaFile+"</html>", "Erreur", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static void ImportPNC() {
		
		pncFile = Config.get("imp.pnc");

		try {
			File fichier = new File(pncFile);
			Workbook workbook;
			workbook = Workbook.getWorkbook(fichier);
			Sheet sheet = workbook.getSheet(0);
			for (int i = 1; i < sheet.getRows()-1; i++) {
				Cell[] cell = sheet.getRow(i);
				int matricule = Integer.parseInt(cell[4].getContents());
				Stagiaire stg; // = DB.getStagiaire(matricule);
				//if (stg == null) {
					Date dateD = new Date(dateFormat.parse(cell[5].getContents()).getTime());
					Date dateF = new Date(dateFormat.parse(cell[6].getContents()).getTime());
					 stg = new Stagiaire(
							  matricule, cell[11].getContents().replace(" ", "").trim(),
							  dateD,dateF,
							  cell[2].getContents(), cell[3].getContents()
							, cell[0].getContents(), cell[12].getContents());
				//}
				//Stage s = DB.getStage(stg);
				Stage s = DB.getStage(stg.codeStage,dateD,dateF);
				if (s != null) {
					s.addStagiaire(stg);
					System.out.println("[INFO] "+s+" -> "+stg);
				}
				else {
					System.out.println("[INFO] pas de stage pour -> "+stg);
				}
			}
		} catch (BiffException e) {
			JOptionPane.showMessageDialog(null, "<html>probleme de lecture de" +
					"<br>"+pncFile+"</html>", "Erreur", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "<html>probleme de lecture de" +
					"<br>"+pncFile+"</html>", "Erreur", JOptionPane.ERROR_MESSAGE);
		} catch (ParseException e) {
			System.out.println("[ERR] importPNC conversion de Date");
		}

	}
}
