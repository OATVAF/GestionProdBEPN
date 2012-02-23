package pack;
import java.io.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.*;

import jxl.*;
import jxl.read.biff.*;

import jxl.write.*;
import jxl.write.biff.RowsExceededException;

/**
 * 
 * @author Jean-Sébastien BERON
 *
 */
public class PasserelleStagiaire {
	
	private static boolean good;
	private static final String pathFilePnc= "dataImport\\OATVPNC.xls";
	private static final String pathFilePnt= "dataImport\\OATVPNT.xls";
	
	
	public static void creerListePourSms(){
		
		good = true;
		ArrayList<Stagiaire> StagiaireList = chargerTousStagiairesPNC();
		StagiaireList = FiltreBOContact(StagiaireList);
		ecritureListeSMSxls(StagiaireList);
		if (good) {
			JOptionPane.showMessageDialog(null, "<html>Operation terminée !" +
					"<br>le fichier est dans dataExport\\ListeSMS.xls</html>", "Termine", JOptionPane.INFORMATION_MESSAGE);
		}//finsi
		
	}//fin creerListePourSms()
	
	public static ArrayList<Stagiaire> chargerTousStagiairesPNC(){
		
		ArrayList<Stagiaire> StagiaireList = new ArrayList<Stagiaire>();
			try {
				File fichier = new File(pathFilePnc);
				Workbook workbook;
				workbook = Workbook.getWorkbook(fichier);
				Sheet sheet = workbook.getSheet(0);
				for (int i = 1; i < sheet.getRows()-1; i++) {
					Cell[] cell = sheet.getRow(i);
					StagiaireList.add(new Stagiaire(cell[4].getContents(), cell[11].getContents(), cell[5].getContents()
							, cell[6].getContents(), cell[2].getContents(), cell[3].getContents()
							, cell[0].getContents(), cell[12].getContents()));
				}
			} catch (BiffException e) {
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
				Workbook workbook;
				workbook = Workbook.getWorkbook(fichier);
				Sheet sheet = workbook.getSheet(0);
				for (int i = 1; i < sheet.getRows()-1; i++) {
					Cell[] cell = sheet.getRow(i);
					StagiaireList.add(new Stagiaire(cell[3].getContents(), cell[9].getContents(), cell[5].getContents()
							, cell[6].getContents(), cell[1].getContents(), cell[2].getContents()
							, cell[0].getContents(), cell[12].getContents()));
				}
			} catch (BiffException e) {
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
	
	public static ArrayList<Stage> ajoutPnt(ArrayList<Stage> stageList,ArrayList<Stagiaire> pntList){
		ArrayList<Stage> newStageList = stageList;
		ArrayList<String> stagePNTList = new ArrayList<String>();
		stagePNTList.add("S2");
		stagePNTList.add("QT");
		int index;
		for (Stage stage : newStageList) {
			index = -1;
			for (int i = 0 ; i < stagePNTList.size(); i++) {
				if(stage.getCode().startsWith(stagePNTList.get(i))){
					index = i;
					break;
				}
			}
			switch (index) {
			case 0:
				for (Stagiaire stagiaire : pntList) {
					if (stagiaire.getCodeStage().trim().endsWith("S2CC")) {
						stage.ajoutStagiaire(stagiaire);
					}
				}
				break;
			case 1:
				ArrayList<String> qtinfo = new ArrayList<String>();
				String codeStage = stage.getCode();
				String chaine = "";
				for (int i = 0; i < codeStage.length(); i++) {
					if(codeStage.substring(i, i+1).equalsIgnoreCase(" ")){
						qtinfo.add(chaine);
						chaine = "";
					}else{
						chaine = chaine + codeStage.substring(i, i+1);
					}
				}
				qtinfo.add(chaine);
				for (Stagiaire stagiaire : pntList) {
					for (String string : qtinfo) {
						if (stagiaire.getCodeStage().startsWith(string)) {
							stage.ajoutStagiaire(stagiaire);
						}
					}
				}
				break;
			default:
				break;
			}
		}
		
		return newStageList;
	}//
	
	public static ArrayList<Stage> ajoutPnc(ArrayList<Stage> stageList,ArrayList<Stagiaire> stagiairePNCList) {
		ArrayList<Stage> newStageList = stageList;
		
		for (Stage stage : newStageList) {
			String strCodeStage = stage.getCode().replace(" ", "");
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
	}

	
	
	@SuppressWarnings("deprecation")
	private static ArrayList<Stagiaire> FiltreBOContact(ArrayList<Stagiaire> StagiaireList){
		
		ArrayList<Stagiaire> newStagiaireList = StagiaireList;
		
		ArrayList<String> stageList = new ArrayList<String>();
		ArrayList<Stagiaire> stagiaireGood = new ArrayList<Stagiaire>();
		FileReader fichier;
			try {
				String ligne;
				fichier = new FileReader("dataSystem\\StageSMSPNC.txt");
				BufferedReader reader = new BufferedReader(fichier);
				while ((ligne = reader.readLine()) != null){
					stageList.add(ligne);
				}
				
				//recuperation de la date de demain
				Calendar cl=new GregorianCalendar();
				Date dateactuelle = new Date();
				if(dateactuelle.getDay() == 5){
					cl.add(Calendar.DATE, 3);
				}else{
					cl.add(Calendar.DATE, 1);
				}
				Date dateDemain = new Date((cl.get(Calendar.YEAR)-1900), cl.get(Calendar.MONTH), cl.get(Calendar.DATE));
				
				for (Stagiaire stagiaire : newStagiaireList) {
					if(dateDemain.before(stagiaire.getDateDeb()) == false && dateDemain.after(stagiaire.getDateFin()) == false){
						for (String string : stageList) {
							if(stagiaire.getCodeStage().startsWith(string)){
								stagiaireGood.add(stagiaire);
								break;
							}
						}
					}
				}
				
			} catch (FileNotFoundException e) {
				good = false;
				JOptionPane.showMessageDialog(null, "<html>fichier non trouvé" +
						"<br/>dataSystem\\StageSMSPNC.txt</html>", "Erreur", JOptionPane.ERROR_MESSAGE);
			} catch (IOException e) {
				good = false;
				JOptionPane.showMessageDialog(null, "<html>probleme de lecture de" +
						"<br/>dataSystem\\StageSMSPNC.txt</html>", "Erreur", JOptionPane.ERROR_MESSAGE);;
			}
			
		newStagiaireList.retainAll(stagiaireGood);
		
		return newStagiaireList;
		
	}//fin FiltreBOContact()
	
	
	private static void ecritureListeSMSxls(ArrayList<Stagiaire> StagiaireList){
		
			try {
				File file = new File("dataExport\\listeSMS.xls");
				WritableWorkbook workbook;
				workbook = Workbook.createWorkbook(file);
				WritableSheet sheet = workbook.createSheet("ListeMAT", 0);
				for (int i = 0; i < StagiaireList.size(); i++) {
					sheet.addCell(new Label(0, i, StagiaireList.get(i).getMatricule()));
				}
				workbook.write(); 
				workbook.close();
			} catch (IOException e) {
				good = false;
				JOptionPane.showMessageDialog(null, "<html>probleme d'ecriture de" +
						"<br/>dataExport\\listeSMS.xls", "Erreur", JOptionPane.ERROR_MESSAGE);
			} catch (RowsExceededException e) {
				good = false;
				JOptionPane.showMessageDialog(null, "<html>probleme d'ecriture de" +
						"<br/>dataExport\\listeSMS.xls</html>", "Erreur", JOptionPane.ERROR_MESSAGE);
			} catch (WriteException e) {
				good = false;
				JOptionPane.showMessageDialog(null, "<html>probleme d'ecriture de" +
						"<br/>dataExport\\listeSMS.xls</html>", "Erreur", JOptionPane.ERROR_MESSAGE);
			}

	}//fin ecritureListeSMSxls

}//fin class
