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

@SpringBootApplication
public class DnDDiscordBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(DnDDiscordBotApplication.class, args);
	}

	@Autowired
	private SummaryListener summaryListener;

	@Bean
	@ConfigurationProperties("discord-api")
	public DiscordApi discordApi() {
		System.out.println("Initialising api");
		DiscordApiBuilder builder = new DiscordApiBuilder().setToken("NzM1ODUyNDA4NTYzMDQwMzM2.XxmUgw.9BndQYp6vMLL21ua1poSwg6Xj1s");
		DiscordApi api = builder.login().join();
		api.addListener(summaryListener);
		return api;
	}
}
