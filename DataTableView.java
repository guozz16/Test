import java.awt.*;
import javax.swing.*;
import java.awt.Component;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
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
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();
		r.setHorizontalAlignment(JLabel.CENTER);
		table.setDefaultRenderer(Object.class, r);
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