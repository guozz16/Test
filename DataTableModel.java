import javax.swing.table.*;
import java.util.*;

public class DataTableModel extends AbstractTableModel {

	List<HashMap> objList = new ArrayList<HashMap>();
	private Vector<String> tableTitle;

	public DataTableModel(Set<String> columnNames){
		tableTitle = new Vector<String>();
		tableTitle.add("id");
		tableTitle.add("name");
		for(String s:columnNames){
			if(!tableTitle.contains(s))
				tableTitle.add(s);
		}
	}

	public String getColumnName(int column){
		return (String)this.tableTitle.get(column);
	}

	public void setData(final List<HashMap> list){
		objList = list;
		fireTableDataChanged();
	}
	
	public int getColumnCount() {
		return tableTitle.size();
	}

	public int getRowCount() {
		return objList.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		HashMap obj = objList.get(rowIndex); // 获取当前行的Student
		String key = tableTitle.get(columnIndex);
		if(obj.containsKey(key)){
			return obj.get(key);
		}
		else{
			return null;
		}
	}

	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return false;
	}

	public Class getColumnClass(int column)
	{
		Class returnValue;
		if ((column >= 0) && (column < getColumnCount()))
		{
			returnValue = getValueAt(0, column).getClass();
		}
		else
		{
			returnValue = Object.class;
		}
		return Object.class;
	}
}