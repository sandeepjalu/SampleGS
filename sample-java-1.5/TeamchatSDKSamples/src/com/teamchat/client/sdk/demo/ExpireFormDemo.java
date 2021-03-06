package com.teamchat.client.sdk.demo;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Expirable;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PollChatlet;
import com.teamchat.client.sdk.impl.TeamchatAPIImpl;

/**
 * 1) This bot listens on the event stream for a keyword call create. 
 * Any text message that begins with create will be caught and a poll
 * chatlet will be posted in the same room.
 * 2) When someone replies to the poll it will expire the form
 * No more replies can be made on the chatlet
 * 
 * Please remember to update the 2 parameter viz.  
 * bot, password. 
 */
public class ExpireFormDemo {
	public static final String bot = "bot@teamchat.com";
	public static final String password = "botpwd";	

	public static void main(String[] args) throws Exception {
		TeamchatAPIImpl.fromFile("config.json")
				.setEmail(bot)
				.setPassword(password)
				.startReceivingEvents(new ExpireFormDemo());
	}
	
	/*
	 * This method posts a poll
	 */
	@OnKeyword("create")
	public void onTest(TeamchatAPI api) {
		api.performPostInCurrentRoom(new PollChatlet().setQuestion("Do you like teamchat?").alias("mypoll"));
	}
	
	/*
	 * This method handles poll replies
	 */
	@OnAlias("mypoll")
	public void onreplytopoll(TeamchatAPI api)
	{
		System.out.println(api.context().currentReply().senderEmail());
	 	System.out.println(api.context().currentReply().getField("resp"));
		Expirable c = (Expirable) api.context().currentChatlet();
		api.context().currentRoom().reply(c.expireNow());
	}
		
}
