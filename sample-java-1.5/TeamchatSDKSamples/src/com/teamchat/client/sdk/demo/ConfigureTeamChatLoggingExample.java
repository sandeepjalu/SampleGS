package com.teamchat.client.sdk.demo;

import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.logging.TeamChatLoggingOptions;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.TextChatlet;

/**
 * A simple bot that show how to configure logging in teamchat api.
 * 
 * Please remember to update the 2 parameter viz.
 * bot, password 
 */
public class ConfigureTeamChatLoggingExample {

	public static final String bot = "chaitu.majeti@gmail.com";
	public static final String password = "654321";
	
	
	/**
	 * This method creates instance of teamchat client and initializes the logging framework, and wait for messages from other users
	 **/
	public static void main(String[] args) {
		
		TeamChatLoggingOptions options=new TeamChatLoggingOptions();
		options.setFileName("loggingbot");
		options.setLogEnabled(true);
		options.setLogLocation("."); // create logs in current directory
		options.setLogLevel(TeamChatLoggingOptions.LoggingLevel.ALL);
		
		TeamchatAPI api = TeamchatAPI.fromFile("config.json")
				.setEmail(bot) 
				.setPassword(password)
				.setLoggingOptions(options);
		api.startReceivingEvents(new HelloWorldBot());
	}
	
	/**
	 * This method posts Hello World message, when any user posts hi message to
	 * the logged in user (see main method)
	 **/
	@OnKeyword("hi")
	public void HelloWorld(TeamchatAPI api) {	
		api.perform( 
				api.context().currentRoom().post(
						new TextChatlet("Hello World")
						)
				);
	}
}
