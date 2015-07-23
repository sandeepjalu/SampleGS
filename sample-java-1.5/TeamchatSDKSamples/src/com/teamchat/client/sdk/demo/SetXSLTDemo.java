package com.teamchat.client.sdk.demo;

import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

/**
 * This bot demonstrates how to set xslt to the chatlet.
 * @author somesh
 *
 */
public class SetXSLTDemo {


	
	public static final String bot = "bot@teamchat.com";
	public static final String password = "botpwd";	

	
	public static void main (String... args){
		TeamchatAPI api = TeamchatAPI.fromFile("config.json")
			    .setEmail(bot) //change to your teamchat registered email id
			    .setPassword(password) //change to your teamchat password
			    .startReceivingEvents(new SetXSLTDemo());//Wait for other user to send message
			    
		
	}
	/**
	 * The method posts a primary chatlet with a specific xslt. 
	 * @param api
	 */
	@OnKeyword("create1")
	public void postChatletOfParticularXSLT(TeamchatAPI  api){
		PrimaryChatlet p = new PrimaryChatlet();
		//Setting the XSLT for the chatlet.
		p.setXSLTForChatlet("primary-2.7.2-somesh1");
		
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
	
	
	/**
	 * The method posts a primary chatlet with a specific xslt. 
	 * @param api
	 */
	@OnKeyword("create2")
	public void postChatletOfParticularXSLT2(TeamchatAPI  api){
		PrimaryChatlet p = new PrimaryChatlet();
		//Setting the XSLT for the chatlet.
		p.setXSLTForChatlet("primary-2.7.2-somesh2");
		
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
}
