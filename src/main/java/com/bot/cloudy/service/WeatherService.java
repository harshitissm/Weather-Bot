package com.bot.cloudy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class WeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    @Value("${WEATHER_ACCESS_KEY}")
    private String ACCESS_KEY;

    @Value("${WEATHER_BASE_URL:http://api.weatherstack.com/current}")
    private String BASE_URL;

    public String getWeatherByCity(String city) {
        RestTemplate restTemplate = new RestTemplate();

        String url =UriComponentsBuilder.fromUriString(BASE_URL)
                .queryParam("query", city)
                .queryParam("access_key", ACCESS_KEY)
                .encode()
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            logger.error("Error fetching weather data: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            return "Error fetching weather data: " + e.getStatusCode();
        } catch (Exception e) {
            logger.error("Unexpected error fetching weather: {}", e.getMessage());
            return "Unexpected error fetching weather data.";
        }
    }
}
