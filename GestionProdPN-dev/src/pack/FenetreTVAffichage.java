package pack;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.*;
import javax.swing.border.*;

/**
 * fenetre qui sert pour le téléffichage<br>
 * affiche les prochains stages du jour avec la salle et l'heure
 * @author BERON Jean-Sébastien
 *
 */
public class FenetreTVAffichage extends JFrame implements Runnable{
	
	private static final long serialVersionUID = 6442873953231455888L;
	
	//constante
	private final int NBAFF = 20;//nombre d'affichage max
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
	
	//attributs temporaires
	private Date dateActuelle;
	private ArrayList<Stage> StageList;
	
	//variables pour le timer
	private Thread runner;
	private boolean run;
	
	/**
	 * constructeur
	 */
	public FenetreTVAffichage(){
		
		//recuperation de la date d'aujourd'hui
		dateActuelle = new Date();
		
		//chargement des stages
		StageList = PasserelleStage.chargerStageList();
		if(StageList.isEmpty()){
			//boite de dialogue
			JOptionPane.showMessageDialog(null, "ERREUR ! pas de stages à afficher !","Erreur",JOptionPane.OK_OPTION);
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
		logoLabel = new JLabel(new ImageIcon("dataSystem\\airfrance.jpg"));
		headerPane.add(logoLabel,BorderLayout.LINE_START);
		welcomeLabel = new JLabel(PasserelleAffichage.getHeaderMsg());
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
		
		//initialisation du message déroulant
		marquee = new Marquee(PasserelleAffichage.getFooterMsg(), 120);
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
				System.out.println("Sleep");
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
		stageLabels = new JLabel[NBAFF][4];
		
		//list qui retient les stages a enlever
		ArrayList<Stage> removeStage = new ArrayList<Stage>();
		for (Stage unstage : StageList) {
			//SG if(unstage.getnbMin() < (nbmin - NBMIN)){
				//enleve les stages qui sont debutés depuis plus de NBMIN minutes
				//SG removeStage.add(unstage);
			//SG }
		}//fin pour
		//suppression des stages a enlever de l'affichage
		StageList.removeAll(removeStage);

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
			stageLabels[i][2].setText(unstage.getFirstModule().getSalle());
			stageLabels[i][2].setFont(new Font("arial", 1, 26));
			stageLabels[i][2].setForeground(Color.BLACK);
			//stageLabels[i][2].setBorder(border);
			stageLabels[i][2].setHorizontalAlignment(SwingConstants.CENTER);
			c.gridx = 2; c.gridy = i; c.weightx = 1;
			stagePane.add(stageLabels[i][2],c);
			
			stageLabels[i][3] = new JLabel();
			stageLabels[i][3].setText(unstage.getFirstModule().getHeureDebut());
			stageLabels[i][3].setFont(new Font("arial", 1, 26));
			stageLabels[i][3].setForeground(Color.BLACK);
			stageLabels[i][3].setHorizontalAlignment(SwingConstants.CENTER);
			//changement de couleur de l'heure
			if((StageList.get(i).getnbMin()-nbmin) <= 0 && (unstage.getnbMin()-nbmin) >= -10){
				stageLabels[i][3].setForeground(new Color(255, 102, 0));
			}
			if((StageList.get(i).getnbMin()-nbmin) < -10){
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