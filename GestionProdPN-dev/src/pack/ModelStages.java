package pack;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.event.ListDataListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

public class ModelStages extends AbstractTableModel
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -1298050647157966932L;
	
	private List<Stage> stagesAll = new ArrayList<Stage>();
	private List<Stage> stages = new ArrayList<Stage>();
	public ComboModel dateModel = new ComboModel();
	
    private final String[] entetes = {"Cie", "Code" , "Libellé", "Leader", "Salle", "Heure"};
	private int[][] colWidths = { 
			{ 20, 40, 40},
			{ 60,250,100},
			{ 80,500,150},
			{ 60,120, 90},
			{ 60,120, 90},
			{ 60, 70, 60}
			};

    public ModelStages() {
        super();
		//chargement des stages J et J+1
		stagesAll = PasserelleStage.lectureStageObj();
		//initialisation de dateList
		//dates = new ArrayList<String>();
		for (Stage s : stagesAll) {
			dateModel.add(s.getDateStr());
		}
    }

    public void selDate(String date) {
		stages = new ArrayList<Stage>();
		for (Stage s : stagesAll) {
    		if(s.getDateStr().equals(date)){
    			stages.add(s);
    		}
    	}
		//tri des stages par ordre alphabetique
		Stage stgTmp ;
		boolean good = false;
		//tant que le tri n'est pas bon
		while (! good) {
			good = true;
			for (int i = 0; i < stages.size()-1; i++) {
				if(stages.get(i).getCode().compareToIgnoreCase(stages.get(i+1).getCode()) > 0){
					good = false;
					//echange
					stgTmp = stages.get(i);
					stages.set(i, stages.get(i+1));
					stages.set(i+1, stgTmp);
				}
			}
		}
		fireTableDataChanged();
		//fireTableRowsInserted(stages.size() -1, stages.size() -1);
    }
    
    public void setColWidth(TableColumnModel tcm) {
    	for (int i =0; i<tcm.getColumnCount(); i++) {
    			tcm.getColumn(i).setMinWidth(colWidths[i][0]);
    			tcm.getColumn(i).setMaxWidth(colWidths[i][1]);
    			tcm.getColumn(i).setPreferredWidth(colWidths[i][2]);
    	}
    }
    
    public int getRowCount() {
		//TypedQuery<Long> q = DB.em.createQuery("SELECT COUNT(s) FROM Stage s", Long.class);
	    //return q.getSingleResult().intValue();
    	return stages.size();
    }

    public int getColumnCount() {
        return entetes.length;
    }

    public String getColumnName(int columnIndex) {
        return entetes[columnIndex];
    }
    
    public Stage getSelectedStage(int rowIndex) {
    	return stages.get(rowIndex);
    }
    
    public Object getValueAt(int rowIndex, int columnIndex) {
		switch(columnIndex){
    	case 0:
        	return stages.get(rowIndex).getCompagnie();
    	case 1:
        	return stages.get(rowIndex).getCode();
    	case 2:
        	return stages.get(rowIndex).getLibelle();
    	case 3:
        	return stages.get(rowIndex).getLeader();
    	case 4:
        	return stages.get(rowIndex).getFirstModule().getSalle();
    	case 5:
        	return stages.get(rowIndex).getFirstModule().getHeureDebut();
    	default:
    		System.out.println("[ERR] ModelStages.getValueAt("+columnIndex+")");
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
	    		write=true;
    	}
    	return write;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if(aValue != null){
            Stage stage = stages.get(rowIndex);
            
            switch(columnIndex){
                case 0:
                	stage.setCompagnie((String)aValue);
                    break;
                case 1:
                	stage.setCode((String)aValue);
                    break;
                case 2:
                	stage.setLibelle((String)aValue);
                    break;
                case 3:
					stage.setLeader((String)aValue);
                   break;
                case 4:
                	stage.getFirstModule().setSalle((String)aValue);
               	break;
                default:
                    System.out.println("[ERR] ModelStages.setValueAt("+columnIndex+")");
            }
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
}

class ComboModel extends AbstractListModel implements ComboBoxModel
{
	private static final long serialVersionUID = 4011116690928828937L;

	List<String> list = new ArrayList<String>();
	String sel = null;
	
	public void setList(List<String> l) {
		list = l;
	}
	
	public void add(String s) {
		if (! list.contains(s)) {
			list.add(s);
		}
	}

	public Object getElementAt(int index) {
		return list.get(index);
	}

	public int getSize() {
		return list.size();
	}

	public Object getSelectedItem() {
		return sel;
	}

	public void setSelectedItem(Object anItem) {
		sel = (String)anItem;
	}
}

