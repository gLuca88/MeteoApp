package MyApp.Meteo;

public class WeatherResponse {
	private String city;
	private String temperature;
	private String description;

	public WeatherResponse(String city, String temperature, String description) {
		this.city = city;
		this.temperature = temperature;
		this.description = description;
	}

	// Getters (obbligatori per Gson)
	public String getCity() {
		return city;
	}

	public String getTemperature() {
		return temperature;
	}

	public String getDescription() {
		return description;
	}
}