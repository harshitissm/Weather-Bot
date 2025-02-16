package com.bot.cloudy.service;

import com.bot.cloudy.model.Subscription;
import com.bot.cloudy.utility.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CloudyBot extends AbilityBot {

    private static final Logger logger = LoggerFactory.getLogger(CloudyBot.class);

    private final SubscriptionService subscriptionService;
    private final WeatherService weatherService;
    private final Map<Long, String> chatStates = new HashMap<>();

    @Autowired
    public CloudyBot(Environment env, SubscriptionService subscription, WeatherService weatherService) {
        super(env.getProperty("BOT_TOKEN"), "Climate2_Bot");
        this.subscriptionService = subscription;
        this.weatherService = weatherService;
    }

    @Override
    public long creatorId() {
        return 1L;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            String messageText = update.getMessage().getText();

            try {
                if (messageText.startsWith("Hi") || messageText.startsWith("Hello") || messageText.startsWith("/start")) {
                    chatStates.remove(chatId);
                    sendMessage(chatId, "Hello, Welcome to Cloudy!\nUse /weather to get weather updates.");
                } else if (messageText.startsWith("/delete")) {
                    String city = subscriptionService.getUserSubscription(chatId);
                    if (city != null) {
                        subscriptionService.deleteUserSubscription(chatId);
                        sendMessage(chatId, "Your subscription for " + city + " is deleted.");
                    } else {
                        sendMessage(chatId, "You have no active subscriptions. Use /subscribe to start.");
                    }
                } else if (messageText.startsWith("/weather")) {
                    chatStates.put(chatId, "awaitingCity");
                    sendMessage(chatId, "Please enter the state for weather information.");
                } else if (messageText.startsWith("/subscribe")) {
                    chatStates.put(chatId, "awaitingSubscription");
                    String city = subscriptionService.getUserSubscription(chatId);
                    if (city != null) {
                        sendMessage(chatId, "You are already subscribed to " + city + ". Enter a new state to update.");
                    } else {
                        sendMessage(chatId, "Enter the state you want to subscribe to.");
                    }
                } else if ("awaitingCity".equals(chatStates.get(chatId))) {
                    chatStates.remove(chatId);
                    try {
                        String weatherJson = weatherService.getWeatherByCity(messageText);
                        String message = JsonConverter.convertWeatherJsonToMessage(weatherJson);
                        sendMessage(chatId, message);
                    } catch (Exception e) {
                        sendMessage(chatId, "Error fetching weather data.");
                    }
                } else if ("awaitingSubscription".equals(chatStates.get(chatId))) {
                    subscriptionService.addUserSubscription(chatId, messageText);
                    chatStates.remove(chatId);
                    sendMessage(chatId, "Subscribed to daily weather updates for " + messageText + ".");
                }
            } catch (Exception e) {
                logger.error("An Error occurred while receiving daily weather", e);
                sendMessage(chatId, "An error occurred.");
            }
        }
    }

    @Scheduled(cron = "0 30 9 * * *", zone = "Asia/Kolkata")
    public void sendDailyWeatherUpdates() {
        logger.info("Scheduled task started: sendDailyWeatherUpdates()");

        List<Subscription> users = subscriptionService.getAllUsers();
        for (Subscription user : users) {
            Long userId = user.getUserId();
            String state = user.getState();
            try {
                String weather = weatherService.getWeatherByCity(state);
                String weatherMessage = JsonConverter.convertWeatherJsonToMessage(weather);
                sendMessage(userId, weatherMessage);
            } catch (Exception e) {
                logger.error("An Error occurred while sending daily weather update for user: {}", userId, e);
                sendMessage(userId, "Error fetching weather for " + state);
            }
        }
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            logger.error("An Error occurred while sending message to Cloudy", e);
        }
    }
}
