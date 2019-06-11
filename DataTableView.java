import java.awt.*;
import javax.swing.*;
import java.awt.Component;
import javax.swing.table.*;
import java.util.*;
public class DataTableView extends AbstractDataView {
	private DataTableModel tableModel;
	private JTable table;
	private String objType;
	public DataTableView (MapObjRegister register, String objType)
	{
		super(register);
		this.objType = objType;
		Set<String> columnNames = register.getRegisteredDataObj("0",objType).keySet();
		tableModel = new DataTableModel(columnNames);
		tableModel.setData(register.viewData(objType));
		table = new JTable(tableModel);
		table.getTableHeader().setFont(new Font(null, Font.BOLD, 14));
		table.getTableHeader().setReorderingAllowed(false);
		table.setPreferredScrollableViewportSize(new Dimension(1080, 500));
		table.setAutoResizeMode(table.AUTO_RESIZE_OFF);
		RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableModel);
		table.setRowSorter(sorter);
		// DefaultTableCellRenderer r = new DefaultTableCellRenderer();
		// r.setHorizontalAlignment(JLabel.CENTER);
		// table.setDefaultRenderer(String.class, r);
		// table.setDefaultRenderer(Integer.class, r);
		JScrollPane scrollPane = new JScrollPane(table
			,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.setLayout(new BorderLayout());
		add("Center",scrollPane);
	}

	public void updateDisplay(){
		tableModel.setData(getData().viewData(objType));
	}

	public JTable getTable(){
		return table;
	}
}