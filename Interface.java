import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.image.*;
import javax.imageio.*;


public class Interface extends JFrame
{
	ServerSocket serversocket;
	Socket socket;
	PrintWriter os;
	ImageIcon img1;
	ImageIcon img2;
	CardLayout cd;
	JPanel panelcard;
	JLabel panelvolt;
	JLabel panelpower;
	public static void main(String args[])
	{
		new Interface();
	}
	public Interface()
	{
		cd=new CardLayout();
		panelvolt = new JLabel();
		panelpower = new JLabel();
		panelcard = new JPanel();
		panelcard.setLayout(cd);
		panelcard.add(panelpower,"1");
		panelcard.add(panelvolt,"2");
		
		JButton button = new JButton("Run");
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try{
					String exe = "python";
			        String command = "test.py";
			        String[] cmdArr = new String[] {exe,command};
			        Process process = Runtime.getRuntime().exec(cmdArr);
			        // is = new DataInputStream(process.getInputStream());
				}
				catch (Exception ee){
					ee.printStackTrace();
				}
			}
		});
		
		JComboBox combobox = new JComboBox();
		combobox.addItem("P-theta");
		combobox.addItem("Q-V");
		combobox.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if(e.getItem().equals("P-theta"))
				{
					cd.show(panelpower.getParent(),"1");
				}
				else if(e.getItem().equals("Q-V"))
				{
					cd.show(panelvolt.getParent(),"2");
				}
			}
		});
		
		JPanel operation = new JPanel();
		operation.add(button);
		operation.add(combobox);
		
		Container container = getContentPane();
		container.add(operation,"North");
		container.add(panelcard,"South");
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(990,850);
		setLocationRelativeTo(null);
		setVisible(true);
		try
		{
			serversocket = new ServerSocket(4700);
		}
		catch(Exception e1)
		{
			System.out.println("Can't listen to:"+e1);
		}
		try
		{
			socket = serversocket.accept();
			System.out.println("connected client  :"+socket.getInetAddress() + socket.getPort());
			os = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		}
		catch(Exception e)
		{
			System.out.println("Error"+e);
		}
		HiThread td = new HiThread();
		td.start();
	}
	
	
	class HiThread extends Thread
	{
		public HiThread()
		{
			super();
		}
		public void run()
		{
			while(true)
			{
				try
				{
					os.println("Hi");
					os.flush();
					Thread.sleep(5000);
					img1 = new ImageIcon("Figure_1.png");
					img2 = new ImageIcon("Figure_2.png");
					img1.getImage().flush();
					panelpower.setIcon(img1);
					img2.getImage().flush();
					panelvolt.setIcon(img2);
				}
				catch(Exception e)
				{
					interrupt();
					System.out.println("Thread finish.");
					break;
				}
			}
		}
	}
}