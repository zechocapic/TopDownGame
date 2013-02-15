package org.topdowngame;

import java.net.*;
import java.io.*;

public class TopDownGameServer {
	public static void main(String[] args) throws IOException {

		ServerSocket serverSocket = null;
        try 
        {
        	serverSocket = new ServerSocket(7);
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

        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        //BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        //String inputLine, outputLine;
        
        //BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        //outputLine = stdIn.readLine();
        out.println("I m the server");
        
        System.out.println("Message from server sent");

        //while ((inputLine = in.readLine()) != null) {
        /*while (true) {
            outputLine = stdIn.readLine();
            out.println(outputLine);
             if (outputLine.equals("Bye."))
                break;
        }*/
        out.close();
        //in.close();
        clientSocket.close();
        serverSocket.close();
    }
}