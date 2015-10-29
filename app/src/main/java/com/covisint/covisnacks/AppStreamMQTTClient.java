package com.covisint.covisnacks;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonStructure;
import javax.json.JsonValue.ValueType;
import javax.websocket.RemoteEndpoint;
import javax.websocket.RemoteEndpoint.Async;
import javax.websocket.Session;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;









import com.covisint.platform.messaging.sendevent.core.SupportedMediaType;
import com.covisint.platform.messaging.sendevent.core.sendevent.SendEvent;
import com.covisint.platform.messaging.sendevent.core.sendevent.io.json.SendEventReader;
import com.covisint.platform.messaging.sendevent.core.sendevent.io.json.SendEventWriter;
//import com.covisint.platform.messaging.sendevent.core.sendevent.SendEvent;
//import com.covisint.platform.messaging.sendevent.core.sendevent.io.json.SendEventReader;
import com.google.api.client.util.Base64;
import com.google.common.net.MediaType;
import com.websocket.SessionHandler;

public class AppStreamMQTTClient implements MqttCallback{

    private static AppStreamMQTTClient INSTANCE = null;

    private SessionHandler webSocketSessionHandler = null;

    // the MQTT client for Connecting to Covisint IoT Platform
    MqttClient client;
    MqttConnectOptions conOpt;
    /**
     * Stream connectivity information. All information provided by Covisint Iot
     * Stream service at the time of stream creation for this application. ALL
     * OF THESE VARIABLES SHOULD BE REPLACED WITH THE VALUES RETURNED WHEN
     * CREATING YOUR COVISINT APPLICATION STREAM
     */
    static String HOST = "mqtt.covapp.io";
    static String PORT = "8883";
    static String USERNAME = "86a71293-c426-40ae-900b-60940c635a0e";
    static String PASSWORD = "d20f640c-cfe8-4148-8255-da92432cda7e";
    static String CLIENTID = "d64Ec891770141CF8fFC";

    int messageId = (int) Math.random() * 50 + 1;// BobDojSequenceNum908


    /**
     * Topic information for application stream. All information provided by the
     * Covisint IoT Stream service at the time of stream creation for this
     * application. ALL OF THESE VARIABLES SHOULD BE REPLACED WITH THE VALUES
     * RETURNED WHEN CREATING YOUR COVISINT APPLICATION STREAM
     */
    static String consumerTopicPlatform = "6a60109b-abd1-44c0-b44d-802ca96651cb";
    static String alertConsumerTopicPlatform = "4d245356-068d-4a1c-808d-a673aa00fe4e";
    static String producerTopicPlatform = "a3841781-80d8-4e4a-a9e3-cd3e0f518e24";

    // the following two flags control whether this example is a publisher, a
    // subscriber or both
    static final Boolean subscriber = true;
    static final Boolean publisher = true;

    /**
     * try to reconnect to the MQTT Broker with connectivity info and
     * credentials
     */
    public void connectionLost(Throwable cause) {
    }

    /**
     *
     * deliveryComplete This callback is invoked when a message published by
     * this client is successfully received by the broker.
     *
     */
    public void deliveryComplete(MqttDeliveryToken token) {
    }

    /**
     *
     * messageArrived This callback is invoked when a message is received on a
     * subscribed topic.
     *
     */
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("Got a message! : " + message.getPayload());



        final SendEventReader reader = new SendEventReader();
        final SendEventWriter writer = new SendEventWriter();

        SendEvent sendEvent = (SendEvent) reader.read(MediaType.parse(com.covisint.platform.messaging.sendevent.core.
                        SupportedMediaType.SENDEVENT_JSON_V1_MT.value()),
                new ByteArrayInputStream(message.getPayload()));
        // decrypt and decode the payload.
        sendEvent.setMessage(new String(decodeMessage(sendEvent.getMessage())));
        sendEvent.setMessage(sendEvent.getMessage().replaceAll("[\n\r]", ""));


        for(Session session : webSocketSessionHandler.getSessions()) {
            System.out.println("sending a message to web sockets with SendEvent");
            JsonObjectBuilder eventObject = Json.createObjectBuilder();

            // Build a JSON object out of the payload.
            final JsonStructure jsonStruct = Json.createReader(new StringReader(sendEvent.getMessage())).read();
            // Make sure its an object and not some other type.
            if (jsonStruct.getValueType() != ValueType.OBJECT) {
                System.out.println("Message payload not JSON");
                return;
            }

            JsonObjectBuilder jBuilderSendEvent =  writer.writeResource(MediaType.parse(SupportedMediaType.SENDEVENT_JSON_V1_MT.value()), sendEvent);
            eventObject
                    .add("sendEvent", jBuilderSendEvent.build())
                    .add("jsonPayload", jsonStruct);

            Async remote = session.getAsyncRemote();
            remote.sendText(eventObject.build().toString());
        }
    }

    /**
     * Decode the message from base64 to byte array.
     *
     * @param message message in Base64.
     * @return the ByteArray.
     * @throws IOException
     */
    private byte[] decodeMessage(String message) throws IOException {
        return Base64.decode(message.getBytes());
    }

    /**
     * Singleton for this object.
     *
     * @return
     */
    public static synchronized AppStreamMQTTClient getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AppStreamMQTTClient();
            INSTANCE.createMQTTConnection();
        }
        return INSTANCE;
    }

    public void setSessionHandler(SessionHandler newWebSocketSessionHandler)
    {
        INSTANCE.webSocketSessionHandler = newWebSocketSessionHandler;
    }

    /**
     * Initialize the MQTT client. Connect to the MQTT broker and subscribe to
     * consumerTopic and alertConsumerTopic
     *
     */
    public void createMQTTConnection() {
        conOpt = new MqttConnectOptions();
        conOpt.setCleanSession(true);
        conOpt.setKeepAliveInterval(30);
        conOpt.setUserName(USERNAME);
        conOpt.setPassword(PASSWORD.toCharArray());

        try {
            client = new MqttClient("ssl://" + HOST + ":" + PORT, CLIENTID);
            client.setCallback(this);
            client.connect(conOpt);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // subscribe to the consumer and alert topics. these topics will
        // have device events delivered to them.
        try {
            client.subscribe(consumerTopicPlatform);
            client.subscribe(alertConsumerTopicPlatform);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Construct a SendCommand message that will be published to the producerTopic. This message will ultimately
     * be delivered to the target device to process.
     *
     * @param deviceId
     * @param commandId
     * @param commandArgs
     */
    public void publishCommand(String deviceId, String commandId, String commandArgs)
    {
        try
        {
            // Check some sensor value and send to Covisint IoT Cloud
            JsonObject deviceEventMessage = Json.createObjectBuilder()
                    .add("messageId", "somegeneratedValue")
                    .add("deviceId", deviceId)
                    .add("commandId", commandId)
                    .add("encodingType", "base64")
                    .add("message", encryptAndEncodeCommandArgs(commandArgs))
                    .build();
            MqttMessage message = new MqttMessage();
            message.setPayload(deviceEventMessage.toString().getBytes());
            // publish command to producer topic
            client.publish(producerTopicPlatform, message);

        } catch (MqttPersistenceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MqttException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    /**
     * Based on the definition for the Application Stream. Encrypt the
     * commandArgs using the producerPrivateKey and then encode.
     *
     * @param commandArgs
     * @return
     */
    private String encryptAndEncodeCommandArgs(String commandArgs) {
        return commandArgs;
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken arg0) {
        // TODO Auto-generated method stub

    }
}
