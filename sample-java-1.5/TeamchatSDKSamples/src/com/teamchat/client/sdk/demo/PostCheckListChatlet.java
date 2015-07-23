package com.teamchat.client.sdk.demo;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.CheckListChatlet;
import com.teamchat.client.sdk.impl.TeamchatAPIImpl;

/**
 * This bot demonstrates how to post and use the checklist chatlet.
 * * Please remember to update the 2 parameter viz.
 * bot, password
 *
 */
public class PostCheckListChatlet {

	public static final String bot = "bot@teamchat.com";
	public static final String password = "botpwd";	

	public static void main(String[] args) throws Exception {
		TeamchatAPIImpl.fromFile("config.json")
				.setEmail(bot)
				.setPassword(password)
				.startReceivingEvents(new PostCheckListChatlet());
	}
	
	/*
	 * This method posts a checklist
	 */
	@OnKeyword("create")
	public void onTest(TeamchatAPI api) {
		api.performPostInCurrentRoom(new CheckListChatlet().setQuestion("Things to do")
				.addOption("Buy a grocery")
				.addOption("Buy a perfumes")
				.addOption("Buy a car")
				.addOption("Buy a house")
				.addOption("Buy a airplane")
				.alias("mychecklist"));
	}
	
	/*
	 * This method handles checklist replies
	 */
	@OnAlias("mychecklist")
	public void onreplytosurvey(TeamchatAPI api)
	{
		System.out.println(api.context().currentReply().senderEmail());
	 	System.out.println(api.context().currentReply().getField("resp"));
	}
}
