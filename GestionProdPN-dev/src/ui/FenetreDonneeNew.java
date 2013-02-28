package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ChangeListener;

import java.awt.FlowLayout;

import javax.swing.ListSelectionModel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;

import pack.Stage;

import data.ModelModules;
import data.ModelStages;
import data.ModelStagiaires;
import pack.Config;


/**
 * Fenetre qui permet a l'utilisateur de modifier les données importées<br>
 * il pourra choisir la date et le stage afin d'en changer plusieurs données<br>
 * tel que le libelle, la salle, le leader, et des modificationsde stagiaires
 * @author BERON Jean-Sébastien
 *
 */
public class FenetreDonneeNew extends JFrame implements ActionListener {

	private static final long serialVersionUID = 3417928306187912598L;
	
	//attributs IHM
	private JPanel contentPane;//conteneur principal
	private JPanel centerPane;//conteneur au center de la fenetre
	private JPanel bluePane;//conteneur servant de décor
	
	private JPanel choixPane;// conteneur contenant les listes déroulantes
	private JComboBox dateBox;

	private JPanel stagePane;//conteneur  contenant le formulaire de modification
	private JButton stagiaireBtn;
	private JButton addBtn;
	private JButton removeBtn;
	private JButton stagesBtn;
	private JButton addStBtn;
	private JButton remStBtn;
	private JButton addMoBtn;
	private JButton remMoBtn;
	
	//attributs qui vont contenir les données temporaires
	//private ArrayList<Stage> stageList;//liste des stages
	private JScrollPane scrollPane;
	private JTable tableStages;
	private JLabel lbl_1;
	
	private ModelStages ms;
	private ModelStagiaires mss;
	private ModelModules msm;
	private JPanel stagiairesPane;
	private JPanel modulesPane;
	private JPanel btn1Pane;
	private JScrollPane scrollPane_1;
	private JTable tableStagiaires;
	private JTable tableModules;
	private JTabbedPane tabbedPane;
	private JPanel infoPane;
	private JLabel lbl_2;
	private JLabel lblDate,lblDate2;
	private JLabel lbl_3;
	private JLabel lblStage,lblStage2;
	
	//-Dswing.defaultlaf=com.sun.java.swing.plaf.windows.WindowsLookAndFeel
	/**
	 * constructeur de la fenetre
	 */
	public FenetreDonneeNew(){
		
		//chargement des listes
		//chargerList();
		
		//formation de la fenetre (titre, taille, etc.)
		this.setTitle(Messages.getString("FenetreDonneeNew.Titre")); //$NON-NLS-1$
		this.setSize(900, 600);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("Window Closing Event"); //$NON-NLS-1$
				if (ms.isMod() || mss.isMod() || msm.isMod()) {
					//fenetre de dialogue
					int rep = JOptionPane.showConfirmDialog(null, Messages.getString("FenetreDonneeNew.saveMsg") //$NON-NLS-1$
							,Messages.getString("FenetreDonneeNew.Mod"),JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE, new ImageIcon(Config.getRes("save.png"))); //$NON-NLS-1$
					//si la reponse est "oui"
					if(rep == JOptionPane.YES_OPTION) {
						ms.saveStages();
						JOptionPane.showMessageDialog(null, Messages.getString("FenetreDonneeNew.savedMsg"), Messages.getString("FenetreDonneeNew.saveTitle"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
					}

				}
			}
		});

		//construction du contentPane
		constructionContentPane();
		
