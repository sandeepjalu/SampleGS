package com.teamchat.client.sdk.demo;

import org.json.JSONArray;

import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.ReportChatlet;
import com.teamchat.client.sdk.impl.TeamchatAPIImpl;

/*
 * Posts a report in a room. The report has a title and a summary that
 * are initially visible. It also has a  details link that can be
 * clicked to get further details in a tabular format.
 * 
 * Please remember to update the 3 parameter viz.
 * bot, password, destUser.
 */
public class PostReportDemo {
	public static final String bot = "bot@teamchat.com";
	public static final String password = "botpwd";	
	public static final String destUser = "user1@teamchat.com";
	
	private static void addRow(String[] rowToAdd, JSONArray rows) {
		JSONArray newRow = new JSONArray();
		for(String celldata : rowToAdd)
			newRow.put(celldata);
		rows.put(newRow);
	}
	
	
	public static void main(String[] args) throws Exception {
		String[] headers = new String[]{"Item", "Color", "Quantity"};
		String[] row1 = new String[]{"Item1", "Red", "400"};
		String[] row2 = new String[]{"Item2", "Green", "200"};
		
		JSONArray rows = new JSONArray();
		addRow(row1, rows);
		addRow(row2, rows);
		
		TeamchatAPI api = TeamchatAPIImpl.fromFile("config.json")
				.setEmail(bot)
				.setPassword(password);
		api.perform(api.context().create().add(destUser)
				.post(new ReportChatlet()
						.setTitle("Demo report")
						.setHeaders(headers)
						.setDetails(rows)
						.setSummary("summitem1", "Total Quantity", "600")
						));
	}
}
