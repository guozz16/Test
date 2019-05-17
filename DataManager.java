import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class DataManager extends JFrame {
	private DataController dataController;
	private DataTableView dataTableView;
	public DataManager(){
		super("Data Manager");

		setJMenuBar(addMenuBar());

		//Initialize data		
		Data data = new Data();
		for(int i=0;i<50;i++){
			data.addStudent("Tom"+i,
				"20161234"+i,"Male",i,"EEA","JiangSu");
		}

		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		dataTableView = new DataTableView(data);
		dataController = new DataController(data,dataTableView.getTable());
		contentPane.add("Center",dataTableView);
		contentPane.add("South",dataController);


		pack();
		setDefaultCloseOperation( EXIT_ON_CLOSE );
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private JMenuBar addMenuBar(){
		JMenuBar jMenuBar = new JMenuBar();
		JMenu jMenu = new JMenu("File");
		jMenu.setMnemonic(KeyEvent.VK_F);
		JMenuItem jMenuItem = new JMenuItem("Open");
		KeyStroke ms = KeyStroke.getKeyStroke(KeyEvent.VK_O,InputEvent.CTRL_MASK);
		jMenuItem.setMnemonic(KeyEvent.VK_O);
		jMenuItem.setAccelerator(ms);
		jMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.out.println("Opened");
			}
		});
		jMenu.add(jMenuItem);
		jMenuItem = new JMenuItem("Save");
		ms = KeyStroke.getKeyStroke(KeyEvent.VK_S,InputEvent.CTRL_MASK);
		jMenuItem.setMnemonic(KeyEvent.VK_S);
		jMenuItem.setAccelerator(ms);
		jMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.out.println("Saved");
			}
		});
		jMenu.add(jMenuItem);
		jMenuBar.add(jMenu);
		jMenu = new JMenu("Edit");
		jMenu.setMnemonic(KeyEvent.VK_E);
		jMenuItem = new JMenuItem("Add");
		jMenuItem.setMnemonic(KeyEvent.VK_A);
		jMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dataController.showAddPanel();
			}
		});
		jMenu.add(jMenuItem);
		jMenuItem = new JMenuItem("Search");
		ms = KeyStroke.getKeyStroke(KeyEvent.VK_F,InputEvent.CTRL_MASK);
		jMenuItem.setMnemonic(KeyEvent.VK_S);
		jMenuItem.setAccelerator(ms);
		jMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dataController.showSearchPanel();
			}
		});
		jMenu.add(jMenuItem);
		jMenuBar.add(jMenu);
		return jMenuBar;
	}
	public static void main(String args[]){
		DataManager m = new DataManager();
		
	}
}