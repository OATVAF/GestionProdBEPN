package pack;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;
import javax.swing.Box;
	
/**
 * c'est la Fenetre Principale de l'application<br>
 * l'utilisateur pourra choisir entre plusieurs boutons<br>
 * ammenant sur les differentes fonctionalités de l'application
 * @author BERON Jean-Sébastien
 *
 */
public class FenetrePrincipale extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = -878725418024527002L;
	
	//attributs JPanel
	private JPanel contentPane;//conteneur principal
	private JPanel headerPane;//conteneur en haut de page
	private JPanel centerPane;//conteneur au center de la fenetre
	private JPanel bluePane;//conteneur servant de decor
	private JPanel prodPane;
	private JPanel pdfPane;
	
	//attributs JLabel
	private JLabel logoLabel;
	private JLabel titleLabel;
	
	//attributs JButton
	private JButton tvAffichageBtn;//bouton du téléAffichage
	private JButton pptAffichageBtn;//bouton du lancement duu powerpoint
	private JButton modifAffichageBtn;//bouton des parametres d'affichage
	private JButton importBtn;//bouton importation DELIA et BO
	private JButton modifBtn;//bouton de modification des données
	private JButton pdfBtn;//bouton de génération des documents
	private JButton majSmsBtn;//bouton pour la liste des sms
	
	//police des boutons
	private Font btnFont;
	private JLabel versLabel;
	private JTabbedPane tabbedPane;
	private JPanel dispPanel;
	private JPanel dataPanel;
	private JPanel statPanel;
	
	/**
	 * constructeur
	 */
	public FenetrePrincipale(){
		
		//formation de la fenetre
		this.setTitle("GestionProd");
		this.setSize(900, 700);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//construction du contentPane
		constructionContentPane();
		
		//visibilité de la fenetre
		this.setContentPane(contentPane);
		this.setVisible(true);
		
	}//fin FenetrePrincipale()
	
	/**
	 * procedure qui consruit le contentPane
	 */
	private void constructionContentPane(){
		
		//instanciation du contentPane
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		
		//construction du bluePane
		bluePane = new JPanel();
		contentPane.add(bluePane, BorderLayout.WEST);
		bluePane.setBackground(Color.BLUE);
		JLabel blueLabel = new JLabel();
		blueLabel.setPreferredSize(new Dimension(50, 25));
		bluePane.add(blueLabel);
		
		//construction du headerPane
		headerPane = new JPanel();
		headerPane.setBackground(Color.WHITE);
		headerPane.setLayout(new BorderLayout());
		
		//instanciation du logo
		logoLabel = new JLabel();
		logoLabel.setIcon(new ImageIcon(FenetrePrincipale.class.getResource("/res/Airfrance.jpg")));
		headerPane.add(logoLabel,BorderLayout.WEST);
		
		//instanciation du titre
		titleLabel = new JLabel("Application Gestion Production");
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setForeground(Color.BLUE);
		titleLabel.setFont(new Font("arial", 1, 30));
		headerPane.add(titleLabel,BorderLayout.CENTER);
		
		//ajout de headerPane dans le contentPane
		contentPane.add(headerPane,BorderLayout.NORTH);
		
		versLabel = new JLabel(PasserelleAffichage.getAppConfig("app.version"));
		versLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});
		versLabel.setForeground(Color.BLUE);
		versLabel.setVerticalAlignment(SwingConstants.TOP);
		versLabel.setFont(new Font("Arial", Font.PLAIN, 10));
		headerPane.add(versLabel, BorderLayout.EAST);
		
		//construction du centerPane
		constructionCenterPane();
		
		contentPane.add(centerPane,BorderLayout.CENTER);
		
	}//fin constructionContentPane()
	
	/**
	 * construction du centerPane
	 */
	private void constructionCenterPane(){
		
		//instanciation du centerPane
		centerPane = new JPanel();
		centerPane.setLayout(new BorderLayout());
		
		//construction du prodPane
		constructionProdPane();
		centerPane.add(prodPane,BorderLayout.CENTER);
		
	}//fin constructionCenterPane()
	
	/**
	 * construction du prodPane
	 */
	private void constructionProdPane(){
		
		//police des boutons
		btnFont = new Font("arial", 1, 14);
		int width = 225;
		int height = 50;
		
		//initialisation du prodPane
		prodPane = new JPanel();
		prodPane.setLayout(new GridLayout(2,0));
		prodPane.setBackground(Color.WHITE);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		prodPane.add(tabbedPane);
		
		dataPanel = new JPanel();
		tabbedPane.addTab("Prod J-1", null, dataPanel, null);
		
		//bouton "importation données DELIA"
		importBtn = new JButton();
		dataPanel.add(importBtn);
		importBtn.setText("<html>Importation des données DELIA et BO<br>et Archivage des données</html>");
		importBtn.setIcon(new ImageIcon(FenetrePrincipale.class.getResource("/res/import.png")));
		importBtn.setFont(btnFont);
		importBtn.addActionListener(this);
		importBtn.setPreferredSize(new Dimension(350, height));
		
		//bouton "modifier les données"
		modifBtn = new JButton();
		dataPanel.add(modifBtn);
		modifBtn.setText("modifier les données");
		modifBtn.setIcon(new ImageIcon(FenetrePrincipale.class.getResource("/res/modif.png")));
		modifBtn.setFont(btnFont);
		modifBtn.setPreferredSize(new Dimension(350, height));
		
		//bouton "générer les Documents"
		pdfBtn = new JButton();
		dataPanel.add(pdfBtn);
		pdfBtn.setText("<html>générer les<br>Documents<html>");
		pdfBtn.setIcon(new ImageIcon(FenetrePrincipale.class.getResource("/res/document.png")));
		pdfBtn.setFont(btnFont);
		pdfBtn.setPreferredSize(new Dimension(width, height));
		
		//bouton "générer les Documents"
		majSmsBtn = new JButton();
		dataPanel.add(majSmsBtn);
		majSmsBtn.setText("<html>Mettre a jour<br>la liste SMS<html>");
		majSmsBtn.setIcon(new ImageIcon(FenetrePrincipale.class.getResource("/res/sms.jpg")));
		majSmsBtn.setFont(btnFont);
		majSmsBtn.setPreferredSize(new Dimension(width, height));
		majSmsBtn.addActionListener(this);
		pdfBtn.addActionListener(this);
		modifBtn.addActionListener(this);
		
		dispPanel = new JPanel();
		tabbedPane.addTab("Prod J", null, dispPanel, null);
		
		//bouton "afficher le téléaffichage"
		tvAffichageBtn = new JButton();
		dispPanel.add(tvAffichageBtn);
		tvAffichageBtn.setText("<html>Afficher le<br>TéléAffichage<html>");
		tvAffichageBtn.setIcon(new ImageIcon(FenetrePrincipale.class.getResource("/res/tv.png")));
		tvAffichageBtn.setFont(btnFont);
		tvAffichageBtn.setPreferredSize(new Dimension(width, height));
		
		//bonton "Afficher le PowerPoint"
		pptAffichageBtn = new JButton();
		dispPanel.add(pptAffichageBtn);
		pptAffichageBtn.setText("Afficher le PowerPoint");
		pptAffichageBtn.setIcon(new ImageIcon(FenetrePrincipale.class.getResource("/res/ppt.png")));
		pptAffichageBtn.setFont(btnFont);
		pptAffichageBtn.setPreferredSize(new Dimension(width, height));
		
		//bonton "Parametres Affichages"
		modifAffichageBtn = new JButton();
		dispPanel.add(modifAffichageBtn);
		modifAffichageBtn.setText("<html>Paramètres<br>Affichages<html>");
		modifAffichageBtn.setIcon(new ImageIcon("dataSystem\\affichage.jpg"));
		modifAffichageBtn.setFont(btnFont);
		modifAffichageBtn.setPreferredSize(new Dimension(width, height));
		modifAffichageBtn.addActionListener(this);
		pptAffichageBtn.addActionListener(this);
		tvAffichageBtn.addActionListener(this);
		
		statPanel = new JPanel();
		tabbedPane.addTab("Prod S", null, statPanel, null);
		
		//initialisation du pdfPane
		pdfPane = new JPanel();
		pdfPane.setBackground(Color.WHITE);
		
		prodPane.add(pdfPane);
		
	}//fin constructionProdPane()

	/**
	 * procedure de l'interface ActionListener<br/>
	 * execute les modifications par rapport au bouton selectionné
	 */
	public void actionPerformed(ActionEvent e) {
		
		//recuperation de la source
		JButton source = (JButton) e.getSource();

		//téléaffichage
		if(source.equals(tvAffichageBtn)){
			new FenetreTVAffichage();
		}
		
		//afficher le powerpoint
		if(source.equals(pptAffichageBtn)){
			PasserelleAffichage.affichagePPT();
		}
		
		//parametres affichage
		if(source.equals(modifAffichageBtn)){
			new FenetreParametreAffichage();
		}
		
		//generer les document
		if(source.equals(pdfBtn)){
			new FenetreGŽnŽrationPDF();
		}
		
		//modifier les données
		if(source.equals(modifBtn)){
			new FenetreDonnee();
		}
		
		//importation
		if(source.equals(importBtn)){
			int rep = JOptionPane.showConfirmDialog(null, "<html>Avez-vous importé Export.txt, OATVPNC.xls et OATVPNT.xls pour J+1 dans le dossier dataImport ?</html>"
					,"Verification",JOptionPane.YES_NO_OPTION);
			if(rep == JOptionPane.YES_OPTION){
				PasserelleStage.importationDonnées();
			}
		}
		
		//liste SMS
		if(source.equals(majSmsBtn)){
			int rep = JOptionPane.showConfirmDialog(null, "Avez-vous importé OATVPNC.xls pour J+1 dans le dossier dataImport ?"
					,"Verification",JOptionPane.YES_NO_OPTION);
			if(rep == JOptionPane.YES_OPTION){
				PasserelleStagiaire.creerListePourSms();
			}
		}
		
	}//fin actionperformed()
	
}//fin class
