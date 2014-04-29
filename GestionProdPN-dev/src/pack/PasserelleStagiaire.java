package pack;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.regex.PatternSyntaxException;

import javax.swing.*;

import data.StgMail;

/*
import jxl.*;
import jxl.read.biff.*;

import jxl.write.*;
import jxl.write.biff.RowsExceededException;
*/
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

/**
 * 
 * @author Jean-Sébastien BERON
 *
 */
public class PasserelleStagiaire {
	
	private static boolean good;
	private static final String pathFilePnc= Config.get("imp.pnc"); //"dataImport\\OATVPNC.xls";
	private static final String pathFilePnt= Config.get("imp.pnt"); //"dataImport\\OATVPNT.xls";
	private static final String pathFileSMS= Config.get("exp.sms"); // "dataExport\\ListSMS.xls"
	private static final String pathDirTests= Config.get("exp.tests.dir"); // "dataExport\\Tests du"
	
	private static SimpleDateFormat dfBO = new SimpleDateFormat("dd/MM/yyyy");
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static Date dateDemain;
	
	public static void creerListePourSms(){
		
		good = true;
		ArrayList<Stagiaire> StagiaireList = chargerTousStagiairesPNC();
		StagiaireList = FiltreBOContact(StagiaireList);
		ecritureListeSMSxls(StagiaireList);
		if (good) {
			JOptionPane.showMessageDialog(null, "<html>Operation terminée !" +
					"<br>le fichier est dans "+pathFileSMS+"</html>", "Termine", JOptionPane.INFORMATION_MESSAGE);
		}// finsi
		
	}//fin creerListePourSms()
	
	public static void creerListePourTests() {
		good = true;
		ArrayList<Stagiaire> StagiaireList = chargerTousStagiairesPNC();
		StagiaireList = FiltreBOContact(StagiaireList);
		ecritureListeTests(StagiaireList);
		//ecritureListeTests2();
		if (good) {
			JOptionPane.showMessageDialog(null, "<html>Operation terminée !" +
					"<br>les fichiers sont dans dataExport/Tests...</html>", "Termine", JOptionPane.INFORMATION_MESSAGE);
		}//finsi
	}

