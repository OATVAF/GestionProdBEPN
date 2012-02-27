package ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.FlowLayout;
import javax.swing.border.BevelBorder;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.JScrollPane;
import javax.swing.ButtonGroup;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Action;

import data.DB;
import data.Import;

import java.awt.event.ActionListener;
import java.sql.Date;

public class MainWin extends JFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8313530912845638478L;
	
	private JTable table;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private final Action _AddAction = new AddAction();
	private final Action _DeleteAction = new DeleteAction();
	private final Action _TVAction = new TVAction();
	private final Action _Import = new ImportAction();

	/**
	 * Create the application.
	 */
	public MainWin() {
		Config.Read();
		DB.Open();
		System.out.println(Config.get("app.version")); //$NON-NLS-1$

		/*
		DB.Purge();
		Import.ImportDelia();
		Import.ImportPNC();
		*/
		
		initialize();
		setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("Window Closing Event"); //$NON-NLS-1$
				DB.Close();
				Config.Write();
			}
		});
		setBounds(100, 100, 620, 440);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel Header = new JPanel();
		Header.setBackground(Color.WHITE);
		getContentPane().add(Header, BorderLayout.NORTH);
		Header.setLayout(new BorderLayout(0, 0));
		
		JLabel lblLogo = new JLabel(""); //$NON-NLS-1$
		lblLogo.setIcon(new ImageIcon(MainWin.class.getResource("/res/Airfrance.jpg"))); //$NON-NLS-1$
		Header.add(lblLogo, BorderLayout.WEST);
		
		JLabel lbTitle = new JLabel(Messages.getString(Messages.getString("MainWin.titleLabel"))); //$NON-NLS-1$
		lbTitle.setForeground(UIManager.getColor("Button.light")); //$NON-NLS-1$
		lbTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lbTitle.setFont(new Font("Lucida Grande", Font.BOLD, 16)); //$NON-NLS-1$
		Header.add(lbTitle, BorderLayout.CENTER);
		
		JLabel lbVersion = new JLabel(Messages.getString("MainWin.versionLabel")); //$NON-NLS-1$
		lbVersion.setForeground(UIManager.getColor("Button.light")); //$NON-NLS-1$
		lbVersion.setVerticalAlignment(SwingConstants.TOP);
		lbVersion.setHorizontalAlignment(SwingConstants.RIGHT);
		lbVersion.setFont(new Font("Lucida Grande", Font.PLAIN, 12)); //$NON-NLS-1$
		Header.add(lbVersion, BorderLayout.EAST);
		
		JPanel Tools = new JPanel();
		Tools.setBackground(UIManager.getColor("Button.light")); //$NON-NLS-1$
		Tools.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		getContentPane().add(Tools, BorderLayout.WEST);
		GridBagLayout gbl_Tools = new GridBagLayout();
		gbl_Tools.columnWidths = new int[]{98, 0};
		gbl_Tools.rowHeights = new int[]{29, 29, 29, 0, 0};
		gbl_Tools.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_Tools.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		Tools.setLayout(gbl_Tools);
		
		JButton btImport = new JButton(Messages.getString("MainWin.ImportBtn")); //$NON-NLS-1$
		btImport.setAction(_Import);
		btImport.setAlignmentX(Component.CENTER_ALIGNMENT);
		GridBagConstraints gbc_btImport = new GridBagConstraints();
		gbc_btImport.fill = GridBagConstraints.HORIZONTAL;
		gbc_btImport.insets = new Insets(0, 0, 5, 0);
		gbc_btImport.gridx = 0;
		gbc_btImport.gridy = 0;
		Tools.add(btImport, gbc_btImport);
		
		JToggleButton btEdit = new JToggleButton(Messages.getString("MainWin.EditerBtn")); //$NON-NLS-1$ //$NON-NLS-1$
		btEdit.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonGroup.add(btEdit);
		GridBagConstraints gbc_btEdit = new GridBagConstraints();
		gbc_btEdit.fill = GridBagConstraints.HORIZONTAL;
		gbc_btEdit.insets = new Insets(0, 0, 5, 0);
		gbc_btEdit.gridx = 0;
		gbc_btEdit.gridy = 1;
		Tools.add(btEdit, gbc_btEdit);
		
		JToggleButton btPublish = new JToggleButton(Messages.getString("MainWin.PublierBtn")); //$NON-NLS-1$ //$NON-NLS-1$
		btPublish.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonGroup.add(btPublish);
		GridBagConstraints gbc_btPublish = new GridBagConstraints();
		gbc_btPublish.insets = new Insets(0, 0, 5, 0);
		gbc_btPublish.fill = GridBagConstraints.HORIZONTAL;
		gbc_btPublish.gridx = 0;
		gbc_btPublish.gridy = 2;
		Tools.add(btPublish, gbc_btPublish);
		
		JButton btTV = new JButton(Messages.getString("MainWin.btTV.text")); //$NON-NLS-1$
		btTV.setAction(_TVAction);
		btTV.setAlignmentX(0.5f);
		GridBagConstraints gbc_btTV = new GridBagConstraints();
		gbc_btTV.fill = GridBagConstraints.HORIZONTAL;
		gbc_btTV.gridx = 0;
		gbc_btTV.gridy = 3;
		Tools.add(btTV, gbc_btTV);
		
		JPanel View = new JPanel();
		getContentPane().add(View, BorderLayout.CENTER);
		View.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		View.add(panel, BorderLayout.NORTH);
		
		JLabel lblDate = new JLabel(Messages.getString("MainWin.lblDate.text")); //$NON-NLS-1$
		lblDate.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(lblDate);
		
		JComboBox comboBox = new JComboBox();
		panel.add(comboBox);
		
		JPanel panel_1 = new JPanel();
		View.add(panel_1, BorderLayout.SOUTH);
		
		JButton btnNewButton = new JButton(Messages.getString("MainWin.btnDelete.text")); //$NON-NLS-1$
		btnNewButton.setAction(_DeleteAction);
		panel_1.add(btnNewButton);
		
		JButton btnAjouter = new JButton(Messages.getString("MainWin.btnAdd.text")); //$NON-NLS-1$
		btnAjouter.setAction(_AddAction);
		panel_1.add(btnAjouter);
		
		JPanel panel_2 = new JPanel();
		View.add(panel_2, BorderLayout.CENTER);

		panel_2.setLayout(new BorderLayout(0, 0));

		table = new JTable(DB.modelStages);

		JScrollPane scrollPane = new JScrollPane(table);
		panel_2.add(scrollPane, BorderLayout.CENTER);
		
	}

	private class AddAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public AddAction() {
			putValue(NAME, Messages.getString("MainWin._AddAction.name")); //$NON-NLS-1$
			putValue(SHORT_DESCRIPTION, Messages.getString("MainWin._Add.short")); //$NON-NLS-1$
		}
		public void actionPerformed(ActionEvent e) {
			DB.modelStages.addStage("CODE", "TYPE", "AVION", new Date(new java.util.Date().getTime())); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	}
	private class DeleteAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public DeleteAction() {
			putValue(NAME, Messages.getString("MainWin._DeleteAction.name")); //$NON-NLS-1$
			putValue(SHORT_DESCRIPTION, Messages.getString("MainWin._Delete.short")); //$NON-NLS-1$
		}
		public void actionPerformed(ActionEvent e) {
            int[] selection = table.getSelectedRows();
            
            for(int i = selection.length - 1; i >= 0; i--){
            	DB.modelStages.removeStage(selection[i]);
            }

		}
	}
	private class ImportAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public ImportAction() {
			putValue(NAME, Messages.getString("MainWin._Import.name")); //$NON-NLS-1$
			putValue(SHORT_DESCRIPTION, Messages.getString("MainWin._Import.short")); //$NON-NLS-1$
		}
		public void actionPerformed(ActionEvent e) {
			DB.Purge();
			Import.ImportDelia();
			Import.ImportPNC();
		}
	}
	private class TVAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public TVAction() {
			putValue(NAME, Messages.getString("MainWin.btnTV")); //$NON-NLS-1$
			putValue(SHORT_DESCRIPTION, Messages.getString("MainWin.btnTV.short")); //$NON-NLS-1$
		}
		public void actionPerformed(ActionEvent e) {
			TVWin tv = new TVWin();
		}
	}
}