		//visibilité de la fenetre
		this.setContentPane(contentPane);
		this.setVisible(true);
		
	}//fin FenetreDonnee()		
		
	/**
	 * procedure qui construit le contentPane
	 */
	private void constructionContentPane() {
		
		//initialisation du contentPane
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		contentPane.setBackground(Color.WHITE);
		
		//Formation du titre en haut de page
		JLabel titreLabel = new JLabel();
		titreLabel.setText(Messages.getString("FenetreDonneeNew.Titre")); //$NON-NLS-1$
		titreLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titreLabel.setFont(new Font("arial", 1, 35));
		contentPane.add(titreLabel, BorderLayout.PAGE_START);
		
		//construction du centerPane
		constructionCenterPane();
		contentPane.add(centerPane);
			
	}//fin constructionContentPane()

	/**
	 * procedure qui construit le centerPane
	 */
	private void constructionCenterPane() {
		
		
		//initialisation du centerPane
		centerPane = new JPanel();
		centerPane.setBackground(Color.WHITE);
		centerPane.setLayout(new BorderLayout(0, 0));
			
		//construction du bluePane
		bluePane = new JPanel();
		bluePane.setBackground(Color.BLUE);
		bluePane.add(new JLabel("               "));
		centerPane.add(bluePane, BorderLayout.WEST);
		
		tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		tabbedPane.addChangeListener(new ChangeListener() {
			    // This method is called whenever the selected tab changes
			    public void stateChanged(ChangeEvent evt) {
			        JTabbedPane pane = (JTabbedPane)evt.getSource();
			        // Get current tab
			        if (pane.getSelectedIndex() >= 1) {
			        	Stage s = ms.getSelectedStage();
			        	if (s != null) {
			        		mss.setStage(s);
			        		msm.setStage(s);
			        		lblStage.setText(s.getCode());
			        		lblDate.setText(ms.filterDate);
			        		lblStage2.setText(s.getCode());
			        		lblDate2.setText(ms.filterDate);
			        	}
			        }
			    }});
		centerPane.add(tabbedPane, BorderLayout.CENTER);
		
		//construction du stagePane
		stagePane = new JPanel();
		stagePane.setBackground(Color.WHITE);
		//centerPane.add(stagePane, BorderLayout.CENTER);
		tabbedPane.addTab(Messages.getString("FenetreDonneeNew.Stages"), null, stagePane, null); //$NON-NLS-1$
		stagePane.setLayout(new BorderLayout(0, 0));
		//tabbedPane.addChangeListener();
		
		//initialisation du choixPane
		choixPane = new JPanel();
		stagePane.add(choixPane, BorderLayout.NORTH);
		choixPane.setBackground(Color.WHITE);
		
		//initialisation et remplissage de dataBox : liste déroulante contenant les dates
		dateBox = new JComboBox();
		dateBox.addActionListener(this);
		choixPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		lbl_1 = new JLabel(Messages.getString("FenetreDonneeNew.Date")); //$NON-NLS-1$
		choixPane.add(lbl_1);
		choixPane.add(dateBox);
		
		tableStages = new JTable();
		ms = new ModelStages(tableStages);
		//tableStages.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableStages.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		scrollPane = new JScrollPane(tableStages);
		ms.selDate("1/1/1");
		dateBox.setModel(ms.dateModel);
		dateBox.setSelectedIndex(ms.dateModel.getSize()-1);
		stagePane.add(scrollPane, BorderLayout.CENTER);

		//conteneur affichant les boutons de modifications
		JPanel btnPane = new JPanel();
		btnPane.setBackground(Color.WHITE);
		stagiaireBtn = new JButton(Messages.getString("FenetreDonneeNew.ToStagiaires")); //$NON-NLS-1$
		stagiaireBtn.setPreferredSize(new Dimension(200, 35));
		stagiaireBtn.addActionListener(this);
		btnPane.add(stagiaireBtn);
		addBtn = new JButton(Messages.getString("FenetreDonneeNew.Add")); //$NON-NLS-1$
		addBtn.setIcon(new ImageIcon(Config.getRes("modif.png"))); //$NON-NLS-1$
		addBtn.setPreferredSize(new Dimension(200, 35));
		addBtn.addActionListener(this);
		btnPane.add(addBtn);
		removeBtn = new JButton(Messages.getString("FenetreDonneeNew.Remove")); //$NON-NLS-1$
		removeBtn.setIcon(new ImageIcon(Config.getRes("remove.png"))); //$NON-NLS-1$
		removeBtn.setPreferredSize(new Dimension(200, 35));
		removeBtn.addActionListener(this);
		btnPane.add(removeBtn);
		stagePane.add(btnPane, BorderLayout.SOUTH);

		// Stagiaires
		stagiairesPane = new JPanel();
		tabbedPane.addTab(Messages.getString("FenetreDonneeNew.Stagiaires"), null, stagiairesPane, null); //$NON-NLS-1$
		stagiairesPane.setLayout(new BorderLayout(0, 0));
		
		infoPane = new JPanel();
		stagiairesPane.add(infoPane, BorderLayout.NORTH);
		infoPane.setBackground(Color.WHITE);
		infoPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		lbl_2 = new JLabel(Messages.getString("FenetreDonneeNew.Date")); //$NON-NLS-1$
		infoPane.add(lbl_2);
		
		lblDate = new JLabel("1/2/3");
		infoPane.add(lblDate);
		
		lbl_3 = new JLabel(Messages.getString("FenetreDonneeNew.Stage")); //$NON-NLS-1$
		infoPane.add(lbl_3);
		
		lblStage = new JLabel("SMG XXX");
		infoPane.add(lblStage);

		tableStagiaires = new JTable();
		tableStagiaires.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		mss = new ModelStagiaires(tableStagiaires);
		scrollPane_1 = new JScrollPane(tableStagiaires);
		stagiairesPane.add(scrollPane_1, BorderLayout.CENTER);
		btn1Pane = new JPanel();
		stagiairesPane.add(btn1Pane, BorderLayout.SOUTH);

		JPanel btnPane2 = new JPanel();
		btnPane2.setBackground(Color.WHITE);
		stagesBtn = new JButton(Messages.getString("FenetreDonneeNew.ListeStages")); //$NON-NLS-1$
		stagesBtn.setPreferredSize(new Dimension(200, 35));
		stagesBtn.addActionListener(this);
		btnPane2.add(stagesBtn);
		addStBtn = new JButton(Messages.getString("FenetreDonneeNew.Add")); //$NON-NLS-1$
		addStBtn.setIcon(new ImageIcon(Config.getRes("modif.png"))); //$NON-NLS-1$
		addStBtn.setPreferredSize(new Dimension(200, 35));
		addStBtn.addActionListener(this);
		btnPane2.add(addStBtn);
		remStBtn = new JButton(Messages.getString("FenetreDonneeNew.Remove")); //$NON-NLS-1$
		remStBtn.setIcon(new ImageIcon(Config.getRes("remove.png"))); //$NON-NLS-1$
		remStBtn.setPreferredSize(new Dimension(200, 35));
		remStBtn.addActionListener(this);
		btnPane2.add(remStBtn);
		stagiairesPane.add(btnPane2, BorderLayout.SOUTH);

		
		// Modules
		modulesPane = new JPanel();
		tabbedPane.addTab(Messages.getString("FenetreDonneeNew.Modules"), null, modulesPane, null); //$NON-NLS-1$
		modulesPane.setLayout(new BorderLayout(0, 0));
		
		infoPane = new JPanel();
		modulesPane.add(infoPane, BorderLayout.NORTH);
		infoPane.setBackground(Color.WHITE);
		infoPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		lbl_2 = new JLabel(Messages.getString("FenetreDonneeNew.Date")); //$NON-NLS-1$
		infoPane.add(lbl_2);
		
		lblDate2 = new JLabel("1/2/3");
		infoPane.add(lblDate2);
		
		lbl_3 = new JLabel(Messages.getString("FenetreDonneeNew.Stage")); //$NON-NLS-1$
		infoPane.add(lbl_3);

		lblStage2 = new JLabel("SMG XXX");
		infoPane.add(lblStage2);
		
		tableModules = new JTable();
		tableModules.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		msm = new ModelModules(tableModules);
		scrollPane_1 = new JScrollPane(tableModules);
		modulesPane.add(scrollPane_1, BorderLayout.CENTER);
		btn1Pane = new JPanel();
		modulesPane.add(btn1Pane, BorderLayout.SOUTH);

		btnPane2 = new JPanel();
		btnPane2.setBackground(Color.WHITE);
		stagesBtn = new JButton(Messages.getString("FenetreDonneeNew.ListeStages")); //$NON-NLS-1$
		stagesBtn.setPreferredSize(new Dimension(200, 35));
		stagesBtn.addActionListener(this);
		btnPane2.add(stagesBtn);
		addMoBtn = new JButton(Messages.getString("FenetreDonneeNew.Add")); //$NON-NLS-1$
		addMoBtn.setIcon(new ImageIcon(Config.getRes("modif.png"))); //$NON-NLS-1$
		addMoBtn.setPreferredSize(new Dimension(200, 35));
		addMoBtn.addActionListener(this);
		btnPane2.add(addMoBtn);
		remMoBtn = new JButton(Messages.getString("FenetreDonneeNew.Remove")); //$NON-NLS-1$
		remMoBtn.setIcon(new ImageIcon(Config.getRes("remove.png"))); //$NON-NLS-1$
		remMoBtn.setPreferredSize(new Dimension(200, 35));
		remMoBtn.addActionListener(this);
		btnPane2.add(remMoBtn);
		modulesPane.add(btnPane2, BorderLayout.SOUTH);
		
	}//fin constructionCenterPane()
	
	/**
	 * procedure de l'interface ActionListener<br/>
	 * execute les modifications par rapport au bouton selectionné
	 */
	public void actionPerformed(ActionEvent e) {
		
		//récupération de la source
		JComponent source = (JComponent) e.getSource();
		
		if (source.equals(dateBox)) {
			ms.selDate((String) dateBox.getSelectedItem());
		}
		
		//si le bouton est le bouton add Stagiaire
		if (source.equals(addStBtn)) {
			mss.newStagiaire();
		}
		//si le bouton est le bouton add Module
		if (source.equals(addMoBtn)) {
			msm.newModule();
		}

		//si le bouton est le bouton du choix du stage
		if (source.equals(remStBtn)) {
			int rep = JOptionPane.NO_OPTION;
			//fenetre de dialogue
			rep = JOptionPane.showConfirmDialog(null, "<html>Supprimer les stagiaires sélectionnés du "+ms.filterDate+" ?</html>"
					,Messages.getString("FenetreDonneeNew.Mod"),JOptionPane.YES_NO_OPTION); //$NON-NLS-1$
			//si la reponse est "oui"
			if(rep == JOptionPane.YES_OPTION) {
				mss.removeSelectedStagiaires();
			}
		}

		//si le bouton est le bouton d'enregistrement
		/*
		if(source.equals(modifBtn)){
			//fenetre de dialogue
			int rep = JOptionPane.showConfirmDialog(null, Messages.getString("FenetreDonneeNew.saveMsg") + //$NON-NLS-1$
					"les modifications seront definitives !</html>" //$NON-NLS-1$
					,Messages.getString("FenetreDonneeNew.Mod"),JOptionPane.YES_NO_OPTION); //$NON-NLS-1$
			//si la reponse est "oui"
			if(rep == JOptionPane.YES_OPTION) {
				ms.saveStages();
				JOptionPane.showMessageDialog(null, Messages.getString("FenetreDonneeNew.savedMsg"), Messages.getString("FenetreDonneeNew.saveTitle"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		*/
		
		//si le bouton est le bouton de l'affichage des stages
		if(source.equals(stagesBtn)){
			tabbedPane.setSelectedIndex(0);
		}

		//si le bouton est le bouton de l'affichage des stagiaires
		if(source.equals(stagiaireBtn)){
			tabbedPane.setSelectedIndex(1);
		}

		//si le bouton est le bouton add Stage
		if (source.equals(addBtn)) {
			ms.newStage();
		}


		//si le bouton est le bouton de la suppression de stage
		if(source.equals(removeBtn)){
			int rep = JOptionPane.NO_OPTION;
			//fenetre de dialogue
			rep = JOptionPane.showConfirmDialog(null, "<html>Supprimer les stages sélectionnés du "+ms.filterDate+" ?"
					,Messages.getString("FenetreDonneeNew.Mod"),JOptionPane.YES_NO_OPTION); //$NON-NLS-1$
			//si la reponse est "oui"
			if(rep == JOptionPane.YES_OPTION) {
				ms.removeSelectedStages();
				//JOptionPane.showMessageDialog(null, "<html>Suppresion réussi !</html>", "Suppression", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		
	}//fin actionPerformed()
	
}//fin class
