import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.table.*;

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
		add("Filter",buildFilterPanel());
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
		addPanel.add(buildUpdateButton());
		addPanel.add(buildDeleteButton());
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
					else if(department.getText().length()==0){
						JOptionPane.showMessageDialog(
							null,
							"Student department cannot be empty!","Error",
							JOptionPane.ERROR_MESSAGE);
					}
					else if(origin.getText().length()==0){
						JOptionPane.showMessageDialog(
							null,
							"Student origin cannot be empty!","Error",
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
						int row = data.getSize()-1;
						row=table.convertRowIndexToView(row);
						if(row!=-1){
							table.setRowSelectionInterval(row,row);
							table.scrollRectToVisible(table.getCellRect(row,0,true));
						}
						
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
	//Locate controll panel in AddStudent
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
						"Please enter at least one of name or ID","Error",
						JOptionPane.ERROR_MESSAGE);
				}
				else{
					if(!(searchName.equals(newSearchName) &&
						searchID.equals(newSearchID))){
						searchName = newSearchName;
						searchID = newSearchID;
						searchCount=0;
					}
					List<Integer> list = data.findStudentWithName(searchName);
					list.retainAll(data.findStudentWithId(searchID));
					Iterator<Integer> it = list.iterator();
					while(it.hasNext()){
						Integer x = it.next();
						x=table.convertRowIndexToView(x);
						if(x==-1){
							it.remove();
						}
					}
					if(list.size()==0){
						JOptionPane.showMessageDialog(
							null,
							"No student matched!","Warning",
							JOptionPane.WARNING_MESSAGE);
					}
					else if(searchCount==list.size()-1){
						int row = list.get(searchCount);
						row=table.convertRowIndexToView(row);
						table.setRowSelectionInterval(row,row);
						table.scrollRectToVisible(table.getCellRect(row,0,true));
						searchCount=0;
					}
					else{
						int row = list.get(searchCount);
						row=table.convertRowIndexToView(row);
						table.setRowSelectionInterval(row,row);
						table.scrollRectToVisible(table.getCellRect(row,0,true));
						searchCount++;
					}
				}
			}
		});
		searchPanel.add(new JLabel("Name:"));
		searchPanel.add(name);
		searchPanel.add(new JLabel("ID:"));
		searchPanel.add(id);
		searchPanel.add(searchButton);
		searchPanel.add(buildUpdateButton());
		searchPanel.add(buildDeleteButton());
		JScrollPane pane = new JScrollPane(searchPanel);
		panel.add("Center",pane);
		return panel;
	}
	//Locate controll panel in SearchStudent
	public void showSearchPanel(){
		card.show(this,"Search");
		searchCount=0;
	}

	public JPanel buildFilterPanel(){
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		JPanel filterPanel = new JPanel();
		JTextField include = new JTextField(10);
		JTextField exclude = new JTextField(10);
		JButton filterButton = new JButton("Filter");
		JButton resetButton = new JButton("Reset");
		filterPanel.add(new JLabel("Include:"));
		filterPanel.add(include);
		filterPanel.add(new JLabel("Exclude:"));
		filterPanel.add(exclude);
		filterPanel.add(filterButton);
		filterPanel.add(resetButton);
		filterPanel.add(buildUpdateButton());
		filterPanel.add(buildDeleteButton());
		JScrollPane pane = new JScrollPane(filterPanel);
		panel.add("Center",pane);
		filterButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String inword = include.getText();
				String exword = exclude.getText();
				TableRowSorter sorter = new TableRowSorter(table.getRowSorter().getModel());
				sorter.setSortKeys(table.getRowSorter().getSortKeys());
				List<RowFilter<Object,Object>> filters = new ArrayList<RowFilter<Object,Object>>(2);
				if(!inword.equals(""))
					filters.add(RowFilter.regexFilter(inword));
				if(!exword.equals(""))
					filters.add(RowFilter.notFilter(RowFilter.regexFilter(exword)));
				sorter.setRowFilter(RowFilter.andFilter(filters));
				table.setRowSorter(sorter);
				table.clearSelection();
				JOptionPane.showMessageDialog(
					null,
					"Finish table filter!","Message",
					JOptionPane.PLAIN_MESSAGE);
			}
		});
		resetButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				TableRowSorter sorter = new TableRowSorter(table.getRowSorter().getModel());
				table.setRowSorter(sorter);
			}
		});
		return panel;
	}

	public void showFilterPanel(){
		card.show(this,"Filter");
	}

	public JButton buildDeleteButton(){
		JButton button = new JButton("Delete");
		button.registerKeyboardAction(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int[] rows = table.getSelectedRows();
				if(rows.length==0){
					JOptionPane.showMessageDialog(
						null,
						"Please select a student before delete.","Error",
						JOptionPane.ERROR_MESSAGE
						);
				}
				else{
					for(int i=0;i<rows.length;i++){
						rows[i]=table.convertRowIndexToModel(rows[i]);
					}
					Object[] options = {"Yes","No"};
					int answer = JOptionPane.showOptionDialog(
						null,
						"Continue to delete selected student?","Message",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
					if(answer==0){
						for(int i=0;i<rows.length;i++){
							data.delStudent(rows[i]-i);
						}
					}
				}
			}
		}, 
			KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,0),
			JComponent.WHEN_IN_FOCUSED_WINDOW);
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int[] rows = table.getSelectedRows();
				if(rows.length==0){
					JOptionPane.showMessageDialog(
						null,
						"Please select a student before delete.","Error",
						JOptionPane.ERROR_MESSAGE
						);
				}
				else{
					for(int i=0;i<rows.length;i++){
						rows[i]=table.convertRowIndexToModel(rows[i]);
					}
					Object[] options = {"Yes","No"};
					int answer = JOptionPane.showOptionDialog(
						null,
						"Continue to delete selected student?","Message",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
					if(answer==0){
						for(int i=0;i<rows.length;i++){
							data.delStudent(rows[i]-i);
						}
					}
				}
			}
		});
		return button;
	}

	public JButton buildUpdateButton(){
		JButton button = new JButton("Update");
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(table.getSelectedRows().length==0){
					JOptionPane.showMessageDialog(
						null,
						"Please select a student to update.","Error",
						JOptionPane.ERROR_MESSAGE
						);
				}
				else if(table.getSelectedRows().length>1){
					JOptionPane.showMessageDialog(
						null,
						"Please update one student at once.","Error",
						JOptionPane.ERROR_MESSAGE
						);
				}
				else{
					int row = table.getSelectedRow();
					row = table.convertRowIndexToModel(row);
					Object[] options = 
					{"Name","ID","Gender","Age","Department","Origin"};
					String select = String.valueOf(JOptionPane.showInputDialog(
						null,
						"Select an item to update:\n","Select Item",
						JOptionPane.PLAIN_MESSAGE,null,options,options[0]));
					switch(select){
						case "Name":
						String input = JOptionPane.showInputDialog(
							null,
							"Please enter the new name:\n","Input",
							JOptionPane.PLAIN_MESSAGE);
						if(input==null)
							break;
						if(input.equals("")){
							JOptionPane.showMessageDialog(
								null,
								"Student name cannot be empty!","Error",
								JOptionPane.ERROR_MESSAGE);
							break;
						}
						data.setStudentName(row,input);
						row = table.convertRowIndexToView(row);
						table.setRowSelectionInterval(row,row);
						table.scrollRectToVisible(table.getCellRect(row,0,true));
						break;
						case "ID":
						input = JOptionPane.showInputDialog(
							null,
							"Please enter the new ID:\n","Input",
							JOptionPane.PLAIN_MESSAGE);
						if(input== null)
							break;
						if(input.equals("")){
							JOptionPane.showMessageDialog(
								null,
								"Student ID cannot be empty!","Error",
								JOptionPane.ERROR_MESSAGE);
							break;
						}
						try{
							data.setStudentId(row,String.valueOf(Integer.parseInt(input)));
							row = table.convertRowIndexToView(row);
							table.setRowSelectionInterval(row,row);
							table.scrollRectToVisible(table.getCellRect(row,0,true));
						}
						catch(NumberFormatException exception){
							JOptionPane.showMessageDialog(
								null,
								"Please enter valid ID number","Error",
								JOptionPane.ERROR_MESSAGE);
						}
						break;

						case "Gender":
						Object[] genderOptions = {"Male","Female"};
						input = String.valueOf(JOptionPane.showInputDialog(
							null,
							"Please select the new gender:\n","Input",
							JOptionPane.PLAIN_MESSAGE,null,genderOptions,genderOptions[0]));
						if(input.equals("null"))
							break;
						data.setStudentGender(row,input);
						row = table.convertRowIndexToView(row);
						table.setRowSelectionInterval(row,row);
						table.scrollRectToVisible(table.getCellRect(row,0,true));
						break;

						case "Age":
						input = JOptionPane.showInputDialog(
							null,
							"Please enter the new age:\n","Input",
							JOptionPane.PLAIN_MESSAGE);
						if(input==null)
							break;
						try{
							data.setStudentAge(row,Integer.parseInt(input));
							row = table.convertRowIndexToView(row);
							table.setRowSelectionInterval(row,row);
							table.scrollRectToVisible(table.getCellRect(row,0,true));
						}
						catch(NumberFormatException exception){
							JOptionPane.showMessageDialog(
								null,
								"Please enter valid age number","Error",
								JOptionPane.ERROR_MESSAGE);
						}
						break;
						case "Department":
						input = JOptionPane.showInputDialog(
							null,
							"Please enter the new deparment:\n","Input",
							JOptionPane.PLAIN_MESSAGE);
						if(input==null)
							break;
						data.setStudentDepartment(row,input);
						row = table.convertRowIndexToView(row);
						table.setRowSelectionInterval(row,row);
						table.scrollRectToVisible(table.getCellRect(row,0,true));
						break;
						case "Origin":
						input = JOptionPane.showInputDialog(
							null,
							"Please enter the new origin:\n","Input",
							JOptionPane.PLAIN_MESSAGE);
						if(input==null)
							break;
						data.setStudentOrigin(row,input);
						row = table.convertRowIndexToView(row);
						table.setRowSelectionInterval(row,row);
						table.scrollRectToVisible(table.getCellRect(row,0,true));
						break;
					}
				}
			}
		});
		return button;
	}
}