package com.teamchat.client.sdk.demo;

import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.TeamchatAPI;

/**
 * This bot listens on the event stream for a keyword call testInCaseSensitive. 
 * Any text message that begins with testInCaseSensitive will be caught and will
 * result in printing out the success message.
 * 
 * 
 * Please remember to update the 2 parameter viz.  
 * bot, password. 
 */
public class ReceiveKeywordDemo {

	public static final String bot = "bot@teamchat.com";
	public static final String password = "botpwd";	

	public static void main (String... args){
		TeamchatAPI api = TeamchatAPI.fromFile("config.json")
			    .setEmail(bot) 
			    .setPassword(password) 
			    .startReceivingEvents(new ReceiveKeywordDemo());
			    
	}
	/**
	 * This method handles the case sensitive keyword.
	 * @param api
	 */
	@OnKeyword(isCaseSensitive=true, value = "testInCaseSensitive")
	public void caseSensitiveKeyword(TeamchatAPI api){
		System.out.println("Sucessfully triggered case sensitive keyword.");
	}
	/**
	 * This method handles the case insensitive keyword.
	 * @param api
	 */
	@OnKeyword(value = "testCaseSensitive")
	public void caseInSensitiveKeyword(TeamchatAPI api){
		System.out.println("Sucessfully triggered case insensitive keyword.");
	}
}
