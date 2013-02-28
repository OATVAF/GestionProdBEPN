package data;

import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

import pack.Module;
import pack.Stage;

import ui.TableModelSorter;

public class ModelModules extends AbstractTableModel
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -1298050647157966932L;
	
	private Stage stage;
	private ArrayList<Module> modules;
	private final String[] entetes = {"Début", "Fin", "Libellé", "Intervenant 1", "Intervenant 1",
			"Moyen"};
	private TableModelSorter sorter;
	private JTable table;
	
	private boolean mod = false;
    
	private static final int[][] colWidths = { 
			{ 50, 80, 65},
			{ 50, 80, 65},
			{ 80,180, 60},
			{ 60,180, 90},
			{ 60,180, 90},
			{ 60,180, 60}
			};

    public ModelModules(JTable _table) {
        super();
        modules = new ArrayList<Module>();
    	table = _table;
		sorter = new TableModelSorter(this);
		table.setModel(sorter);
		TableColumnModel tcm = table.getColumnModel();
    	for (int i =0; i<tcm.getColumnCount(); i++) {
			tcm.getColumn(i).setMinWidth(colWidths[i][0]);
			tcm.getColumn(i).setMaxWidth(colWidths[i][1]);
			tcm.getColumn(i).setPreferredWidth(colWidths[i][2]);
    	}
    	sorter.setTableHeader(table.getTableHeader());

		table.setRowHeight(18);
		FontUIResource uiFont = (FontUIResource) UIManager.get("Table.font");
		Font font = new Font(uiFont.getName(), uiFont.getStyle(), uiFont.getSize()+2);
		table.setFont(font);
		fireTableDataChanged();
    }
	
	public void setStage(Stage s) {
		stage = s;
		modules = stage.getModuleList();
		fireTableDataChanged();
	}
	
	/**
	 * retourne le nombre de lignes
	 */
	public int getRowCount() {
		return modules.size();
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
	
	public void newModule() {
		Module m = new Module((long)-1, stage.getCode(), "", stage.getDateStr(), "18:00","18:15");
		stage.ajoutModule(m);
        setMod(true);
		fireTableDataChanged();
	}
	
    public ArrayList<Module> getSelectedmodules() {
    	ArrayList<Module> ml = new ArrayList<Module>();
    	int[] idx = table.getSelectedRows();
    	for (int i : idx) {
    		if (i >= 0) {
    			ml.add(modules.get(sorter.modelIndex(i)));
    		}
    	}
    	return ml;
    }

	public void removemodules(ArrayList<Module> ml) {
		stage.supprimerModule(ml);
		setMod(true);
		fireTableDataChanged();
	}
	
	public void removeSelectedmodules() {
    	removemodules(getSelectedmodules());
	}

	/**
	 * retourne la valeur de la case du tableau dont les coordonnées sont passés en paramétre
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		switch(columnIndex){
			case 0:
            	return modules.get(rowIndex).getHeureDebut();
        	case 1:
            	return modules.get(rowIndex).getHeureFin();
        	case 2:
            	return modules.get(rowIndex).getLibelle();
        	case 3:
            	return modules.get(rowIndex).getNomLeader()+modules.get(rowIndex).getNomIntervenant();
        	case 4:
            	return modules.get(rowIndex).getNomAide();
        	case 5:
            	return modules.get(rowIndex).getSalle();
        	default:
            	return null;
		}
	}
	
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
    	boolean write = false;
    	switch (columnIndex) {
    		case 0:
    		case 1:
	    	case 2:
	    	case 3:
	    	case 4:
	    	case 5:
	    		write = true;
    	}
    	return write;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if(aValue != null){
            Module s = modules.get(rowIndex);
            
            switch(columnIndex){
            	case 0:
            		s.setHeureDebut((String)aValue);
            		break;
            	case 1:
                	s.setHeureFin((String)aValue);
                    break;
                case 2:
                	s.setLibelle((String)aValue);
                    break;
                case 3:
                	s.setNomLeader((String)aValue);
                    break;
                case 4:
					s.setNomAide((String)aValue);
                   break;
                case 5:
					s.setSalle((String)aValue);
               	break;
                default:
                    System.out.println("[ERR] ModelModules.setValueAt("+columnIndex+")");
            }
            setMod(true);
            System.out.println("Set Value " + columnIndex + " of " + rowIndex);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public Class getColumnClass(int columnIndex){
        switch(columnIndex){
            default:
                return Object.class;
        }
    }

	public boolean isMod() {
		return mod;
	}

	public void setMod(boolean mod) {
		this.mod = mod;
	}
}