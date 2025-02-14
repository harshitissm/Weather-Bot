package com.bot.cloudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
@EnableMongoAuditing
@PropertySource("classpath:application-local.properties")
public class CloudyBotSpringBootApplication {

    public static void main(String[] args) throws TelegramApiException {
        ConfigurableApplicationContext ctx = SpringApplication.run(CloudyBotSpringBootApplication.class, args);
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(ctx.getBean("cloudyBot", AbilityBot.class));
    }

}
