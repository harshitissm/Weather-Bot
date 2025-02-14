package com.bot.cloudy.utility;

import org.json.JSONObject;

public class JsonConverter {

    public static String convertWeatherJsonToMessage(String weatherJson) {
        JSONObject jsonObject = new JSONObject(weatherJson);

        // Extract relevant weather details
        JSONObject location = jsonObject.getJSONObject("location");
        JSONObject current = jsonObject.getJSONObject("current");

        String cityName = location.getString("name");
        String country = location.getString("country");
        int temperature = current.getInt("temperature");
        int feelsLike = current.getInt("feelslike");
        String weatherDescription = current.getJSONArray("weather_descriptions").getString(0);

        return String.format(
                "🌤 Weather Update for %s, %s:\n" +
                        "🌡 Temperature: %d°C\n" +
                        "🤔 Feels Like: %d°C\n" +
                        "🌦 Condition: %s",
                cityName, country, temperature, feelsLike, weatherDescription
        );
    }

}
