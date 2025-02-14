package com.bot.cloudy.controller;

import com.bot.cloudy.service.WeatherService;
import com.bot.cloudy.utility.JsonConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/details/{stateName}")
    public ResponseEntity<String> getWeatherDetails(@PathVariable String stateName) {
        String weatherByCity = weatherService.getWeatherByCity(stateName);
        String weatherDetails = JsonConverter.convertWeatherJsonToMessage(weatherByCity);
        return ResponseEntity.ok(weatherDetails);
    }

}
