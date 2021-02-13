package com.discordbot.dnd;

import com.discordbot.dnd.listeners.*;
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
	private ShantyListener shantyListener;

	@Autowired
	private PirateRateListener pirateRateListener;

	@Autowired
	private PirateListener pirateListener;

	@Autowired
	private InventoryListener inventoryListener;

	@Autowired
	private WorkListener workListener;

	@Autowired
	private Environment env;

	@Bean
	@ConfigurationProperties("discord-api")
	public DiscordApi discordApi() {
		System.out.println("Initialising api");
		String token = env.getProperty("TOKEN");
		System.out.println(token);
		DiscordApiBuilder builder = new DiscordApiBuilder().setToken(token).setAllNonPrivilegedIntents();
		DiscordApi api = builder.login().join();
		System.out.println("Initialising listeners");
		api.addListener(summaryListener);
		api.addListener(handoutListener);
		api.addListener(shantyListener);
		api.addListener(pirateRateListener);
		api.addListener(inventoryListener);
		api.addListener(pirateListener);
		api.addListener(workListener);
		api.getListeners().values().forEach(value -> value.forEach(list -> System.out.println(list.toString())));
		System.out.println("Initiated");
		api.addMessageCreateListener(event -> {
			if (event.getMessageContent().equalsIgnoreCase(".ping")) {
				event.getChannel().sendMessage("Pong!");
			}
		});
		return api;
	}
}
