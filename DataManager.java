import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.swing.filechooser.*;
import javax.swing.border.*;

public class DataManager extends JFrame {
	private DataController dataController;
	private DataTableView dataTableView;
	private Data data;
	public DataManager(){
		super("Data Manager");

		setJMenuBar(addMenuBar());

		//Initialize data		
		data = new Data();
		for(int i=0;i<50;i++){
			data.addStudent("Tom"+i,
				"20161234"+i,"Male",i,"EEA","JiangSu");
		}
		//Set contentPane
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		dataTableView = new DataTableView(data);
		dataController = new DataController(data,dataTableView.getTable());
		contentPane.add("Center",dataTableView);
		contentPane.add("North",dataController);
		//Set visible
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
				JFileChooser chooser=new JFileChooser();
				FileNameExtensionFilter fileFilter = new FileNameExtensionFilter(
				"(*.data)", "data");
				chooser.setFileFilter(fileFilter);
				chooser.showOpenDialog(null);
				File file = chooser.getSelectedFile();
				if(file!=null){
		        	try{
						String fname = chooser.getName(file);
						if(fname.indexOf(".data")==-1)
							file=new File(chooser.getCurrentDirectory(),fname+".data");
						BufferedReader reader = new BufferedReader(new FileReader(file));
						String str = null;
						data.clearData();
						while((str = reader.readLine())!=null){
							data.addStudentFromString(str);
						}
						reader.close();
					}
					catch(Exception except){
						JOptionPane.showMessageDialog(
							null,
							"Error: Problem with opening file!","Error",
							JOptionPane.ERROR_MESSAGE);
						except.printStackTrace();
					}
				}
			}
		});
		jMenu.add(jMenuItem);
		jMenuItem = new JMenuItem("Save");
		ms = KeyStroke.getKeyStroke(KeyEvent.VK_S,InputEvent.CTRL_MASK);
		jMenuItem.setMnemonic(KeyEvent.VK_S);
		jMenuItem.setAccelerator(ms);
		jMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter fileFilter = new FileNameExtensionFilter(
		        "(*.data)", "data");
		        chooser.setFileFilter(fileFilter);
				int option = chooser.showSaveDialog(null);
				if(option==JFileChooser.APPROVE_OPTION){
					File file = chooser.getSelectedFile();
					String fname = chooser.getName(file);
					if(fname.indexOf(".data")==-1)
						file=new File(chooser.getCurrentDirectory(),fname+".data");
					try{
						FileWriter fileWriter = new FileWriter(file);
						BufferedWriter out = new BufferedWriter(fileWriter);
						for(int i=0;i<data.getSize();i++){
							out.write(data.get(i).toString());
							out.newLine();
						}
						out.close();
						fileWriter.close();
					}
					catch(IOException except){
						System.err.println("IOException");
						except.printStackTrace();
					}
				}
			}
		});
		jMenu.add(jMenuItem);
		jMenuBar.add(jMenu);
		jMenu = new JMenu("View");
		jMenu.setMnemonic(KeyEvent.VK_V);
		jMenuItem = new JMenuItem("Add");
		ms = KeyStroke.getKeyStroke(KeyEvent.VK_N,InputEvent.CTRL_MASK);
		jMenuItem.setMnemonic(KeyEvent.VK_A);
		jMenuItem.setAccelerator(ms);
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
		jMenuItem = new JMenuItem("Filter");
		ms = KeyStroke.getKeyStroke(KeyEvent.VK_L,InputEvent.CTRL_MASK);
		jMenuItem.setMnemonic(KeyEvent.VK_F);
		jMenuItem.setAccelerator(ms);
		jMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dataController.showFilterPanel();
			}
		});
		jMenu.add(jMenuItem);
		jMenuBar.add(jMenu);
		return jMenuBar;
	}
	public static void main(String args[]){
		System.out.println(System.getProperty("java.version"));
		DataManager m = new DataManager();
	}
}