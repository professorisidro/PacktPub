package br.com.isiflix.simplewebserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import br.com.isiflix.simplewebserver.util.WebLogger;

public class WebServer {

	private int port = 80;
	private ServerSocket serverSocket;

	public WebServer(int port) {
		this.port = port;
		try {
			this.serverSocket = new ServerSocket(this.port);
		} catch (IOException ex) {
			WebLogger.error("Couldn't initialize MySimpleWebServer on port " + port);
		}
		
		while (true) {
			try {
				Socket socket = this.serverSocket.accept();
				this.handleRequest(socket);
				socket.close();
			}
			catch(IOException ex) {	
				WebLogger.error("I couln't handle request form client");
			}
		}
	}
	
	public WebServer() {
		this(80);
	}
	
	public void handleRequest(Socket socket) {
		String mainRequest;
		String destinationHost;
		String headers;
		String blankLine;
		String tmpLine=null;
		String requestPath=null;
		String requestMethod=null;
		Map<String, String> params = new HashMap<String, String>();
		
		
		
		WebLogger.log("New request from "+socket.getInetAddress().getHostAddress());
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			mainRequest = br.readLine();
			WebLogger.log("Main Request = "+mainRequest);
			destinationHost = br.readLine();
			WebLogger.log("Dest   Host  = "+destinationHost);
			
			
			
			do {
				tmpLine = br.readLine();
				
			} while (!tmpLine.isBlank());
			br.close();
			
			handleOutput(socket, requestPath, requestMethod, params);
		}
		
		catch(Exception ex) {
			WebLogger.error("Problems handling request from "+socket.getInetAddress().getHostAddress());
		}
	}

	private void handleOutput(Socket socket, String requestPath, String requestMethod, Map<String, String> params) {
		// TODO Auto-generated method stub
		
	}
	
	private String extractPath(String request) {
		
	}
	private String extractHttpMethod(String request) {
		
	}

	
}
