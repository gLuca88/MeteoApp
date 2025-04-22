package MyApp.Meteo;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.*;
import java.util.*;

public class WeatherHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		// Legge il parametro city dalla query string
		URI requestURI = exchange.getRequestURI();
		String query = requestURI.getQuery(); // es: city=Roma
		String city = "Roma"; // default

		if (query != null) {
			Map<String, String> params = parseQuery(query);
			city = params.getOrDefault("city", "Roma");
		}

		// Chiamata API esterna
		String apiUrl = "https://wttr.in/" + city + "?format=j1";
		String json = fetch(apiUrl);

		// Parsing JSON per ottenere temperatura e descrizione
		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
		JsonArray conditions = jsonObject.getAsJsonArray("current_condition");
		JsonObject current = conditions.get(0).getAsJsonObject();

		String temp = current.get("temp_C").getAsString();
		String desc = current.getAsJsonArray("weatherDesc").get(0).getAsJsonObject().get("value").getAsString();

		// Crea oggetto risposta e lo converte in JSON
		WeatherResponse meteo = new WeatherResponse(city, temp, desc);
		String jsonResponse = gson.toJson(meteo);

		// Imposta intestazioni e invia risposta JSON
		exchange.getResponseHeaders().add("Content-Type", "application/json");
		exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);
		OutputStream os = exchange.getResponseBody();
		os.write(jsonResponse.getBytes());
		os.close();
	}

	// Metodo per leggere la query string (es: city=Roma)
	private Map<String, String> parseQuery(String query) {
		Map<String, String> map = new HashMap<>();
		for (String pair : query.split("&")) {
			String[] parts = pair.split("=", 2); // <-- aggiunto limite 2
			if (parts.length == 2) {
				map.put(parts[0], parts[1] != null ? parts[1] : ""); // <-- gestiamo valore null
			} else if (parts.length == 1) {
				map.put(parts[0], "");
			}
		}
		return map;
	}

	// Metodo per fare una richiesta HTTP GET e restituire il corpo della risposta
	private String fetch(String apiUrl) throws IOException {
		URL url = new URL(apiUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");

		try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
			StringBuilder result = new StringBuilder();
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
			return result.toString();
		}
	}

	// Versione esposta solo per testing
	public Map<String, String> testableParseQuery(String query) {
		return parseQuery(query);
	}
}