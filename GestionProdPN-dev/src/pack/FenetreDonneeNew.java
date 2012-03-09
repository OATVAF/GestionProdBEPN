package pack;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

import java.awt.FlowLayout;
import javax.swing.ListSelectionModel;
import javax.swing.JLayeredPane;

/**
 * Fenetre qui permet a l'utilisateur de modifier les données importées<br>
 * il pourra choisir la date et le stage afin d'en changer plusieurs données<br>
 * tel que le libelle, la salle, le leader, et des modificationsde stagiaires
 * @author BERON Jean-Sébastien
 *
 */
public class FenetreDonneeNew extends JFrame implements ActionListener{

	private static final long serialVersionUID = 3417928306187912598L;
	
	//attributs IHM
	private JPanel contentPane;//conteneur principal
	private JPanel centerPane;//conteneur au center de la fenetre
	private JPanel bluePane;//conteneur servant de décor
	
	private JPanel choixPane;// conteneur contenant les listes déroulantes
	private JComboBox dateBox;
	
	private JPanel stagePane;//conteneur  contenant le formulaire de modification
	private JTextField nomField;
	private JTextField libField;
	private JTextField salleField;
	private JTextField leaderField;
	private JButton stagiaireBtn;
	private JButton modifBtn;
	private JButton removeBtn;
	
	//attributs qui vont contenir les données temporaires
	//private ArrayList<Stage> stageList;//liste des stages
	private ArrayList<String> dateList;//listes des dates
	private String date;
	private String code;
	private JScrollPane scrollPane;
	private JTable table;
	private JLabel lblDate;
	
	private ModelStages ms;
	private JLayeredPane layeredPane;

	//-Dswing.defaultlaf=com.sun.java.swing.plaf.windows.WindowsLookAndFeel
	/**
	 * constructeur de la fenetre
	 */
	public FenetreDonneeNew(){
		
		//chargement des listes
		//chargerList();
		
		//formation de la fenetre (titre, taille, etc.)
		this.setTitle("modification des données");
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
		titreLabel.setText("Modification des Données");
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
		centerPane.setLayout(new BorderLayout());
		centerPane.setBackground(Color.WHITE);
		
		layeredPane = new JLayeredPane();
		centerPane.add(layeredPane, BorderLayout.NORTH);
		
		//construction du bluePane
		bluePane = new JPanel();
		bluePane.setBackground(Color.BLUE);
		bluePane.add(new JLabel("               "));
		centerPane.add(bluePane,BorderLayout.WEST);
		
		//construction du stagePane
		constructionStagePane();
		centerPane.add(stagePane, BorderLayout.CENTER);
		stagePane.setLayout(new BorderLayout(0, 0));
		
		//initialisation du choixPane
		choixPane = new JPanel();
		stagePane.add(choixPane, BorderLayout.NORTH);
		choixPane.setBackground(Color.WHITE);
		
		//initialisation et remplissage de dataBox : liste déroulante contenant les dates
		dateBox = new JComboBox();
		dateBox.addActionListener(this);
		choixPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		lblDate = new JLabel("Date :");
		choixPane.add(lblDate);
		choixPane.add(dateBox);
		
				
		ms = new ModelStages();
		table = new JTable(ms);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ms.setColWidth(table.getColumnModel());
		scrollPane = new JScrollPane(table);
		ms.selDate("1/1/1");
		dateBox.setModel(ms.dateModel);
		dateBox.setSelectedIndex(ms.dateModel.getSize()-1);
		
		//conteneur affichant les boutons de modifications
		JPanel btnPane = new JPanel();
		btnPane.setBackground(Color.WHITE);
		stagiaireBtn = new JButton("voir les stagiaires");
		stagiaireBtn.setPreferredSize(new Dimension(200, 35));
		stagiaireBtn.addActionListener(this);
		btnPane.add(stagiaireBtn);
		modifBtn = new JButton("<html>enregistrer les<br> modifications</html>");
		modifBtn.setIcon(new ImageIcon("dataSystem\\save.jpg"));
		modifBtn.setPreferredSize(new Dimension(200, 35));
		modifBtn.addActionListener(this);
		btnPane.add(modifBtn);
		removeBtn = new JButton("supprimer ce stage");
		removeBtn.setIcon(new ImageIcon("dataSystem\\remove.jpg"));
		removeBtn.setPreferredSize(new Dimension(200, 35));
		removeBtn.addActionListener(this);
		btnPane.add(removeBtn);
		stagePane.add(btnPane, BorderLayout.SOUTH);

		
		
		stagePane.add(scrollPane);
		
	}//fin constructionCenterPane()
	
