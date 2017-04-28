package testreceiver;
import com.microsoft.azure.sdk.iot.service.*;
import com.microsoft.azure.sdk.iot.service.exceptions.IotHubException;

import java.io.IOException;
import java.net.URISyntaxException;

public class Cloud2DeviceSender {

	//HostName=riv.azure-devices.net;SharedAccessKeyName=iothubowner;SharedAccessKey=lkqf7Ycx56OlZGPg0YzYhF8S0u+YBbnhCV/ohHXmBvM=
	private static final String connectionString = "HostName=riv.azure-devices.net;SharedAccessKeyName=iothubowner;SharedAccessKey=lkqf7Ycx56OlZGPg0YzYhF8S0u+YBbnhCV/ohHXmBvM=";
	private static final String deviceId = "214279";
	private static final IotHubServiceClientProtocol protocol = IotHubServiceClientProtocol.AMQPS;
	 
	public static void main(String[] args) {
		  ServiceClient serviceClient;
		try {
			serviceClient = ServiceClient.createFromConnectionString(
					     connectionString, protocol);
	

				   if (serviceClient != null) {
				     serviceClient.open();
				     FeedbackReceiver feedbackReceiver = serviceClient
				       .getFeedbackReceiver(deviceId);
				     if (feedbackReceiver != null) feedbackReceiver.open();

				     Message messageToSend = new Message("test message");
				     messageToSend.setDeliveryAcknowledgement(DeliveryAcknowledgement.Full);

				     serviceClient.send(deviceId, messageToSend);
				     System.out.println("Message sent to device");

				     FeedbackBatch feedbackBatch = feedbackReceiver.receive(10000);
				     if (feedbackBatch != null) {
				       System.out.println("Message feedback received, feedback time: "
				         + feedbackBatch.getEnqueuedTimeUtc().toString());
				     }

				     if (feedbackReceiver != null) feedbackReceiver.close();
				     serviceClient.close();
				   }
				 
		} catch (IOException | IotHubException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
