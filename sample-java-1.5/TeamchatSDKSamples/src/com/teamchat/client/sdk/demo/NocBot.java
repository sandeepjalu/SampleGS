package com.teamchat.client.sdk.demo;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Chatlet;
import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.BypassChatletCopy;
import com.teamchat.client.sdk.chatlets.LocationChatlet;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;
import com.teamchat.client.sdk.impl.TeamchatAPIImpl;
/*
 * This is an example of complex workflow bot */
public class NocBot {
	public static final String bot = "bot@teamchat.com";
	public static final String password = "botpwd";	
	
	private static final String[] NEW_TICKET_PARAMS = new String[]{"issue","addr","lsi","srno"};
	private static final String[] NEW_TICKET_LABELS = new String[]{"Issue","Address","LSI","SR NO#"};	
	private static final String ASSIGNEE = "assignee";
	
	private static final String[] REPORT_PARAMS = new String[]{"ticketid", "issue","addr","lsi","srno","assignee","acceptor", "curloc","clientloc", "status"};
	private static final String[] REPORT_LABELS = new String[]{"Ticket", "Issue","Address","LSI","SR NO","Assigned To","Acceptor", "Current Location","Client Loc", "Status"};
	
	private static final String[] DAILY_REPORT_PARAMS = new String[]{"ticketid", "issue","acceptor", "status"};
	private static final String[] DAILY_REPORT_LABELS = new String[]{"Ticket", "Issue","Acceptor", "Status"};
	
	public static void main(String[] args)
	{
		TeamchatAPI api = TeamchatAPIImpl.fromFile("config.json")
				.setEmail(bot)
				.setPassword(password);
		
		api.context().byId("551b8e2793cd89f65bbf9612").post(new TextChatlet("Hello.."));
		api.startReceivingEvents(new NocBot());
	}

	
	
	@OnKeyword("create")
	public void onCreateKeyword(TeamchatAPI api) {
		Form f = api.objects().form();
		for(int i=0;i<NEW_TICKET_PARAMS.length; i++)
			f.addField(api.objects().input().label(NEW_TICKET_LABELS[i]).name(NEW_TICKET_PARAMS[i]));
		
		api.perform(
				api.context().currentRoom().post(
						new PrimaryChatlet().setQuestion("New call")
						.setReplyScreen(f)
						.setReplyLabel("Create")
						.alias("create")));
	}
	
	@OnAlias("create")
	public void onNewTicket(TeamchatAPI api) {
		
		String ticketId = createTicket(api);
		JSONObject ticketdata = getTicketData(api, ticketId);
		
		Chatlet assigmentChatlet = new PrimaryChatlet()
										.setQuestionHtml(getTicketHtml(ticketdata, false))
										.setReplyScreen(api.objects().form()
															.addField(api.objects().input()
																		.name(ASSIGNEE)
																		.label("Assign To")
															)
										).setReplyLabel("Assign")
										.alias("assignment")
										.alias("ticketid:" + ticketId);
			
		api.perform(api.context().byId(ticketdata.getString("tlroom")).post(assigmentChatlet));
	}
	
	@OnAlias("assignment")
	public void onTicketAssign(TeamchatAPI api) {
		String ticketId = getTicketId(api);		
		JSONObject ticketdata = getTicketData(api, ticketId);
		ticketdata.put("assignee", api.context().currentReply().getField(ASSIGNEE));
		api.data().addField( "ticketdata", ticketId, ticketdata.toString());		

		 api.perform(api.context().byId(ticketdata.getString("tlroom"))
			.post(new PrimaryChatlet()
				.setQuestionHtml(getTicketHtml(ticketdata, false))
				.setReplyAction("Accept", "accepted","1")
				.alias("accepted")
				.alias("ticketid:" + ticketId)
				)
			);
	}
	
	@OnAlias("accepted")
	public void onAccept(TeamchatAPI api) {		
		String ticketId = getTicketId(api);		
		JSONObject ticketdata = getTicketData(api, ticketId);
		String sender = api.context().currentReply().senderEmail();
		
		ticketdata.put("acceptor", api.context().currentReply().senderName());
		ticketdata.put("acceptorEmail", sender);

		api.data().addField( "ticketdata", ticketId, ticketdata.toString());
		publishReport(api, ticketId);		
		api.perform(api.context().create()
						.add(sender).setName("NOC Bot")
						.post(new LocationChatlet()
									.setQuestion("Submit your current location")
									.alias("currloc")
									.alias("ticketid:" + ticketId)));
	}
	
