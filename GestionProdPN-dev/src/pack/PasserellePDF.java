package pack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


/**
 * classe technique qui gére la génération des document en format PDF
 * @author BERON Jean-Sebastien
 *
 */
public class PasserellePDF {
	
	/**
	 * creer le pdf pour la liste des stagiaires pour le stage passé en parametre
	 * @param leStage
	 */
	public static void creationListeStagiaire(Stage leStage){
		
		//creation du dossier
		String date = leStage.getDateStr();
		date = date.replace("/", ".");
		String pathDossier = "ListeStagiaire du "+date;
		new File("dataExport\\"+pathDossier).mkdir();
		
		try {
			//creation du document et du fichier
			Document doc = new Document(PageSize.A4,20,20,10,0);
			FileOutputStream fichier = new FileOutputStream("dataExport\\"+pathDossier+"\\listeStagiaire - "+leStage.getCode()+".pdf");
			//ouverture du writer
			PdfWriter.getInstance(doc, fichier);
			doc.open();
			
			//construction du header
			PdfPTable header = new PdfPTable(1);
			header.setWidthPercentage(100);
			
			//la date
			PdfPCell cell = new PdfPCell(new Phrase(leStage.getDateStr(),new Font(Font.HELVETICA, 14, Font.BOLD)));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorder(0);
			header.addCell(cell);
			
			//le code du stage
			Phrase par = new Phrase(leStage.getCode(),new Font(Font.HELVETICA, 28, Font.BOLD));
			cell = new PdfPCell(par);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setPadding(10);
			cell.setBorder(0);
			header.addCell(cell);
			
			//le libelle du stage
			par = new Phrase(leStage.getLibelle(),new Font(Font.HELVETICA, 14, Font.BOLD));
			cell = new PdfPCell(par);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setPadding(10);
			cell.setBorder(0);
			header.addCell(cell);
			
			//construction du center
			PdfPTable center = new PdfPTable(8);
			center.setWidthPercentage(100);
			
			int i;
			for (i = 0; i < leStage.getSizeStagiaireList(); i++) {
				for (int j = 0; j < 5; j++) {
					String info = leStage.getEltStagiaireList(i).getInfo(j);
					Phrase phrs = new Phrase(info,new Font(Font.HELVETICA, 11));
					cell = new PdfPCell(phrs);
					cell.setPadding(5);
					if(j != 3 && j != 2){
						cell.setColspan(2);
					}//finpour
					cell.setBorder(0);
					center.addCell(cell);
				}//finpour
			}//finpour
			
			if(i<24){
				for (int j = i; j <= 24; j++) {
					for (int k = 0; k < 5; k++) {
						Phrase phrs = new Phrase(" ");
						cell = new PdfPCell(phrs);
						cell.setBorder(0);
						if(j != 3 && j != 2){
							cell.setColspan(2);
						}//finpour
						cell.setPadding(5);
						center.addCell(cell);
					}//finpour
				}//finpour
			}//finsi
			
			//construction du footer
			PdfPTable footer = new PdfPTable(3);
			footer.setWidthPercentage(95);
			cell = new PdfPCell();
			cell.setBorder(0);
			footer.addCell(cell);
			cell = new PdfPCell(new Phrase("Formateur : "+leStage.getLeader(),new Font(Font.HELVETICA, 12, Font.BOLD)));
			cell.setColspan(2);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setBorder(0);
			footer.addCell(cell);
			cell = new PdfPCell(new Phrase(leStage.getFirstModule().getSalle(),new Font(Font.HELVETICA, 22, Font.BOLD)));
			cell.setBorder(0);
			footer.addCell(cell);
			cell = new PdfPCell(new Phrase(leStage.getFirstModule().getLibelle(),new Font(Font.HELVETICA, 18, Font.BOLD)));
			cell.setBorder(0);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			footer.addCell(cell);
			cell = new PdfPCell(new Phrase(leStage.getFirstModule().getHeureDebut(),new Font(Font.HELVETICA, 22, Font.BOLD)));
			cell.setBorder(0);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			footer.addCell(cell);
			
			//ajout des composants
			doc.add(header);
			doc.add(center);
			doc.add(footer);
			
			//fermeture du writer
			doc.close();
			
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "<html>erreur" +
					"<br/>de fichier</html>", "Erreur", JOptionPane.ERROR_MESSAGE);
		} catch (DocumentException e) {
			JOptionPane.showMessageDialog(null, "<html>erreur" +
					"<br/>d'ecriture</html>", "Erreur", JOptionPane.ERROR_MESSAGE);
		}
		
	}//fin 
	
	/**
	 * creer le pdf pour la liste d'emargement pour le stage passé en parametre
	 * @param leStage
	 */
	public static void creationListeEmargement(Stage leStage){
		
		String date = leStage.getDateStr();
		date = date.replace("/", ".");
		String pathDossier = "Emargement du "+date;
		new File("dataExport\\"+pathDossier).mkdir();
		
		try {
			
			//creation du document
			Document doc = new Document(PageSize.A4,10,10,10,0);
			FileOutputStream fichier = new FileOutputStream("dataExport\\"+pathDossier+"\\listeEmargement - "+leStage.getCode()+".pdf");
			PdfWriter.getInstance(doc, fichier);
			doc.open();
			
			PdfPTable header = new PdfPTable(1);
			header.setWidthPercentage(100);
			
			PdfPCell cell = new PdfPCell(new Phrase(leStage.getDateStr(),new Font(Font.HELVETICA, 12, Font.BOLD)));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorder(0);
			header.addCell(cell);

			Phrase par = new Phrase(leStage.getCode(),new Font(Font.HELVETICA, 22, Font.BOLD));
			cell = new PdfPCell(par);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setPadding(5);
			cell.setBorder(0);
			header.addCell(cell);
			
			//formation du center
			PdfPTable center = new PdfPTable(5);
			//center.setWidthPercentage(95);

			float[] widths = new float[] { 1f, 8f, 1.5f, 4f, 1.5f };
			center.setWidths(widths);
			
			Font fontB = new Font(Font.HELVETICA, 10, Font.BOLD);
			cell = new PdfPCell(new Phrase("",fontB));
			cell.setBorder(0);
			center.addCell(cell);
			//cell = new PdfPCell(new Phrase("Stagiaire",font));
			//cell.setBorder(0);
			//cell.setColspan(6);
			cell.setPhrase(new Phrase("Stagiaire",fontB));
			center.addCell(cell);
			//cell = new PdfPCell(new Phrase("Presence",font));
			//cell.setBorder(0);
			//cell.setColspan(2);
			cell.setPhrase(new Phrase("Presence",fontB));
			center.addCell(cell);
			//cell = new PdfPCell(new Phrase("Emargement",font));
			//cell.setBorder(0);
			//cell.setColspan(4);
			cell.setPhrase(new Phrase("Emargement",fontB));
			center.addCell(cell);
			//cell = new PdfPCell(new Phrase("IS.VS/MP",font));
			//cell.setColspan(2);
			//cell.setBorder(0);
			cell.setPhrase(new Phrase("IS.VS/MP",fontB));
			center.addCell(cell);
			
			int i;
			Font font = new Font(Font.HELVETICA, 10);
			// TODO SG font constante
			/*
			if(leStage.getSizeStagiaireList()>=20 && leStage.getSizeStagiaireList() < 25){
				font.setSize(11);
			}
			if(leStage.getSizeStagiaireList()>=25){
				font.setSize(9);
			}
			*/
			for (i = 0; i < leStage.getSizeStagiaireList(); i++) {
					cell.setPhrase(new Phrase(""+(i+1), font));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorder(7);
					center.addCell(cell);
					// String str = "";
					// TODO  SG liste statique
					/*
					for (int j = 0; j < 5; j++) {
						str = str + leStage.getEltStagiaireList(i).getInfo(j);
						if(j == 1){
							str = str+"\n";
						}else{
							if(j < 4){
								str = str + "  ";
							}
						}
					}//fin pour
					*/
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					PdfPTable stgCell = new PdfPTable(10);
					center.setWidthPercentage(100);

					//str = String.format("%-20s%-20s\n%25s%10s%10s", 
					cell.setBorder(0);
					cell.setColspan(5);
					cell.setPhrase(new Phrase(leStage.getEltStagiaireList(i).getInfo(0), fontB));
					stgCell.addCell(cell);
					cell.setPhrase(new Phrase(leStage.getEltStagiaireList(i).getInfo(1), font));
					stgCell.addCell(cell);

					cell.setColspan(1);
					cell.setPhrase(new Phrase(" ", font));
					stgCell.addCell(cell);

					cell.setColspan(2);
					cell.setPhrase(new Phrase(leStage.getEltStagiaireList(i).getInfo(3), font));
					stgCell.addCell(cell);

					cell.setColspan(5);
					cell.setPhrase(new Phrase(leStage.getEltStagiaireList(i).getInfo(4), font));
					stgCell.addCell(cell);

					cell.setColspan(2);
					cell.setPhrase(new Phrase(leStage.getEltStagiaireList(i).getInfo(2), font));
					stgCell.addCell(cell);
					
					//Phrase phrs = new Phrase(str, font);
					//cell = new PdfPCell(phrs);
					//cell.setColspan(6);
					cell.setColspan(1);
					center.addCell(stgCell);
					//presence
					//cell = new PdfPCell(new Phrase(" "));
					//cell.setColspan(2);
					center.addCell(" ");
					//emargement
					//cell.setColspan(4);
					center.addCell(" ");
					//isvsmp
					//cell = new PdfPCell(new Phrase(" "));
					//cell.setColspan(2);
					center.addCell(" ");
			}
			if(i<=20){
				cell.setBorder(0);
				cell.setPhrase(new Phrase(" ", font));
				cell.setColspan(5);
				for (int j = i; j < 20; j++) {
					//for (int k = 0; k < 5; k++) {
						//Phrase phrs = new Phrase("- \n ", new Font(Font.TIMES_ROMAN, 10));
						//cell = new PdfPCell(phrs);
						//cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						//cell.setPadding(4);
						center.addCell(cell);
					//}
				}//fin pour
			}//fin si
			
			PdfPTable footer = new PdfPTable(3);
			footer.setWidthPercentage(75);
			cell = new PdfPCell(new Phrase("Nombre de stagiaires :",new Font(Font.HELVETICA, 14, Font.BOLD)));
			cell.setColspan(3);
			cell.setBorder(0);
			footer.addCell(cell);
			
			cell = new PdfPCell(new Phrase("Prévus",new Font(Font.HELVETICA, 14, Font.BOLD)));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorderWidthTop(0);
			cell.setBorderWidthLeft(0);
			cell.setBorderWidthBottom(1);
			cell.setBorderWidthRight(1);
			footer.addCell(cell);
			
			cell = new PdfPCell(new Phrase("Présents",new Font(Font.HELVETICA, 14, Font.BOLD)));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorderWidthTop(0);
			cell.setBorderWidthLeft(1);
			cell.setBorderWidthBottom(1);
			cell.setBorderWidthRight(1);
			footer.addCell(cell);
			
			cell = new PdfPCell(new Phrase("Maxi Présents",new Font(Font.HELVETICA, 14, Font.BOLD)));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorderWidthTop(0);
			cell.setBorderWidthLeft(1);
			cell.setBorderWidthRight(0);
			cell.setBorderWidthBottom(1);
			footer.addCell(cell);
			
			cell = new PdfPCell(new Phrase(""+leStage.getSizeStagiaireList(),new Font(Font.HELVETICA, 14, Font.BOLD)));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorderWidthTop(1);
			cell.setBorderWidthLeft(0);
			cell.setBorderWidthRight(1);
			cell.setBorderWidthBottom(0);
			footer.addCell(cell);
			
			cell = new PdfPCell(new Phrase(" ",new Font(Font.HELVETICA, 14, Font.BOLD)));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorderWidthTop(1);
			cell.setBorderWidthLeft(1);
			cell.setBorderWidthRight(1);
			cell.setBorderWidthBottom(0);;
			footer.addCell(cell);
			
			cell = new PdfPCell(new Phrase(""+leStage.getMaxiPresent(),new Font(Font.HELVETICA, 14, Font.BOLD)));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorderWidthTop(1);
			cell.setBorderWidthLeft(1);
			cell.setBorderWidthRight(0);
			cell.setBorderWidthBottom(0);
			footer.addCell(cell);
			
			//formateur
			PdfPTable footer2 = new PdfPTable(1);
			footer2.setWidthPercentage(80);
			par = new Phrase("Formateurs : _ _ _ _ _ _ _ _ _ _ _ _ _ _ _/ "+leStage.getLeader(),new Font(Font.HELVETICA, 14, Font.BOLD));
			cell = new PdfPCell(par);
			cell.setBorder(0);
			footer2.addCell(cell);
			
			//ajout des composants
			doc.add(header);
			doc.add(new Paragraph("        "));
			doc.add(center);
			doc.add(new Paragraph("        "));
			doc.add(footer);
			doc.add(footer2);
			
			doc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
	}//fin
	
	/**
	 * creer le pdf pour la liste d'emargement pour le stage passé en parametre
	 * @param leStage
	 */
	public static void creationListeEmargementVide(Stage leStage){
		
		String date = leStage.getDateStr();
		date = date.replace("/", ".");
		String pathDossier = "Emargement du "+date;
		new File("dataExport\\"+pathDossier).mkdir();
		
		try {
			
			//creation du document
			Document doc = new Document(PageSize.A4,10,10,10,0);
			FileOutputStream fichier = new FileOutputStream("dataExport\\"+pathDossier+"\\listeEmargement - "+leStage.getCode()+".pdf");
			PdfWriter.getInstance(doc, fichier);
			doc.open();
			
			PdfPTable header = new PdfPTable(1);
			header.setWidthPercentage(100);
			
			PdfPCell cell = new PdfPCell(new Phrase(leStage.getDateStr(),new Font(Font.HELVETICA, 12, Font.BOLD)));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorder(0);
			header.addCell(cell);

			Phrase par = new Phrase(leStage.getCode(),new Font(Font.HELVETICA, 24, Font.BOLD));
			cell = new PdfPCell(par);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setPadding(5);
			cell.setBorder(0);
			header.addCell(cell);
			
			PdfPTable center = new PdfPTable(34);
			center.setWidthPercentage(95);
			Font font = new Font(Font.HELVETICA, 10, Font.BOLD);
			cell = new PdfPCell(new Phrase("",font));
			cell.setBorder(0);
			cell.setColspan(2);
			center.addCell(cell);
			cell = new PdfPCell(new Phrase("Nom",font));
			cell.setBorder(0);
			cell.setColspan(8);
			center.addCell(cell);
			cell = new PdfPCell(new Phrase("Prenom",font));
			cell.setBorder(0);
			cell.setColspan(8);
			center.addCell(cell);
			cell = new PdfPCell(new Phrase("Matricule",font));
			cell.setBorder(0);
			cell.setColspan(8);
			center.addCell(cell);
			cell = new PdfPCell(new Phrase("Emargement",font));
			cell.setColspan(8);
			cell.setBorder(0);
			center.addCell(cell);
			
			font = new Font(Font.HELVETICA, 13, Font.BOLD);
			for (int j = 0; j < 20; j++) {
					Phrase phrs = new Phrase(""+(j+1), font);
					cell = new PdfPCell(phrs);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setColspan(2);
					center.addCell(cell);
					//nom
					phrs = new Phrase(" \n ", font);
					cell = new PdfPCell(phrs);
					cell.setColspan(8);
					center.addCell(cell);
					//prenom
					phrs = new Phrase("  ", font);
					cell = new PdfPCell(phrs);;
					cell.setColspan(8);
					center.addCell(cell);
					//matricule
					for (int k = 0; k < 8; k++) {
						phrs = new Phrase(" ", font);
						cell = new PdfPCell(phrs);
						center.addCell(cell);
					}
					//emargement
					phrs = new Phrase("  ", font);
					cell = new PdfPCell(phrs);
					cell.setPadding(4);
					cell.setColspan(8);
					center.addCell(cell);
			}//fin pour
			
			//nombre de stagiares
			PdfPTable footer = new PdfPTable(3);
			footer.setWidthPercentage(75);
			cell = new PdfPCell(new Phrase("Nombre de stagiaires :",new Font(Font.HELVETICA, 14, Font.BOLD)));
			cell.setColspan(3);
			cell.setBorder(0);
			footer.addCell(cell);
			
			cell = new PdfPCell(new Phrase("Prévus",new Font(Font.HELVETICA, 14, Font.BOLD)));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorderWidthTop(0);
			cell.setBorderWidthLeft(0);
			cell.setBorderWidthBottom(1);
			cell.setBorderWidthRight(1);
			footer.addCell(cell);
			
			//presents
			cell = new PdfPCell(new Phrase("Présents",new Font(Font.HELVETICA, 14, Font.BOLD)));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorderWidthTop(0);
			cell.setBorderWidthLeft(1);
			cell.setBorderWidthBottom(1);
			cell.setBorderWidthRight(1);
			footer.addCell(cell);
			
			//maxipresent
			cell = new PdfPCell(new Phrase("Maxi Présents",new Font(Font.HELVETICA, 14, Font.BOLD)));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorderWidthTop(0);
			cell.setBorderWidthLeft(1);
			cell.setBorderWidthRight(0);
			cell.setBorderWidthBottom(1);
			footer.addCell(cell);
			
			//cellule vide
			cell = new PdfPCell(new Phrase(" ",new Font(Font.HELVETICA, 14, Font.BOLD)));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorderWidthTop(1);
			cell.setBorderWidthLeft(0);
			cell.setBorderWidthRight(1);
			cell.setBorderWidthBottom(0);
			footer.addCell(cell);
			
			//cellule vide
			cell = new PdfPCell(new Phrase(" ",new Font(Font.HELVETICA, 14, Font.BOLD)));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorderWidthTop(1);
			cell.setBorderWidthLeft(1);
			cell.setBorderWidthRight(1);
			cell.setBorderWidthBottom(0);
			footer.addCell(cell);
			
			//cellule vide
			cell = new PdfPCell(new Phrase(" ",new Font(Font.HELVETICA, 14, Font.BOLD)));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorderWidthTop(1);
			cell.setBorderWidthLeft(1);
			cell.setBorderWidthRight(0);
			cell.setBorderWidthBottom(0);
			footer.addCell(cell);
			
			//formateur
			PdfPTable footer2 = new PdfPTable(1);
			footer2.setWidthPercentage(80);
			par = new Phrase("Formateurs : _ _ _ _ _ _ _ _ _ _ _ _ _ _ _/ "+leStage.getLeader(),new Font(Font.HELVETICA, 14, Font.BOLD));
			cell = new PdfPCell(par);
			cell.setBorder(0);
			footer2.addCell(cell);
			
			//ajout des composants
			doc.add(header);
			doc.add(new Paragraph("        "));
			doc.add(center);
			doc.add(new Paragraph("        "));
			doc.add(footer);
			doc.add(footer2);
			
			//fermeture
			doc.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
	}//fin
	
	/**
	 * creer les pdf des feuilles de routes FSS
	 */
	public static void creationAllFRFSS(ArrayList<Stage> stageList){
		
		ArrayList<String> fssList = new ArrayList<String>();
		String date = stageList.get(0).getDateStr();
		
		for (Stage leStage : stageList) {
			ArrayList<Module> moduleList = leStage.getModuleList();
			for (Module module : moduleList) {
				if( ! module.getNomLeader().equalsIgnoreCase("")){
					if(! fssList.contains(module.getNomLeader())){
						fssList.add(module.getNomLeader());
					}
				}//finsi
				if( ! module.getNomAide().equalsIgnoreCase("")){
					if(! fssList.contains(module.getNomAide())){
						fssList.add(module.getNomAide());
					}
				}//finsi
			}//finpour
		}//finpour
		
		for (String nomFss : fssList) {
			ArrayList<Module> moduleList = new ArrayList<Module>();
			for (Stage leStage : stageList) {
				for (Module module : leStage.getModuleList()) {
					if(module.getNomLeader().equalsIgnoreCase(nomFss)){
						moduleList.add(module);
					}else{
						if(module.getNomAide().equalsIgnoreCase(nomFss)){
							moduleList.add(module);
						}
					}
				}//finpour
			}//finpour
			if(moduleList.size()!=0){
				creationFeuilRouteFSS(date, nomFss, moduleList);
			}
		}//finpour
		
	}//fin creationAllFRFSS()
	
	/**
	 * creer le pdf pour la feuille de route pour les FSS
	 * @param Nom
	 * @param moduleList
	 */
	public static void creationFeuilRouteFSS(String date, String Nom, ArrayList<Module> moduleList){
		
		//creation du dossier
		String ladate = date.replace("/", ".");
		String pathDossier = "Feuilles de routes du "+ladate;
		new File("dataExport\\"+pathDossier).mkdir();
		
		
		// Suppression des feuilles inutiles :
		if (Nom.startsWith("IFH PNC") || Nom.startsWith("IPNC") || Nom.startsWith("CADRE PNT")
				|| Nom.startsWith("Infirmière EXT") || Nom.startsWith("SFI")) {
			return;
		}
		
		//tri
		Module modTemps ;
		boolean good = false;
		//tant que le tri n'est pas bon
		while (! good) {
			good = true;
			for (int i = 0; i < moduleList.size()-1; i++) {
				if(moduleList.get(i).getnbMin() > moduleList.get(i+1).getnbMin()){
					good = false;
					//echange
					modTemps = moduleList.get(i);
					moduleList.set(i, moduleList.get(i+1));
					moduleList.set(i+1, modTemps);
				}//finsi
			}//finpour
		}//fin tant que
		
		
		try {
			
			Document doc = new Document(PageSize.A4);
			FileOutputStream fichier = new FileOutputStream("dataExport\\"+pathDossier+"\\FeuilleRouteFSS - "+Nom+".pdf");
			PdfWriter.getInstance(doc, fichier);
			doc.open();
			
			PdfPTable header = new PdfPTable(1);
			Phrase phrs = new Phrase(date,new Font(Font.HELVETICA, 15, Font.BOLD));
			PdfPCell cell = new PdfPCell(phrs);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorder(0);
			header.addCell(cell);
			phrs = new Phrase(Nom,new Font(Font.HELVETICA, 20, Font.BOLD));
			cell = new PdfPCell(phrs);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorder(0);
			header.addCell(cell);
			phrs = new Phrase("Stages du jour",new Font(Font.HELVETICA, 15, Font.BOLD));
			cell = new PdfPCell(phrs);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorder(0);
			header.addCell(cell);
			
			PdfPTable center = new PdfPTable(5);
			center.setWidthPercentage(100);
			float[] widths = new float[] { 1f, 1f, 1f, 1.2f, 1.2f };
			center.setWidths(widths);

			phrs = new Phrase("Code Stage",new Font(Font.HELVETICA, 12, Font.BOLD));
			cell = new PdfPCell(phrs);
			cell.setBorder(0);
			cell.setPadding(10);
			center.addCell(cell);
			phrs = new Phrase("Leader/Aide",new Font(Font.HELVETICA, 12, Font.BOLD));
			cell = new PdfPCell(phrs);
			cell.setBorder(0);
			cell.setPadding(10);
			center.addCell(cell);
			phrs = new Phrase("Horaire",new Font(Font.HELVETICA, 12, Font.BOLD));
			cell = new PdfPCell(phrs);
			cell.setBorder(0);
			cell.setPadding(10);
			center.addCell(cell);
			phrs = new Phrase("Module",new Font(Font.HELVETICA, 12, Font.BOLD));
			cell = new PdfPCell(phrs);
			cell.setBorder(0);
			cell.setPadding(10);
			center.addCell(cell);
			phrs = new Phrase("Salle",new Font(Font.HELVETICA, 12, Font.BOLD));
			cell = new PdfPCell(phrs);
			cell.setBorder(0);
			cell.setPadding(10);
			center.addCell(cell);
			for (Module module : moduleList) {
				phrs = new Phrase(module.getCodeStage(),new Font(Font.HELVETICA, 12));
				cell = new PdfPCell(phrs);
				cell.setBorder(0);
				cell.setPadding(10);
				center.addCell(cell);
				String lead = "Leader";
				String aide = module.getNomAide();
				if(! module.getNomLeader().equalsIgnoreCase(Nom)){
					lead = "Aide";
					aide = module.getNomLeader();
				}
				phrs = new Phrase(lead,new Font(Font.HELVETICA, 12));
				if (!aide.equals("")) {
					phrs.add(new Phrase("\n"+aide,new Font(Font.HELVETICA, 9, Font.ITALIC)));

				}
				//phrs = new Phrase(lead,new Font(Font.HELVETICA, 10));
				cell = new PdfPCell(phrs);
				cell.setBorder(0);
				cell.setPadding(10);
				center.addCell(cell);
				phrs = new Phrase(module.getHeureDebut()+" - "+module.getHeureFin(),new Font(Font.HELVETICA, 12));
				cell = new PdfPCell(phrs);
				cell.setBorder(0);
				cell.setPadding(10);
				center.addCell(cell);
				phrs = new Phrase(module.getLibelle(),new Font(Font.HELVETICA, 12));
				cell = new PdfPCell(phrs);
				cell.setBorder(0);
				cell.setPadding(10);
				center.addCell(cell);
				phrs = new Phrase(module.getSalle(),new Font(Font.HELVETICA, 12));
				cell = new PdfPCell(phrs);
				cell.setBorder(0);
				cell.setPadding(10);
				center.addCell(cell);
			}
			
			doc.add(header);
			doc.add(new Phrase("  "));
			doc.add(center);
			
			doc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
	}//fin
	
	/**
	 * creer le pdf pour le FREP du stage passé en parametre
	 * @param leStage
	 */
	public static void creationFREP(Stage leStage){
		
		String date = leStage.getDateStr();
		date = date.replace("/", ".");
		String pathDossier = "FREP du "+date;
		new File("dataExport\\"+pathDossier).mkdir();
		
		try {
			Document doc = new Document(PageSize.A4,60,60,10,10);
			FileOutputStream fichier;
			fichier = new FileOutputStream("dataExport\\"+pathDossier+"\\FREP - "+leStage.getCode()+".pdf");
			PdfWriter.getInstance(doc, fichier);
			doc.open();
			
			//construction du header
			PdfPTable header = new PdfPTable(1);
			header.setWidthPercentage(100);
			
			//la date
			PdfPCell cell = new PdfPCell(new Phrase(leStage.getDateStr(),new Font(Font.HELVETICA, 14, Font.BOLD)));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorder(0);
			header.addCell(cell);
			
			//le code du stage
			Phrase par = new Phrase(leStage.getCode(),new Font(Font.HELVETICA, 28, Font.BOLD));
			cell = new PdfPCell(par);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setPadding(5);
			cell.setBorder(0);
			header.addCell(cell);
			
			//l'intitulé
			par = new Phrase("Fiche récaputulative de fin de formation",new Font(Font.HELVETICA, 18, Font.BOLD));
			cell = new PdfPCell(par);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setPadding(5);
			cell.setBorder(0);
			header.addCell(cell);
			
			doc.add(header);
			
			par = new Phrase("\n");
			doc.add(par);
			
			par = new Phrase("Conformément au programme, les exercices pratiques suivant ont été réalisés :");
			doc.add(par);
			
			ArrayList<String> list = new ArrayList<String>();
			list .add("  •  Equipement");
			list .add("  •  Feu - Fumée");
			list .add("  •  Manœuvres en mode normal des portes");
			list .add("  •  Manœuvres en mode secours des portes");
			list .add("  •  Trappe PSU");
			list .add("  •  Manœuvres en mode secours des issues d'aile");
			list .add("  •  Evacuation par toboggan");
			list .add("  •  Convertible");
			list .add("  •  Evacuation par toboggan dégonflé");
			list .add("  •  Evacuation par le dispositif associé à une issu d'aile");
			list .add("  •  Vol simulé en maquette ou simulatuer cabine");
			list .add("  •  Vol simulé en mode alternatif (en salle)");
			
			PdfPTable listTable = new PdfPTable(4);
			listTable.setWidthPercentage(100);
			for (String string : list) {
				cell = new PdfPCell(new Phrase(string));
				cell.setColspan(3);
				cell.setPadding(3);
				cell.setBorder(0);
				listTable.addCell(cell);
				cell = new PdfPCell(new Phrase("Oui / Sans objet *"));
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBorder(0);
				cell.setPadding(3);
				listTable.addCell(cell);
			}//fin pour
			
			cell = new PdfPCell(new Phrase(" • Visualisation sur avion"));
			cell.setColspan(2);
			cell.setPadding(3);
			cell.setBorder(0);
			listTable.addCell(cell);
			cell = new PdfPCell(new Phrase("Immat. : _ _ _ _ _ _ / sans objet *"));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setBorder(0);
			cell.setColspan(2);
			cell.setPadding(3);
			listTable.addCell(cell);
			
			cell = new PdfPCell(new Phrase(" • Visualisation virtuelle"));
			cell.setColspan(2);
			cell.setPadding(3);
			cell.setBorder(0);
			listTable.addCell(cell);
			cell = new PdfPCell(new Phrase("Type-avion : _ _ _ _ _ _ / sans objet *"));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setBorder(0);
			cell.setColspan(2);
			cell.setPadding(3);
			listTable.addCell(cell);
			
			listTable.addCell(new PdfPCell(new Phrase(" ")));
			
			doc.add(listTable);
			
			
			PdfPTable question = new PdfPTable(5);
			question.setWidthPercentage(100);
			cell = new PdfPCell(new Phrase("Des stagiaires ont-ils echoué aux exercices pratiques ?"));
			cell.setColspan(4);
			cell.setPadding(5);
			cell.setBorder(0);
			question.addCell(cell);
			cell = new PdfPCell(new Phrase("Oui / Non *"));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setBorder(0);
			cell.setPadding(5);
			question.addCell(cell);
			doc.add(question);
			
			par = new Phrase("      Si oui, lesquels ? _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ \n");
			doc.add(par);
			
			par = new Phrase("      Sur quel exercice ? _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ \n");
			doc.add(par);
			
			question = new PdfPTable(5);
			question.setWidthPercentage(100);
			cell = new PdfPCell(new Phrase("Cette information a-t-elle été transmise à IS.VS MP ?"));
			cell.setColspan(4);
			cell.setPadding(5);
			cell.setBorder(0);
			question.addCell(cell);
			cell = new PdfPCell(new Phrase("Oui / Non *"));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setBorder(0);
			cell.setPadding(5);
			question.addCell(cell);
			doc.add(question);
			
			question = new PdfPTable(5);
			question.setWidthPercentage(100);
			cell = new PdfPCell(new Phrase("Des stagiaires sont-ils partis en cours de formation ?"));
			cell.setColspan(4);
			cell.setPadding(5);
			cell.setBorder(0);
			question.addCell(cell);
			cell = new PdfPCell(new Phrase("Oui / Non *"));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setBorder(0);
			cell.setPadding(5);
			question.addCell(cell);
			doc.add(question);
			
			par = new Phrase("      Si oui, lesquels ?  _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ \n");
			doc.add(par);
			
			question = new PdfPTable(5);
			question.setWidthPercentage(100);
			cell = new PdfPCell(new Phrase("Un des exercices a-t-il été réalisé en mode dérogatoire ?"));
			cell.setColspan(4);
			cell.setPadding(5);
			cell.setBorder(0);
			question.addCell(cell);
			cell = new PdfPCell(new Phrase("Oui / Non *"));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setBorder(0);
			cell.setPadding(5);
			question.addCell(cell);
			doc.add(question);
			
			par = new Phrase("      Si oui, lesquels ?  _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ \n");
			doc.add(par);
			
			question = new PdfPTable(5);
			question.setWidthPercentage(100);
			cell = new PdfPCell(new Phrase("La durée de formation s'est-elle écartée de plus de 15mn du temps prévu ?"));
			cell.setColspan(4);
			cell.setPadding(5);
			cell.setBorder(0);
			question.addCell(cell);
			cell = new PdfPCell(new Phrase("Oui / Non *"));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setBorder(0);
			cell.setPadding(5);
			question.addCell(cell);
			doc.add(question);
			
			par = new Phrase("      Si oui, pourquoi ?  _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ \n");
			doc.add(par);
			
			par = new Phrase("Voir Remarques / particularités du stage au verso (AT, problèmes rencontrés, ...) \n \n");
			doc.add(par);
			
			par = new Phrase("Nom du FSS leader : _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ \n");
			par.setFont(new Font(Font.HELVETICA, 14));
			doc.add(par);
			
			PdfPTable sign = new PdfPTable(3);
			sign.setWidthPercentage(100);
			cell = new PdfPCell(new Phrase("Signature du FSS leader :"));
			cell.setBorder(0);
			cell.setColspan(2);
			sign.addCell(cell);
			cell = new PdfPCell(new Phrase("Visa du CPO :"));
			cell.setBorder(0);
			sign.addCell(cell);
			doc.add(sign);
			
			par = new Phrase("\n\n\n* Rayer la mention inutile",new Font(Font.HELVETICA, 8));
			doc.add(par);
			
			doc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	/**
	 * creer le pdf pour le surbook du stage passé en parametre
	 * @param leStage
	 */
	public static void creationSurbook(Stage leStage){
		
		String date = leStage.getDateStr();
		date = date.replace("/", ".");
		String pathDossier = "Surbook du "+date;
		new File("dataExport\\"+pathDossier).mkdir();
		
		try {
			FileOutputStream fichier;
			fichier = new FileOutputStream("dataExport\\"+pathDossier+"\\Surbook - "+leStage.getCode()+".pdf");
			Document doc = new Document(PageSize.A4,40,40,20,20);
			PdfWriter.getInstance(doc, fichier);
			doc.open();
			
			PdfPTable header = new PdfPTable(3);
			header.setWidthPercentage(95);
			Phrase phrs = new Phrase("Direction Générale de la Qualité et des Opérations\n" +
									"Centre de Formation du PN\n" +
									"Formation Sécurité Sauvetage du PN"
									,new Font(Font.HELVETICA, 12, Font.BOLD));
			PdfPCell cell = new PdfPCell(phrs);
			cell.setBorder(0);
			cell.setColspan(2);
			header.addCell(cell);
			Image img = Image.getInstance("dataSystem\\AirfranceKLM.jpg");
			cell = new PdfPCell(img);
			cell.setBorder(0);
			header.addCell(cell);
			
			doc.add(header);
			
			doc.add(new Phrase("\n"));
			
			PdfPTable title = new PdfPTable(1);
			title.setWidthPercentage(75);
			
			phrs = new Phrase(leStage.getDateStr(),new Font(Font.HELVETICA, 15, Font.BOLD));
			cell = new PdfPCell(phrs);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorder(0);
			title.addCell(cell);
			phrs = new Phrase(leStage.getCode(),new Font(Font.HELVETICA, 15, Font.BOLD));
			cell = new PdfPCell(phrs);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorder(0);
			title.addCell(cell);
			phrs = new Phrase(leStage.getLibelle(),new Font(Font.HELVETICA,15,Font.BOLD));
			cell = new PdfPCell(phrs);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorder(0);
			title.addCell(cell);
			phrs = new Phrase(" ");
			cell = new PdfPCell(phrs);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorder(0);
			title.addCell(cell);
			phrs = new Phrase("- Stagiaire à l'heure refusé cause surbook -",new Font(Font.HELVETICA,18,Font.BOLD));
			cell = new PdfPCell(phrs);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			title.addCell(cell);
			
			doc.add(title);
			
			phrs = new Phrase("\n\nNom du stagiaire : _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _\n"
								,new Font(Font.HELVETICA,15));
			doc.add(phrs);
			
			phrs = new Phrase("matricule : _   _   _   _   _   _   _   _\n\n"
								,new Font(Font.HELVETICA, 15));
			doc.add(phrs);
			
			phrs = new Phrase("Madame, Monsieur,\n\n\n" +
					"Les programmes d'instruction déposés à la DGAC prévoient un nombre maximum de stagiaires admissibles" +
					"en stage ( 16 sur les stages de spécialisation, 18 sur les stages de maintien des compétences).\n\n" +
					"Afin d'optimiser l'utilisation des moyens d'instruction, les services dits de production" +
					"PNC sont amenés à gréer des stages avec surbook.\n\n" +
					"Le nombre de stagiaires présents aujourd'hui sur le vôtre a conduit votre formateur à en limiter l'accés." +
					"Il n'existe en effet aucune tolérance sur le nombre maximum de stagiaires admissibles.\n\n" +
					"le choix de la personne invitée à se faire inscrire sur un autre stage se fait :"
					,new Font(Font.HELVETICA,12));
			doc.add(phrs);
			
			List list = new List(false);
			list.add(new ListItem(new Phrase("conformément à l'interligne 04-079 du 4 octobre 2004",new Font(Font.HELVETICA,12))));
			list.add(new ListItem(new Phrase("selon les indications des services de produstion PNC",new Font(Font.HELVETICA,12))));
			doc.add(list);
			
			phrs = new Phrase("\nNous vous remercions de votre compréhension et votre prions de bien vouloir prendre contact" +
					"avec la permanence opérationnelle située Salle 003 au RDC du BEPN afin de faire enregistrer votre présence et" +
					"vous rendre ensuite au suivi planning muni du présent document.\n\n\n"
					,new Font(Font.HELVETICA,12));
			doc.add(phrs);
			
			PdfPTable footer = new PdfPTable(2);
			footer.setWidthPercentage(100);
			cell = new PdfPCell(new Phrase("Signature du FSS :",new Font(Font.HELVETICA, 13)));
			cell.setBorder(0);
			footer.addCell(cell);
			cell = new PdfPCell(new Phrase("Visa du CPO :",new Font(Font.HELVETICA, 13)));
			cell.setBorder(0);
			footer.addCell(cell);
			doc.add(footer);
			
			doc.close();
			
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
			
	}//fin creationSurbook(

	/**
	 * creer le pdf pour la checkListe pour le pôle Administratif
	 * @param date
	 */
	public static void creationCheckListAdm(String ladate, ArrayList<Stage> stageList){
		
		String date = ladate.replace("/", ".");
		String pathDossier = "CheckListPoleAdm du "+date;
		new File("dataExport\\"+pathDossier).mkdir();
		
		//tri des stages par ordre alphabetique
		Stage stgTemps ;
		boolean good = false;
		//tant que le tri n'est pas bon
		while (! good) {
			good = true;
			for (int i = 0; i < stageList.size()-1; i++) {
				if(stageList.get(i).getCode().compareToIgnoreCase(stageList.get(i+1).getCode()) > 0){
					good = false;
					//echange
					stgTemps = stageList.get(i);
					stageList.set(i, stageList.get(i+1));
					stageList.set(i+1, stgTemps);
				}//finsi
			}//finpour
		}//fin tant que
		
		try {
			FileOutputStream fichier;
			fichier = new FileOutputStream("dataExport\\"+pathDossier+"\\CheckListPôleAdm - "+date+".pdf");
			Document doc = new Document(PageSize.A4,20,20,10,10);
			doc.setPageSize(PageSize.A4.rotate());
			doc.newPage();
			PdfWriter.getInstance(doc, fichier);
			doc.open();
			
			PdfPTable header = new PdfPTable(2);
			header.setWidthPercentage(100);
			
			Phrase phrs = new Phrase("RETOUR Listes Stagiaires et Emargements", new Font(Font.HELVETICA, 15, Font.BOLD));
			PdfPCell cell = new PdfPCell(phrs);
			cell.setBorder(0);
			header.addCell(cell);
			phrs = new Phrase(ladate, new Font(Font.HELVETICA, 15, Font.BOLD));
			cell = new PdfPCell(phrs);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setBorder(0);
			header.addCell(cell);
			
			doc.add(header);
			doc.add(new Phrase("\n"));
			
			Font fonth1 = new Font(Font.HELVETICA, 15, Font.BOLD);
			Font fonth2 = new Font(Font.HELVETICA, 12, Font.BOLD);
			Font fonth3 = new Font(Font.HELVETICA, 9, Font.BOLD);
			
			BaseColor gray = new BaseColor(175, 175, 175);
			BaseColor lightGray = new BaseColor(210, 210, 210);
			
			PdfPTable tableau = new PdfPTable(14);
			tableau.setWidthPercentage(100);
			
			cell = new PdfPCell();
			cell.setColspan(3);
			cell.setBorder(0);
			tableau.addCell(cell);
			
			phrs = new Phrase("ABSENCES",fonth1);
			cell = new PdfPCell(phrs);
			cell.setBackgroundColor(gray);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setColspan(3);
			tableau.addCell(cell);
			
			phrs = new Phrase("TRAITEMENT AF",fonth1);
			cell = new PdfPCell(phrs);
			cell.setBackgroundColor(gray);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setColspan(2);
			tableau.addCell(cell);
			
			phrs = new Phrase("TRAITEMENT TIERS",fonth1);
			cell = new PdfPCell(phrs);
			cell.setBackgroundColor(gray);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setColspan(2);
			tableau.addCell(cell);
			
			phrs = new Phrase("TRAITEMENT DELIA",fonth1);
			cell = new PdfPCell(phrs);
			cell.setBackgroundColor(gray);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setColspan(2);
			tableau.addCell(cell);
			
			phrs = new Phrase("FREP",fonth1);
			cell = new PdfPCell(phrs);
			cell.setBackgroundColor(gray);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setRowspan(2);
			tableau.addCell(cell);
			
			phrs = new Phrase("STATS",fonth1);
			cell = new PdfPCell(phrs);
			cell.setBackgroundColor(gray);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setRowspan(2);
			tableau.addCell(cell);
			
			//deuxieme ligne
			
			cell = new PdfPCell();
			cell.setColspan(3);
			cell.setBorder(0);
			tableau.addCell(cell);
			
			phrs = new Phrase("ABS",fonth3);
			cell = new PdfPCell(phrs);
			cell.setBackgroundColor(lightGray);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			tableau.addCell(cell);
			
			phrs = new Phrase("R/Prod",fonth3);
			cell = new PdfPCell(phrs);
			cell.setBackgroundColor(lightGray);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			tableau.addCell(cell);
			
			phrs = new Phrase("Débarqués",fonth3);
			cell = new PdfPCell(phrs);
			cell.setBackgroundColor(lightGray);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			tableau.addCell(cell);
			
			phrs = new Phrase("ITRI",fonth3);
			cell = new PdfPCell(phrs);
			cell.setBackgroundColor(lightGray);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			tableau.addCell(cell);
			
			phrs = new Phrase("SCAN",fonth3);
			cell = new PdfPCell(phrs);
			cell.setBackgroundColor(lightGray);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			tableau.addCell(cell);
			
			phrs = new Phrase("Attestations",fonth3);
			cell = new PdfPCell(phrs);
			cell.setBackgroundColor(lightGray);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			tableau.addCell(cell);
			
			phrs = new Phrase("SCAN",fonth3);
			cell = new PdfPCell(phrs);
			cell.setBackgroundColor(lightGray);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			tableau.addCell(cell);
			
			phrs = new Phrase("PREVUS",fonth3);;
			cell = new PdfPCell(phrs);
			cell.setBackgroundColor(lightGray);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			tableau.addCell(cell);
			
			phrs = new Phrase("PRESENTS",fonth3);
			cell = new PdfPCell(phrs);
			cell.setBackgroundColor(lightGray);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			tableau.addCell(cell);
			
			//lignes de stage
			int index = 0;
			for (Stage lestage : stageList) {
				//premiere cellule
				index++;
				phrs = new Phrase(lestage.getCode(),fonth2);
				cell = new PdfPCell(phrs);
				cell.setColspan(3);
				cell.setFixedHeight(480 / stageList.size());
				cell.setBackgroundColor(gray);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				tableau.addCell(cell);
				//autres cellules
				for (int i = 3; i < tableau.getNumberOfColumns(); i++) {
					phrs = new Phrase("");
					cell = new PdfPCell(phrs);
					if ((index % 2 ) == 0) {
						cell.setBackgroundColor(lightGray);
					}
					tableau.addCell(cell);
				}//fin pour
			}//fin pour chaque
			
			doc.add(tableau);
			
			//frmeture du document
			doc.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}//fin creationCheckListAdm()
	
}//fin class
