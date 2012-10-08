package ui;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import pack.Config;
import pack.PasserelleStage;
import pack.PasserelleStagiaire;

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
	private JPanel dataPane;
	private JPanel affPane;
	private JPanel pdfPane;
	private JPanel smsPane;
	
	//attributs JLabel
	private JLabel logoLabel;
	private JLabel titleLabel;
	private JLabel versLabel;

	//attributs JButton
	private JButton tvAffichageBtn;//bouton du téléAffichage
	private JButton pptAffichageBtn;//bouton du lancement duu powerpoint
	private JButton modifAffichageBtn;//bouton des parametres d'affichage
	private JButton importBtn;//bouton importation DELIA et BO
	private JButton modifBtn;//bouton de modification des données
	private JButton pdfBtn;//bouton de génération des documents
	private JButton majSmsBtn;//bouton pour la liste des sms
	private JButton majTestsBtn;//bouton pour la liste des sms
	private JButton InterviewBtn;//bouton pour les listes interview

	//police des boutons
	private Font btnFont;
	
	//private boolean TVall = false;
	
	/**
	 * constructeur
	 */
	public FenetrePrincipale(String[] args){
		
		System.out.println("Site:"+Config.get("app.site"));
		if (args.length > 0) {
			for (String s: args) {
				//if (s.equals("TV=all")) { //$NON-NLS-1$
				//	TVall = true;
				//}
				if (s.indexOf("=") > 0) {
					String[] p = s.split("=");
					if (p.length > 1) {
						System.out.println("Config param:"+p[0]+" val:"+p[1]);
						Config.set(p[0], p[1]);
					}
				}
			}
		}
		
        try {
			UIManager.setLookAndFeel(
			        UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		//formation de la fenetre
		this.setTitle(Messages.getString("FenetrePrincipale.TitreFenetre")); //$NON-NLS-1$
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
		
		//construction du headerPane
		headerPane = new JPanel();
		headerPane.setBackground(Color.WHITE);
		headerPane.setLayout(new BorderLayout());
		
		//instanciation du logo
		logoLabel = new JLabel();
		logoLabel.setIcon(new ImageIcon(Config.getRes("Airfrance.jpg"))); //$NON-NLS-1$
		headerPane.add(logoLabel,BorderLayout.WEST);
		
		//instanciation du titre
		titleLabel = new JLabel(Messages.getString("FenetrePrincipale.Titre")); //$NON-NLS-1$
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setForeground(Color.BLUE);
		titleLabel.setFont(new Font(Messages.getString("FenetrePrincipale.FontA"), 1, 30)); //$NON-NLS-1$
		headerPane.add(titleLabel,BorderLayout.CENTER);
		
		versLabel = new JLabel(Config.get("app.site")+"/"+Config.get("app.version")); //$NON-NLS-1$
		versLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		versLabel.setVerticalAlignment(SwingConstants.TOP);
		versLabel.setForeground(Color.BLUE);
		versLabel.setFont(new Font(Messages.getString("FenetrePrincipale.FontA"), 1, 9)); //$NON-NLS-1$
		headerPane.add(versLabel,BorderLayout.EAST);

		//ajout de headerPane dans le contentPane
		contentPane.add(headerPane,BorderLayout.NORTH);
		
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
		
		//construction du bluePane
		bluePane = new JPanel();
		bluePane.setBackground(Color.BLUE);
		JLabel blueLabel = new JLabel();
		blueLabel.setPreferredSize(new Dimension(50, 25));
		bluePane.add(blueLabel);
		
		//ajout du bluePane
		centerPane.add(bluePane,BorderLayout.WEST);
		
		//construction du prodPane
		constructionProdPane();
		centerPane.add(prodPane,BorderLayout.CENTER);
		
	}//fin constructionCenterPane()
	
	/**
	 * construction du prodPane
	 */
	private void constructionProdPane(){
		
		//police des boutons
		btnFont = new Font(Messages.getString("FenetrePrincipale.FontA"), 1, 14); //$NON-NLS-1$
		int width = 225;
		int height = 50;
		
		//initialisation du prodPane
		prodPane = new JPanel();
		prodPane.setLayout(new GridLayout(4,0));
		prodPane.setBackground(Color.WHITE);
		
		//initialisation du dataPane
		dataPane = new JPanel();
		dataPane.setBackground(Color.WHITE);
		
		//bouton "importation données DELIA"
		importBtn = new JButton();
		importBtn.setText(Messages.getString("FenetrePrincipale.importBtn")); //$NON-NLS-1$
		importBtn.setIcon(new ImageIcon(Config.getRes("import.png"))); //$NON-NLS-1$
		importBtn.setFont(btnFont);
		importBtn.addActionListener(this);
		importBtn.setPreferredSize(new Dimension(350, height));
		dataPane.add(importBtn);
		
		//bouton "modifier les données"
		modifBtn = new JButton();
		modifBtn.setText(Messages.getString("FenetrePrincipale.modifBtn")); //$NON-NLS-1$
		modifBtn.setIcon(new ImageIcon(Config.getRes("modif.png"))); //$NON-NLS-1$
		modifBtn.setFont(btnFont);
		modifBtn.setPreferredSize(new Dimension(350, height));
		modifBtn.addActionListener(this);
		dataPane.add(modifBtn);
		
		prodPane.add(dataPane);
		
		//initialisation du affPane
		affPane = new JPanel();
		affPane.setBackground(Color.WHITE);
		
		//bouton "afficher le téléaffichage"
		tvAffichageBtn = new JButton();
		tvAffichageBtn.setText(Messages.getString("FenetrePrincipale.tvBtn")); //$NON-NLS-1$
		tvAffichageBtn.setIcon(new ImageIcon(Config.getRes("tv.png"))); //$NON-NLS-1$
		tvAffichageBtn.setFont(btnFont);
		tvAffichageBtn.setPreferredSize(new Dimension(width, height));
		tvAffichageBtn.addActionListener(this);
		affPane.add(tvAffichageBtn);
		
		//bonton "Afficher le PowerPoint"
		pptAffichageBtn = new JButton();
		pptAffichageBtn.setText(Messages.getString("FenetrePrincipale.pptBtn")); //$NON-NLS-1$
		pptAffichageBtn.setIcon(new ImageIcon(Config.getRes("ppt.png"))); //$NON-NLS-1$
		pptAffichageBtn.setFont(btnFont);
		pptAffichageBtn.setPreferredSize(new Dimension(width, height));
		pptAffichageBtn.addActionListener(this);
		affPane.add(pptAffichageBtn);
		
		//bonton "Parametres Affichages"
		modifAffichageBtn = new JButton();
		modifAffichageBtn.setText(Messages.getString("FenetrePrincipale.affModBtn")); //$NON-NLS-1$
		modifAffichageBtn.setIcon(new ImageIcon(Config.getRes("affichage.png"))); //$NON-NLS-1$
		modifAffichageBtn.setFont(btnFont);
		modifAffichageBtn.setPreferredSize(new Dimension(width, height));
		modifAffichageBtn.addActionListener(this);
		affPane.add(modifAffichageBtn);
		
		prodPane.add(affPane);
		
		//initialisation du pdfPane
		pdfPane = new JPanel();
		pdfPane.setBackground(Color.WHITE);
		
		//bouton "générer les Documents"
		pdfBtn = new JButton();
		pdfBtn.setText(Messages.getString("FenetrePrincipale.pdfBtn")); //$NON-NLS-1$
		pdfBtn.setIcon(new ImageIcon(Config.getRes("document.png"))); //$NON-NLS-1$
		pdfBtn.setFont(btnFont);
		pdfBtn.setPreferredSize(new Dimension(width, height));
		pdfBtn.addActionListener(this);
		pdfPane.add(pdfBtn);
		
		prodPane.add(pdfPane);
		
		//initialisation du smsPane
		smsPane = new JPanel();
		smsPane.setBackground(Color.WHITE);
		
		//bouton "générer les Documents"
		majSmsBtn = new JButton();
		majSmsBtn.setText(Messages.getString("FenetrePrincipale.smsBtn")); //$NON-NLS-1$
		majSmsBtn.setIcon(new ImageIcon(Config.getRes("sms.png"))); //$NON-NLS-1$
		majSmsBtn.setFont(btnFont);
		majSmsBtn.setPreferredSize(new Dimension(width, height));
		majSmsBtn.addActionListener(this);
		smsPane.add(majSmsBtn);
		
		//bouton "générer les Lists"
		majTestsBtn = new JButton();
		majTestsBtn.setText(Messages.getString("FenetrePrincipale.testBtn")); //$NON-NLS-1$
		//majTestsBtn.setIcon(new ImageIcon("dataSystem\\sms.jpg"));
		majTestsBtn.setFont(btnFont);
		majTestsBtn.setPreferredSize(new Dimension(width, height));
		majTestsBtn.addActionListener(this);
		smsPane.add(majTestsBtn);

		//bouton "générer les Lists Interview"
		InterviewBtn = new JButton();
		InterviewBtn.setText(Messages.getString("FenetrePrincipale.intervBtn")); //$NON-NLS-1$
		//majTestsBtn.setIcon(new ImageIcon("dataSystem\\sms.jpg"));
		InterviewBtn.setFont(btnFont);
		InterviewBtn.setPreferredSize(new Dimension(width, height));
		InterviewBtn.addActionListener(this);
		smsPane.add(InterviewBtn);

		//ajout
		prodPane.add(smsPane);
		
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
			//recherche de l'executable de powerpoint
			Runtime x = Runtime.getRuntime();
			String[] args = { Config.get("aff.cheminpptexe"),
							  "/s",
							  Config.get("aff.ppt")
							  };
			//execution du diaporama
			try {
				x.exec(args);
			} catch (IOException e1) {
				//boite de dialogue d'erreur
				JOptionPane.showMessageDialog(null, "soit le chemin vers powerpnt.exe est incorrect ! soit le ppt n'est pas nommé TVAFFPPT.ppt !", "Erreur", JOptionPane.ERROR_MESSAGE);
			}

		}
		
		//parametres affichage
		if(source.equals(modifAffichageBtn)){
			new FenetreParametreAffichage();
		}
		
		//generer les document
		if(source.equals(pdfBtn)){
			new FenetreGénérationPDFNew();
		}
		
		//modifier les données
		if(source.equals(modifBtn)){
			new FenetreDonneeNew();
		}
		
		//importation
		if(source.equals(importBtn)){
			int rep = JOptionPane.showConfirmDialog(null, Messages.getString("FenetrePrincipale.exportMsg") //$NON-NLS-1$
					,Messages.getString("FenetrePrincipale.verif"),JOptionPane.YES_NO_OPTION); //$NON-NLS-1$
			if(rep == JOptionPane.YES_OPTION){
				PasserelleStage.importationDonnées();
			}
		}
		
		//liste SMS
		if(source.equals(majSmsBtn)){
			int rep = JOptionPane.showConfirmDialog(null, Messages.getString("FenetrePrincipale.importMsg") //$NON-NLS-1$
					,Messages.getString("FenetrePrincipale.verif"),JOptionPane.YES_NO_OPTION); //$NON-NLS-1$
			if(rep == JOptionPane.YES_OPTION){
				PasserelleStagiaire.creerListePourSms();
			}
		}
	
		//liste matricules
		if(source.equals(majTestsBtn)){
			int rep = JOptionPane.showConfirmDialog(null, Messages.getString("FenetrePrincipale.importMsg") //$NON-NLS-1$
					,Messages.getString("FenetrePrincipale.verif"),JOptionPane.YES_NO_OPTION); //$NON-NLS-1$
			if(rep == JOptionPane.YES_OPTION){
				PasserelleStagiaire.creerListePourTests();
			}
		}
		
		//listes interview
		if(source.equals(InterviewBtn)){
			int rep = JOptionPane.showConfirmDialog(null, Messages.getString("FenetrePrincipale.importIntervMsg") //$NON-NLS-1$
					,Messages.getString("FenetrePrincipale.verif"),JOptionPane.YES_NO_OPTION); //$NON-NLS-1$
			if(rep == JOptionPane.YES_OPTION){
				PasserelleStagiaire.creerListePourInterview();
			}
		}

	}//fin actionperformed()
	
}//fin class
