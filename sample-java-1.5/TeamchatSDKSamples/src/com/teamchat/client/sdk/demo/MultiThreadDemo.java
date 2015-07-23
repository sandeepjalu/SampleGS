package com.teamchat.client.sdk.demo;

import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.TextChatlet;

public class MultiThreadDemo {
	/**
	 * This method creates instance of teamchat client, login using specified 
	 * email/password and wait for messages from other users
	 **/
	public static void main(String[] args) throws Exception {
		final TeamchatAPI api = TeamchatAPI.fromFile("config.json")
				.setEmail("bot@teamchat.com") //change to your teamchat registered email id
				.setPassword("password") //change to your teamchat password
				.setTimeout(3000)
				;
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				api.startReceivingEvents(new MultiThreadDemo()); //Wait for other user to send message
			}
		});
		t.start();
		
		Thread.sleep(5000);
		System.out.println("Trying to stop");
		api.stopReceivingEvents();
	}

	/**
	 * This method posts Hello World message, when any user posts hi message to
	 * the logged in user (see main method)
	 **/
	@OnKeyword("testkw")
	public void HelloWorld(TeamchatAPI api) {
		api.perform( 
				api.context().currentRoom().post(
						new TextChatlet("Hi there")
						)
				);
	}
}
