package ui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.border.*;

import pack.Config;
import pack.PasserelleStage;
import pack.Stage;


/**
 * fenetre qui sert pour le t�l�ffichage<br>
 * affiche les prochains stages du jour avec la salle et l'heure
 * @author BERON Jean-S�bastien
 *
 */
public class FenetreTVAffichage extends JFrame implements Runnable{
	
	private static final long serialVersionUID = 6442873953231455888L;
	
	//constante
	private final int NBAFF = 19;//nombre d'affichage max
	private final int NBMIN = 30;//nombre de minutes 
	
	//attributs IHM
	private JPanel contentPane;
	private JPanel headerPane;
	private JLabel logoLabel;
	private JLabel welcomeLabel;
	private JPanel centerPane;
	private JPanel titlePane;
	private JLabel titleLabel;
	private JPanel stagePane;
	private JPanel footerPane;
	private Marquee marquee;
	private JLabel timeLabel;
	private JLabel[][] stageLabels;
	private HashMap<String,ImageIcon> logoIcons = new HashMap<String,ImageIcon>(); 
	
	//attributs temporaires
	private Date dateActuelle;
	private ArrayList<Stage> StageList;
	
	//variables pour le timer
	private Thread runner;
	private boolean run;
	
	private boolean TVall = false;
			
	/**
	 * constructeur
	 */
	public FenetreTVAffichage(boolean all){
		
		TVall = all;
		
		//recuperation de la date d'aujourd'hui
		dateActuelle = new Date();
		
		//chargement des stages
		StageList = PasserelleStage.chargerStageList(all);
		if(StageList.isEmpty()){
			//boite de dialogue
			JOptionPane.showMessageDialog(null, "ERREUR ! pas de stages � afficher !","Erreur",JOptionPane.OK_OPTION);
		}
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
		
		//construction du contentPane
		constructionContentPane();
		
		this.setContentPane(contentPane);
		this.setVisible(true);
		
		//lancement du timer de la fenetre
		start();
		
	}//fin TVAffichage()
	
	/**
	 * procedure qui lance le timer de la fenetre
	 */
	private void start(){
		
		runner = new Thread(this);
		runner.start();
		run = true;
		
	}//fin start()
	
	/**
	 * procedure qui construit le contentPane
	 */
	private void constructionContentPane(){
		
		//initialisation du contentPane
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		contentPane.setBackground(Color.WHITE);
		
		//construction du headerPane
		constructionHeaderPane();
		contentPane.add(headerPane,BorderLayout.PAGE_START);
		
		//construction du centerpane
		constructionCenterPane();
		contentPane.add(centerPane,BorderLayout.CENTER);
		
		//construction du footerPane
		constructionFooterPane();
		contentPane.add(footerPane,BorderLayout.PAGE_END);
		
	}//fin constructionContentPane()
	
	/**
	 * procedure qui construit le headerPane
	 */
	private void constructionHeaderPane(){
		
		//initialisation du headerPane
		headerPane = new JPanel();
		headerPane.setLayout(new BorderLayout());
		headerPane.setBackground(Color.WHITE);
		
		//formation et ajout des composants
		logoLabel = new JLabel(new ImageIcon(Config.getRes("Airfrance.jpg")));
		headerPane.add(logoLabel,BorderLayout.LINE_START);
		welcomeLabel = new JLabel(Config.get("aff.header"));
		welcomeLabel.setFont(new Font("Arial", 1, 30));
		welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		welcomeLabel.setForeground(Color.BLUE);
		headerPane.add(welcomeLabel,BorderLayout.CENTER);
		
	}//fin constructionHeaderPane()
	
	private void constructionCenterPane(){
		
		//initialisation du centerPane
		centerPane = new JPanel();
		centerPane.setLayout(new BorderLayout());
		centerPane.setBackground(Color.WHITE);
		
		constructionTitlePane();
		centerPane.add(titlePane,BorderLayout.PAGE_START);
		
		constructionStagePane();
		centerPane.add(stagePane,BorderLayout.CENTER);
		
	}
	