	@OnAlias("currloc")
	public void onUpdateCurrentLocation(TeamchatAPI api) {
		String ticketId = getTicketId(api);		
		JSONObject ticketdata = getTicketData(api, ticketId);
		String sender = api.context().currentReply().senderEmail();	
		ticketdata.put("curloc", api.context().currentReply().getField("locname"));
		api.data().addField( "ticketdata", ticketId, ticketdata.toString());		
		
		publishReport(api, ticketId);		
		api.perform(api.context().create()
						.add(sender).setName("NOC Bot")
						.post(new LocationChatlet()
									.setQuestion("Submit once reach client location")
									.alias("clientloc")
									.alias("ticketid:" + ticketId)));		
		
	}
	
	@OnKeyword("setup")
	public void onSetupKeyword(TeamchatAPI api) {
		api.performPostInCurrentRoom(new PrimaryChatlet()
			.setQuestion("Configure Room type")
			.setReplyScreen(api.objects().form()
					.addField(api.objects().select().name("reptype").label("Room Type").addOption("noc").addOption("tl"))
					.addField(api.objects().input().name("repcode").label("Email of TL"))
					)
			.alias("setup")
		);
	}
	
	@OnAlias("setup")
	public void onSetup(TeamchatAPI api) {
		String roomid = api.context().currentRoom().getId();
		String type = api.context().currentReply().getField("reptype");
		String code = api.context().currentReply().getField("repcode");
		
		if(type.equals("noc")) {
			api.data().addField("noc1", roomid, code);
		} else if (type.equals("tl")) {
			try {
				String prevcode = api.data().getField("noc1", roomid);
				api.data().addField("noc2", prevcode, null);
			} catch (Exception e){}
			api.data().addField("noc1", roomid, code);
			api.data().addField("noc2", code, roomid);			
		}
	}
	
	@OnAlias("clientloc")
	public void onUpdateClientLocation(TeamchatAPI api) {
		String ticketId = getTicketId(api);		
		JSONObject ticketdata = getTicketData(api, ticketId);
		String sender = api.context().currentReply().senderEmail();		
		ticketdata.put("clientloc", api.context().currentReply().getField("locname"));
		api.data().addField( "ticketdata", ticketId, ticketdata.toString());		
		publishReport(api, ticketId);
		
		api.perform(api.context().create()
				.add(sender).setName("NOC Bot")
				.post(new PrimaryChatlet()
							.setQuestion("Update status for Ticket:" + ticketId)
							.setReplyScreen(api.objects().form()
									.addField(api.objects().select()
												.name("status")
												.label("Status")
												.addOption("Issue Resolved")
												.addOption("Issue Not Resolved")
												.addOption("Issue Pending Resolved")
									).addField(api.objects().input().name("remarks").label("Remarks"))
							)
							.alias("feedback")
							.alias("ticketid:" + ticketId)));		
	}
	@OnAlias("feedback")
	public void onFeedback(TeamchatAPI api) {
		String ticketId = getTicketId(api);		
		JSONObject ticketdata = getTicketData(api, ticketId);
		ticketdata.put("status", api.context().currentReply().getField("status") + " " + api.context().currentReply().getField("remarks"));
		api.data().addField( "ticketdata", ticketId, ticketdata.toString());		
		publishReport(api, ticketId);		
	}
	
	private String getTicketId(TeamchatAPI api) {
		String[] aliases = api.context().currentChatletAlias();
		if(aliases == null)
			return null;
		for(String a : aliases) {
			if(a.startsWith("ticketid:"))
				return a.substring(9);
		}
		return null;
	}
	
	private JSONObject getTicketData(TeamchatAPI api, String ticketId)
	{
		String data = api.data().getField("ticketdata", ticketId);
		if(data!=null)
			return new JSONObject(data);
		return null;
	}
	
	private String getTicketHtml(JSONObject data, boolean includeEmpty)
	{
		String html = "<div>";
		for(int i=0;i<REPORT_PARAMS.length;i++)
		{
			if(data.has(REPORT_PARAMS[i]))
				html  += "<div>" + REPORT_LABELS[i] + ": " + data.getString(REPORT_PARAMS[i]) + "</div>";
			else if(includeEmpty)
				html  += "<div>" + REPORT_LABELS[i] + ": </div>";
		}
		html += "</div>";			
		return html;
	}
	