	/**
	 * procedure qui construit le satgePane
	 */
	private void constructionStagePane(){
		
		//initialisation du stagePane
		stagePane = new JPanel();
		stagePane.setBackground(Color.WHITE);
		
	}//fin constructionStagePane()
	
	/**
	 * procedure qui remplit le conteneur stagePane avec les informations du stage
	 * @param leStage
	 */
	@SuppressWarnings("deprecation")
	public void afficherInfoStage(Stage leStage){
		/*
		//reinitialisation du stagePane
		stagePane.removeAll();
		
		//police du formulaire
		Font font = new Font("arial", 1, 16);
		
		//conteneur affichant le code et la date du stage
		JPanel codestagePane = new JPanel();
		codestagePane.setBackground(Color.WHITE);
		JLabel codeLabel = new JLabel("Stage :");
		codeLabel.setFont(font);
		codeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		codestagePane.add(codeLabel);
		JLabel codeStgLabel = new JLabel(leStage.getCode()+" du "+leStage.getDateStr());
		codeStgLabel.setFont(font);
		codestagePane.add(codeStgLabel);
		stagePane.add(codestagePane);

		//partie du formulaire pour le nom du stage
		JPanel nomPane = new JPanel();
		nomPane.setBackground(Color.WHITE);
		JLabel nomLabel = new JLabel("Nom du Stage :");
		nomLabel.setFont(font);
		nomLabel.setHorizontalAlignment(SwingConstants.CENTER);
		nomPane.add(nomLabel);
		nomField = new JTextField(leStage.getCodeI());
		nomField.setPreferredSize(new Dimension(250, 25));
		nomPane.add(nomField);
		stagePane.add(nomPane);

		//partie du formulaire pour le libelle du stage
		JPanel libPane = new JPanel();
		libPane.setBackground(Color.WHITE);
		JLabel libLabel = new JLabel("Libellé du Stage :");
		libLabel.setFont(font);
		libLabel.setHorizontalAlignment(SwingConstants.CENTER);
		libPane.add(libLabel);
		libField = new JTextField(leStage.getLibelle());
		libField.setPreferredSize(new Dimension(250, 25));
		libPane.add(libField);
		stagePane.add(libPane);
		
		//partie du formulaire pour la salle du stage
		JPanel sallePane = new JPanel();
		sallePane.setBackground(Color.WHITE);
		JLabel salleLabel = new JLabel("premiere salle du Stage :");
		salleLabel.setFont(font);
		salleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		sallePane.add(salleLabel);
		salleField = new JTextField(leStage.getFirstModule().getSalle());
		salleField.setPreferredSize(new Dimension(250, 25));
		sallePane.add(salleField);
		stagePane.add(sallePane);
		
		//partie du formulaire pour le leader du stage
		JPanel leaderPane = new JPanel();
		leaderPane.setBackground(Color.WHITE);
		JLabel leaderLabel = new JLabel("leader du premier module du Stage :");
		leaderLabel.setFont(font);
		leaderLabel.setHorizontalAlignment(SwingConstants.CENTER);
		leaderPane.add(leaderLabel);
		leaderField = new JTextField(leStage.getLeader());
		leaderField.setPreferredSize(new Dimension(250, 25));
		leaderPane.add(leaderField);
		stagePane.add(leaderPane);
		*/
		
		//conteneur affichant les boutons de modifications
		JPanel btnPane = new JPanel();
		btnPane.setBackground(Color.WHITE);
		stagiaireBtn = new JButton("voir les stagiaires");
		stagiaireBtn.setPreferredSize(new Dimension(200, 35));
		stagiaireBtn.addActionListener(this);
		btnPane.add(stagiaireBtn);
		modifBtn = new JButton("<html>enregistrer les<br> modifications</html>");
		modifBtn.setIcon(new ImageIcon("dataSystem\\save.jpg"));
		modifBtn.setPreferredSize(new Dimension(200, 35));
		modifBtn.addActionListener(this);
		btnPane.add(modifBtn);
		removeBtn = new JButton("supprimer ce stage");
		removeBtn.setIcon(new ImageIcon("dataSystem\\remove.jpg"));
		removeBtn.setPreferredSize(new Dimension(200, 35));
		removeBtn.addActionListener(this);
		btnPane.add(removeBtn);
		stagePane.add(btnPane);
		
		//actualisation de la fenetre
		this.show();
		
	}//fin afficherInfoStage()