	/**
	 * procedure qui construit le titlePane
	 */
	@SuppressWarnings("deprecation")
	private void constructionTitlePane(){
		
		//initialisation du titlePane
		titlePane = new JPanel();
		titlePane.setBackground(Color.BLUE);
		
		//initialisation 
		titleLabel = new JLabel();
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(new Font("Arial", 1, 30));
		String strDay = Integer.toString(dateActuelle.getDate());
		if(dateActuelle.getDate()<10){
			strDay = "0"+strDay;//met un "0" si le jour en < a 10
		}
		String strMonth = Integer.toString((dateActuelle.getMonth()+1));
		if(dateActuelle.getMonth()<10){
			strMonth = "0"+strMonth;//met un "0" si le mois en < a 10
		}
		String strYear = Integer.toString(dateActuelle.getYear()+1900);
		titleLabel.setText("Stages du "+strDay+"/"+strMonth+"/"+strYear);
		titlePane.add(titleLabel);
		
	}//fin constructionTitlePane()
	
	/**
	 * procedure qui construit le stagePane
	 */
	private void constructionStagePane(){
		
		//initialisation du stagePane
		stagePane = new JPanel();
		//stagePane.setLayout(new GridLayout(0,4));
		stagePane.setLayout(new GridBagLayout());
		stagePane.setBackground(Color.WHITE);
				
	}//fin constructionStagePane()
	
	/**
	 * construction du footerPane
	 */
	private void constructionFooterPane(){
		
		//initialisation du footerPane
		footerPane = new JPanel();
		footerPane.setLayout(new BorderLayout());
		
		//initialisation du message d�roulant
		marquee = new Marquee(Config.get("aff.footer"), 120);
		marquee.start();
		footerPane.add(marquee,BorderLayout.CENTER);
		
		//initialisation du label affichant l'heure
		timeLabel = new JLabel();
		timeLabel.setBackground(Color.WHITE);
		timeLabel.setBorder(new LineBorder(Color.BLUE, 2));
		timeLabel.setFont(new Font("Arial", 1, 45));
		footerPane.add(timeLabel,BorderLayout.EAST);
		
	}//fin constructionFooterPane()

