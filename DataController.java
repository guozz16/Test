import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class DataController extends JPanel {
	private Data data;
	private CardLayout card;
	public DataController(Data controlledData)
	{
		super();
		data = controlledData;
		card = new CardLayout();
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
		JButton search = new JButton("Search");
		panel.add(search);
		return panel;
	}
	public void showSearchPanel(){
		card.show(this,"Search");
	}
}