package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import data.ModelStages;

import pack.PasserellePDF;
import pack.Stage;


/**
 * fenetre qui permet a l'utilisateur de générer les document pour les stages de J et J+1
 * @author BERON Jean-Sébastien
 *
 */
public class FenetreGénérationPDFNew extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = 7185100043755309312L;
	
	//atributs IHM
	private JPanel contentPane;//conteneur principal
	//private JPanel headerPane;//conteneur en haut de page
	private JPanel centerPane;//conteneur au centre de la fenetre
	private JPanel stagePane;
	private JPanel bluePane;//conteneur servant de décor
	private JPanel pdfPane;//conteneur contenant les formulaires pour la génération des documents
	
	//listes déroulantes
	private JPanel choixPane;
	private JComboBox dateBox;
	
	//composants des formulaires pour la génération des documents
	private JScrollPane scrollPane;
	private JTable tableStages;
	private JLabel lbl_1;
	private JButton listStagiaireBtn;
	private JButton allListStagiaireBtn;
	private JButton listEmargBtn;
	private JButton allListEmargBtn;
	private JButton frepBtn;
	private JButton surbookBtn;
	private JButton checkListBtn;
	private JButton fssBtn;
	
	////attributs qui vont contenir les données temporaires
	//private ArrayList<Stage> stageList;
	//private ArrayList<String> dateList;
	private String date;

	// Models
	private ModelStages ms;
	
	/**
	 * contructeur de la ffenetre
	 */
	public FenetreGénérationPDFNew(){
		
		//chargement des listes
		//chargerList();
		
		//formation de la fenetre
		this.setTitle(Messages.getString("FenetreGénérationPDFNew.Titre")); //$NON-NLS-1$
		this.setSize(700, 700);
		//this.setResizable(false);
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
	/*
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
	*/
	/**
	 * procedure qui construit le contentPane
	 */
	private void constructionContentPane() {
		
		//construction du contentPane
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		contentPane.setBackground(Color.WHITE);
				
		// Titre
		JLabel titreLabel = new JLabel();
		titreLabel.setText(Messages.getString("FenetreGénérationPDFNew.Titre")); //$NON-NLS-1$
		titreLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titreLabel.setFont(new Font("arial", 1, 35)); //$NON-NLS-1$
		contentPane.add(titreLabel, BorderLayout.PAGE_START);
				
		//construction du centerPane
		constructionCenterPane();
		contentPane.add(centerPane);
		
	}//fin constructionContentPane()

	/**
	 * procedure qui construit le centerPane
	 */
	private void constructionCenterPane() {
		
		// centerPane
		centerPane = new JPanel();
		centerPane.setBackground(Color.WHITE);
		centerPane.setLayout(new BorderLayout(0, 0));
		
		// bluePane
		bluePane = new JPanel();
		bluePane.setBackground(Color.BLUE);
		bluePane.add(new JLabel("               "));
		centerPane.add(bluePane,BorderLayout.WEST);
		
		// stagePane
		stagePane = new JPanel();
		stagePane.setLayout(new BorderLayout(0, 0));
		centerPane.add(stagePane,BorderLayout.CENTER);
		
		// choixPane
		choixPane = new JPanel();
		stagePane.add(choixPane, BorderLayout.NORTH);
		choixPane.setBackground(Color.WHITE);
		choixPane.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
		
		// Date
		dateBox = new JComboBox();
		dateBox.addActionListener(this);
		/*
		for (String laDate : dateList) {
			dateBox.addItem(laDate);
		}//finpour
		*/
		lbl_1 = new JLabel("Date :");
		choixPane.add(lbl_1);
		choixPane.add(dateBox);
		
		tableStages = new JTable();
		ms = new ModelStages(tableStages,ModelStages.MODE.CODE_ONLY);
		tableStages.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		scrollPane = new JScrollPane(tableStages);
		ms.selDate("1/1/1");
		dateBox.setModel(ms.dateModel);
		dateBox.setSelectedIndex(ms.dateModel.getSize()-1);
		scrollPane.setPreferredSize(new Dimension(300,200));
		tableStages.selectAll();
		stagePane.add(scrollPane, BorderLayout.WEST);
		
		
		constructionPdfPane();
		stagePane.add(pdfPane, BorderLayout.CENTER);
		
		// Séléction défaut
		//dateBox.setSelectedIndex(dateBox.getItemCount()-1);

	}//fin constructionCenterPane()
	
	/**
	 * construction du pdfPane
	 */
	private void constructionPdfPane(){
		
		pdfPane = new JPanel();
		pdfPane.setLayout(new GridLayout(7,1,20,20));
		pdfPane.setBackground(Color.WHITE);
		afficherPDFPane();
	}
	
	/**
	 * procedure qui remplit le conteneur pdfpane avec les informations du stage
	 */
	@SuppressWarnings("deprecation")
	public void afficherPDFPane(){
		
		//clear du Panel
		pdfPane.removeAll();
		
		//police des composants
		Font font = new Font("arial", 1, 12); //$NON-NLS-1$
		
		//titre : "document pour le JJ/MM/YYYY"
		/*
		JLabel titreLabel = new JLabel(Messages.getString("FenetreGénérationPDFNew.Titre2")+date); //$NON-NLS-1$
		titreLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titreLabel.setFont(font);
		pdfPane.add(titreLabel);
		 */
		
		//conteneur pour la génération des listes de stagiaires
		JPanel listStagiairePane = new JPanel();
		listStagiairePane.setBackground(Color.WHITE);
		JLabel listStagiaireLabel = new JLabel(Messages.getString("FenetreGénérationPDFNew.ListeStg")); //$NON-NLS-1$
		listStagiaireLabel.setHorizontalAlignment(SwingConstants.CENTER);
		listStagiaireLabel.setFont(font);
		listStagiaireLabel.setPreferredSize(new Dimension(300, 15));
		listStagiairePane.add(listStagiaireLabel);
		
		allListStagiaireBtn = new JButton(Messages.getString("FenetreGénérationPDFNew.DoAllStg")); //$NON-NLS-1$
		allListStagiaireBtn.addActionListener(this);
		allListStagiaireBtn.setPreferredSize(new Dimension(300, 20));
		listStagiairePane.add(allListStagiaireBtn);
		
		listStagiaireBtn = new JButton(Messages.getString("FenetreGénérationPDFNew.DoSelStg")); //$NON-NLS-1$
		listStagiaireBtn.addActionListener(this);
		listStagiaireBtn.setPreferredSize(new Dimension(300, 20));
		listStagiairePane.add(listStagiaireBtn);
		
		pdfPane.add(listStagiairePane);
		
		//conteneur pour la génération des listes d'Emargement
		JPanel listEmargementPane = new JPanel();
		listEmargementPane.setBackground(Color.WHITE);
		
		JLabel listEmargementLabel = new JLabel(Messages.getString("FenetreGénérationPDFNew.ListeEm")); //$NON-NLS-1$
		listEmargementLabel.setHorizontalAlignment(SwingConstants.CENTER);
		listEmargementLabel.setFont(font);
		listEmargementLabel.setPreferredSize(new Dimension(300, 15));
		listEmargementPane.add(listEmargementLabel);
		 
		allListEmargBtn = new JButton(Messages.getString("FenetreGénérationPDFNew.DoAllStg")); //$NON-NLS-1$
		allListEmargBtn.addActionListener(this);
		allListEmargBtn.setPreferredSize(new Dimension(300, 20));
		listEmargementPane.add(allListEmargBtn);
		
		listEmargBtn = new JButton(Messages.getString("FenetreGénérationPDFNew.DoSelStg")); //$NON-NLS-1$
		listEmargBtn.addActionListener(this);
		listEmargBtn.setPreferredSize(new Dimension(300, 20));
		listEmargementPane.add(listEmargBtn);
		pdfPane.add(listEmargementPane);
		
		//conteneur pour la génération des surbook
		JPanel surbookPane = new JPanel();
		surbookPane.setBackground(Color.WHITE);
		
		JLabel surbookLabel = new JLabel(Messages.getString("FenetreGénérationPDFNew.Surbook")); //$NON-NLS-1$
		surbookLabel.setHorizontalAlignment(SwingConstants.CENTER);
		surbookLabel.setPreferredSize(new Dimension(200, 15));
		surbookLabel.setFont(font);
		surbookPane.add(surbookLabel);
		
		surbookBtn = new JButton(Messages.getString("FenetreGénérationPDFNew.DoSurbook")); //$NON-NLS-1$
		surbookBtn.addActionListener(this);
		surbookBtn.setPreferredSize(new Dimension(200, 20));
		surbookPane.add(surbookBtn);
		pdfPane.add(surbookPane);
		
		//conteneur pour la génération des FREP
		JPanel frepPane = new JPanel();
		frepPane.setBackground(Color.WHITE);
		JLabel frepLabel = new JLabel(Messages.getString("FenetreGénérationPDFNew.FREP")); //$NON-NLS-1$
		frepLabel.setHorizontalAlignment(SwingConstants.CENTER);
		frepLabel.setFont(font);
		frepLabel.setPreferredSize(new Dimension(300, 15));
		frepPane.add(frepLabel);
		
		frepBtn = new JButton(Messages.getString("FenetreGénérationPDFNew.DoAllFREP")); //$NON-NLS-1$
		frepBtn.addActionListener(this);
		frepBtn.setPreferredSize(new Dimension(300, 25));
		frepPane.add(frepBtn);
		pdfPane.add(frepPane);
		
		//conteneur pour la génération de la checkList
		JPanel checkListPane = new JPanel();
		checkListPane.setBackground(Color.WHITE);
		JLabel checkListLabel = new JLabel(Messages.getString("FenetreGénérationPDFNew.CL_Adm")); //$NON-NLS-1$
		checkListLabel.setHorizontalAlignment(SwingConstants.CENTER);
		checkListLabel.setPreferredSize(new Dimension(300, 15));
		checkListLabel.setFont(font);
		checkListPane.add(checkListLabel);
		
		checkListBtn = new JButton(Messages.getString("FenetreGénérationPDFNew.DoCL")); //$NON-NLS-1$
		checkListBtn.addActionListener(this);
		checkListBtn.setPreferredSize(new Dimension(300, 25));
		checkListPane.add(checkListBtn);
		pdfPane.add(checkListPane);
		
		//conteneur pour la génération des feuilles de routes
		JPanel fssPane = new JPanel();
		fssPane.setBackground(Color.WHITE);
		JLabel fssLabel = new JLabel(Messages.getString("FenetreGénérationPDFNew.F_Route_FSS")); //$NON-NLS-1$
		fssLabel.setHorizontalAlignment(SwingConstants.CENTER);
		fssLabel.setPreferredSize(new Dimension(300, 15));
		fssLabel.setFont(font);
		fssPane.add(fssLabel);
		fssBtn = new JButton(Messages.getString("FenetreGénérationPDFNew.DoAllFSS")); //$NON-NLS-1$
		fssBtn.addActionListener(this);
		fssBtn.setPreferredSize(new Dimension(300, 25));
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
			ms.selDate((String) dateBox.getSelectedItem());
			/*
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
			*/
		}
		
		//si le bouton est le bouton de la génération d'un liste de stagiaires
		if(source.equals(listStagiaireBtn)){
			for (Stage leStage :  ms.getSelectedStages()) {
				PasserellePDF.creationListeStagiaire(leStage);
				PasserellePDF.creationAffichageSalle(leStage);
			}
			//fenetre de dialogue
			JOptionPane.showMessageDialog(null, Messages.getString("FenetreGénérationPDFNew.OpDone"), Messages.getString("FenetreGénérationPDFNew.Create"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
		}//finsi
		
		//si le bouton est le bouton de la génération d'un liste d'emargement
		if(source.equals(listEmargBtn)){
			for (Stage leStage :  ms.getSelectedStages()) {
				if(leStage.getSizeStagiaireList() != 0){//si il y a des stagiaires
					PasserellePDF.creationListeEmargement(leStage);
				}else{
					PasserellePDF.creationListeEmargementVide(leStage);
				}
			}
			//fenetre de dialogue
			JOptionPane.showMessageDialog(null, Messages.getString("FenetreGénérationPDFNew.OpDone"), Messages.getString("FenetreGénérationPDFNew.Create"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
		}//finsi
		
		//si le bouton est le bouton de la génération de toutes les listes de stagiaires
		if(source.equals(allListStagiaireBtn)){
			for (Stage leStage : ms.getStagesInList()) {
				PasserellePDF.creationListeStagiaire(leStage);
				PasserellePDF.creationAffichageSalle(leStage);
			}//finpour
			//fenetre de dialogue
			JOptionPane.showMessageDialog(null, Messages.getString("FenetreGénérationPDFNew.OpDone"), Messages.getString("FenetreGénérationPDFNew.Create"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
		}//finsi
		
		//si le bouton est le bouton de la génération de toutes les listes d'emargement
		if(source.equals(allListEmargBtn)){
			for (Stage leStage : ms.getStagesInList()) {
				if(leStage.getSizeStagiaireList() != 0){//si il y a des stagiaires
					PasserellePDF.creationListeEmargement(leStage);
				}else{
					PasserellePDF.creationListeEmargementVide(leStage);
				}
			}//finpour
			//fenetre de dialogue
			JOptionPane.showMessageDialog(null, Messages.getString("FenetreGénérationPDFNew.OpDone"), Messages.getString("FenetreGénérationPDFNew.Create"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
		}//finsi
		
		//si le bouton est le bouton de la génération d'une FREP
		if(source.equals(frepBtn)){
			for (Stage leStage : ms.getStagesInList()) {
				PasserellePDF.creationFREP(leStage);
			}
			JOptionPane.showMessageDialog(null, Messages.getString("FenetreGénérationPDFNew.OpDone"), Messages.getString("FenetreGénérationPDFNew.Create"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
		}//finsi
		
		//si le bouton est le bouton de la génération d'un surbook
		if(source.equals(surbookBtn)){
			for (Stage leStage : ms.getSelectedStages()) {
				PasserellePDF.creationSurbook(leStage);
			}
			JOptionPane.showMessageDialog(null, Messages.getString("FenetreGénérationPDFNew.OpDone"), Messages.getString("FenetreGénérationPDFNew.Create"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
		}//finsi
		
		//si le bouton est le bouton de la génération dde la checkList
		if(source.equals(checkListBtn)){
			PasserellePDF.creationCheckListAdm(date,ms.getStagesInList());
			JOptionPane.showMessageDialog(null, Messages.getString("FenetreGénérationPDFNew.OpDone"), Messages.getString("FenetreGénérationPDFNew.Create"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
		}//finsi
		
		//si le bouton est le bouton pour les feuilles de routes
		if(source.equals(fssBtn)){
			PasserellePDF.creationAllFRFSS(ms.getStagesInList());
			JOptionPane.showMessageDialog(null, Messages.getString("FenetreGénérationPDFNew.OpDone"), Messages.getString("FenetreGénérationPDFNew.Create"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
	}//fin actionPerformed()
	
}//fin class
