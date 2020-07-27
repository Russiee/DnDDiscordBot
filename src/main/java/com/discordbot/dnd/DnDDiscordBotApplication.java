package com.discordbot.dnd;

import com.discordbot.dnd.listeners.HandoutListener;
import com.discordbot.dnd.listeners.SummaryListener;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class DnDDiscordBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(DnDDiscordBotApplication.class, args);
	}

	@Autowired
	private SummaryListener summaryListener;

	@Autowired
	private HandoutListener handoutListener;

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
		api.addListener(handoutListener);
		api.getListeners().values().forEach(value -> value.forEach(list -> System.out.println(list.toString())));
		return api;
	}
}
