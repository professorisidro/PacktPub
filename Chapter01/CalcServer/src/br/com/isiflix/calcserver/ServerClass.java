package br.com.isiflix.calcserver;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import io.isiflix.calc.Request;
import io.isiflix.calc.Response;

public class ServerClass {
	public static void main(String[] args) {
		try {
			ServerSocket serverSocket = new ServerSocket(8350);
			while (true) {
				Socket socket = serverSocket.accept();
				System.out.println("New connection!!");
				
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
				Request req = (Request)in.readObject();
				
				System.out.println(req);
				Response rep = new Response();
				// tratamento da request
				switch(req.getOper()) {
				case "+":
					rep.setStatus("Ok");
					rep.setValue(req.getOp1()+ req.getOp2());
					break;
				case "-":
					rep.setStatus("Ok");
					rep.setValue(req.getOp1()- req.getOp2());
					break;
				case "*":
					rep.setStatus("Ok");
					rep.setValue(req.getOp1()* req.getOp2());
				case "^":
					rep.setStatus("OK");
					rep.setValue(Math.pow(req.getOp1(), req.getOp2()));
				case "sqrt":
					if (req.getOp1() >= 0) {
						rep.setStatus("Ok");
						rep.setValue(Math.sqrt(req.getOp1()));
					}
					else {
						rep.setStatus("Invalid");
					}
					break;
				case "/":
					if (req.getOp2() != 0) {
						rep.setStatus("Ok");
						rep.setValue(req.getOp1() / req.getOp2());
					}
					else {
						rep.setStatus("Invalid");
					}
					break;
				default:
					rep.setStatus("Unsupported");
				}
					
				System.out.println(rep);
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
				out.writeObject(rep);
				
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}

}
