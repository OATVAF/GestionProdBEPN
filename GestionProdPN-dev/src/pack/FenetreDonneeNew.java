package pack;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
import java.util.ArrayList;

import javax.swing.ListSelectionModel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableColumnModel;

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
	private JButton modifBtn;
	private JButton removeBtn;
	private JButton stagesBtn;
	private JButton addStBtn;
	private JButton remStBtn;
	
	//attributs qui vont contenir les données temporaires
	//private ArrayList<Stage> stageList;//liste des stages
	private JScrollPane scrollPane;
	private JTable tableStages;
	private JLabel lbl_1;
	
	private ModelStages ms;
	private ModelStagiaires mss;
	private JPanel stagiairesPane;
	private JPanel btn1Pane;
	private JScrollPane scrollPane_1;
	private JTable tableStagiaires;
	private JTabbedPane tabbedPane;
	private JPanel infoPane;
	private JLabel lbl_2;
	private JLabel lblDate;
	private JLabel lbl_3;
	private JLabel lblStage;

	//-Dswing.defaultlaf=com.sun.java.swing.plaf.windows.WindowsLookAndFeel
	/**
	 * constructeur de la fenetre
	 */
	public FenetreDonneeNew(){
		
		//chargement des listes
		//chargerList();
		
		//formation de la fenetre (titre, taille, etc.)
		this.setTitle(Messages.getString("FenetreDonneeNew.Titre")); //$NON-NLS-1$
		this.setSize(800, 600);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
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
			        if (pane.getSelectedIndex() == 1) {
			        	Stage s = ms.getSelectedStage();
			        	if (s != null) {
			        		mss.setStage(s);
			        		lblStage.setText(s.getCode());
			        		lblDate.setText(ms.filterDate);
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
		tableStages.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
		modifBtn = new JButton(Messages.getString("FenetreDonneeNew.Save")); //$NON-NLS-1$
		modifBtn.setIcon(new ImageIcon(Messages.getString("FenetreDonneeNew.saveImg"))); //$NON-NLS-1$
		modifBtn.setPreferredSize(new Dimension(200, 35));
		modifBtn.addActionListener(this);
		btnPane.add(modifBtn);
		removeBtn = new JButton(Messages.getString("FenetreDonneeNew.Remove")); //$NON-NLS-1$
		removeBtn.setIcon(new ImageIcon(Messages.getString("FenetreDonneeNew.remImg"))); //$NON-NLS-1$
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
		tableStagiaires.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
		addStBtn.setIcon(new ImageIcon(Messages.getString("FenetreDonneeNew.saveImg"))); //$NON-NLS-1$
		addStBtn.setPreferredSize(new Dimension(200, 35));
		addStBtn.addActionListener(this);
		btnPane2.add(addStBtn);
		remStBtn = new JButton(Messages.getString("FenetreDonneeNew.Remove")); //$NON-NLS-1$
		remStBtn.setIcon(new ImageIcon(Messages.getString("FenetreDonneeNew.remImg"))); //$NON-NLS-1$
		remStBtn.setPreferredSize(new Dimension(200, 35));
		remStBtn.addActionListener(this);
		btnPane2.add(remStBtn);
		stagiairesPane.add(btnPane2, BorderLayout.SOUTH);

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
		
		//si le bouton est le bouton du choix de la date
		if (source.equals(addStBtn)) {
			mss.newStagiaire();
		}
		
		//si le bouton est le bouton du choix du stage
		if (source.equals(remStBtn)) {
			mss.remStagiaire();
		}

		//si le bouton est le bouton d'enregistrement
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
		
		//si le bouton est le bouton de l'affichage des stages
		if(source.equals(stagesBtn)){
			tabbedPane.setSelectedIndex(0);
		}

		//si le bouton est le bouton de l'affichage des stagiaires
		if(source.equals(stagiaireBtn)){
			tabbedPane.setSelectedIndex(1);
		}

		//si le bouton est le bouton de la suppression de stage
		if(source.equals(removeBtn)){
			//fenetre de dialogue
			Stage s = ms.getSelectedStage();
			int rep = JOptionPane.showConfirmDialog(null, "<html>Supprimer le stage "+s.getCode()+" du "+ms.filterDate+" ?<br>" +
					"les modifications seront definitives !</html>"
					,Messages.getString("FenetreDonneeNew.Mod"),JOptionPane.YES_NO_OPTION); //$NON-NLS-1$
			//si la reponse est "oui"
			if(rep == JOptionPane.YES_OPTION) {
				ms.removeStage(s);
				//ms.saveStages();
				//JOptionPane.showMessageDialog(null, "<html>Suppresion réussi !</html>", "Suppression", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		
	}//fin actionPerformed()
	
}//fin class
