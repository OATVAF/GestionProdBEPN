package data;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

public class ModelCombo extends AbstractListModel implements ComboBoxModel
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
