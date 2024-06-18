package br.com.isiflix.simplewebserver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import br.com.isiflix.simplewebserver.util.WebConfig;
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
			} catch (IOException ex) {
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
		String tmpLine = null;
		String requestPath = null;
		String requestMethod = null;
		Map<String, String> params = new HashMap<String, String>();

		

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			mainRequest = br.readLine();
			WebLogger.log("Main Request = " + mainRequest);
			destinationHost = br.readLine();
			WebLogger.log("Dest   Host  = " + destinationHost);

			requestPath = extractPath(mainRequest);
			requestMethod = extractHttpMethod(mainRequest);

			// tratar os possíveis cabeçalhos
			do {
				tmpLine = br.readLine();

			} while (!tmpLine.isBlank());

			handleOutput(socket, requestPath, requestMethod, params);

			socket.close();
		}

		catch (Exception ex) {
			WebLogger.error("Problems handling request from " + socket.getInetAddress().getHostAddress());
		}
	}

	private void handleOutput(Socket socket, String requestPath, String requestMethod, Map<String, String> params) {
		if (requestMethod.equals("GET")) {
			String completePath = WebConfig.DOCUMENT_ROOT + requestPath;

			try {

				PrintWriter pr = new PrintWriter(socket.getOutputStream());
				FileReader f = new FileReader(completePath);
				if (f != null) {
					byte[] content = Files.readAllBytes(Paths.get(completePath));
					String fullContent = new String(content);

					pr.println("HTTP/1.1 200 OK");
					pr.println("Date: " + LocalDate.now().toString());
					pr.println("Content-Type: text/html");
					pr.println("Content-Length: " + fullContent.length());
					pr.println("");
					pr.println(fullContent);
					f.close();
					pr.close();

				} 					

			} catch (Exception ex) {
				generate404(socket);
				WebLogger.error(ex.getMessage());
			}
		}

	}

	private String extractPath(String request) {
		return request.split(" ")[1];
	}

	private String extractHttpMethod(String request) {
		return request.split(" ")[0];
	}

	
	private void generate404(Socket socket) {
		try {
			PrintWriter pr = new PrintWriter(socket.getOutputStream());
			String message = "<html><body><h1>Not Found!</h1></body></html>";
			pr.println("HTTP/1.1 404 Not Found");
			pr.println("Date: "+ LocalDate.now().toString());
			pr.println("Content-Type: text/html");
			pr.println("Content-Length: "+message.length());
			pr.println("");
			pr.println(message);
			pr.close();
		}
		catch(Exception ex) {
			WebLogger.error(ex.getMessage());
		}
	}
}
