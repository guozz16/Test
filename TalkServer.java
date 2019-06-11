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

public class TalkServer extends JFrame{
	ObjectInputStream is;
	PrintWriter os;
	MapObjRegister register1;
	MapObjRegister register2;
	Socket socket1;
	Socket socket2;
	ServerSocket server;
	public static void main(String args[]) {
		HashMap obj = new HashMap();
		obj.put("1","a");
		obj.put("2","b");
		System.out.println(obj);
		String temp = obj.toString();
		String[] strs = temp.substring(1,temp.length()-1).split(", ");
		HashMap m = new HashMap();
		for(String s : strs){
			System.out.println(s);
			String[]ms = s.split("=");
			m.put(ms[0],ms[1]);
		}
		System.out.println(m);
	}
	// public TalkServer(){
	// 	super("Server");
	// 	clientList = new Vector<String>();
	// 	Container contentPane = getContentPane();
	// 	contentPane.setLayout(new BorderLayout());
	// 	allDataPanel = new AllDataPanel();
	// 	dataControllPanel = new DataControllPanel();
	// 	String[] listData = new String[]{"Client1","Client2"};
 //        comboBox = new JComboBox<String>(listData);
 //        comboBox.addItemListener(new ItemListener() {
 //            @Override
 //            public void itemStateChanged(ItemEvent e) {
 //                if (e.getStateChange() == ItemEvent.SELECTED) {
 //                	String temp = comboBox.getSelectedItem().toString();
 //                	if(clientList.contains(temp)){
 //                		allDataPanel.showDataPanel(temp);
 //                	}
 //                	else{
 //                		JOptionPane.showMessageDialog(
	// 						null,
	// 						"Client not connected!","Error",
	// 						JOptionPane.ERROR_MESSAGE);
 //                	}
 //                }
 //            }
 //        });
 //        contentPane.add("North",comboBox);
 //        contentPane.add("Center",dataControllPanel);
	// 	contentPane.add("South",allDataPanel);
	// 	setSize(800,800);
	// 	setLocationRelativeTo(null);
	// 	setDefaultCloseOperation( EXIT_ON_CLOSE );
	// 	setVisible(true);
	// }

	

	// public JButton buildFreshButton(){
	// 	JButton button = new JButton("ConnectFresh");
	// 	button.addActionListener(new ActionListener(){
	// 		public void actionPerformed(ActionEvent e){
	// 			os.println("hi");
	// 			os.flush();
	// 		}
	// 	});
	// 	return button;
	// }

	// public boolean close(){
	// 	try{
	// 		is.close(); // 关闭Socket输入流
	// 		socket.close(); // 关闭Socket
	// 		server.close(); // 关闭ServerSocket
	// 	}
	// 	catch (Exception e){
	// 		e.printStackTrace();
	// 		return false;
	// 	}
	// 	return true;
	// }
	
	// public boolean connect(Integer port){
	// 	try {
	// 		server = null;
	// 		try {
	// 			server = new ServerSocket(port);
	// 			// 创建一个ServerSocket
	// 		} catch (Exception e) {
	// 			System.out.println("can not listen to:" + e);
	// 			return false;
	// 			// 出错，打印出错信息
	// 		}
	// 		socket = null;
	// 		try {
	// 			socket = server.accept();
	// 			System.out.println("connected client  :" +
	// 			socket.getInetAddress() + socket.getPort());
	// 			// 使用accept()阻塞等待客户请求，有客户
	// 			// 请求到来则产生一个Socket对象，并继续执行
	// 		} catch (Exception e) {
	// 			System.out.println("Error." + e);
	// 			return false;
	// 		}
	// 		Object obj;
	// 		is = new ObjectInputStream(socket.getInputStream());
	// 		// 由Socket对象得到输入流，并构造相应的BufferedReader对象
	// 		os = new PrintWriter(socket.getOutputStream());
	// 		// // 由Socket对象得到输出流，并构造PrintWriter对象
	// 		// BufferedReader sin = new BufferedReader(new InputStreamReader(
	// 		// 		System.in));
	// 		// // 由系统标准输入设备构造BufferedReader对象
	// 		while(true){
	// 			if((obj=is.readObject())!=null){
	// 				System.out.println(obj);
	// 				if(obj.equals("bye")){
	// 					System.out.println("OK, bye~");
	// 					return true;
	// 				}
	// 			}
	// 		}
	// 		// System.out.println("Client:" + is.readLine());
	// 		// 在标准输出上打印从客户端读入的字符串
	// 		// line = sin.readLine();
	// 		// 从标准输入读入一字符串
	// 		// while (!line.equals("bye")) {
	// 		// 	// 如果该字符串为 "bye"，则停止循环
	// 		// 	os.println(line);
	// 		// 	// 向客户端输出该字符串
	// 		// 	os.flush();
	// 		// 	// 刷新输出流，使Client马上收到该字符串
	// 		// 	System.out.println("Server:" + line);
	// 		// 	// 在系统标准输出上打印读入的字符串
	// 		// 	System.out.println("Client:" + is.readLine());
	// 		// 	// 从Client读入一字符串，并打印到标准输出上
	// 		// 	line = sin.readLine();
	// 		// 	// 从系统标准输入读入一字符串
	// 		// }// 继续循环
	// 		// os.close(); // 关闭Socket输出流
	// 	} catch (Exception e) {
	// 		System.out.println("Error:" + e);
	// 		// 出错，打印出错信息
	// 		return false;
	// 	}
	// }
}
