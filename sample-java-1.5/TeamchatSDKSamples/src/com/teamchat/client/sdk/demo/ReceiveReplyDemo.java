package com.teamchat.client.sdk.demo;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.impl.TeamchatAPIImpl;

/**
 * When someone sends a text message "create" in a room that contains
 * this bot, a new form is posted that asks for the personal details.
 * Futher when anyone replies to this form, the personal details are
 * printed out to the console.
 * 
 * Please remember to update the bot and password fields below.
 * 
 */
public class ReceiveReplyDemo {
	public static final String bot = "bot@teamchat.com";
	public static final String password = "botpwd";	

	public static final String[] fieldsToSave = new String[]{
		"name","age","gender"
	};
	
	@OnKeyword("create")
	public void onCreate(TeamchatAPI api) {
		api.perform(
				api.context().currentRoom().post(
						new PrimaryChatlet()
							.setQuestion("Please enter your details")
							.setReplyScreen(api.objects().form()
									.addField(api.objects().input().label("Name").name("name"))
									.addField(api.objects().input().label("Age").name("age"))
									.addField(api.objects().select().label("Gender").name("gender")
											.addOption("Male")
											.addOption("Female"))
									)
							.alias("persdet")
							));
	}
	
	@OnAlias("persdet")
	public void onData(TeamchatAPI api) {
		String name = api.context().currentReply().getField("name");
		String age = api.context().currentReply().getField("age");
		String gender = api.context().currentReply().getField("gender");
		
		System.out.println("Received data");
		System.out.println("\tName:" + name);
		System.out.println("\tAge:" + age);
		System.out.println("\tGender:" + gender);
	}

	public static void main(String[] args) throws Exception {
		TeamchatAPIImpl.fromFile("config.json")
				.setEmail(bot)
				.setPassword(password)
				.startReceivingEvents(new ReceiveReplyDemo());
	}
}