	/**
	 * procedure de l'interface ActionListener<br/>
	 * execute les modifications par rapport au bouton selectionné
	 */
	public void actionPerformed(ActionEvent e) {
		
		//récupération de la source
		JComponent source = (JComponent) e.getSource();
		
		if (source.equals(dateBox)) {
			//stageBox.removeAllItems();
			date = (String) dateBox.getSelectedItem();
			ms.selDate(date);
		}
		
		/*
		//si le bouton est le bouton du choix de la date
		if (source.equals(vueBtn)) {
			ArrayList<Stage> vueStagelist = new ArrayList<Stage>();
			for (Stage leStage : stageList) {
				if(leStage.getDateStr().equals(date)){
					vueStagelist.add(leStage);
				}
			}//finpour
			if(! vueStagelist.isEmpty()){
				new FenetreStage(vueStagelist);
			}
		}//finsi
		
		//si le bouton est le bouton du choix du stage
		if (source.equals(stageBox)) {
			code = (String) stageBox.getSelectedItem();
			for (Stage leStage : stageList) {
				if(leStage.getDateStr().equals(date) && leStage.getCode().equals(code)){
					afficherInfoStage(leStage);
					break;
				}
			}//fin pour
		}//finsi
		
		//si le bouton est le bouton d'enregistrement
		if(source.equals(modifBtn)){
			//fenetre de dialogue
			int rep = JOptionPane.showConfirmDialog(null, "<html>enregistrer les modifications sur ce stage ?<br>" +
					"les modifications seront definitives !</html>"
					,"modification",JOptionPane.YES_NO_OPTION);
			//si la reponse est "oui"
			if(rep == JOptionPane.YES_OPTION){
				for (Stage leStage : stageList) {
					if(leStage.getDateStr().equals(date) && leStage.getCode().equals(code)){
						leStage.setCode(nomField.getText());
						leStage.setLibelle(libField.getText());
						leStage.getFirstModule().setSalle(salleField.getText());
						leStage.setLeader(leaderField.getText());
						break;
					}//finsi
				}//finpour
				PasserelleStage.ecritureStageObj(stageList);//ecriture des stages avec les modifs
				JOptionPane.showMessageDialog(null, "<html>Enregistrement réussi !</html>", "Enregistrement", JOptionPane.INFORMATION_MESSAGE);
			}//finsi
		}//finsi
		*/
		//si le bouton est le bouton de l'affichage des stagiaires
		if(source.equals(stagiaireBtn)){
			Stage s = ms.getSelectedStage(table.getSelectedRow());
			//afficherInfoStage(s);
			new FenetreStagiaire(s); //affichage de la fenetre
		}//finsi
		/*
		//si le bouton est le bouton de la suppression de stage
		if(source.equals(removeBtn)){
			//fenetre de dialogue
			int rep = JOptionPane.showConfirmDialog(null, "<html>Supprimer le stage "+code+" du "+date+" ?<br>" +
					"les modifications seront definitives !</html>"
					,"modification",JOptionPane.YES_NO_OPTION);
			//si la reponse est "oui"
			if(rep == JOptionPane.YES_OPTION){
				for (Stage leStage : stageList) {
					if(leStage.getDateStr().equals(date) && leStage.getCode().equals(code)){
						stageBox.removeItem(leStage.getCode());
						stageList.remove(leStage);
						PasserelleStage.ecritureStageObj(stageList);//ecriture des stages avec les modifs
						JOptionPane.showMessageDialog(null, "<html>Suppresion réussi !</html>", "Suppression", JOptionPane.INFORMATION_MESSAGE);
						break;
					}
				}//finpour
				code = (String) stageBox.getItemAt(0);
				for (Stage leStage : stageList) {
					if(leStage.getDateStr().equals(date) && leStage.getCode().equals(code)){
						afficherInfoStage(leStage);//affichage des infos du premier stage de la liste
						break;
					}
				}//finpour
			}//finsi
		}//finsi
		*/
		
	}//fin actionPerformed()
	
}//fin class
