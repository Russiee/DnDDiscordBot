package com.discordbot.dnd;

import com.discordbot.dnd.listeners.SummaryListener;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class DnDDiscordBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(DnDDiscordBotApplication.class, args);
	}

	@Autowired
	private SummaryListener summaryListener;

	@Autowired
	private Environment env;

	@Bean
	@ConfigurationProperties("discord-api")
	public DiscordApi discordApi() {
		System.out.println("Initialising api");
		String token = env.getProperty("TOKEN");
		DiscordApiBuilder builder = new DiscordApiBuilder().setToken(token);
		DiscordApi api = builder.login().join();
		api.addListener(summaryListener);
		return api;
	}
}
