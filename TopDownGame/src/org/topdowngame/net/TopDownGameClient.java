package org.topdowngame.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class TopDownGameClient extends Thread 
{
	private Socket echoSocket = null;
	private PrintWriter outToEchoSocket = null;
	private BufferedReader fromEchoSocket = null;
	//private BufferedReader stdin;
	//private String userInput;
	private String messageServer;
	
	public void run()
	{
		try
		{
			echoSocket = new Socket("localhost", 4444);
			outToEchoSocket = new PrintWriter(echoSocket.getOutputStream(), true);
			fromEchoSocket = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
		}
		catch (UnknownHostException e)
		{
			System.err.println("No localhost");
		}
		catch (IOException e)
		{
			System.err.println("No IO for localhost");
		}
		System.out.println("Connection to server established");
		
		// Send greeting message to server
		outToEchoSocket.println("Hello from a client");
		System.out.println("Hello from client sent");
		
		// Get greeting message from server
		if (fromEchoSocket != null)
		{
			try 
			{
				messageServer = new String(fromEchoSocket.readLine());
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			System.out.println("MESSAGE FROM SERVER : " + messageServer);
		}

		//long startTime = System.currentTimeMillis();
		while(true)
		{
			//System.out.println("CLIENT TIME : " + (System.currentTimeMillis() - startTime));
			//outToEchoSocket.println("time elapsed is " + (System.currentTimeMillis() - startTime));
			try 
			{
				Thread.sleep(1000);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	public void clientSays(String s)
	{
		outToEchoSocket.println(s);
	}

}
