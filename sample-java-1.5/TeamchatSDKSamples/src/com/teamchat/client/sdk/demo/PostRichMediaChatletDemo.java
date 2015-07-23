package com.teamchat.client.sdk.demo;

import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.RichmediaChatlet;
import com.teamchat.client.sdk.impl.TeamchatAPIImpl;

/*
 * Posts an Richmedia (image) in a room. 
 * 
 * For posting image, two URLs are required, one for thumb nail and other the actual image.
 * 
 * Please remember to update the 3 parameter viz.
 * bot, password, destUser.
 */
public class PostRichMediaChatletDemo {

	public static final String bot = "bot@teamchat.com";
	public static final String password = "botpwd";
	public static final String destUser =  "user1@teamchat.com";
	
	public static void main(String[] args)
	{
		TeamchatAPI api = TeamchatAPIImpl.fromFile("config.json")
				.setEmail(bot)
				.setPassword(password);
		api.perform(api.context().create().add(destUser)
				.post(new RichmediaChatlet()
				.setFileUrl("https://farm4.staticflickr.com/3668/10815631375_1d49b50a13_z_d.jpg")
				.setFileSize("11 kb")
				.setFileMime("image/jpg")
				.setThumbnailUrl("https://farm4.staticflickr.com/3668/10815631375_1d49b50a13_q_d.jpg")));
	}
}
