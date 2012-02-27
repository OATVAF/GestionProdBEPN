package data;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.swing.table.AbstractTableModel;

public class ModelStages extends AbstractTableModel
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -1298050647157966932L;
	
	private List<Stage> stages = new ArrayList<Stage>();
    private final String[] entetes = {"Id", "Code", "Type", "Avion", "Libellé", "Date"};
    
    public ModelStages() {
        super();
		TypedQuery<Stage> q = DB.em.createQuery("SELECT s FROM Stage s", Stage.class);
		stages = q.getResultList();
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

    public Object getValueAt(int rowIndex, int columnIndex) {
        switch(columnIndex){
            case 0:
                return stages.get(rowIndex).getId();
            case 1:
                return stages.get(rowIndex).getCode();
            case 2:
                return stages.get(rowIndex).getType();
            case 3:
                return stages.get(rowIndex).getAvion();
            case 4:
            	return stages.get(rowIndex).getLibelle();
            case 5:
                return stages.get(rowIndex).getDate();
            default:
                System.out.println("[ERR] ModelStages.getValueAt("+columnIndex+")");
                return null;
       }
    }
    
	/**
	 * constructeur
	 * @param code
	 * @param type
	 * @param avion
	 * @param date
	 */

    public Stage addStage(String code, String type, String avion, Date date) {
    	    DB.em.getTransaction().begin();
    	    Stage stage = new Stage(code, type, avion, date);
    	    DB.em.persist(stage);
    	    stages.add(stage);
    	    DB.em.getTransaction().commit();		
    		System.out.println("[ModelStage] new stage: " + stage.toString());
    		fireTableRowsInserted(stages.size() -1, stages.size() -1);
    		return stage;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
    	boolean write = false;
    	switch (columnIndex) {
	    	case 1:
	    	case 2:
	    	case 3:
	    		write=true;
    	}
    	return write;
    }

    
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if(aValue != null){
            Stage stage = stages.get(rowIndex);
            
    	    DB.em.getTransaction().begin();
            switch(columnIndex){
                case 1:
                	stage.setCode((String)aValue);
                    break;
                case 2:
                	stage.setType((String)aValue);
                    break;
                case 3:
                	stage.setAvion((String)aValue);
                    break;
                case 5:
                	stage.setDate((Date)aValue);
                    break;
                default:
                    System.out.println("[ERR] ModelStages.setValueAt("+columnIndex+")");
            }
    	    DB.em.getTransaction().commit();
            System.out.println("Set Value " + columnIndex + " of " + rowIndex);
        }
    }
    @SuppressWarnings("unchecked")
	@Override
    public Class getColumnClass(int columnIndex){
        switch(columnIndex){
            case 5 :
                return Date.class;
            default:
                return Object.class;
        }
    }
    
	public Stage getStage(String code) {
		Stage res = null;
		try {
			TypedQuery<Stage> q = DB.em.createQuery("SELECT s FROM Stage s WHERE s.code = :code", Stage.class);
			res = q.setParameter("code",code).getSingleResult();
		}
		catch (NoResultException e) {
			return null;
		}
		return res;
	}
	
	public Stage getStage(String code, Date date) {
		Stage res = null;
		try {
			TypedQuery<Stage> q = DB.em.createQuery("SELECT s FROM Stage s WHERE s.code = :code AND s.date = :date", Stage.class);
			q.setParameter("code",code);
			q.setParameter("date",date);
			res = q.getSingleResult();
		}
		catch (NoResultException e) {
			return null;
		}
		return res;
	}
	
	public List<Stage> getStageTV(Date date) {
		List<Stage> res = null;
		try {
			TypedQuery<Stage> q = DB.em.createQuery("SELECT s FROM Stage s WHERE s.date >= {d '2012-02-26'}", Stage.class);
			res = q.getResultList();
		}
		catch (NoResultException e) {
			return null;
		}
		return res;
	}

	public void removeStage(int rowIndex) {
		Stage stage = stages.get(rowIndex);
	    DB.em.getTransaction().begin();
	    DB.em.remove(stage);
	    DB.em.getTransaction().commit();		
		System.out.println("[ModelStage] remove stage: " + stage.toString());
    	stages.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
	}
}
