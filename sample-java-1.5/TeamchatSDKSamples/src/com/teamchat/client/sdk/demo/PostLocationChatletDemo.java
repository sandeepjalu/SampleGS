package com.teamchat.client.sdk.demo;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.LocationChatlet;
import com.teamchat.client.sdk.impl.TeamchatAPIImpl;

/*
 * Posts a location request in a room. 
 * 
 * When a members in the group respond by sharing their location, 
 * the location information is printed on the console.
 * 
 * Please remember to update the 3 parameter viz.
 * bot, password, destUser.
 */
public class PostLocationChatletDemo {

	public static final String bot = "somesh.mishra+1@webaroo.com";
	public static final String password = "testBot";
	public static final String destUser =  "somesh.mishra@webaroo.com";	
	
	public static void main(String[] args)
	{
		TeamchatAPI api = TeamchatAPIImpl.fromFile("config.json")
				.setEmail(bot)
				.setPassword(password);
		api.perform(api.context().create().add(destUser)
				.post(new LocationChatlet().setQuestion("Please share your location").alias("locsent")));
		
		api.startReceivingEvents(new PostLocationChatletDemo());
	}
	
	@OnAlias("locsent")
	public void locSent(TeamchatAPI api)
	{
		 System.out.println("Sender:" + api.context().currentReply().senderEmail());
		 System.out.println("Location:" + api.context().currentReply().getField("location"));
		 System.out.println("Place:"+ api.context().currentReply().getField("locname"));
		 System.out.println("Lat:"+ api.context().currentReply().getField("lat"));
		 System.out.println("Long:"+ api.context().currentReply().getField("lng"));
		 System.out.println("Provider:"+ api.context().currentReply().getField("provider"));
		 System.out.println("Accuracy:"+ api.context().currentReply().getField("accuracy"));
	}
}
