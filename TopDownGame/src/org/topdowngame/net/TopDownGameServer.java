package org.topdowngame.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TopDownGameServer {
	public static void main(String[] args) throws IOException {

		ServerSocket serverSocket = null;
		try 
		{
			serverSocket = new ServerSocket(4444);
		} 
		catch (IOException e) 
		{
			System.err.println("Could not listen on port: 4444.");
			System.exit(1);
		}
		System.out.println("Server Socket launched");

		Socket clientSocket = null;
		try 
		{
			clientSocket = serverSocket.accept();
		} 
		catch (IOException e) 
		{
			System.err.println("Accept failed.");
			System.exit(1);
		}
		System.out.println("Client Socket launched");

		PrintWriter outToClientSocket = new PrintWriter(clientSocket.getOutputStream(), true);
		BufferedReader inFromClientSocket = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		String inputLine;
		//String outputLine;
		int countMessage = 0;

		//BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		//outputLine = stdIn.readLine();
		outToClientSocket.println("Hello from server");
		System.out.println("Hello from server sent");

		/*while ((inputLine = inFromClientSocket.readLine()) != null) {
			outToClientSocket.println("message " + countMessage);
			countMessage++;
			System.out.println("MESSAGE FROM CLIENT : " + inputLine);
			if (inputLine.equals("Bye.")) break;
		}*/
		while (true) {
			outToClientSocket.println("message " + countMessage);
			countMessage++;
			inputLine = inFromClientSocket.readLine();
			System.out.println("MESSAGE FROM CLIENT : " + inputLine);
			if (inputLine.equals("Bye.")) break;
		}
		
		outToClientSocket.close();
		inFromClientSocket.close();
		clientSocket.close();
		serverSocket.close();
    }
}