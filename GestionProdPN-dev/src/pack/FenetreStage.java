package pack;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;

/**
 * Fenetre pour avoir une vue générale des stages
 * @author BERON Jean-Sébastien
 *
 */
public class FenetreStage extends JFrame{
	
	private static final long serialVersionUID = 2463453494524378755L;
	
	private JPanel contentPane;
	private JPanel centerPane;
	
	private JTable tableau;//tableau contenant les informations sur les stages
	
	private ArrayList<Stage> stageList;

	/**
	 * constructeur
	 * @param stageList
	 */
	public FenetreStage(ArrayList<Stage> stageList){
		
		this.stageList = stageList;
		
		//formation de la fenetre (titre, taille, etc.)
		this.setTitle("Vue des Stages");
		this.setSize(800, 600);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		constructionContentPane();
		
		this.setContentPane(contentPane);
		this.setVisible(true);
		
	}//fin constructeur

	private void constructionContentPane() {

		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setLayout(new BorderLayout());
		
		JLabel titreLabel = new JLabel("Stages du "+stageList.get(0).getDateStr());
		titreLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titreLabel.setFont(new Font("arial", 1, 35));
		contentPane.add(titreLabel,BorderLayout.PAGE_START);
		
		constructionCenterPane();
		contentPane.add(centerPane,BorderLayout.CENTER);
		
	}//fin constructionContentPane()

	private void constructionCenterPane() {
		
		//initialisation du centerPane
		centerPane = new JPanel();
		centerPane.setLayout(new BorderLayout());
		
		//construction du bluePane
		JPanel bluePane = new JPanel();
		bluePane.setBackground(Color.BLUE);
		bluePane.add(new JLabel("               "));
		centerPane.add(bluePane,BorderLayout.WEST);
		
		//le tableau des stages
		ModelStage modele = new ModelStage(stageList);
		tableau = new JTable();
		tableau.setFont(new Font("arial", 1, 16));
		tableau.setModel(modele);
		tableau.setBackground(Color.WHITE);
    	centerPane.add(new JScrollPane(tableau),BorderLayout.CENTER);
		
	}//fin constructionCenterPane()
	
}//fin class

/**
 * 
 * @author BERON Jean-Sebastien
 *
 */
class ModelStage extends AbstractTableModel {
	
	private static final long serialVersionUID = -4189189270303777510L;
	
	//attributs
	private final ArrayList<Stage> stageList;
	private final String[] entetes = {"Code Stage", "Libelle", "Salle", "HeureDébut"};
	
	/**
	 * constructeur
	 * @param stageList
	 */
	public ModelStage(ArrayList<Stage> stageList) {
        super();
        this.stageList = stageList;
    }
	
	/**
	 * retourne le nombre de lignes
	 */
	public int getRowCount() {
		return stageList.size();
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
            	return stageList.get(rowIndex).getCode();
        	case 1:
            	return stageList.get(rowIndex).getLibelle();
        	case 2:
            	return stageList.get(rowIndex).getFirstModule().getSalle();
        	case 3:
            	return stageList.get(rowIndex).getFirstModule().getHeureDebut();
        	default:
            	return null; //Ne devrait jamais arriver
		}//fin switch
		
	}//getValueAt

}//fin class
