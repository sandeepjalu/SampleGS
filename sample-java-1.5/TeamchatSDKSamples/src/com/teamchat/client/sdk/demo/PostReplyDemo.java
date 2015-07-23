package com.teamchat.client.sdk.demo;

import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Chatlet;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;
import com.teamchat.client.sdk.impl.TeamchatAPIImpl;

/**
 * The bot posts multi-field form using primary chatlet 
 * when user sends "create" keyword.  
 * 
 * When user sends "postreply" keyword, it adds a sample
 * reply to this form
 * 
 * Please remember to update the 2 parameter viz. bot, password 
 */
public class PostReplyDemo {
	public static final String bot = "bot@teamchat.com";
	public static final String password = "botpwd";
	
	public static void main(String[] args)
	{
		TeamchatAPI api = TeamchatAPIImpl.fromFile("config.json")
				.setEmail(bot)
				.setPassword(password);
		
		api.startReceivingEvents(new PostReplyDemo());
	}

	/*
	 * method will be called when user sends "postreply" keyword
	 */
	@OnKeyword("postreply")
	public void handleReply(TeamchatAPI api)
	{
		Chatlet[] chatlet = api.context().chatletByAlias("profileform");
		
		if(chatlet==null ||  !(chatlet.length>0))
		{
			api.performPostInCurrentRoom(new TextChatlet("Create the profile form first"));
			return;
		}
		
		api.perform(
				api.context().currentRoom().reply(chatlet[0].createReply()
						.addField("name", "Random Name")
						.addField("city", "Mumbai")
			)
		);		
	}	
	
	/*
	 * This method will be called when user sends a keyword "create" in the room
	 */
	@OnKeyword("create")
	public void onCreate(TeamchatAPI api)
	{
		PrimaryChatlet p = new PrimaryChatlet().setQuestion("Please enter your profile")
				.setReplyScreen(api.objects().form()
					.addField(api.objects().input().name("name").label("Name"))
					.addField(api.objects().select().name("city").label("City")
							.addOption("Delhi").addOption("Mumbai")
							.addOption("Kolkata").addOption("Chennai")))
				.showDetails(true)
				.setDetailsLabel("Entries")
				.allowComments(true)
				.setReplyLabel("Enter");
		
		p.alias("profileform");
		api.performPostInCurrentRoom(p);
	}	
}