	/**
	 * procedure de l'interface Runnable
	 */
	@SuppressWarnings("deprecation")
	public void run() {
		
		//variables locales
		String strTime = "";
		String strnNextTime = "";
		
		//
		while(run){// boucle infini
			//actualisation de la date et de l'heure
			dateActuelle = new Date();
			//recuperation de l'heure
			String min = ""+dateActuelle.getMinutes();
			String heure = ""+dateActuelle.getHours();
			if(dateActuelle.getHours()<10){
				heure = "0"+heure;
			}
			if(dateActuelle.getMinutes()<10){
				min = "0"+min;
			}
			strTime = "     "+heure+":"+min+"     ";
			timeLabel.setText(strTime);
			
			//actualisation des stages toutes les minutes
			if(! strnNextTime.equalsIgnoreCase(strTime)){
				strnNextTime = strTime;
				
				strTime = strTime.trim();
				int nbmin;
				nbmin = Integer.parseInt(strTime.substring(0, 2))*60 + Integer.parseInt(strTime.substring(3, 5));
				
				afficherStages(nbmin);
			}//finsi
			try {
				//System.out.println("Sleep");
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}//fin tantque
		System.out.println("Thread Affichage leaving run()");
	}//fin run()
	
	/**
	 * procedure qui affiche les stages
	 * @param nbmin
	 */
	private void afficherStages(int nbmin){
		
		centerPane.remove(stagePane);
		constructionStagePane();
		centerPane.add(stagePane,BorderLayout.CENTER);
		stageLabels = new JLabel[NBAFF][5];
		
		if (TVall == false) {
			//list qui retient les stages a enlever
			ArrayList<Stage> removeStage = new ArrayList<Stage>();
			for (Stage unstage : StageList) {
				if(unstage.getnbMin() < (nbmin - NBMIN)){
					//enleve les stages qui sont debut�s depuis plus de NBMIN minutes
					removeStage.add(unstage);
				}
			}//fin pour
			//suppression des stages a enlever de l'affichage
			StageList.removeAll(removeStage);
		}
		
		//affichage
		int i = 0;
		GridBagConstraints c = new GridBagConstraints();
		for (Stage unstage : StageList) {
			if(i == NBAFF){
				break;
			}
			int j = 0;
			
			//Border border = LineBorder.createGrayLineBorder();
			c.fill = GridBagConstraints.BOTH; // HORIZONTAL;
			c.weighty = 1;
			
			stageLabels[i][j] = new JLabel();
			//stageLabels[i][j].setText(unstage.getCompagnie());
			stageLabels[i][j].setFont(new Font("arial", 1, 26));
			stageLabels[i][j].setBackground(Color.BLUE);
			stageLabels[i][j].setForeground(Color.BLACK);
			stageLabels[i][j].setHorizontalAlignment(SwingConstants.CENTER);
			if (! logoIcons.containsKey(unstage.getCompagnie())) {
				logoIcons.put(unstage.getCompagnie(), 
						new ImageIcon("dataSystem/logos/"+unstage.getCompagnie()+".jpg"));
			}
			stageLabels[i][j].setIcon(logoIcons.get(unstage.getCompagnie()));
			stageLabels[i][j].setSize(10,5);
			c.gridx = j; c.gridy = i; c.weightx = 0.3;
			stagePane.add(stageLabels[i][j],c);
			j++;

			stageLabels[i][j] = new JLabel();
			stageLabels[i][j].setText(unstage.getCode());
			stageLabels[i][j].setFont(new Font("arial", 1, 22));
			stageLabels[i][j].setForeground(Color.BLACK);
			stageLabels[i][j].setHorizontalAlignment(SwingConstants.CENTER);
			c.gridx = j; c.gridy = i; c.weightx = 1;
			stagePane.add(stageLabels[i][j], c);
			j++;
			
			stageLabels[i][j] = new JLabel();
			stageLabels[i][j].setText(unstage.getLibelle());
			stageLabels[i][j].setFont(new Font("arial", 1, 22));
			stageLabels[i][j].setForeground(Color.BLACK);
			stageLabels[i][j].setHorizontalAlignment(SwingConstants.CENTER);
			c.gridx = j; c.gridy = i; c.weightx = 2;
			stagePane.add(stageLabels[i][j],c);
			j++;
			
			stageLabels[i][j] = new JLabel();
			stageLabels[i][j].setText(unstage.getFirstModule().getSalle());
			stageLabels[i][j].setFont(new Font("arial", 1, 26));
			stageLabels[i][j].setForeground(Color.BLACK);
			//stageLabels[i][j].setBorder(border);
			stageLabels[i][j].setHorizontalAlignment(SwingConstants.CENTER);
			c.gridx = j; c.gridy = i; c.weightx = 1;
			stagePane.add(stageLabels[i][j],c);
			j++;
			
			stageLabels[i][j] = new JLabel();
			stageLabels[i][j].setText(unstage.getFirstModule().getHeureDebut());
			stageLabels[i][j].setFont(new Font("arial", 1, 26));
			stageLabels[i][j].setForeground(Color.BLACK);
			stageLabels[i][j].setHorizontalAlignment(SwingConstants.CENTER);
			//changement de couleur de l'heure
			if((StageList.get(i).getnbMin()-nbmin) <= 0 && (unstage.getnbMin()-nbmin) >= -10){
				stageLabels[i][j].setForeground(new Color(255, 102, 0));
			}
			if((StageList.get(i).getnbMin()-nbmin) < -10){
				stageLabels[i][j].setForeground(new Color(255, 0, 0));
			}
			c.gridx = j; c.gridy = i; c.weightx = 1;
			stagePane.add(stageLabels[i][j],c);
			j++;
						
			i++;
		}
		/*
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
		*/
		
	}//fin afficherStages()

}//fin class