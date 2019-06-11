import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.border.*;
import java.io.*;
import java.net.*;
import java.applet.Applet;

public class Server extends JFrame{
	ObjectInputStream is;
	PrintWriter os;
	Socket socket;
	ServerSocket server;
	JTextField text;
	public Server(){
		super("Server");
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add("Center",new JLabel("I'm a server!"));
		contentPane.add(buildFreshButton());
		setSize(300,300);
		setLocation(1200,300);
		setDefaultCloseOperation( EXIT_ON_CLOSE );
		setVisible(true);
		while(true){
			boolean flag = connect(4700);
			if(flag){
				System.exit(0);
			}
		}
	}
	public static void main(String args[]) {
		Server ts = new Server();
	}

	public JButton buildFreshButton(){
		JButton button = new JButton("Fresh");
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				os.println("hi");
				os.flush();
			}
		});
		return button;
	}

	public boolean close(){
		try{
			is.close(); // 关闭Socket输入流
			socket.close(); // 关闭Socket
			server.close(); // 关闭ServerSocket
		}
		catch (Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public boolean connect(Integer port){
		try {
			server = null;
			try {
				server = new ServerSocket(port);
				// 创建一个ServerSocket
			} catch (Exception e) {
				System.out.println("can not listen to:" + e);
				return false;
				// 出错，打印出错信息
			}
			socket = null;
			try {
				socket = server.accept();
				System.out.println("connected client  :" +
				socket.getInetAddress() + socket.getPort());
				// 使用accept()阻塞等待客户请求，有客户
				// 请求到来则产生一个Socket对象，并继续执行
			} catch (Exception e) {
				System.out.println("Error." + e);
				return false;
			}
			Object obj;
			is = new ObjectInputStream(socket.getInputStream());
			// 由Socket对象得到输入流，并构造相应的BufferedReader对象
			os = new PrintWriter(socket.getOutputStream());
			// // 由Socket对象得到输出流，并构造PrintWriter对象
			// BufferedReader sin = new BufferedReader(new InputStreamReader(
			// 		System.in));
			// // 由系统标准输入设备构造BufferedReader对象
			while(true){
				if((obj=is.readObject())!=null){
					System.out.println(obj);
					if(obj.equals("bye")){
						System.out.println("OK, bye~");
						return true;
					}
				}
			}
			// System.out.println("Client:" + is.readLine());
			// 在标准输出上打印从客户端读入的字符串
			// line = sin.readLine();
			// 从标准输入读入一字符串
			// while (!line.equals("bye")) {
			// 	// 如果该字符串为 "bye"，则停止循环
			// 	os.println(line);
			// 	// 向客户端输出该字符串
			// 	os.flush();
			// 	// 刷新输出流，使Client马上收到该字符串
			// 	System.out.println("Server:" + line);
			// 	// 在系统标准输出上打印读入的字符串
			// 	System.out.println("Client:" + is.readLine());
			// 	// 从Client读入一字符串，并打印到标准输出上
			// 	line = sin.readLine();
			// 	// 从系统标准输入读入一字符串
			// }// 继续循环
			// os.close(); // 关闭Socket输出流
		} catch (Exception e) {
			System.out.println("Error:" + e);
			// 出错，打印出错信息
			return false;
		}
	}
}
