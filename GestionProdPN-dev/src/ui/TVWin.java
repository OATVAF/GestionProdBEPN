package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.sql.Date;
import java.sql.Time;
import java.util.GregorianCalendar;

import javax.swing.*;
import javax.swing.border.*;

import data.DB;
import data.Module;
import data.Stage;

/**
 * fenetre qui sert pour le téléffichage<br>
 * affiche les prochains stages du jour avec la salle et l'heure
 * @author BERON Jean-Sébastien
 *
 */
public class TVWin extends JFrame implements Runnable{
	
	private static final long serialVersionUID = 6442873953231455888L;
	
	//constante
	private final int  NBAFF = Config.getI("aff.nbstages");		//nombre d'affichage max
	private final long NBMIN = Config.getI("aff.nbmin");		//nombre de minutes 
	
	//attributs temporaires
	private Date dateActuelle;
	private Time heureActuelle;
	private long nbMin;
	private ArrayList<Stage> StageList;
	
    private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static DateFormat timeFormat = new SimpleDateFormat("HH:mm");
    
	//variables pour le timer
	private Thread runner;
	private boolean run;
    private Timer timer;

	// UI
	private JLabel timeLabel;
	private JPanel stagePane;
	/**
	 * constructeur
	 */
	public TVWin(){
		
		//recuperation de la date d'aujourd'hui
		dateActuelle = new Date(new java.util.Date().getTime());
		
		//chargement des stages
		this.setTitle("TVAffichage");
		this.setSize(800, 600);
		this.setResizable(true);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				System.out.println("Window Closed Event");
				run = false;
			}
		});
		
		initialize();
		afficherStages();
		this.setVisible(true);

		// timer de la date
        timer = new Timer(5000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				afficherStages();
    			System.out.println("[INFO] Timer out");
			}
    	});
		start();
		
	}//fin TVAffichage()
	
	/**
	 * procedure qui lance le timer de la fenetre
	 */
	private void start(){
		
		runner = new Thread(this);
		runner.start();
		timer.start();
		run = true;
		
	}//fin start()
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		//initialisation du contentPane
		/*
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		contentPane.setBackground(Color.WHITE);
		getContentPane().add(contentPane);
		*/
		//initialisation du headerPane
		JPanel headerPane = new JPanel();
		headerPane.setLayout(new BorderLayout());
		headerPane.setBackground(Color.WHITE);
		
		//formation et ajout des composants
		JLabel logoLabel = new JLabel(new ImageIcon(TVWin.class.getResource("/res/Airfrance.jpg")));
		headerPane.add(logoLabel,BorderLayout.LINE_START);
		JLabel welcomeLabel = new JLabel(Config.get("aff.header"));
		welcomeLabel.setFont(new Font("Arial", 1, 30));
		welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		welcomeLabel.setForeground(Color.BLUE);
		headerPane.add(welcomeLabel,BorderLayout.CENTER);
		getContentPane().add(headerPane,BorderLayout.PAGE_START);
		
		//construction du centerpane
		//initialisation du centerPane
		JPanel centerPane = new JPanel();
		centerPane.setLayout(new BorderLayout());
		centerPane.setBackground(Color.WHITE);
		
		//initialisation du titlePane
		JPanel titlePane = new JPanel();
		titlePane.setBackground(Color.BLUE);
		
		//initialisation 
		JLabel titleLabel = new JLabel();
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(new Font("Arial", 1, 30));
		
		//actualisation de la date et de l'heure
		titleLabel.setText("Stages du "+dateFormat.format(dateActuelle));
		titlePane.add(titleLabel);
		centerPane.add(titlePane,BorderLayout.PAGE_START);
		
		//initialisation du stagePane
		stagePane = new JPanel();
		//stagePane.setLayout(new GridLayout(0,4));
		stagePane.setLayout(new GridBagLayout());
		stagePane.setBackground(Color.WHITE);		
		centerPane.add(stagePane,BorderLayout.CENTER);
		
		getContentPane().add(centerPane,BorderLayout.CENTER);
		
		//construction du footerPane
		//initialisation du footerPane
		JPanel footerPane = new JPanel();
		footerPane.setLayout(new BorderLayout());
		
		//initialisation du message déroulant
		Marquee marquee = new Marquee(Config.get("aff.footer"), 120);
		marquee.start();
		footerPane.add(marquee,BorderLayout.CENTER);
		
		//initialisation du label affichant l'heure
		timeLabel = new JLabel();
		timeLabel.setBackground(Color.WHITE);
		timeLabel.setBorder(new LineBorder(Color.BLUE, 2));
		timeLabel.setFont(new Font("Arial", 1, 45));
		footerPane.add(timeLabel,BorderLayout.EAST);
		getContentPane().add(footerPane,BorderLayout.PAGE_END);
		
	}//fin constructionContentPane()
		

	/**
	 * procedure de l'interface Runnable
	 */
	//@SuppressWarnings("deprecation")
	public void run() {
		
		//variables locales
		String strTime = "";
		String strnNextTime = "";

		//
		while(run){// boucle infini
			//actualisation de la date et de l'heure
			dateActuelle  = new Date(new java.util.Date().getTime());
			heureActuelle = new Time(new java.util.Date().getTime());
			
			strTime = "     "+timeFormat.format(dateActuelle)+"     ";
			timeLabel.setText(strTime);
			
			//actualisation des stages toutes les minutes
			nbMin = heureActuelle.getTime();
			/*
			if(! strnNextTime.equalsIgnoreCase(strTime)){
				strnNextTime = strTime;
				
				strTime = strTime.trim();
				int nbMin;
				nbMin = Integer.parseInt(strTime.substring(0, 2))*60 + Integer.parseInt(strTime.substring(3, 5));
				
				afficherStages(nbMin);
			}*/
			//finsi
			try {
				//System.out.println("Sleep");
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}//fin tantque
		System.out.println("Thread Affichage leaving run()");
	}//fin run()
	
	/**
	 * procedure qui affiche les stages
	 */
	private void afficherStages() {
		
		//centerPane.remove(stagePane);
		//constructionStagePane();
		//centerPane.add(stagePane,BorderLayout.CENTER);
		JLabel[][] stageLabels = new JLabel[NBAFF][4];
		
		StageList = (ArrayList<Stage>) DB.modelStages.getStageTV(dateActuelle);
		//list qui retient les stages a enlever
		// ArrayList<Stage> removeStage = new ArrayList<Stage>();
		// SG for (Stage unstage : StageList) {
			//SG if(unstage.getnbMin() < (nbMin - NBMIN)){
				//enleve les stages qui sont debutés depuis plus de NBMIN minutes
				//SG removeStage.add(unstage);
			//SG }
		// SG }//fin pour
		//suppression des stages a enlever de l'affichage
		// StageList.removeAll(removeStage);

		//affichage
		int i = 0;
		GridBagConstraints c = new GridBagConstraints();
		for (Stage unstage : StageList) {
			if(i == NBAFF){
				break;
			}
			//Border border = LineBorder.createGrayLineBorder();
			
			c.fill = GridBagConstraints.HORIZONTAL;
			
			stageLabels[i][0] = new JLabel();
			stageLabels[i][0].setText(unstage.getCode());
			stageLabels[i][0].setFont(new Font("arial", 1, 22));
			stageLabels[i][0].setForeground(Color.BLACK);
			stageLabels[i][0].setHorizontalAlignment(SwingConstants.CENTER);
			c.gridx = 0; c.gridy = i; c.weightx = 1;
			stagePane.add(stageLabels[i][0], c);
			
			stageLabels[i][1] = new JLabel();
			String libelle = unstage.getLibelle();
			stageLabels[i][1].setText(libelle);
			stageLabels[i][1].setFont(new Font("arial", 1, 22));
			stageLabels[i][1].setForeground(Color.BLACK);
			stageLabels[i][1].setHorizontalAlignment(SwingConstants.CENTER);
			c.gridx = 1; c.gridy = i; c.weightx = 2;
			stagePane.add(stageLabels[i][1],c);
			
			stageLabels[i][2] = new JLabel();
			stageLabels[i][2].setText(unstage.getFirstModule().getMoyen());
			stageLabels[i][2].setFont(new Font("arial", 1, 26));
			stageLabels[i][2].setForeground(Color.BLACK);
			//stageLabels[i][2].setBorder(border);
			stageLabels[i][2].setHorizontalAlignment(SwingConstants.CENTER);
			c.gridx = 2; c.gridy = i; c.weightx = 1;
			stagePane.add(stageLabels[i][2],c);
			
			Module fm = unstage.getFirstModule();
			stageLabels[i][3] = new JLabel();
			stageLabels[i][3].setText(timeFormat.format(fm.getDebut()));
			stageLabels[i][3].setFont(new Font("arial", 1, 26));
			stageLabels[i][3].setForeground(Color.BLACK);
			stageLabels[i][3].setHorizontalAlignment(SwingConstants.CENTER);
			//changement de couleur de l'heure
			
			if( (nbMin-fm.getDebut().getTime()) > 0 && (nbMin-fm.getDebut().getTime()) < Config.getI("stage.delay")) {
				stageLabels[i][3].setForeground(new Color(255, 102, 0));
			}
			if( (fm.getDebut().getTime()-nbMin) > Config.getI("stage.delay")){
				stageLabels[i][3].setForeground(new Color(255, 0, 0));
			}
			c.gridx = 3; c.gridy = i; c.weightx = 1;
			stagePane.add(stageLabels[i][3],c);
			i++;
		}
		//ajout de case vide pour rendre l'affichage plus joli quand il se vide
		if(i < 10){
			for (int k = i; k < 10; k++) {
				for (int j = 0; j < 4; j++) {
					stageLabels[i][j] = new JLabel();
					c.gridx = j; c.gridy = i; c.weightx = 1;
					stagePane.add(stageLabels[i][j],c);
				}
			}
		}//finsi
		
	}//fin afficherStages()

}//fin class