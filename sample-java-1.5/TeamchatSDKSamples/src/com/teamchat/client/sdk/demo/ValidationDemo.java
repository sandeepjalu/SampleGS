package com.teamchat.client.sdk.demo;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

/**
 * This bot demonstrates how to implement validations in chatlet forms.
 ** Please remember to update the 2 parameter viz.
 * bot, password
 *
 */
public class ValidationDemo {

	public static final String bot = "bot@teamchat.com";
	public static final String password = "botpwd";

	public static void main(String... args) {
		TeamchatAPI api = TeamchatAPI.fromFile("config.json")
				.setEmail(bot) 
				.setPassword(password) 
				.startReceivingEvents(new ValidationDemo());

	}

	@OnKeyword("postform")
	public void postFormWithValidation(TeamchatAPI api) {
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setReplyLabel("Test Validation")
						.setQuestion("This is a test form.")
						.setReplyScreen(
								api.objects()
										.form()
										//creating a numeric field
										.addField(
												api.objects()
														.input()
														.name("numeric")
														.label("This is a numeric field")
														.addRegexValidation(
																"\\d+",
																"Only numbers are allowed."))
										//creating a mandatory field
										.addField(
												api.objects()
														.input()
														.name("mandatory")
														.label("This is a Mandatory Field")
														.addRegexValidation(
																"[^()]",
																"Please enter a value."))
										//creating a date field						
										.addField(
												api.objects()
														.input()
														.name("date")
														.label("This is a date Field. Please use dd/mm/yyyy format.")
														.addRegexValidation(
																"(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)",
																"Please enter a valid date.")))
						.alias("reply")));
	}
	
	@OnAlias("reply")
	public void processReply(TeamchatAPI api){
		System.out.println("Numeric : "+api.context().currentReply().getField("numeric"));
		System.out.println("Mandatory : "+api.context().currentReply().getField("mandatory"));
		System.out.println("Date : "+api.context().currentReply().getField("date"));
	}
}
