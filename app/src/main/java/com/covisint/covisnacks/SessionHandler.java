
package com.covisint.covisnacks;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;

///import com.covisint.iot.referenceImpl.mqtt.AppStreamMQTTClient;

@ApplicationScoped
public class SessionHandler {

    private final Set<Session> sessions = new HashSet<>();

    //BrokerService brokerService = BrokerService.getInstance();
    
    
    

    /**
     * Removes a WebSocket session.
     * 
     * @param session Session to be removed
     */
    public void removeSession(Session session) {
        sessions.remove(session);
    }

    /**
     * Adds a WebSocket Session.
     * 
     * @param session Session to be added
     */
    public void addSession(Session session) {
        sessions.add(session);
    }

    /**
     * To start receiving EventData from server.
     * 
     * @return Event messages from the server
     */
    public String startRecieve() {
//        brokerService.startReceiverService();
//        return brokerService.getEventData().toString();
    	return "";
    }

    /**
     * To stop receiving EventData from server.
     */
    public void stopReceive() {
        System.out.println("Session Handler Stopped Receiving");
        //brokerService.stopReceiverService();
    }

    /**
     * To publish a command.
     * 
     * @param deviceId Id of the device
     * @param lightStatus Light color of the LED
     */
    public void sendCommand(String deviceId, String lightStatus) {
        //brokerService.sendACommand(deviceId, lightStatus);

    }

    /**
     * To retrieve commands for a particular device in a particular realm.
     * 
     * @param deviceId Id of the device
     * @param realm realm being searched
     */
    public String getCommandsForRealmAndDeviceId(String deviceId, String realm) {
        //return brokerService.getCommandsForRealmAndDeviceId(deviceId, realm);
    	return "";
    }

//    /**
//     * To retrieve devices for a particular realm.
//     *
//     * @param deviceId Id of the device
//     * @param realm realm being searched
//     */
    public String getAllDevicesForRealm(String realm) {
        //return brokerService.getAllDevicesForRealm(realm);
    	return "";
    }

	public Set<Session> getSessions() {
		return sessions;
	}

}
