import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.lang.Math;
import java.util.List;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class Client1 extends JFrame {
	Socket socket;
	BufferedReader is;
	ObjectOutputStream os;
	String fname;
	static Integer port = 4700;
	static Boolean flag = false;
	static MapObjRegister register = new MapObjRegister();
	private static String serverHost = "127.0.0.1";//the IP address"183.172.212.200"
	public static void main(String args[]) {
		Client c = new Client("变电站1");
	}

	public Client(String name){
		super("Client");
		FreshThread th1 = new FreshThread();
		ReadThread th2 = new ReadThread();
		fname = name+".QS";
		readData(fname);
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		DataPanel dataPanel = new DataPanel(register);
		contentPane.add("North",buildControllPanel(dataPanel));
		contentPane.add("Center",dataPanel);
		setSize(1080,800);
		setDefaultCloseOperation( EXIT_ON_CLOSE );
		setLocationRelativeTo(null);
		setVisible(true);
		th1.start();
		th2.start();
	}
	public JPanel buildControllPanel(DataPanel dataPanel){
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		JPanel controllPanel = new JPanel();
		List<String> checkList = new ArrayList<String>();
		checkList.addAll(register.getRegisteredDataObjTypes());
		for(int i=0;i<checkList.size();i++){
			JCheckBox jCheckBox = new JCheckBox(checkList.get(i));
			jCheckBox.setSelected(true);
			jCheckBox.addItemListener(new MyItemListener(checkList,dataPanel));
			controllPanel.add(jCheckBox);
		}
		controllPanel.add(buildConnectButton());
		controllPanel.add(buildQuitButton());
		JScrollPane pane = new JScrollPane(controllPanel);
		panel.add("Center",pane);
		return panel;
	}

	public JButton buildConnectButton(){
		JButton button = new JButton("Connect");
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(flag){
					JOptionPane.showMessageDialog(
						null,
						"Already connected!","Warning",
						JOptionPane.WARNING_MESSAGE);
				}
				else if(connect(port)){
					JOptionPane.showMessageDialog(
						null,
						"Connect to server successfully!","Message",
						JOptionPane.PLAIN_MESSAGE);
					flag = true;
				}
				else{
					JOptionPane.showMessageDialog(
						null,
						"Can not connect to server!","Error",
						JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		return button;
	}

	public JButton buildQuitButton(){
		JButton button = new JButton("Quit");
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(quit()){
					System.exit(0);
				}
				else{
					JOptionPane.showMessageDialog(
						null,
						"Problem to quit!","Error",
						JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		return button;
	}

	public boolean quit(){
		if(flag){
			try{
				os.writeObject("bye");
				os.flush();
				is.close();
				os.close();
				socket.close();
			}
			catch(Exception e){
				e.printStackTrace();
				return false;
			}
			return true;
		}
		else 
			return true;
	}

	public boolean connect(Integer port){
		try{
			socket = new Socket(serverHost, port);
			is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			os = new ObjectOutputStream(socket.getOutputStream());
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void send(String objType, String key){
		try{
			os.writeObject(objType);
			os.writeObject(key);
			os.writeObject(register.getRegisteredDataObj(key,objType));
			os.flush();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void sendData(){
		try{
			os.writeObject("start");
			for (String objType: register.getRegisteredDataObjTypes()){
				for(String key: register.getRegisteredDataObjs(objType).keySet()){
					send(objType,key);
				}
			}
			os.writeObject("finish");
			os.flush();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public boolean readData(String fname){
		File file = new File(fname);
		try{
			InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
			BufferedReader reader = new BufferedReader(isr);
			String str = null;
			while((str = reader.readLine())!=null){
				String [] arr = str.split("\\s+");
				if(arr[0].indexOf("::")!=-1){
					String objType = arr[0].substring(arr[0].indexOf("<")+1,arr[0].indexOf(":"));
					//read @ data
					str = reader.readLine();
					if(str==null || str.indexOf("@")==-1){
						System.out.println("Format error!");
						return false;
					}
					String [] keyList = str.split("\\s+");
					
					//read // data
					str = reader.readLine();
					if(str==null){
						System.out.println("Format error!");
						return false;
					}
					//get each # data
					Integer count = 0;
					while((str = reader.readLine())!=null){
						arr = str.split("\\s+");
						if(arr[0].indexOf("::")!=-1){
							break;
						}
						HashMap<String, String> obj 
						= new HashMap<String, String>();
						for(int i=1;i<keyList.length;i++){
							obj.put(keyList[i],arr[i]);
						}
						//To simulate the refresh process,
						//randomize the voltage of bus and the power of generator
						if(objType.equals("Bus")){
							double temp = Double.valueOf(obj.get("V"));
							temp += Math.random() -0.5;
							obj.put("V",String.valueOf(temp));
							temp = Double.valueOf(obj.get("Ang"));
							temp += Math.random() -0.5;
							obj.put("Ang",String.valueOf(temp));
						}
						if(objType.equals("Unit")){
							double temp = Double.valueOf(obj.get("P"));
							temp += Math.random() -0.5;
							obj.put("P",String.valueOf(temp));
							temp = Double.valueOf(obj.get("Q"));
							temp += Math.random() -0.5;
							obj.put("Q",String.valueOf(temp));
						}
						//Add # data to register
						register.addDataObj(count.toString(),objType,obj);
						count++;
					}
				}
			}
			reader.close();
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	class FreshThread extends Thread{
		public FreshThread(){
			super();
		}
		public void run(){
			while (true)
			{
				try{
					Thread.sleep(2000);
					readData(fname);
					if(flag)
						sendData();
				}
				catch (InterruptedException e)
				{
					interrupt();
					System.out.println("thread finish");
				}
			}
		}
	}
	class ReadThread extends Thread{
		public ReadThread(){
			super();
		}
		public void run(){
			while (true)
			{
				String line;
				try{
					if((line=is.readLine())!=null){
						if(line.equals("Send")){
							readData(fname);
							sendData();
							System.out.println("Send");
						}
					}
				}
				catch (Exception e)
				{
					
				}
			}
		}
	}

	class DataPanel extends JPanel{
		MapObjRegister register;
		List<DataTableView> viewList;
		List<String> nameList;
		public DataPanel(MapObjRegister register){
			super();
			this.register = register;
			viewList = new ArrayList<DataTableView>();
			nameList = new ArrayList<String>();
			setLayout(new GridLayout(0,1));
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
			for(int i=0;i<viewList.size();i++){
				JPanel temp = new JPanel();
				temp.setLayout(new BorderLayout());
				temp.add("North",new JLabel(nameList.get(i)));
				temp.add("Center",viewList.get(i));
				add(temp);
			}
			revalidate();
		}
	}
	class MyItemListener implements ItemListener
	{
		private List checkList;
		private DataPanel dataPanel;
		public MyItemListener(List checkList, DataPanel dataPanel){
			this.checkList = checkList;
			this.dataPanel = dataPanel;
		}
	    public void itemStateChanged(ItemEvent e)
	    {
	        JCheckBox jcb = (JCheckBox) e.getItem();
	        if (jcb.isSelected())
	        {   
	            dataPanel.add(jcb.getText());
	        } else
	        {
	            dataPanel.remove(checkList.indexOf(jcb.getText()));
	        }

	    }
	}
}