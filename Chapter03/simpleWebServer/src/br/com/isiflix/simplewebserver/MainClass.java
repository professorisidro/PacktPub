package br.com.isiflix.simplewebserver;

import br.com.isiflix.simplewebserver.util.WebLogger;

public class MainClass {
	public static void main(String[] args) {
		WebLogger.welcome();
		new WebServer(8055);
		
	}

}
