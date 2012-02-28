package pack;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * fenetre qui permet a l'utilisateur de générer les document pour les stages de J et J+1
 * @author BERON Jean-Sébastien
 *
 */
public class FenetreGenerationPDF extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = 7185100043755309312L;
	
	//atributs IHM
	private JPanel contentPane;//conteneur principal
	private JPanel headerPane;//conteneur en haut de page
	private JPanel centerPane;//conteneur au centre de la fenetre
	private JPanel bluePane;//conteneur servant de décor
	private JPanel pdfPane;//conteneur contenant les formulaires pour la génération des documents
	
	//listes déroulantes
	private JComboBox dateBox;
	
	//composants des formulaires pour la génération des documents
	private JComboBox stageBox1;
	private JButton listStagiaireBtn;
	private JButton allListStagiaireBtn;
	private JComboBox stageBox2;
	private JButton listEmargBtn;
	private JButton allListEmargBtn;
	private JButton frepBtn;
	private JComboBox stageBox4;
	private JButton surbookBtn;
	private JButton checkListBtn;
	private JButton fssBtn;
	
	////attributs qui vont contenir les données temporaires
	private ArrayList<Stage> stageList;
	private ArrayList<String> dateList;
	private String date;

	/**
	 * contructeur de la ffenetre
	 */
	public FenetreGenerationPDF(){
		
		//chargement des listes
		chargerList();
		
		//formation de la fenetre
		this.setTitle("Génération des documents PDF");
		this.setSize(700, 700);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		//construction du contentPane
		constructionContentPane();
		
		//visibilité de la fenetre
		this.setContentPane(contentPane);
		this.setVisible(true);
		
	}//fin FenetreGénérationPDF()
	
	/**
	 * procedure qui charge les differents objet temporaires pour les modifications
	 */
	private void chargerList(){
		
		//chargement des stages J et J+1
		stageList = PasserelleStage.lectureStageObj();
		dateList = new ArrayList<String>();
		//remplissage de la liste des dates de stages
		for (Stage leStage : stageList) {
			if(! dateList.contains(leStage.getDateStr())){
				dateList.add(leStage.getDateStr());
			}//finsi
		}//finpour
		
	}//fin chargerList()

	/**
	 * procedure qui construit le contentPane
	 */
	private void constructionContentPane() {
		
		//construction du contentPane
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setLayout(new BorderLayout());
		
		//headerPane
		headerPane = new JPanel();
		headerPane.setBackground(Color.WHITE);
		
		//titreLabel
		JLabel titreLabel = new JLabel();
		titreLabel.setText("Génération des Documents");
		titreLabel.setFont(new Font("arial", 1, 35));
		headerPane.add(titreLabel);
		
		contentPane.add(headerPane, BorderLayout.PAGE_START);
		
		//construction du centerPane
		constructionCenterPane();
		contentPane.add(centerPane, BorderLayout.CENTER);
		
	}//fin constructionContentPane()

	/**
	 * procedure qui construit le centerPane
	 */
	private void constructionCenterPane() {
		
		//centerPane
		centerPane = new JPanel();
		centerPane.setBackground(Color.WHITE);
		centerPane.setLayout(new BorderLayout());
		
		//bluePane
		bluePane = new JPanel();
		bluePane.setBackground(Color.BLUE);
		JLabel bluelabel = new JLabel();
		bluelabel.setPreferredSize(new Dimension(50, 25));
		bluePane.add(bluelabel);
		centerPane.add(bluePane,BorderLayout.WEST);
		
		//choixPane
		JPanel choixPane = new JPanel();
		choixPane.setBackground(Color.WHITE);
		choixPane.setLayout(new GridLayout(1,2,10,10));
		dateBox = new JComboBox();
		for (String laDate : dateList) {
			dateBox.addItem(laDate);
		}//finpour
		choixPane.add(dateBox);
		dateBox.addActionListener(this);
		centerPane.add(choixPane, BorderLayout.NORTH);
		
		constructionPdfPane();
		centerPane.add(pdfPane, BorderLayout.CENTER);
		
	}//fin constructionCenterPane()
	
	/**
	 * construction du pdfPane
	 */
	private void constructionPdfPane(){
		
		pdfPane = new JPanel();
		pdfPane.setLayout(new GridLayout(7,1,20,20));
		pdfPane.setBackground(Color.WHITE);
		
	}
	
	/**
	 * procedure qui remplit le conteneur pdfpane avec les informations du stage
	 */
	@SuppressWarnings("deprecation")
	public void afficherPDFPane(){
		
		//clear du Panel
		pdfPane.removeAll();
		
		//police des composants
		Font font = new Font("arial", 1, 20);
		
		//titre : "document pour le JJ/MM/YYYY"
		JLabel titreLabel = new JLabel("Documents pour le "+date);
		titreLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titreLabel.setFont(font);
		pdfPane.add(titreLabel);
		
		//conteneur pour la génération des listes de stagiaires
		JPanel listStagiairePane = new JPanel();
		listStagiairePane.setBackground(Color.WHITE);
		JLabel listStagiaireLabel = new JLabel("Liste des Stagiaires");
		listStagiaireLabel.setHorizontalAlignment(SwingConstants.CENTER);
		listStagiaireLabel.setFont(font);
		listStagiaireLabel.setPreferredSize(new Dimension(300, 25));
		listStagiairePane.add(listStagiaireLabel);
		stageBox1 = new JComboBox();
		stageBox1.setPreferredSize(new Dimension(300, 25));
		listStagiairePane.add(stageBox1);
		allListStagiaireBtn = new JButton("Générer la liste de TOUS les stages");
		allListStagiaireBtn.addActionListener(this);
		allListStagiaireBtn.setPreferredSize(new Dimension(300, 25));
		listStagiairePane.add(allListStagiaireBtn);
		listStagiaireBtn = new JButton("Générer la liste pour le stage sélectionné");
		listStagiaireBtn.addActionListener(this);
		listStagiaireBtn.setPreferredSize(new Dimension(300, 25));
		listStagiairePane.add(listStagiaireBtn);
		pdfPane.add(listStagiairePane);
		
		//conteneur pour la génération des listes d'Emargement
		JPanel listEmargementPane = new JPanel();
		listEmargementPane.setBackground(Color.WHITE);
		JLabel listEmargementLabel = new JLabel("Liste d'Emargement");
		listEmargementLabel.setHorizontalAlignment(SwingConstants.CENTER);
		listEmargementLabel.setFont(font);
		listEmargementLabel.setPreferredSize(new Dimension(300, 25));
		listEmargementPane.add(listEmargementLabel);
		stageBox2 = new JComboBox();
		stageBox2.setPreferredSize(new Dimension(300, 25));
		listEmargementPane.add(stageBox2);
		allListEmargBtn = new JButton("Générer la liste de TOUS les stages");
		allListEmargBtn.addActionListener(this);
		allListEmargBtn.setPreferredSize(new Dimension(300, 25));
		listEmargementPane.add(allListEmargBtn);
		listEmargBtn = new JButton("Générer la liste pour le stage sélectionné");
		listEmargBtn.addActionListener(this);
		listEmargBtn.setPreferredSize(new Dimension(300, 25));
		listEmargementPane.add(listEmargBtn);
		pdfPane.add(listEmargementPane);
		
		//conteneur pour la génération des surbook
		JPanel surbookPane = new JPanel();
		surbookPane.setBackground(Color.WHITE);
		JLabel surbookLabel = new JLabel("Surbook");
		surbookLabel.setHorizontalAlignment(SwingConstants.CENTER);
		surbookLabel.setPreferredSize(new Dimension(150, 25));
		surbookLabel.setFont(font);
		surbookPane.add(surbookLabel);
		stageBox4 = new JComboBox();
		stageBox4.setPreferredSize(new Dimension(250, 25));
		surbookPane.add(stageBox4);
		surbookBtn = new JButton("<html>Générer le Surbook<br>pour le stage sélectionné</html>");
		surbookBtn.addActionListener(this);
		surbookBtn.setPreferredSize(new Dimension(200, 35));
		surbookPane.add(surbookBtn);
		pdfPane.add(surbookPane);
		
		//conteneur pour la génération des FREP
		JPanel frepPane = new JPanel();
		frepPane.setBackground(Color.WHITE);
		JLabel frepLabel = new JLabel("FREP");
		frepLabel.setHorizontalAlignment(SwingConstants.CENTER);
		frepLabel.setFont(font);
		frepLabel.setPreferredSize(new Dimension(300, 25));
		frepPane.add(frepLabel);
		frepBtn = new JButton("<html>Générer le FREP<br>pour TOUS les stages</html>");
		frepBtn.addActionListener(this);
		frepBtn.setPreferredSize(new Dimension(300, 35));
		frepPane.add(frepBtn);
		pdfPane.add(frepPane);
		
		//conteneur pour la génération de la checkList
		JPanel checkListPane = new JPanel();
		checkListPane.setBackground(Color.WHITE);
		JLabel checkListLabel = new JLabel("CheckList pour le pôle Admin");
		checkListLabel.setHorizontalAlignment(SwingConstants.CENTER);
		checkListLabel.setPreferredSize(new Dimension(300, 25));
		checkListLabel.setFont(font);
		checkListPane.add(checkListLabel);
		checkListBtn = new JButton("<html>Générer la checkList<br>pour le pôle Admin</html>");
		checkListBtn.addActionListener(this);
		checkListBtn.setPreferredSize(new Dimension(300, 35));
		checkListPane.add(checkListBtn);
		pdfPane.add(checkListPane);
		
		//conteneur pour la génération des feuilles de routes
		JPanel fssPane = new JPanel();
		fssPane.setBackground(Color.WHITE);
		JLabel fssLabel = new JLabel("Feuilles de routes FSS");
		fssLabel.setHorizontalAlignment(SwingConstants.CENTER);
		fssLabel.setPreferredSize(new Dimension(300, 25));
		fssLabel.setFont(font);
		fssPane.add(fssLabel);
		fssBtn = new JButton("<html>Générer TOUTES les feuilles<br>de routes FSS</html>");
		fssBtn.addActionListener(this);
		fssBtn.setPreferredSize(new Dimension(300, 35));
		fssPane.add(fssBtn);
		pdfPane.add(fssPane);
		
		//actualisation de la fenetre
		this.show();
		
	}//fin afficherPDFPane()

	/**
	 * procedure de l'interface ActionListener<br/>
	 * execute les modifications par rapport au bouton selectionné
	 */
	public void actionPerformed(ActionEvent e) {
		
		//recuperation de la source
		JComponent source = (JComponent) e.getSource();
		
		//si le bouton est le bouton du choix de la date
		if (source.equals(dateBox)) {
			date = (String) dateBox.getSelectedItem();
			afficherPDFPane();//affichage du pdfPane
			stageBox1.removeAllItems();
			stageBox2.removeAllItems();
			stageBox4.removeAllItems();
			//remplissage des listes déroulantes de stages
			for (Stage leStage : stageList) {
				if(leStage.getDateStr().equals(date)){
					stageBox1.addItem(leStage.getCode());
					stageBox2.addItem(leStage.getCode());
					stageBox4.addItem(leStage.getCode());
				}//finsi
			}//finpour
			
		}//finsi
		
		//si le bouton est le bouton de la génération d'un liste de stagiaires
		if(source.equals(listStagiaireBtn)){
			for (Stage leStage : stageList) {
				if(leStage.getDateStr().equals(date) && leStage.getCode().equals(stageBox1.getSelectedItem())){
					PasserellePDF.creationListeStagiaire(leStage);
					//fenetre de dialogue
					JOptionPane.showMessageDialog(null, "<html>Opération terminée !</html>", "Creation", JOptionPane.INFORMATION_MESSAGE);
					break;
				}
			}//finpour
		}//finsi
		
		//si le bouton est le bouton de la génération d'un liste d'emargement
		if(source.equals(listEmargBtn)){
			for (Stage leStage : stageList) {
				if(leStage.getDateStr().equals(date) && leStage.getCode().equals(stageBox2.getSelectedItem())){
					if(leStage.getSizeStagiaireList() != 0){//si il y a des stagiaires
						PasserellePDF.creationListeEmargement(leStage);
					}else{
						PasserellePDF.creationListeEmargementVide(leStage);
					}
					//fenetre de dialogue
					JOptionPane.showMessageDialog(null, "<html>Opération terminée !</html>", "Creation", JOptionPane.INFORMATION_MESSAGE);
					break;
				}
			}//finpour
		}//finsi
		
		//si le bouton est le bouton de la génération de toutes les listes de stagiaires
		if(source.equals(allListStagiaireBtn)){
			for (Stage leStage : stageList) {
				if(leStage.getDateStr().equals(date)){
					PasserellePDF.creationListeStagiaire(leStage);
				}
			}//finpour
			//fenetre de dialogue
			JOptionPane.showMessageDialog(null, "<html>Opération terminée !</html>", "Creation", JOptionPane.INFORMATION_MESSAGE);
		}//finsi
		
		//si le bouton est le bouton de la génération de toutes les listes d'emargement
		if(source.equals(allListEmargBtn)){
			for (Stage leStage : stageList) {
				if(leStage.getDateStr().equals(date)){
					if(leStage.getSizeStagiaireList() != 0){//si il y a des stagiaires
						PasserellePDF.creationListeEmargement(leStage);
					}else{
						PasserellePDF.creationListeEmargementVide(leStage);
					}
				}
			}//finpour
			//fenetre de dialogue
			JOptionPane.showMessageDialog(null, "<html>Opération terminée !</html>", "Creation", JOptionPane.INFORMATION_MESSAGE);
		}//finsi
		
		//si le bouton est le bouton de la génération d'une FREP
		if(source.equals(frepBtn)){
			for (Stage leStage : stageList) {
				if(leStage.getDateStr().equals(date)){
					PasserellePDF.creationFREP(leStage);
				}
			}//finpour
			JOptionPane.showMessageDialog(null, "<html>Opération terminée !</html>", "Creation", JOptionPane.INFORMATION_MESSAGE);
		}//finsi
		
		//si le bouton est le bouton de la génération d'un surbook
		if(source.equals(surbookBtn)){
			for (Stage leStage : stageList) {
				if(leStage.getDateStr().equals(date) && leStage.getCode().equals(stageBox4.getSelectedItem())){
					PasserellePDF.creationSurbook(leStage);
					JOptionPane.showMessageDialog(null, "<html>Opération terminée !</html>", "Creation", JOptionPane.INFORMATION_MESSAGE);
					break;
				}//finsi
			}//finpour
		}//finsi
		
		//si le bouton est le bouton de la génération dde la checkList
		if(source.equals(checkListBtn)){
			ArrayList<Stage> newStageList = new ArrayList<Stage>();
			for (Stage leStage : stageList) {
				if(leStage.getDateStr().equals(date)){
					newStageList.add(leStage);
				}//finsi
			}//finpour
			PasserellePDF.creationCheckListAdm(date,newStageList);
			JOptionPane.showMessageDialog(null, "<html>Opération terminée !</html>", "Creation", JOptionPane.INFORMATION_MESSAGE);
		}//finsi
		
		//si le bouton est le bouton pour les feuilles de routes
		if(source.equals(fssBtn)){
			ArrayList<Stage> newStageList = new ArrayList<Stage>();
			for (Stage leStage : stageList) {
				if(leStage.getDateStr().equals(date)){
					newStageList.add(leStage);
				}//finsi
			}//finpour
			PasserellePDF.creationAllFRFSS(newStageList);
			JOptionPane.showMessageDialog(null, "<html>Opération terminée !</html>", "Creation", JOptionPane.INFORMATION_MESSAGE);
		}
		
	}//fin actionPerformed()
	
}//fin class
