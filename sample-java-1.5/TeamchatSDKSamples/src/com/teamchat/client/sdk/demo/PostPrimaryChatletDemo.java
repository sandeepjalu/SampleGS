package com.teamchat.client.sdk.demo;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.impl.TeamchatAPIImpl;

/**
 * The bot posts multi-field form using primary chatlet 
 * when user sends "create" keyword.  It contains different types 
 * of fields, text and dropdown
 * 
 * It shows how to enabled comments, details and set labels for replies
 * 
 * It also demonstrate how to set alias on the form and 
 * handle replies made by users
 * 
 * Please remember to update the 2 parameter viz. bot, password 
 */
public class PostPrimaryChatletDemo {
	public static final String bot = "bot@teamchat.com";
	public static final String password = "botpwd";
	
	public static void main(String[] args)
	{
		TeamchatAPI api = TeamchatAPIImpl.fromFile("config.json")
				.setEmail(bot)
				.setPassword(password);
		
		api.startReceivingEvents(new PostPrimaryChatletDemo());
	}
	/*
	 * This method will be called when user sends a keyword "create" in the room
	 */
	@OnKeyword("create")
	public void onCreate(TeamchatAPI api)
	{
		PrimaryChatlet p = new PrimaryChatlet();
		
		//Set the question for the chatlet
		p.setQuestion("Please enter your profile");
		
		//alternatively HTML can also be set for question
		//p.setQuestionHtml("<b><font style=\"color:red\">Please enter your profile</font></b>");
		
		//Create reply form for this primary chatlet and add fields to it
		Form f = api.objects().form();
		
		//a text field to enter name
		f.addField(api.objects().input().name("name").label("Name:"));
		
		//a dropdown to select city
		f.addField(api.objects().select().name("city").label("City")
				.addOption("Delhi").addOption("Mumbai")
				.addOption("Kolkata").addOption("Chennai"));
		
		//finally add this Form to primary chatlet as replyscreen
		p.setReplyScreen(f);
		
		//Decide if replies will be visible to everyone in the room
		//Also, label can be changed
		p.showDetails(true);
		p.setDetailsLabel("Entries");
		
		//Decide if commenting is enabled on this chatlet or not
		p.allowComments(true);
		
		//Can change reply button label
		p.setReplyLabel("Enter");
		
		//Can assign an alias, if you want to handle replies on this chatlet
		//The method with @OnAlias annotation will be called, when anyone replies
		//to this chatlet. For example, look at method handleReply in this class
		//it has annotation @OnAlias("profile"), which we are setting below.
		p.alias("profile");
		
		//finally post the chatlet in the current room
		api.performPostInCurrentRoom(p);
	}
	
	/*
	 * This method prints the replies to the chatlet 
	 * that was posted in the create method.
	 */
	@OnAlias("profile")
	public void handleReply(TeamchatAPI api)
	{
		//email of the user who replied
		System.out.println("Sender:" + api.context().currentReply().senderEmail());
		
		//value entered in the field "name" and "city" 
		System.out.println("Name entered:" + api.context().currentReply().getField("name"));
		System.out.println("City entered:" + api.context().currentReply().getField("city"));
	}
}
