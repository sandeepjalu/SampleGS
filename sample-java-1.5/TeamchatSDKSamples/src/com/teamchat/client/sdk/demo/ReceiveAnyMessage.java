package com.teamchat.client.sdk.demo;

import com.teamchat.client.annotations.OnMsg;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.impl.TeamchatAPIImpl;

public class ReceiveAnyMessage {
	public static final String bot = "bot@teamchat.com";
	public static final String password = "botpwd";
	public static final String destUser =  "user1@teamchat.com";
	
	public static void main(String[] args)
	{
		TeamchatAPI api = TeamchatAPIImpl.fromFile("config.json")
				.setEmail(bot)
				.setPassword(password);
		
		api.startReceivingEvents(new ReceiveAnyMessage());
	}
	
	@OnMsg
	public void msgReceived(TeamchatAPI api)
	{
		System.out.println("Message Received is:" + api.context().currentChatlet().raw());
	}
}

