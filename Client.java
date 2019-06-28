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

public class Client
{
	Socket socket;
	BufferedReader is;
	ObjectOutputStream os;
	ImageIcon img;
	
	public static void main(String args[])
	{
		Client client = new Client();
	}
	
	public Client()
	{
		img = new ImageIcon("123.png");
		try
		{
			socket = new Socket("127.0.0.1",3000);
			is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			os = new ObjectOutputStream(socket.getOutputStream());
		}
		catch(IOException e)
		{
			System.out.println("Error"+e);
		}
		SendThread td = new SendThread();
		td.start();
	}
	
	class SendThread extends Thread
	{
		public SendThread()
		{
			super();
		}
		public void run()
		{
			while(true)
			{
				String line;
				try
				{
//					line=is.readLine();
//					System.out.println(line);
					if((line=is.readLine())!=null)
					{
						System.out.println("收到东西");
						if(line.equals("Hi"))
						{
							os.writeObject(img);
							os.flush();
							System.out.println("收到Hi");
						}
					}
				}
				catch(Exception e)
				{
					System.out.println("error");
					break;
				}
			}
		}
	}
}