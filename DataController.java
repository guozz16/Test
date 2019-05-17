import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;

public class DataController extends JPanel {
	private Data data;
	private JTable table;
	private CardLayout card;
	private int searchCount;
	private String searchName;
	private String searchID;
	public DataController(Data controlledData,JTable controlledTable)
	{
		super();
		data = controlledData;
		table = controlledTable;
		card = new CardLayout();
		searchCount = 0;
		searchName = "";
		searchID = "";
		setLayout(card);
		add("Add",buildAddPanel());
		add("Search",buildSearchPanel());
		
	}

	public JPanel buildAddPanel(){
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		JPanel addPanel = new JPanel();
		JTextField name = new JTextField(10);
		JTextField id = new JTextField(10);
		String[] choice = {"Male","Female"};
		JComboBox gender = new JComboBox(choice);
		JTextField age = new JTextField(4);
		JTextField department = new JTextField(10);
		JTextField origin = new JTextField(10);
		JButton addButton = new JButton("Add");
		addPanel.add(new JLabel("Add Student:"));
		addPanel.add(name);
		addPanel.add(id);
		addPanel.add(gender);
		addPanel.add(age);
		addPanel.add(department);
		addPanel.add(origin);
		addPanel.add(addButton);
		JScrollPane pane = new JScrollPane(addPanel);
		panel.add("Center",pane);
		addButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
					if(name.getText().length()==0){
						JOptionPane.showMessageDialog(
							null,
							"Student name cannot be empty!","Error",
							JOptionPane.ERROR_MESSAGE);
					}
					else{
						data.addStudent(
						name.getText(),
						String.valueOf(Integer.parseInt(id.getText())),
						String.valueOf(gender.getSelectedItem()),
						Integer.parseInt(age.getText()),
						department.getText(),
						origin.getText()
						);
						int row = table.getRowCount()-1;
						table.setRowSelectionInterval(row,row);
						table.scrollRectToVisible(table.getCellRect(row,0,true));
					JOptionPane.showMessageDialog(
						null,
						"Student information added successfully!","Message",
						JOptionPane.PLAIN_MESSAGE);
					}
				}

				catch(NumberFormatException exception){
					JOptionPane.showMessageDialog(
						null,
						"Please enter valid id number and age number","Error",
						JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		return panel;
	}
	public void showAddPanel(){
		card.show(this,"Add");
	}
	public JPanel buildSearchPanel(){
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		JPanel searchPanel = new JPanel();
		JTextField name = new JTextField(10);
		JTextField id = new JTextField(10);
		JButton searchButton = new JButton("Search");
		searchButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String newSearchName = name.getText();
				String newSearchID = id.getText();
				if(newSearchName.length()==0 && 
					newSearchID.length()==0){
					JOptionPane.showMessageDialog(
						null,
						"Please enter at least one of name and ID","Error",
						JOptionPane.ERROR_MESSAGE);
				}
				else{
					if(searchName.equals(newSearchName) && 
						searchID.equals(newSearchID)){
						searchCount++;
					}
					else{
						searchName=newSearchName;
						searchID=newSearchID;
						searchCount=0;
					}
					List<Integer> list = data.findStudentWithName(searchName);
					list.retainAll(data.findStudentWithId(searchID));
					if(list.size()==0){
						JOptionPane.showMessageDialog(
							null,
							"No student matched.","Warning",
							JOptionPane.WARNING_MESSAGE);
					}
					else if(searchCount==list.size()){
						searchCount = 0;
						int row = list.get(0);
						table.setRowSelectionInterval(row,row);
						table.scrollRectToVisible(table.getCellRect(row,0,true));
					}
					else{
						int row = list.get(searchCount);
						table.setRowSelectionInterval(row,row);
						table.scrollRectToVisible(table.getCellRect(row,0,true));
					}
				}
			}
		});
		searchPanel.add(new JLabel("Name:"));
		searchPanel.add(name);
		searchPanel.add(new JLabel("ID:"));
		searchPanel.add(id);
		searchPanel.add(searchButton);
		JScrollPane pane = new JScrollPane(searchPanel);
		panel.add("Center",pane);
		return panel;
	}
	public void showSearchPanel(){
		card.show(this,"Search");
	}
}