	public static void creerListePourInterview() {
		good = true;
		String pnL[] = { "pnc", "pnt" };
		String cfg, pnIn="", pnOut;
		boolean header = true;
		//String pPNCS_1 = Config.get("imp.pnc-1"); //"dataImport/PNC_S-1.xls";
		//String pPNTS_1 = Config.get("imp.pnt-1"); //"dataImport/PNT_S-1.xls";
		
		try {
			for (String pn : pnL) {
				cfg="exp.interview."+pn+".";
				pnIn = Config.get(cfg+"fileIn");
				int num = Config.getI(cfg+"num");
				ArrayList<StgMail> StgList = new ArrayList<StgMail>();
				String[] qListes = new String[num];
				PrintWriter[] P = new PrintWriter[num];
				boolean append = Config.getB(cfg+"append");
				
				File f = new File(pnIn);
				Workbook wb = WorkbookFactory.create(f);
				Sheet sh = wb.getSheetAt(0);
				Iterator<Row> rows = sh.rowIterator();
				StgMail h = new StgMail(rows.next(),0);
				for (int i=1; rows.hasNext(); i++) {
					Row row = rows.next();
					if (row.getLastCellNum() > 0)
						StgList.add(new StgMail(row,i-1));
				}
				//wb.close();
	
				for (int i=0; i< num; i++) {
					qListes[i]=(Config.get(cfg+(i+1)));
					pnOut= Config.get(cfg+"fileOut")+qListes[i]+".txt";
					P[i] = new PrintWriter(new FileWriter(pnOut, append));
					if (header) {
						P[i].println(h.toString());
						header = false;
					}
				}
				
				for (StgMail s : StgList) {
					P[s.n % num].println(s.toString());
				}
				for (int i=0; i< num; i++) {
					P[i].close();
				}
			}
		} catch (InvalidFormatException  e) {
			good = false;
			JOptionPane.showMessageDialog(null, "<html>probleme de lecture de" +
					"<br>"+pnIn+"</html>", "Erreur", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			good = false;
			JOptionPane.showMessageDialog(null, "<html>probleme de lecture de" +
					"<br>"+pnIn+"</html>", "Erreur", JOptionPane.ERROR_MESSAGE);
		}
		
		if (good) {
			JOptionPane.showMessageDialog(null, "<html>Operation terminée !" +
					"<br>les fichiers sont dans dataExport/Interview/...</html>", "Termine", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public static ArrayList<Stagiaire> chargerTousStagiairesPNC(){
		
		ArrayList<Stagiaire> StagiaireList = new ArrayList<Stagiaire>();
			try {
				File fichier = new File(pathFilePnc);
				Workbook workbook = WorkbookFactory.create(fichier);			
				Sheet sheet = workbook.getSheetAt(0);
				Iterator<Row> rows = sheet.rowIterator();
				rows.next();	// Skip header
				while (rows.hasNext()) {
					Row row = rows.next();
					if (row.getLastCellNum() > 0)
						StagiaireList.add(new Stagiaire(
							  row.getCell(4).getStringCellValue(), row.getCell(11).getStringCellValue()
							, dfBO.format(row.getCell(5).getDateCellValue()), dfBO.format(row.getCell(6).getDateCellValue())
							, row.getCell(2).getStringCellValue(), row.getCell(3).getStringCellValue()
							, row.getCell(0).getStringCellValue(), row.getCell(12).getStringCellValue()));
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("=> fin tableau Import PNC");
			} catch (InvalidFormatException e) {
				good = false;
				JOptionPane.showMessageDialog(null, "<html>probleme de lecture de" +
						"<br/>"+pathFilePnc+"</html>", "Erreur", JOptionPane.ERROR_MESSAGE);
			} catch (IOException e) {
				good = false;
				JOptionPane.showMessageDialog(null, "<html>probleme de lecture de" +
						"<br/>"+pathFilePnc+"</html>", "Erreur", JOptionPane.ERROR_MESSAGE);
			}

		return StagiaireList;
		
	}//fin chargerTousStagiairesPNC()
	
	public static ArrayList<Stagiaire> chargerTousStagiairesPNT(){
		
		ArrayList<Stagiaire> StagiaireList = new ArrayList<Stagiaire>();
			try {
				File fichier = new File(pathFilePnt);
				Workbook workbook = WorkbookFactory.create(fichier);			
				Sheet sheet = workbook.getSheetAt(0);
				Iterator<Row> rows = sheet.rowIterator();
				rows.next();	// Skip header
				while (rows.hasNext()) {
					Row row = rows.next();
					if (row.getLastCellNum() > 0)
						StagiaireList.add(new Stagiaire(
							  row.getCell(3).getStringCellValue(), row.getCell(9).getStringCellValue()
							, dfBO.format(row.getCell(4).getDateCellValue()), dfBO.format(row.getCell(5).getDateCellValue())
							, row.getCell(1).getStringCellValue(), row.getCell(2).getStringCellValue()
							, row.getCell(0).getStringCellValue(), row.getCell(12).getStringCellValue()));
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("=> fin tableau Import PNT");
			} catch (InvalidFormatException e) {
				good = false;
				JOptionPane.showMessageDialog(null, "<html>probleme de lecture de" +
						"<br/>"+pathFilePnt+"</html>", "Erreur", JOptionPane.ERROR_MESSAGE);
			} catch (IOException e) {
				good = false;
				JOptionPane.showMessageDialog(null, "<html>probleme de lecture de" +
						"<br/>"+pathFilePnt+"</html>", "Erreur", JOptionPane.ERROR_MESSAGE);
			}

		return StagiaireList;
		
	}//fin chargerTousStagiairesPNT()
	
	public static /*ArrayList<Stage>*/ void ajoutPnt(ArrayList<Stage> stageList,ArrayList<Stagiaire> pntList){
 		//ArrayList<Stage> newStageList = stageList;
		String site = Config.get("app.site");
		String s2pat = Config.get("imp.pnt.s2."+site);

		// Tri par Spe puis Nom pour répartition équitable CDB/OPL dans les stages S2
		if (Config.getB("imp.pnt.s2.spe_sort")) {
			Collections.sort(pntList, new StagiaireSpeNameComparator());
		}
		
		for (Stage stage : stageList) {
			if (stage.getCodeI().startsWith("S2")) {
				int n = 0;
				long modulo = 0;
				for (Stagiaire stagiaire : pntList) {
					if (stagiaire.getCodeStage().trim().matches(s2pat) 
							&& stagiaire.isInStageS2(stage)) {
						// modulo pour les S2
						modulo = (n % stage.getIdxMax()) +1 ;
						if (modulo == stage.getIdx()) {
							System.out.println("Ajout PNT "+n+ " " + stagiaire.getNom() + " au stage " +stage.getCode() + stagiaire.getCodeStage());
							stage.ajoutStagiaire(stagiaire);
						}
						n++;
					}
				}
			}
			if (stage.getCodeI().startsWith("QT")) {
				String qtPat = "(QTQTQT";
				String codeStage = stage.getCodeI();
				for (String s : codeStage.split(" ")) {
					if (s.length() < 6) {
						continue;
					}
					if (qtPat == null) {
						qtPat = "^("+s.substring(0,2) + "." + s.substring(3,6)+".";
					}
					else {
						qtPat += "|"+s.substring(0,2) + "." + s.substring(3,6)+".";
					}
				}
				qtPat += ") *";
				
				try {
					for (Stagiaire stagiaire : pntList) {
						if (stagiaire.getCodeStage().matches(qtPat)) {
							System.out.println("Ajout PNT :" + stagiaire.getNom() + ":"+stagiaire.getCodeStage()
									+ " au stage :" + ":" +stage.getCode());
							stage.ajoutStagiaire(stagiaire);
						}
					}
				}
				catch (PatternSyntaxException e) {
					JOptionPane.showMessageDialog(null, "<html>Problème de pattern pour le stage : " + codeStage +
							"<br>Vérifier les codes de QT dans Délia!</html>", "Erreur", JOptionPane.ERROR_MESSAGE);;
				}
				if (stage.getSizeStagiaireList() == 0) {
					JOptionPane.showMessageDialog(null, "<html>Aucun stagiaire pour le stage : " + codeStage +
							"<br>Vérifier les codes de QT dans Délia!</html>", "Erreur", JOptionPane.ERROR_MESSAGE);;

				}
			}
		}
		//return newStageList;
	}//
	
	public static /*ArrayList<Stage>*/ void ajoutPnc(ArrayList<Stage> stageList,ArrayList<Stagiaire> pncList) {
		//ArrayList<Stage> newStageList = stageList;
		String site = Config.get("app.site");
		String p123Pat = Config.get("imp.pm123.pat");
		String m123Pat = Config.get("imp.m123.pat."+site);
		String p123Date = "";
		
		// Tri par Spe puis Nom pour répartition équitable CCP/CC/HST dans les stages dédoublés
		if (Config.getB("imp.pnc.smg.spe_sort")) {
			Collections.sort(pncList, new StagiaireStageSpeNameComparator());
		}

		Stage stage = null;
		int n = 0;
		long modulo = 0;

Next:	for (Stagiaire stagiaire : pncList) {
			String strCodeStagiaire = stagiaire.getCodeStage().replace(" ", "").trim();
			// System.out.println("S:"+stagiaire.getNom()+"/"+strCodeStagiaire);
			// Find stage for code
			if (! stagiaire.isInStage(stage)) {
				for (Stage s : stageList) {
					String strCodeStage = s.getCodeI().replace(" ", "");
					if (strCodeStage.matches(p123Pat)) {
						p123Date=s.getDateStr();
					}
					if ( stagiaire.isInStage(s)) {
						stage = s;
						n = 0;
					}
				}
			}
			if (stagiaire.isInStage(stage)) {
				Stage stageI = stage;
				if (stage.getIdxMax() > 0) {
					modulo = (n % stage.getIdxMax()) +1 ;
					stageI = stage.getCoStageI((int)modulo);
				}
				stageI.ajoutStagiaire(stagiaire);
				System.out.println("Ajout PNC "+n+" "+stagiaire.getNom() +" sur "+stageI.getCode());
				n++;
			}
			else if (strCodeStagiaire.matches(m123Pat) 
					&& p123Date.equals(stagiaire.getDateDebStage())) {
				int nP = Integer.parseInt(""+strCodeStagiaire.charAt(1));
				String code = "M"+nP+" "+site.toUpperCase();
				System.out.println("+ "+code);
				Module mod =  new Module(new Long(nP), 
						code, "", stagiaire.getDateDebStage(),
						Config.get("imp.m123.m"+nP+".debut"),
						Config.get("imp.m123.m"+nP+".fin"));
				mod.setCompagnie(Config.get("imp.m123.comp"));
				mod.setSalle(Config.get("imp.m123.salle"));
				stage = new Stage(mod);
				stage.ajoutStagiaire(stagiaire);
				System.out.println("Ajout PNC M "+stagiaire.getNom() +" sur "+stage.getCode());
				stageList.add(stage);
				continue Next;
			}
		}
		/*
		for (Stage stage : newStageList) {
			String strCodeStage = stage.getCodeI().replace(" ", "");
			for (Stagiaire stagiaire : stagiairePNCList) {
				String strCodeStagiaire = stagiaire.getCodeStage().replace(" ", "").trim();
				if(strCodeStagiaire.startsWith(strCodeStage)
						&& ! stage.getDateDt().before(stagiaire.getDateDeb())  
						&& ! stage.getDateDt().after(stagiaire.getDateFin())){
					stage.ajoutStagiaire(stagiaire);
				}else{
					if(strCodeStage.startsWith(strCodeStagiaire)
							&& ! stage.getDateDt().before(stagiaire.getDateDeb())  
							&& ! stage.getDateDt().after(stagiaire.getDateFin())){
						stage.ajoutStagiaire(stagiaire);

					}
				}
			}
		}
		
		return newStageList;
		*/
		//return stageList;
	}

	
	
	@SuppressWarnings("deprecation")
	private static ArrayList<Stagiaire> FiltreBOContact(ArrayList<Stagiaire> StagiaireList){
		
		ArrayList<Stagiaire> newStagiaireList = StagiaireList;
		
		//ArrayList<String> stageList = new ArrayList<String>();
		ArrayList<Stagiaire> stagiaireGood = new ArrayList<Stagiaire>();
		String selDate = "";
		//FileReader fichier;
			try {
				/*
				String ligne;
				fichier = new FileReader("dataSystem\\StageSMSPNC.txt");
				BufferedReader reader = new BufferedReader(fichier);
				while ((ligne = reader.readLine()) != null){
					stageList.add(ligne);
				}
				*/
				
				//recuperation de la date de demain
				Calendar cl=new GregorianCalendar();
				Date dateactuelle = new Date();
				if(dateactuelle.getDay() == 5){
					cl.add(Calendar.DATE, 3);
				}else{
					cl.add(Calendar.DATE, 1);
				}

				dateDemain = new Date((cl.get(Calendar.YEAR)-1900), cl.get(Calendar.MONTH), cl.get(Calendar.DATE));		
				selDate = JOptionPane.showInputDialog("Séléction de la date: ", dateFormat.format(dateDemain));
				dateDemain = dateFormat.parse(selDate);				

				for (Stagiaire stagiaire : newStagiaireList) {
					if(dateDemain.before(stagiaire.getDateDeb()) == false && dateDemain.after(stagiaire.getDateFin()) == false){
						/*
						for (String string : stageList) {
							if(stagiaire.getCodeStage().startsWith(string)){
								stagiaireGood.add(stagiaire);
								break;
							}
						}
						*/
						if (stagiaire.getCodeStage().matches(Config.get("exp.sms.pattern"))) {
							stagiaireGood.add(stagiaire);
						}
					}
				}
			/*	
			} catch (FileNotFoundException e) {
				good = false;
				JOptionPane.showMessageDialog(null, "<html>fichier non trouvé" +
						"<br/>dataSystem\\StageSMSPNC.txt</html>", "Erreur", JOptionPane.ERROR_MESSAGE);
			} catch (IOException e) {
				good = false;
				JOptionPane.showMessageDialog(null, "<html>probleme de lecture de" +
						"<br/>dataSystem\\StageSMSPNC.txt</html>", "Erreur", JOptionPane.ERROR_MESSAGE);;
			*/
			} catch (ParseException e) {
				good = false;
				JOptionPane.showMessageDialog(null, "<html>probleme de format" +
						"<br/>de la date " + selDate + "</html>", "Erreur", JOptionPane.ERROR_MESSAGE);;
			}
			
		newStagiaireList.retainAll(stagiaireGood);
		
		return newStagiaireList;
		
	}//fin FiltreBOContact()
	
	
	private static void ecritureListeSMSxls(ArrayList<Stagiaire> StagiaireList){
		
			try {
				File file = new File(pathFileSMS);
				Workbook workbook = new HSSFWorkbook();
				Sheet sheet = workbook.createSheet("ListeMAT");
				for (int i = 0; i < StagiaireList.size(); i++) {
					Row row = sheet.createRow(i);
					row.createCell(0).setCellValue(StagiaireList.get(i).getMatricule());
					row.createCell(1).setCellValue(StagiaireList.get(i).getCodeStage());
					row.createCell(2).setCellValue(StagiaireList.get(i).getDateDebStage());
				}
				FileOutputStream fileOut = new FileOutputStream(file);
				workbook.write(fileOut);
				fileOut.close();
			} catch (IOException e) {
				good = false;
				JOptionPane.showMessageDialog(null, "<html>probleme d'ecriture de<br>"+pathFileSMS,
						"Erreur", JOptionPane.ERROR_MESSAGE);
			} catch (IllegalArgumentException  e) {
				good = false;
				JOptionPane.showMessageDialog(null, "<html>probleme d'ecriture de<br>"+pathFileSMS,
						"Erreur", JOptionPane.ERROR_MESSAGE);
			}

	}//fin ecritureListeSMSxls

	private static void ecritureListeTests(ArrayList<Stagiaire> stagiaireList) {
		Hashtable<String,StringBuffer> stgMap = new Hashtable<String,StringBuffer>();
		String pathDossier = pathDirTests+dateFormat.format(dateDemain)+"/";
		String key = "";
		String group = Config.get("exp.tests.group.pattern");
		SimpleDateFormat jF = new SimpleDateFormat("EEEE");
		String jour = jF.format(dateDemain).toUpperCase();
		
		for (Stagiaire s : stagiaireList) {
			String code = s.getCodeStage().replace(" ", "").trim();
			code = code.substring(0,3)+" "+code.substring(3);
			if (code.matches(group)) {
				code = code.replaceAll(group, "$1");
				code += " "+jour;
			}
			String matr = "M"+s.getMatricule().subSequence(0, 6);
			if (stgMap.containsKey(code)) {
				//System.out.println("[INFO] ajout stage:"+code+" et matr:"+matr);
				stgMap.get(code).append("|"+matr);
			}
			else {
				System.out.println("[INFO]   +++ stage:"+code+" et matr:"+matr);
				stgMap.put(code,new StringBuffer(matr));
			}
		}

		try {
			new File(pathDossier).mkdir();
			
			
			Enumeration<String> e = stgMap.keys();
			 while (e.hasMoreElements()) {
				  key = e.nextElement();
				  StringBuffer value = stgMap.get(key);
				  
				  FileWriter fichier = new FileWriter(pathDossier+key+".txt");
				  PrintWriter printer = new PrintWriter(fichier);
				  printer.println(value);
				  printer.close();
			 }
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "<html>probleme d'ecriture de" +
					"<br>"+pathDossier+key+".txt", "Erreur", JOptionPane.ERROR_MESSAGE);
		}
	}

	@SuppressWarnings({ "deprecation", "unused" })
	private static void ecritureListeTests2() {
		ArrayList<Stage> stageList = PasserelleStage.lectureStageObj();
		String selDate = "";
		StringBuffer line;
		String code="",pathDossier="";
		
		//recuperation de la date de demain
		Calendar cl=new GregorianCalendar();
		Date dateactuelle = new Date();
		if(dateactuelle.getDay() == 5){
			cl.add(Calendar.DATE, 3);
		}else{
			cl.add(Calendar.DATE, 1);
		}
		try {
			dateDemain = new Date((cl.get(Calendar.YEAR)-1900), cl.get(Calendar.MONTH), cl.get(Calendar.DATE));		
			selDate = JOptionPane.showInputDialog("Séléction de la date: ", dateFormat.format(dateDemain));
			dateDemain = dateFormat.parse(selDate);
	
			pathDossier = pathDirTests+dateFormat.format(dateDemain)+"/";
			
			for (Stage stage : stageList) {
				if(dateDemain.before(stage.getDateDt()) == false 
						&& dateDemain.after(stage.getDateDt()) == false
						&& stage.getCode().matches(Config.get("exp.sms.pattern")) ) {
					code = stage.getCode().replace(" ", "").trim();
					code = code.substring(0,3)+" "+code.substring(3);
					line = new StringBuffer();
					for (Stagiaire s : stage.getStagiaireList()) {
						String matr = "M"+s.getMatricule().subSequence(0, 6);
						if (line.length() > 0) {
							//System.out.println("[INFO] ajout stage:"+code+" et matr:"+matr);
							line.append("|"+matr);
						}
						else {
							//System.out.println("[INFO]   +++ stage:"+code+" et matr:"+matr);
							line.append(matr);
						}
					} 
					new File(pathDossier).mkdir();
					FileWriter fichier = new FileWriter(pathDossier+code+"_2.txt");
					PrintWriter printer = new PrintWriter(fichier);
					printer.println(line);
					printer.close();
				}
			}
		}
		catch (IOException e) {
			JOptionPane.showMessageDialog(null, "<html>probleme d'ecriture de" +
					"<br>"+pathDossier+code+".txt", "Erreur", JOptionPane.ERROR_MESSAGE);
		}
		catch (ParseException e) {
			good = false;
			JOptionPane.showMessageDialog(null, "<html>probleme de format" +
				"<br/>de la date " + selDate + "</html>", "Erreur", JOptionPane.ERROR_MESSAGE);;
		}				
	}

}//fin class
