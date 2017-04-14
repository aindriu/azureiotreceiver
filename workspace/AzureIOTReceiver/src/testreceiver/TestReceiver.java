package testreceiver;

import java.io.IOException;
import java.net.URISyntaxException;

import com.google.gson.Gson;
import com.microsoft.azure.eventhubs.*;
import com.microsoft.azure.sdk.iot.device.DeviceClient;
import com.microsoft.azure.sdk.iot.device.IotHubClientProtocol;
import com.microsoft.azure.sdk.iot.device.IotHubEventCallback;
import com.microsoft.azure.sdk.iot.device.IotHubStatusCode;
import com.microsoft.azure.sdk.iot.device.Message;
import com.microsoft.azure.servicebus.*;

import java.nio.charset.Charset;
import java.time.*;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.*;


public class TestReceiver {

	private static IotHubClientProtocol protocol = IotHubClientProtocol.MQTT;
	private static String deviceId = "testdevice";
	private static DeviceClient client;

	private static String connString = "HostName=riv.azure-devices.net;SharedAccessKeyName=iothubowner;SharedAccessKey=lkqf7Ycx56OlZGPg0YzYhF8S0u+YBbnhCV/ohHXmBvM=";

	private static class StringDataPoint {
		 public String deviceId;
		 public long sendTime;
		 public String myString;


		   public String serialize() {
		     Gson gson = new Gson();
		     return gson.toJson(this);
		   }
		 }
	
	
	 private static class EventCallback implements IotHubEventCallback
	 {
	   public void execute(IotHubStatusCode status, Object context) {
	     System.out.println("IoT Hub responded to message with status: " + status.name());

	     if (context != null) {
	       synchronized (context) {
	         context.notify();
	       }
	     }
	   }
	 }
	 
	 private static class MessageSender implements Runnable {

		 public void run()  {
			    try {
			      double avgWindSpeed = 10; // m/s
			      Random rand = new Random();

			      while (true) {
			        double currentWindSpeed = avgWindSpeed + rand.nextDouble() * 4 - 2;
			        StringDataPoint strDataPoint = new StringDataPoint();
			        strDataPoint.deviceId = deviceId;
			        
			        strDataPoint.sendTime = Instant.now().toEpochMilli();
			        strDataPoint.myString = "teststr";

			        String msgStr = strDataPoint.serialize();
			        Message msg = new Message(msgStr);
			        System.out.println("Sending: " + msgStr);

			        Object lockobj = new Object();
			        EventCallback callback = new EventCallback();
			        client.sendEventAsync(msg, callback, lockobj);

			        synchronized (lockobj) {
			          lockobj.wait();
			        }
			        Thread.sleep(1000);
			      }
			    } catch (InterruptedException e) {
			      System.out.println("Finished.");
			    }
			  }
	 }
	 

	private static String connStr = "Endpoint=sb://iothub-ns-riv-144717-4d6a93f30c.servicebus.windows.net/;EntityPath=riv;SharedAccessKeyName=iothubowner;SharedAccessKey=lkqf7Ycx56OlZGPg0YzYhF8S0u+YBbnhCV/ohHXmBvM=";
	
	private static EventHubClient receiveMessages(final String partitionId)
	 {
	   EventHubClient client = null;
	   try {
	     client = EventHubClient.createFromConnectionStringSync(connStr);
	   }
	   catch(Exception e) {
	     System.out.println("Failed to create client: " + e.getMessage());
	     System.exit(1);
	   }
	   try {
		   
	     client.createReceiver( 
	       EventHubClient.DEFAULT_CONSUMER_GROUP_NAME,  
	       partitionId,  
	       Instant.EPOCH).thenAccept(new Consumer<PartitionReceiver>()
	     {
	       public void accept(PartitionReceiver receiver)
	       {
	         System.out.println("** Created receiver on partition " + partitionId);
	         try {
	           while (true) {
	             Iterable<EventData> receivedEvents = receiver.receive(200).get();
	             int batchSize = 0;
	             if (receivedEvents != null)
	             {
	               for(EventData receivedEvent: receivedEvents)
	               {
	            	   
	                 System.out.println(String.format("Offset: %s, SeqNo: %s, EnqueueTime: %s", 
	                   receivedEvent.getSystemProperties().getOffset(), 
	                   receivedEvent.getSystemProperties().getSequenceNumber(), 
	                   receivedEvent.getSystemProperties().getEnqueuedTime()));
	                 System.out.println(String.format("| Device ID: %s", receivedEvent.getSystemProperties().get("iothub-connection-device-id")));
	                 System.out.println(String.format("| Message Payload: %s", new String(receivedEvent.getBody(),
	                   Charset.defaultCharset())));
	                 batchSize++;
	               }
	             }
	             System.out.println(String.format("Partition: %s, ReceivedBatch Size: %s", partitionId,batchSize));
	           }
	         }
	         catch (Exception e)
	         {
	           System.out.println("Failed to receive messages: " + e.getMessage());
	         }
	       }
	     });
	   }
	   catch (Exception e)
	   {
	     System.out.println("Failed to create receiver: " + e.getMessage());
	   }
	   return client;
	 }
	
	public static void main(String[] args) throws IOException {
		
		EventHubClient client0 = receiveMessages("0");
		EventHubClient client1 = receiveMessages("1");
		System.out.println("Press ENTER to exit.");
		System.in.read();
		try
		{
		  client0.closeSync();
		  client1.closeSync();
		  System.exit(0);
		}
		catch (ServiceBusException sbe)
		{
		  System.exit(1);
		}
	}

	//private static String connString = "HostName=rivtec.azure-devices.net;DeviceId=testDevice;SharedAccessKey=a3nFHxz3GDigj96JCTui03VMftQn5Q8JozETaUq2IKU=";  
	public static void main2( String[] args ) throws IOException, URISyntaxException {

		
		client = new DeviceClient(connString, protocol);
		  client.open();

		  MessageSender sender = new MessageSender();

		  ExecutorService executor = Executors.newFixedThreadPool(1);
		  executor.execute(sender);
		  
		  System.out.println("Press ENTER to exit.");
		  System.in.read();
		  executor.shutdownNow();
		  client.close();
		}
	
}
