package com.teamchat.client.sdk.demo;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.sdk.Chatlet;
import com.teamchat.client.sdk.TeamchatAPI;

/**
 * This bot demonstrates how to post chatlet by reference.
 ** Please remember to update the 2 parameter viz.
 * bot, password
 *
 */
public class PostChatletByReference {

	
	public static final String bot = "bot@teamchat.com";
	public static final String password = "botpwd";	
	public static final String chatletId="Your chatlet Id";
	public static final String postTo="Recievers Email";
	
	public static void main (String... args){
		TeamchatAPI api = TeamchatAPI.fromFile("config.json")
			    .setEmail(bot) 
			    .setPassword(password) 
			    .startReceivingEvents(new PostChatletByReference());
			    
		Chatlet chatlet=api.context().chatletByRef(chatletId);
		api.perform(api.context().create().add(postTo).post(chatlet.alias("ref")));
	}
	/**
	 * Handles reply to the chatlet.
	 * @param api
	 */
	@OnAlias("ref")
	public void onRef(TeamchatAPI api){
		System.out.println("Received reply for the chatlet.");
	}
}
