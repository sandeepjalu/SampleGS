package com.teamchat.client.demo.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.TextChatlet;
import com.teamchat.client.utils.WebAppTeamChatAPI;

/**
 * Servlet to demonstrate how to obtain the instance of the TeamchatAPI.
 */

public class GetTeamChatAPIDemoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public GetTeamChatAPIDemoServlet() {
    	
    }

	/**This method when called it post a message to the sent email.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email=request.getParameter("email");
		//Retrieving the instance of the TeamchatAPI
		TeamchatAPI api=WebAppTeamChatAPI.getTeamchatAPIInstance(getServletConfig());
		
		api.perform(api.context().create().add(email).post(new TextChatlet("Hi, Have a nice day.")));
	}

}
