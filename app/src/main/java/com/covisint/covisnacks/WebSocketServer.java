
package com.covisint.covisnacks;

import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

//import com.covisint.iot.referenceImpl.mqtt.AppStreamMQTTClient;

@ApplicationScoped
@ServerEndpoint("/actions")
public class WebSocketServer {

    // Initialize a new SessionHandler
    @Inject
    private SessionHandler sessionHandler = new SessionHandler();

    AppStreamMQTTClient appStream = AppStreamMQTTClient.getInstance();

    /**
     * Opens a WebSocket session.
     *
     * @param session
     */
    @OnOpen
    public void open(Session session) {
        System.out.println("Session opened........");
        sessionHandler.addSession(session);

        if (sessionHandler.getSessions().size()  == 1)
        	appStream.setSessionHandler(sessionHandler);
    }

    /**
     * Closes a WebSocket session.
     *
     * @param session
     */
    @OnClose
    public void close(Session session) {
        System.out.println("Session closed");
        sessionHandler.removeSession(session);
    }

    /**
     * Throws an error in case of issue in WebSocket session.
     *
     * @param error
     */
    @OnError
    public void onError(Throwable error) {
        System.out.println("Session error");
        Logger.getLogger(WebSocketServer.class.getName()).log(Level.SEVERE, null, error);
    }

    /**
     * Handles the messages coming on the server from the browser.
     *
     * @param message Incoming message from the client
     * @param session Current WebSocket session
     * @return String Message from server to the client/browser
     */
    @OnMessage
    public String handleMessage(String message, Session session) {


        try {
			System.out.println("Handling messages");
			if (message.equals("GL1")) {
				this.appStream.publishCommand("80ede915-76ad-46a8-971b-e49ba545d03f", "bd69917c-7aa6-45eb-905b-ef332826ed93", "eyJjb2xvciI6MSwic3dpdGNoIjoxfQ==");
			} else if (message.equals("GL0")) {
				//{"color":1,"switch":0}
				this.appStream.publishCommand("80ede915-76ad-46a8-971b-e49ba545d03f", "bd69917c-7aa6-45eb-905b-ef332826ed93", "eyJjb2xvciI6MSwic3dpdGNoIjowfQ==");
			} else if (message.equals("BL1")) {
				// {"color":2,"switch":1}
				this.appStream.publishCommand("80ede915-76ad-46a8-971b-e49ba545d03f", "bd69917c-7aa6-45eb-905b-ef332826ed93", "eyJjb2xvciI6Miwic3dpdGNoIjoxfQ==");
			} else if (message.equals("BL0")) {
				this.appStream.publishCommand("80ede915-76ad-46a8-971b-e49ba545d03f", "bd69917c-7aa6-45eb-905b-ef332826ed93", "eyJjb2xvciI6Miwic3dpdGNoIjowfQ==");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return "";
    }

}
