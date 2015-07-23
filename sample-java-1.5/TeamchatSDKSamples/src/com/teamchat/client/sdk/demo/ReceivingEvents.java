package com.teamchat.client.sdk.demo;

import com.teamchat.client.annotations.OnBotAdded;
import com.teamchat.client.annotations.OnMemberAdded;
import com.teamchat.client.annotations.OnMemberLeft;
import com.teamchat.client.annotations.OnMemberRemoved;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.utils.UserDetail;


/**
 * This bot demonstrates how we can handle the different events occurring in the room.
 * * Please remember to update the 2 parameter viz.
 * bot, password
 *
 */
public class ReceivingEvents {
	
	public static final String bot = "bot@teamchat.com";
	public static final String password = "botpwd";

	public static void main (String... args){
		TeamchatAPI api = TeamchatAPI.fromFile("config.json")
			    .setEmail(bot) 
			    .setPassword(password) 
			    .startReceivingEvents(new ReceivingEvents());//Wait for other user to send message
			    
	}
	/**
	 * This method handles the memberAdded event.
	 * @param api
	 */
	@OnMemberAdded
	public void onJoin(TeamchatAPI api){
		System.out.println("Successfully received memberAdded event :");
		System.out.println("Current Message : "+api.context().currentEvent().getMessage());
		System.out.println("Event timestamp : "+api.context().currentEvent().getDate());
		for (UserDetail userDetail:api.context().currentEvent().getUserDetails()){
			System.out.println("Users added : "+userDetail.getUserId());
		}
	}
	/**
	 * This method handles the memberLeft event.
	 * @param api
	 */
	@OnMemberLeft
	public void onLeft(TeamchatAPI api){
		System.out.println("Successfully received memberLeft event :");
		System.out.println("Current Message : "+api.context().currentEvent().getMessage());
		System.out.println("Event timestamp  : "+api.context().currentEvent().getDate());
		for (UserDetail userDetail:api.context().currentEvent().getUserDetails()){
			System.out.println("Users left : "+userDetail.getUserId());
		}
	}
	/**
	 * This method handles the memberRemoved event.
	 * @param api
	 */
	@OnMemberRemoved
	public void onRemove(TeamchatAPI api){
		System.out.println("Successfully received memberRemoved event :");
		System.out.println("Current Message : "+api.context().currentEvent().getMessage());
		System.out.println("Event timestamp  : "+api.context().currentEvent().getDate());
		for (UserDetail userDetail:api.context().currentEvent().getUserDetails()){
			System.out.println("Users removed : "+userDetail.getUserId());
		}
	}
	/**
	 * This method handles the bot added event.
	 * @param api
	 */
	@OnBotAdded
	public void onBotAdded(TeamchatAPI api){
		System.out.println("Successfully received bot added event :");
		System.out.println("Current Message : "+api.context().currentEvent().getMessage());
		System.out.println("Event timestamp  : "+api.context().currentEvent().getDate());
		for (UserDetail userDetail:api.context().currentEvent().getUserDetails()){
			System.out.println("Bot added : "+userDetail.getUserId());
		}
	}
}
