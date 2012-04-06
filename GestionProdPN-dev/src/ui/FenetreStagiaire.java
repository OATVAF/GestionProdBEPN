package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;

import pack.Stage;
import pack.Stagiaire;


/**
 * fenetre affichant la liste des stagiaires pour un stage<br>
 * permet aussi d'aujouter ou de supprimer des stagiaires
 * @author BERON Jean-Sebastien
 *
 */
public class FenetreStagiaire extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = 5904658082956351620L;
	
	//attributs
	private JPanel contentPane;//conteneur principal
	private JPanel bluePane;//conteneur servant de decor
	private JPanel centerPane;//conteneur en centre de la fenetre
	private JPanel footerPane;// conteneur en bas de page
	
	private JTable tableau;//tableau contenant les informations sur les stagiaires
	
	//composant du formulaire de suppression
	private JTextField removeField;
	private JButton removeBtn;
	
	//composant du formulaire d'ajout
	private JTextField nomField;
	private JTextField prenomField;
	private JTextField matField;
	private JTextField speField;
	private JTextField commentField;
	private JButton ajoutBtn;
	private JButton cancelBtn;
	
	//le stage des stagiaires a afficher
	private Stage stage;
	
	/**
	 * constructeur
	 */
	public FenetreStagiaire(Stage leStage){
		
		//recuperation du stage
		this.stage = leStage;
		
		//formation de la fenetre
		this.setTitle("liste des stagiaires");
		this.setSize(600, 700);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		//construction du contentPane
		constructionContentPane();
		
		//visibilité de la fenetre
		this.setContentPane(contentPane);
		this.setVisible(true);
		
	}//fin FenetreStagiaire()

	/**
	 * procedure qui construit le contentPane
	 */
	private void constructionContentPane() {
		
		//initialisation du contentPane
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setLayout(new BorderLayout());
		
		//formation de l'entete
		JLabel titreLabel = new JLabel();
		titreLabel.setText("<html>Liste des Stagiaires sur le<br>"+stage.getCode()+" du<br>"+stage.getDateStr()+"</html>");
		titreLabel.setFont(new Font("arial", 1, 25));
		titreLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(titreLabel,BorderLayout.PAGE_START);
		
		//construction du bluePane
		bluePane = new JPanel();
		bluePane.setBackground(Color.BLUE);
		bluePane.add(new JLabel("           "));
		contentPane.add(bluePane,BorderLayout.WEST);
		
		//construction du centerPane
		constructionCenterPane();
		contentPane.add(centerPane);
		
	}//fin constructionContentPane()
	
	private void constructionCenterPane() {
		
		centerPane = new JPanel();
		centerPane.setBackground(Color.WHITE);
		centerPane.setLayout(new BorderLayout());
		
		ModelStagiaire modele = new ModelStagiaire(stage.getStagiaireList());
		tableau = new JTable();
		tableau.setFont(new Font("arial", 1, 14));
		tableau.setModel(modele);
		tableau.setBackground(Color.WHITE);
    	centerPane.add(new JScrollPane(tableau),BorderLayout.CENTER);
    	
    	constructionFooterPane();
    	centerPane.add(footerPane,BorderLayout.PAGE_END);
		
	}//fin constructionCenterPane()

	/**
	 * procedure qui construit le footerPane
	 */
	private void constructionFooterPane() {
		
		footerPane = new JPanel();
    	footerPane.setBackground(Color.WHITE);
    	footerPane.setLayout(new BorderLayout());
    	
    	JPanel ajoutPane = new JPanel();
    	ajoutPane.setBackground(Color.WHITE);
    	ajoutPane.setLayout(new GridLayout(6,2,15,5));
    	JLabel nomLabel = new JLabel("entrer le nom :");
    	ajoutPane.add(nomLabel);
    	nomField = new JTextField();
    	ajoutPane.add(nomField);
    	JLabel prenomLabel = new JLabel("entrer le prenom :");
    	ajoutPane.add(prenomLabel);
    	prenomField = new JTextField();
    	ajoutPane.add(prenomField);
    	JLabel matLabel = new JLabel("entrer le matricule (8 chiffres):");
    	ajoutPane.add(matLabel);
    	matField = new JTextField();
    	ajoutPane.add(matField);
    	JLabel speLabel = new JLabel("entrer la Fonction :");
    	ajoutPane.add(speLabel);
    	speField = new JTextField();
    	ajoutPane.add(speField);
    	JLabel commentLabel = new JLabel("entrer le commentaire :");
    	ajoutPane.add(commentLabel);
    	commentField = new JTextField();
    	ajoutPane.add(commentField);
    	ajoutBtn = new JButton("ajouter");
    	ajoutBtn.addActionListener(this);
    	ajoutPane.add(ajoutBtn);
    	cancelBtn = new JButton("annuler");
    	cancelBtn.addActionListener(this);
    	ajoutPane.add(cancelBtn);
    	footerPane.add(ajoutPane,BorderLayout.CENTER);
    	
    	JPanel removePane = new JPanel();
    	removePane.setBackground(Color.WHITE);
    	JLabel removeLabel = new JLabel("index du stagiaire a enlever");
    	removePane.add(removeLabel);
    	removeField = new JTextField(2);
    	removePane.add(removeField);
    	removeBtn = new JButton("supprimer");
    	removeBtn.addActionListener(this);
    	removePane.add(removeBtn);
    	footerPane.add(removePane, BorderLayout.PAGE_END);
		
	}//fin constructionFooterPane()
	
	/**
	 * procedure de suppression d'un stagiaire
	 */
	public void suppression(){
		
		//variable temporaire
		boolean good = true;
		
		try {
			int index = Integer.parseInt(removeField.getText());
			if (index > stage.getSizeStagiaireList() || index <= 0 || stage.getSizeStagiaireList()==0) {
				good = false;
			}else{
				stage.supprimerStagiaire(index-1);
				this.dispose();
				new FenetreStagiaire(stage);
			}//fin si
		}catch (NumberFormatException e1) {
			JOptionPane.showMessageDialog(null, "<html>Erreur" +
					"<br>l'index entré est hors de la liste</html>", "Erreur", JOptionPane.INFORMATION_MESSAGE);
		}
		
		if (! good) {
			JOptionPane.showMessageDialog(null, "<html>Erreur" +
					"<br>l'index que vous avez saisi est hors de la liste</html>", "Suppression", JOptionPane.INFORMATION_MESSAGE);
		}else{
			JOptionPane.showMessageDialog(null, "<html>suppression réussi !<br></html>", "Suppression", JOptionPane.INFORMATION_MESSAGE);
		}
	}//fin suppression()
	
	/**
	 * procedure qui fait la verification du formulaire<br>
	 * et l'ajout du nouveau stagiaire
	 */
	public void ajout(){
		boolean good = true;
		try{
			if(Integer.parseInt(matField.getText()) < 9999999 || matField.getText().length() != 8){
				good = false;
			}
		}catch (NumberFormatException e1) {
			JOptionPane.showMessageDialog(null, "<html>Erreur" +
					"<br>le matricule doit etre composé de 8 chiffres</html>", "Erreur", JOptionPane.INFORMATION_MESSAGE);
		}
		if(nomField.getText().equals("") 
		|| prenomField.getText().equals("")
		|| speField.getText().equals("")
		|| commentField.getText().equals("")){
			good = false;
		}
		if (! good) {
			JOptionPane.showMessageDialog(null, "<html>l'Ajout a été annulé !<br>champ(s) incorrect(s)</html>", "Ajout", JOptionPane.INFORMATION_MESSAGE);
		}else{
			Stagiaire leStagiaire = new Stagiaire(matField.getText(), stage.getCode(), stage.getDateStr(), stage.getDateStr(),
					nomField.getText().toUpperCase(), prenomField.getText().toUpperCase(), speField.getText().toUpperCase(), commentField.getText().toUpperCase());
			stage.ajoutStagiaire(leStagiaire);
			this.dispose();
			new FenetreStagiaire(stage);
			JOptionPane.showMessageDialog(null, "<html>l'Ajout réussi !<br></html>", "Ajout", JOptionPane.INFORMATION_MESSAGE);
		}
	}//fin ajout()
	
	/**
	 * actionPerformed()
	 */
	public void actionPerformed(ActionEvent e) {
		
		//recuperation de la source
		JButton source = (JButton) e.getSource();
		
		if(source.equals(removeBtn)){
			int rep = JOptionPane.showConfirmDialog(null, "<html>la suppresion sera definitive si vous enregistrez!<br> continuez ?</html>"
					,"modification",JOptionPane.YES_NO_OPTION);
			if(rep == JOptionPane.YES_OPTION){
				suppression();
			}
		}//finsi
		
		if (source.equals(cancelBtn)) {
			nomField.setText("");
			prenomField.setText("");
			matField.setText("");
			speField.setText("");
			commentField.setText("");
		}//finsi
		
		if (source.equals(ajoutBtn)){
			int rep = JOptionPane.showConfirmDialog(null, "<html>l'ajout sera definitif si vous enregistrez!<br> continuez ?</html>"
					,"modification",JOptionPane.YES_NO_OPTION);
			if(rep == JOptionPane.YES_OPTION){
				ajout();
			}
		}//finsi
		
	}//fin actionPerformed()

}//fin class