	private void publishReport(TeamchatAPI api, String ticketId)
	{
		JSONObject ticketdata = getTicketData(api, ticketId);
		String reportformid = null;
		
		if(ticketdata.has("reportformid"))
			reportformid = ticketdata.getString("reportformid");
		else
		{
			reportformid= UUID.randomUUID().toString().replace("-", "").toLowerCase();
			ticketdata.put("reportformid", reportformid);
		}
		
		api.data().addField( "ticketdata", ticketId, ticketdata.toString());
		
		PrimaryChatlet p = new PrimaryChatlet().setQuestionHtml(getTicketHtml(ticketdata, true));
		BypassChatletCopy report = new BypassChatletCopy(p);
		report.setFormId(reportformid);
		
		JSONArray publishtoRooms = ticketdata.getJSONArray("publishtorooms");
		
		for(int i=0; i<publishtoRooms.length(); i++)
		{
			api.perform(api.context().byId(publishtoRooms.getString(i)).post(report));
		}		
	}
	
	private String getTLRoom(TeamchatAPI api) {
		//return api.data().getField("NOCtoTL", api.context().currentRoom().getId());
		try {
			return api.data().getField("noc2", api.data().getField("noc1", api.context().currentRoom().getId()));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	private String createTicket(TeamchatAPI api)
	{
		api.data().addToField("ticksequence", "ticketid", 1);
		String ticketId = api.data().getField("ticksequence", "ticketid");
		String nocroom = api.context().currentRoom().getId();
		String tlroom = getTLRoom(api);
		
		if(tlroom == null) {
			//TODO
			return null;
		}

		JSONArray publishtoRooms = new JSONArray();
		publishtoRooms.put(nocroom);
		publishtoRooms.put(tlroom);
		
		JSONObject ticketdata = new JSONObject();
		ticketdata.put("publishtorooms", publishtoRooms);
		ticketdata.put("ticketid", ticketId);
		ticketdata.put("nocroom", nocroom);
		ticketdata.put("tlroom", tlroom);
			
		for(int i=0;i<NEW_TICKET_PARAMS.length;i++)
			ticketdata.put(NEW_TICKET_PARAMS[i], api.context().currentReply().getField(NEW_TICKET_PARAMS[i]));
		addToTicketList(api, ticketId);
		api.data().addField( "ticketdata", ticketId, ticketdata.toString());
		return ticketId;
	}
		
	
	private void addToTicketList(TeamchatAPI api, String ticketId)
	{		
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");		
		String date = formatter.format(Calendar.getInstance().getTime());
		JSONArray tlist = null;
		String list = api.data().getField("ticketlist",date);
		
		if(list == null)
		{
			tlist = new JSONArray();
		}
		else
		{
			tlist = new JSONArray(list);
		}
		
		tlist.put(ticketId);
		api.data().addField("ticketlist",date, tlist.toString());
	}	
	
	@OnKeyword("report")
	public void onReport(TeamchatAPI api)
	{
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");		
		String date = formatter.format(Calendar.getInstance().getTime());
		JSONArray tlist = null;
		String list = api.data().getField("ticketlist",date);
		
		if(list == null)
			return;
					
		tlist = new JSONArray(list);
		publishDailyReport(api, tlist);
	}
	
	private void publishDailyReport(TeamchatAPI api, JSONArray tickets)
	{
		String roomid = api.context().currentRoom().getId();
		int ticketcount = 0;
		

		String html = "<table border=\"1\" cellpadding=\"1\" cellspacing=\"0\">";
		
		html += "<tr>";
		for (int j = 0; j < DAILY_REPORT_LABELS.length; j++) 
			html += "<td>" + DAILY_REPORT_LABELS[j] + "</td>";
		html += "</tr>";
		
		for(int i=0;i<tickets.length();i++)
		{
			JSONObject data = getTicketData(api, tickets.getString(i));
			
			if(!(data.getString("nocroom").equals(roomid) || data.getString("tlroom").equals(roomid)))
				continue;
				
			ticketcount++;
			html += "<tr>";
			for (int j = 0; j < DAILY_REPORT_PARAMS.length; j++) {
				if (data.has(DAILY_REPORT_PARAMS[j]))
					html += "<td>" + data.getString(DAILY_REPORT_PARAMS[j])	+ "</td>";
				else
					html += "<td> </td>";
			}
			html += "</tr>";
		}
		html += "</table>";			
		
		if(ticketcount==0)
			html = "No tickets for today";
		
		PrimaryChatlet p = new PrimaryChatlet().setQuestionHtml(html);
		api.perform(api.context().currentRoom().post(p));
	}	
}
