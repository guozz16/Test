import java.awt.*;
import javax.swing.*;
import java.awt.Component;
import javax.swing.table.*;
public class DataTableView extends AbstractDataView {
	private StudentTableModel tableModel;
	private JTable table;
	public DataTableView (Data data)
	{
		super(data);
		Object[] columnNames = 
		{"Name","ID","Gender","Age","Department","Origin"};
		tableModel = new StudentTableModel();
		tableModel.setStudent(data.viewData());
		table = new JTable(tableModel);
		table.getTableHeader().setFont(new Font(null, Font.BOLD, 14));
		table.getTableHeader().setReorderingAllowed(false);
		table.setPreferredScrollableViewportSize(new Dimension(1080, 500));
		RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableModel);
		table.setRowSorter(sorter);
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();
		r.setHorizontalAlignment(JLabel.CENTER);
		table.setDefaultRenderer(String.class, r);
		table.setDefaultRenderer(Integer.class, r);
		JScrollPane scrollPane = new JScrollPane(table);
		this.setLayout(new BorderLayout());
		add("Center",scrollPane);
	}

	public void updateDisplay(){
		tableModel.setStudent(getData().viewData());
	}

	public JTable getTable(){
		return table;
	}
}