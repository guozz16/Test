import javax.swing.table.*;
import java.util.*;

public class StudentTableModel extends AbstractTableModel {

	//保存一个Student的列表
	List<Student> stuList = new ArrayList<Student>();
	private Vector tableTitle;

	public StudentTableModel(){
		tableTitle = new Vector();
		tableTitle.add("Name");
		tableTitle.add("ID");
		tableTitle.add("Gender");
		tableTitle.add("Age");
		tableTitle.add("Department");
		tableTitle.add("Origin");

	}

	public String getColumnName(int column){
		return (String)this.tableTitle.get(column);
	}
	//设置Student列表
	public void setStudent(final List<Student> list){
		stuList = list;
		fireTableDataChanged();
	}
	
	//返回JTable的列数
	public int getColumnCount() {
		return tableTitle.size();
	}

	//返回JTable的行数
	public int getRowCount() {
		return stuList.size();
	}

	// 从List中拿出rowIndex行columnIndex列显示的值     用于设置该TableModel指定单元格的值
	public Object getValueAt(int rowIndex, int columnIndex) {
		Student student = stuList.get(rowIndex); // 获取当前行的Student
		switch (columnIndex) { // 根据列,返回值
		case 0:
			return student.getName();   
		case 1:
			return student.getID();     
		case 2:
			return student.getGender();       
		case 3:
			return student.getAge(); 
		case 4:
			return student.getDepartment();   
		case 5:
			return student.getOrigin();  
		default:
			break;
		}
		return null;
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
		return returnValue;
	}
}