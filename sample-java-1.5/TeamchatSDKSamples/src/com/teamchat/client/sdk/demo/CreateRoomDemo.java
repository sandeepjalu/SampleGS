package com.teamchat.client.sdk.demo;

import com.teamchat.client.sdk.Room;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.TextChatlet;
import com.teamchat.client.sdk.impl.TeamchatAPIImpl;

/**
 * Creates a new room between bot, roomMember1 and roomMember2
 * and posts a message in that room
 * 
 * Please remember to update the above 4 parameter viz.
 * bot, password, roomMember1, roomMember2.
 */
public class CreateRoomDemo {
	static final String bot = "chaitu.majeti@gmail.com";
	public static final String password = "654321";
	public static final String roomMember1 = "sandeepjalu@teamchat.com";
	public static final String roomMember2 = "user2@teamchat.com";

	public static void main(String[] args) throws Exception {
		TeamchatAPI api = TeamchatAPIImpl.fromFile("config.json")
				.setEmail(bot)
				.setPassword(password);
		Room r = api.context().create()
				.add(roomMember1);
				//.add(roomMember2);

		api.perform(r.post(new TextChatlet("Hi there")));
		
		String roomid = r.getId();
		//you can save this roomid to send messages to the same
		//room rather than recreating a new room every time.
		
		api.perform(api.context().byId(roomid).post(
				new TextChatlet("Welcome back")));
	}
}
