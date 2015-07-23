package com.teamchat.client.demo.bot;

import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.TextChatlet;

/**
 * This is a demo bot.
 * 
 * @author somesh
 *
 */
public class DemoBot {

	/**
	 * Method to respond to the hi keyword in a room.
	 * @param api
	 */
	@OnKeyword("hi")
	public void HelloWorld(TeamchatAPI api) {
	        api.perform(
	            api.context().currentRoom().post(
	            new TextChatlet("Hello World!!")
	        )
	    );
	}
}
