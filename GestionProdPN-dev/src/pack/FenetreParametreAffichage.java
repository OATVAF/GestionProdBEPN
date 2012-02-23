package pack;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * 
 * @author BERON Jean-Sebastien
 *
 */
public class FenetreParametreAffichage extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = -4428471051416128434L;
	
	//principaux JPanel
	private JPanel contentPane;
	private JPanel headerPane;
	private JPanel centerPane;
	private JPanel footerPane;
	
	//JPanel secondaires
	private JPanel infoHdpane;
	private JPanel infoFtpane;
	private JPanel infoPptpane;
	
	//composants
	private JLabel titreLabel;
	private JButton applyBtn;
	private JTextField msgHdTextField;
	private JTextField msgFtTextField;
	private JTextField pathPptTextField;
	
	/**
	 * constructeur
	 */
	public FenetreParametreAffichage(){
		
		this.setTitle("verification des données");
		this.setSize(450, 300);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		//construction du contentPane
		constructionContentPane();
		
		this.setContentPane(contentPane);
		this.setVisible(true);
		
	}//fin FenetreParametreAffichage()
	
	/**
	 * procedure qui construit le contentPane
	 */
	private void constructionContentPane(){
		
		//initialisation du contentPane
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		
		//construction du headerPane
		headerPane = new JPanel();
		headerPane.setBackground(Color.WHITE);
		titreLabel = new JLabel("Parametre Affichage");
		titreLabel.setFont(new Font("arial", 1, 20));
		headerPane.add(titreLabel);
		
		//construction du centerPane
		constructionCenterPane();
		
		//construction du footerPane
		footerPane = new JPanel();
		footerPane.setBackground(Color.WHITE);
		applyBtn = new JButton("Appliquer");
		applyBtn.addActionListener(this);
		footerPane.add(applyBtn);
		
		//ajout des composant au contentPane
		contentPane.add(headerPane,BorderLayout.PAGE_START);
		contentPane.add(centerPane,BorderLayout.CENTER);
		contentPane.add(footerPane,BorderLayout.PAGE_END);
		
	}//fin constructionContentPane()
	
	/**
	 * procedure qui construit le centerPane
	 */
	private void constructionCenterPane(){
		
		//initialisation du centerpane
		centerPane = new JPanel();
		centerPane.setBackground(Color.WHITE);
		centerPane.setLayout(new GridLayout(3,1));
		
		//construction du infoHdpance
		infoHdpane = new JPanel();
		infoHdpane.setBackground(Color.WHITE);
		infoHdpane.add(new JLabel("message en entete (- de 50 caracteres recommandé): "));
		msgHdTextField = new JTextField(PasserelleAffichage.getHeaderMsg());
		msgHdTextField.setPreferredSize(new Dimension(400, 25));
		infoHdpane.add(msgHdTextField);
		
		//construction du infoFtpane
		infoFtpane = new JPanel();
		infoFtpane.setBackground(Color.WHITE);
		infoFtpane.add(new JLabel("message en bas de page (- de 100 caracteres recommandé): "));
		msgFtTextField = new JTextField(PasserelleAffichage.getFooterMsg());
		msgFtTextField.setPreferredSize(new Dimension(400, 25));
		infoFtpane.add(msgFtTextField);

		//construction du infopptpane
		infoPptpane = new JPanel();
		infoPptpane.setBackground(Color.WHITE);
		infoPptpane.add(new JLabel("chemin absolu vers : powerpnt.exe"));
		pathPptTextField = new JTextField(PasserelleAffichage.getCheminPptExe());
		pathPptTextField.setPreferredSize(new Dimension(400, 25));
		infoPptpane.add(pathPptTextField);
		
		//ajout des composants au centerpane
		centerPane.add(infoHdpane);
		centerPane.add(infoFtpane);
		centerPane.add(infoPptpane);
		
	}//fin constructionCenterPane()

	/**
	 * actionPerformed()
	 */
	public void actionPerformed(ActionEvent e) {
		
		//recuperation de la source de l'action
		JButton source = (JButton) e.getSource();
		
		//application si la source est applyBtn
		if(source.equals(applyBtn)){
			PasserelleAffichage.ecritureTextAff(msgHdTextField.getText(), msgFtTextField.getText(),pathPptTextField.getText());
			this.dispose();
		}//finsi
		
	}//fin actionPerformed()

}//fin class
