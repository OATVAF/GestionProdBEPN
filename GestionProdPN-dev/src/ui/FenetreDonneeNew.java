package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ChangeListener;

import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.ListSelectionModel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;

import org.jdesktop.swingworker.SwingWorker;

import pack.Stage;

import data.ModelModules;
import data.ModelStages;
import data.ModelStagiaires;
import pack.Config;

/**
 * Fenetre qui permet a l'utilisateur de modifier les données importées<br>
 * il pourra choisir la date et le stage afin d'en changer plusieurs données<br>
 * tel que le libelle, la salle, le leader, et des modificationsde stagiaires
 * @author BERON Jean-Sébastien
 *
 */
public class FenetreDonneeNew extends JFrame implements ActionListener, PropertyChangeListener {

	private static final long serialVersionUID = 3417928306187912598L;
	
	//attributs IHM
	private JPanel contentPane;//conteneur principal
	private JPanel centerPane;//conteneur au center de la fenetre
	private JPanel bluePane;//conteneur servant de décor
	private JPanel selPane;
	private JPanel choixPane;// conteneur contenant les listes déroulantes
	private JPanel barPane;


	private JComboBox dateBox;
	private JComboBox stageBox;
	private JLabel dateLbl;
	private JLabel stageLbl;
	private JButton addBtn;
	private JButton removeBtn;
	private JButton importBtn;
	
	//attributs qui vont contenir les données temporaires
	//private ArrayList<Stage> stageList;//liste des stages
	private JTabbedPane tabbedPane;

	private JScrollPane stagePane;
	private JScrollPane stagiairePane;
	private JScrollPane modulePane;

	private JTable tableStages;
	private JTable tableStagiaires;
	private JTable tableModules;

	
	private ModelStages ms;
	private ModelStagiaires mss;
	private ModelModules msm;

	private JProgressBar progressBar;
	private JTextPane statusPane;
	private StatusBar statusBar;

	private static final int STAGES_IDX = 0;
	private static final int STAGIAIRES_IDX = 1;
	private static final int MODULES_IDX = 2;

	private static FenetreDonneeNew FDN;
	
	private Task task;

	
	class Task extends SwingWorker<Void, Void> {
		
        /*
         * Main task. Executed in background thread.
         */
		@Override
        public Void doInBackground() {
			int progress = 0;
            setProgress(0);

            while (! isCancelled() && (progress<=100)) {
            	setProgress(progress++);
            	try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}
            }
			return null;
		}
		
