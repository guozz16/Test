import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.border.*;
import java.io.*;
import java.net.*;
import java.applet.Applet;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.*;
import java.util.List;
public class Server extends JFrame{
	Vector<String> clientList;
	JComboBox comboBox;
	AllDataPanel allDataPanel;
	Vector<DataPanel> dataPanelList;
	ControllPanel controllPanel;
	HashMap<String, Integer> flagList;

	public static void main(String args[]) {
		Server myServer = new Server();
	}

	public Server(){
		super("Server");
		clientList = new Vector<String>();
		dataPanelList = new Vector<DataPanel>();
		flagList = new HashMap<String,Integer>();
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		allDataPanel = new AllDataPanel();
		controllPanel = new ControllPanel();
        contentPane.add("Center",controllPanel);
		contentPane.add("South",allDataPanel);
		setSize(800,800);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		ClientThread td1 = new ClientThread(4700,"Client1");
		ClientThread td2 = new ClientThread(2000,"Client2");
		td1.start();
		td2.start();
	}

	class AllDataPanel extends JPanel{
		private CardLayout card;
		public AllDataPanel(){
			card = new CardLayout();
			setLayout(card);
		}
		public void addDataPanel(String clientName, DataPanel dataPanel){
			add(clientName,dataPanel);
		}
		public void showDataPanel(String clientName){
			card.show(this,clientName);
		}
		
	}
	class ControllPanel extends JPanel{
		public ControllPanel(){
			setLayout(new BorderLayout());
			JPanel controllPanel = new JPanel();
			String[] listData = new String[]{"Client1","Client2","Client3"};
	        comboBox = new JComboBox(listData);
	        comboBox.addItemListener(new ItemListener() {
	            @Override
	            public void itemStateChanged(ItemEvent e) {
	                if (e.getStateChange() == ItemEvent.SELECTED) {
	                	String temp = comboBox.getSelectedItem().toString();
	                	if(clientList.contains(temp)){
	                		allDataPanel.showDataPanel(temp);
	                	}
	                	else{
	                		JOptionPane.showMessageDialog(
								null,
								"Client not connected!","Error",
								JOptionPane.ERROR_MESSAGE);
	                	}
	                }
	            }
	        });
	        controllPanel.add(comboBox);
			List <String> checkList = new ArrayList<String>();
			checkList.add("Substation");
			checkList.add("Bus");
			checkList.add("Transformer");
			checkList.add("ACline");
			checkList.add("Unit");
			for(int i=0;i<checkList.size();i++){
				JCheckBox jCheckBox = new JCheckBox(checkList.get(i));
				jCheckBox.setSelected(true);
				jCheckBox.addItemListener(new MyItemListener(checkList));
				controllPanel.add(jCheckBox);
			}
			controllPanel.add(buildRequestButton());
			JScrollPane pane = new JScrollPane(controllPanel);
			add("Center",pane);
		}
		public JButton buildRequestButton(){
			JButton button = new JButton("Request");
			button.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					String clientName = comboBox.getSelectedItem().toString();
					if(clientList.contains(clientName)){
						flagList.put(clientName,1);
					}
				}
			});
			return button;
		}
	}

	class MyItemListener implements ItemListener
	{
		private List checkList;
		public MyItemListener(List checkList){
			this.checkList = checkList;
		}
	    public void itemStateChanged(ItemEvent e)
	    {
	    	Integer index = comboBox.getSelectedIndex();
	        JCheckBox jcb = (JCheckBox) e.getItem();
	        if (jcb.isSelected())
	        {   
	            dataPanelList.get(index).add(jcb.getText());
	        } else
	        {
	            dataPanelList.get(index).remove(checkList.indexOf(jcb.getText()));
	        }
	    }
	}

	class DataPanel extends JPanel{
		MapObjRegister register;
		List<DataTableView> viewList;
		List<String> nameList;
		JPanel panel;
		public DataPanel(MapObjRegister register){
			super();
			this.register = register;
			this.panel = new JPanel();
			setPreferredSize(new Dimension(800,700));
			viewList = new ArrayList<DataTableView>();
			nameList = new ArrayList<String>();
			setLayout(new BorderLayout());
			panel.setLayout(new GridLayout(0,1));
			add("Center",panel);
			for(String s : register.getRegisteredDataObjTypes()){
				viewList.add(new DataTableView(register,s));
				nameList.add(s);
			}
			fresh();
		}
		public void remove(int index){
			viewList.remove(index);
			nameList.remove(index);
			fresh();
		}
		public void add(String s){
			viewList.add(new DataTableView(register,s));
			nameList.add(s);
			fresh();
		}
		public void fresh(){
			removeAll();
			repaint();
			panel.removeAll();
			panel.repaint();
			for(int i=0;i<viewList.size();i++){
				JPanel temp = new JPanel();
				temp.setLayout(new BorderLayout());
				temp.add("North",new JLabel(nameList.get(i)));
				temp.add("Center",viewList.get(i));
				panel.add(temp);
			}
			panel.revalidate();
			add(panel);
			revalidate();
		}
	}
	public void connect(Integer port, ObjectInputStream is, PrintWriter os,
		MapObjRegister register, Socket socket,String clientName){
		try {
			ServerSocket server = null;
			try {
				server = new ServerSocket(port);
			} catch (Exception e) {
				System.out.println("can not listen to: " + e);
			}

			try {
				socket = server.accept();
				System.out.println("connected client: " +
				socket.getInetAddress() + socket.getPort());
			} catch (Exception e) {
				e.printStackTrace();
			}

			Object obj;
			is = new ObjectInputStream(socket.getInputStream());
			os = new PrintWriter(socket.getOutputStream());

			if((obj=is.readObject())!=null && obj.equals("start")){
				Object objType = null;
				Object key = null;
				//Start to initialize data
				while(true){
					if((objType=is.readObject())!=null){
						if(objType.equals("finish"))
							break;
						else if((key=is.readObject())!=null){
							if((obj=is.readObject())!=null){
								String temp = obj.toString();
								String[] strs = temp.substring(1,temp.length()-1).split(", ");
								HashMap m = new HashMap();
								for(String s : strs){
									String[]ms = s.split("=");
									m.put(ms[0],ms[1]);
								}
								register.addDataObj(key.toString(),objType.toString(),m);
							}
						}
					}
				}
			}
			//finish data initialization
			DataPanel tempPanel = new DataPanel(register);
			dataPanelList.add(tempPanel);
			allDataPanel.addDataPanel(clientName,tempPanel);
			allDataPanel.showDataPanel(clientName);
			tempPanel.fresh();
			clientList.add(clientName);
			flagList.put(clientName,0);
			//Start to fresh data
			while(true){
				if(flagList.get(clientName)==1){
					System.out.println("Request");
					os.println("Send");
					os.flush();
					flagList.put(clientName,0);
				}
				if((obj=is.readObject())!=null && obj.equals("start")){
					Object objType = null;
					Object key = null;
					while(true){
						if((objType=is.readObject())!=null){
							if(objType.equals("finish"))
								break;
							else if((key=is.readObject())!=null){
								if((obj=is.readObject())!=null){
									String temp = obj.toString();
									String[] strs = temp.substring(1,temp.length()-1).split(", ");
									HashMap m = new HashMap();
									for(String s : strs){
										String[]ms = s.split("=");
										m.put(ms[0],ms[1]);
									}
									register.removeDataObj(key.toString(),objType.toString());
									register.addDataObj(key.toString(),objType.toString(),m);
								}
							}
						}
					}
				}
			}	
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	class ClientThread extends Thread
	{
		private Integer port;
		private MapObjRegister register;
		private Socket socket;
		private String clientName;
		private ObjectInputStream is;
		private PrintWriter os;
		public ClientThread(Integer port,String clientName)
		{
			super();
			this.port = port;
			this.register = new MapObjRegister();
			this.socket = new Socket();
			this.clientName = clientName;
		}
		public void run()
		{
			connect(port,is,os,register,socket,clientName);
		}
	}
}
