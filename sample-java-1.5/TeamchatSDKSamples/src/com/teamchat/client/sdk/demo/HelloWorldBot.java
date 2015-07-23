package com.teamchat.client.sdk.demo;

import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.TextChatlet;

/**
 * A simple bot that response to the word "hi" that is posted in
 * any room containing the bot.
 * 
 * Please remember to update the 2 parameter viz.
 * bot, password 
 */
public class HelloWorldBot {
	public static final String bot = "chaitu.majeti@gmail.com";
	public static final String password = "654321";
	
	/**
	 * This method creates instance of teamchat client, login using specified 
	 * email/password and wait for messages from other users
	 **/
	
	
	public static void main(String[] args) {
		TeamchatAPI api = TeamchatAPI.fromFile("config.json")
				.setEmail(bot)
				.setPassword(password);
		
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
		
		System.out.println(api.context().byId(null).toString());
	}
	
	@OnKeyword("xyz") // can we store xyz in some variable? Or any input from the user in some variable.
	public void Reverse(TeamchatAPI api){
		api.perform(api.context().currentRoom().post(new TextChatlet("zyx")));
	}
	
}