        @Override
        public void done() {
            Toolkit.getDefaultToolkit().beep();
            setCursor(null); //turn off the wait cursor
            statusBar.update(Messages.getString("FenetreDonneeNew.savedMsg"), 0);
        }

	}
	
	//-Dswing.defaultlaf=com.sun.java.swing.plaf.windows.WindowsLookAndFeel
	/**
	 * constructeur de la fenetre
	 */
	public FenetreDonneeNew(){
		
		//chargement des listes
		//chargerList();
		
		//formation de la fenetre (titre, taille, etc.)
		this.setTitle(Messages.getString("FenetreDonneeNew.Titre")); //$NON-NLS-1$
		this.setSize(900, 600);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		FDN=this;
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("Window Closing Event"); //$NON-NLS-1$
				if (ms.isMod() || mss.isMod() || msm.isMod()) {
					//fenetre de dialogue
					int rep = JOptionPane.showConfirmDialog(null, Messages.getString("FenetreDonneeNew.saveMsg") //$NON-NLS-1$
							,Messages.getString("FenetreDonneeNew.Mod"),JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE, new ImageIcon(Config.getRes("save.png"))); //$NON-NLS-1$
					//si la reponse est "oui"
					if(rep == JOptionPane.YES_OPTION) {
						setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
						task = new Task();
						task.addPropertyChangeListener(FDN);
						task.execute();
						ms.saveStages();
						task.cancel(true);
						//JOptionPane.showMessageDialog(null, Messages.getString("FenetreDonneeNew.savedMsg"),
						//		Messages.getString("FenetreDonneeNew.saveTitle"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
					}
				}
			}
		});

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
		
		selPane = new JPanel();
		centerPane.add(selPane, BorderLayout.CENTER);
		selPane.setLayout(new BorderLayout(0, 0));
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		selPane.add(tabbedPane);
		tabbedPane.addChangeListener(new ChangeListener() {
			    // This method is called whenever the selected tab changes
			    public void stateChanged(ChangeEvent evt) {
			        JTabbedPane pane = (JTabbedPane)evt.getSource();
			        // Get current tab
			        switch (pane.getSelectedIndex()) {
			        case 0:
			        	stageBox.setEnabled(false);
			    		importBtn.setEnabled(false);
			        	break;
			        case 1:			        	
			        case 2:
			        	stageBox.setEnabled(true);
			    		importBtn.setEnabled(pane.getSelectedIndex()==1);
			        	Stage s = ms.getSelectedStage();
			        	if (s != null) {
			        		mss.setStage(s);
			        		msm.setStage(s);
			        		stageBox.setSelectedItem(s);
			        	}
			        }
			    }});
		
		dateBox = new JComboBox();
		stageBox = new JComboBox();
		importBtn = new JButton();
		barPane = new JPanel();
		progressBar = new JProgressBar();
		statusPane = new JTextPane();
		statusBar = new StatusBar(statusPane, progressBar);

		tableStages = new JTable();
		tableStages.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		ms = new ModelStages(tableStages);
		stagePane = new JScrollPane(tableStages);
		ms.selDate("1/1/1");
		tabbedPane.addTab(Messages.getString("FenetreDonneeNew.Stages"), null, stagePane, null); //$NON-NLS-1$
		
		tableStagiaires = new JTable();
		tableStagiaires.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		mss = new ModelStagiaires(tableStagiaires);
		stagiairePane = new JScrollPane(tableStagiaires);
		tabbedPane.addTab(Messages.getString("FenetreDonneeNew.Stagiaires"), null, stagiairePane, null); //$NON-NLS-1$
		
		tableModules = new JTable();
		tableModules.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		msm = new ModelModules(tableModules);
		modulePane = new JScrollPane(tableModules);
		tabbedPane.addTab(Messages.getString("FenetreDonneeNew.Modules"), null, modulePane, null);
		//tabbedPane.addChangeListener();
		
		//initialisation du choixPane
		choixPane = new JPanel();
		choixPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		choixPane.setBackground(Color.WHITE);
		selPane.add(choixPane, BorderLayout.NORTH);

		//initialisation et remplissage de dataBox : liste déroulante contenant les dates
		dateLbl = new JLabel(Messages.getString("FenetreDonneeNew.Date")); //$NON-NLS-1$
		choixPane.add(dateLbl);

		dateBox.addActionListener(this);
		dateBox.setModel(ms.dateModel);
		dateBox.setSelectedIndex(ms.dateModel.getSize()-1);
		choixPane.add(dateBox);
		
		stageLbl = new JLabel(Messages.getString("FenetreDonneeNew.Stage")); //$NON-NLS-1$
		choixPane.add(stageLbl);
		
		stageBox.addActionListener(this);
		stageBox.setModel(ms.stageModel);
		//stageBox.setSelectedIndex(ms.stageModel.getSize()-1);
		choixPane.add(stageBox);
		addBtn = new JButton(Messages.getString("FenetreDonneeNew.Add")); //$NON-NLS-1$
		choixPane.add(addBtn);
		addBtn.setIcon(new ImageIcon(Config.getRes("modif.png"))); //$NON-NLS-1$
		addBtn.setPreferredSize(new Dimension(160, 35));
		removeBtn = new JButton(Messages.getString("FenetreDonneeNew.Remove")); //$NON-NLS-1$
		choixPane.add(removeBtn);
		removeBtn.setIcon(new ImageIcon(Config.getRes("remove.png"))); //$NON-NLS-1$
		removeBtn.setPreferredSize(new Dimension(160, 35));
		importBtn.setText(Messages.getString("FenetreDonneeNew.Import"));
		choixPane.add(importBtn);
		importBtn.setPreferredSize(new Dimension(160, 35));
		importBtn.setIcon(new ImageIcon(Config.getRes("import.png"))); //$NON-NLS-1$
		
		removeBtn.addActionListener(this);
		addBtn.addActionListener(this);
		importBtn.addActionListener(this);
	
		// Bar
		
		selPane.add(barPane, BorderLayout.SOUTH);
		barPane.setBackground(Color.WHITE);
		barPane.setLayout(new BorderLayout(5, 5));
		barPane.add(progressBar, BorderLayout.EAST);
		barPane.add(statusPane, BorderLayout.CENTER);		
		statusBar.select();
		statusBar.update(null, 0);

	}//fin constructionCenterPane()
	
	/**
	 * procedure de l'interface ActionListener<br/>
	 * execute les modifications par rapport au bouton selectionné
	 */
	public void actionPerformed(ActionEvent e) {
		int i = tabbedPane.getSelectedIndex();
		
		// Status
		statusBar.setMax(100);
		statusBar.update(null, 0);

		//récupération de la source
		JComponent source = (JComponent) e.getSource();
		
		if (source.equals(dateBox)) {
			ms.selDate((String) dateBox.getSelectedItem());
		}
		else if (source.equals(stageBox)) {
			mss.setStage(stageBox.getSelectedItem());
    		msm.setStage(stageBox.getSelectedItem());
		}
		else if (source.equals(addBtn)) {
			switch (i) {
			case STAGES_IDX:
				ms.newStage();
				break;
			case STAGIAIRES_IDX:
				mss.newStagiaire();
				break;
			case MODULES_IDX:
				msm.newModule();
				break;
			}
		}

		else if(source.equals(removeBtn)){
			int rep = JOptionPane.NO_OPTION;
				        
        	String msg = tabbedPane.getTitleAt(i);
			rep = JOptionPane.showConfirmDialog(null, "<html>Supprimer les " + msg +" sélectionnés du "+ms.filterDate+" ?</html>"
					,Messages.getString("FenetreDonneeNew.Mod"),JOptionPane.YES_NO_OPTION); //$NON-NLS-1$
			//si la reponse est "oui"
			if(rep == JOptionPane.YES_OPTION) {
				switch (i) {
				case STAGES_IDX:
					ms.removeSelectedStages();
					break;
				case STAGIAIRES_IDX:
					mss.removeSelectedStagiaires();
					break;
				case MODULES_IDX:	
					msm.removeSelectedmodules();
					break;
				}
			}
		}
		else if(source.equals(importBtn)){
			String split[];
			try {
				String data = (String) Toolkit.getDefaultToolkit()
				        .getSystemClipboard().getData(DataFlavor.stringFlavor);
				if (data.length() > 0) {
					// convert \/ to \n
					data = data.replaceAll("\\s*\\/\\s*", "\r");
					// convert String into InputStream
					InputStream is = new ByteArrayInputStream(data.getBytes());
					// read it with BufferedReader
					BufferedReader br = new BufferedReader(new InputStreamReader(is));

					String line, spe, nom, pnom, prenom, sect;
					int jn=0;
					while ((line = br.readLine()) != null) {
						System.out.println("Import :"+line);
						spe=nom=pnom=prenom=sect="";
						split = line.split("[\\t\\s]+");
						for (int j=0; j<split.length; j++) {
							split[j]=split[j].replaceAll(Config.get("imp.clients.rem"), "");
							if (split[j].matches(Config.get("imp.clients.spe"))) {
								spe=split[j];
							}
							else if (split[j].matches(Config.get("imp.clients.sec"))) {
								sect=split[j];
							}
							else if (split[j].matches(Config.get("imp.clients.nom"))) {
								pnom=nom; jn=j;
								nom += (nom.length()>0 ? " ": "") + split[j];
							}
							else if (split[j].matches(Config.get("imp.clients.pre"))) {
								prenom += (prenom.length()>0 ? " ": "") + split[j];
							}
						}
						if (prenom == "" && jn > 1) {
							nom=pnom;
							prenom=split[jn];
						}
						mss.newStagiaire(nom, prenom, spe, sect);
					}
				}
			} catch (HeadlessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (UnsupportedFlavorException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
		}
	}//fin actionPerformed()

	public void propertyChange(PropertyChangeEvent evt) {
	    if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            statusBar.update(progress);
        }
	}
	
}//fin class