/**
 * 
 * @author BERON Jean-Sebastien
 *
 */
class ModelStagiaire extends AbstractTableModel {
	
	private static final long serialVersionUID = -4189189270303777510L;
	
	//attributs
	private final ArrayList<Stagiaire> stagiaireList;
	private final String[] entetes = {"Nb", "Nom", "Prenom", "Mat", "Spé", "Comment"};
	
	/**
	 * constructeur
	 * @param stagiaireList
	 */
	public ModelStagiaire(ArrayList<Stagiaire> stagiaireList) {
        super();
        this.stagiaireList = stagiaireList;
    }
	
	/**
	 * retourne le nombre de lignes
	 */
	public int getRowCount() {
		return stagiaireList.size();
	}

	/**
	 * retourne le nombre de colonnes
	 */
	public int getColumnCount() {
		return entetes.length;
	}

	/**
	 * retourne les noms de colonnes
	 */
	public String getColumnName(int columnIndex) {
        return entetes[columnIndex];
    }
	
	/**
	 * retourne la valeur de la case du tableau dont les coordonnées sont passés en paramétre
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		switch(columnIndex){
			case 0:
				return (rowIndex+1);
        	case 1:
            	return stagiaireList.get(rowIndex).getNom();
        	case 2:
            	return stagiaireList.get(rowIndex).getPrenom();
        	case 3:
            	return stagiaireList.get(rowIndex).getMatricule();
        	case 4:
            	return stagiaireList.get(rowIndex).getSpe();
        	case 5:
            	return stagiaireList.get(rowIndex).getComment();
        	default:
            	return null; //Ne devrait jamais arriver
		}//fin switch
		
	}//getValueAt

}//fin class
