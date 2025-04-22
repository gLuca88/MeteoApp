package MyApp.Meteo;

import com.sun.net.httpserver.HttpServer;

import MyApp.Meteo.WeatherHandler;
import java.io.IOException;

import java.net.InetSocketAddress;

public class Main {
	public static void main(String[] args) throws IOException {
		// Creazione del server sulla porta 8000
		HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

		// Imposta la rotta "/weather" e assegna un handler
		server.createContext("/weather", new WeatherHandler());

		// Avvia il server
		server.start();
		System.out.println("✅ Server avviato su http://localhost:8000/weather");
	}
}