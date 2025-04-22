package MyApp.Meteo;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class WeatherHandlerTest {

	@Test
	public void testParseQueryWithCity() {
		WeatherHandler handler = new WeatherHandler();
		Map<String, String> result = handler.testableParseQuery("city=Roma");

		assertTrue(result.containsKey("city"));
		assertEquals("Roma", result.get("city"));
	}

	@Test
	public void testParseQueryMultipleParams() {
		WeatherHandler handler = new WeatherHandler();
		Map<String, String> result = handler.testableParseQuery("city=Milano&foo=bar");

		assertEquals("Milano", result.get("city"));
		assertEquals("bar", result.get("foo"));
	}

	@Test
	public void testParseQueryMissingValue() {
		WeatherHandler handler = new WeatherHandler();
		Map<String, String> result = handler.testableParseQuery("city=");

		assertEquals("", result.get("city"));
	}
}