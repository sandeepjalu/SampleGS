package com.teamchat.client.sdk.demo;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.SurveyChatlet;
import com.teamchat.client.sdk.impl.TeamchatAPIImpl;
/**
 * This bot demonstrates how to post and use the survey chatlet.
 * * Please remember to update the 2 parameter viz.
 * bot, password
 *
 */
public class PostSurveyChatletDemo {

	public static final String bot = "bot@teamchat.com";
	public static final String password = "botpwd";	

	public static void main(String[] args) throws Exception {
		TeamchatAPIImpl.fromFile("config.json")
				.setEmail(bot)
				.setPassword(password)
				.startReceivingEvents(new PostSurveyChatletDemo());
	}
	
	/*
	 * This method posts a survey
	 */
	@OnKeyword("create")
	public void onTest(TeamchatAPI api) {
		api.performPostInCurrentRoom(new SurveyChatlet().setQuestion("Do you like teamchat?")
				.addOption("Completeley")
				.addOption("Partially")
				.addOption("SomeWhat")
				.addOption("No")
				.addOption("No Reply")
				.alias("mysurvey"));
	}
	
	/*
	 * This method handles survey replies
	 */
	@OnAlias("mysurvey")
	public void onreplytosurvey(TeamchatAPI api)
	{
		System.out.println(api.context().currentReply().senderEmail());
	 	System.out.println(api.context().currentReply().getField("resp"));
	}
}
