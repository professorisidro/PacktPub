package br.com.isiflix.calcclient;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import io.isiflix.calc.Request;
import io.isiflix.calc.Response;

public class ClientClass {
	public static void main(String[] args) {
		try {
			Scanner scanner = new Scanner(System.in);
			String oper;
			Double op1, op2=null;
			oper = scanner.nextLine();
			op1 = Double.parseDouble(scanner.nextLine());
			switch(oper) {
			case "+":
			case "-":
			case "*":
			case "/":
			case "^":
				op2 = Double.parseDouble(scanner.nextLine());
				break;
			}
			Request req = new Request(op1, op2, oper);
			Socket socket = new Socket("localhost", 8350);
			System.out.println("Connecting on server");
			// send request
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(req);
			
			// wait for response
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			Response rep = (Response)in.readObject();
			System.out.println(rep);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
