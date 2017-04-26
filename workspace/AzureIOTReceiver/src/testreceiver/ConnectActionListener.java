package testreceiver;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.function.Consumer;

import javax.swing.JOptionPane;

import org.json.JSONObject;

import com.microsoft.azure.eventhubs.EventData;
import com.microsoft.azure.eventhubs.EventHubClient;
import com.microsoft.azure.eventhubs.PartitionReceiver;
import com.microsoft.azure.servicebus.ServiceBusException;

public class ConnectActionListener implements ActionListener {

	private static GUI testGUI;

	public ConnectActionListener(GUI testGUI) {
		this.testGUI = testGUI;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		EventHubClient client0 = receiveMessages("0");
		EventHubClient client1 = receiveMessages("1");

	}

	private static EventHubClient receiveMessages(final String partitionId) {
		EventHubClient client = null;
		try {

			client = EventHubClient.createFromConnectionStringSync(testGUI.getConnectionString());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Failed  to create client: " + e.getMessage(), "ERROR",
					JOptionPane.WARNING_MESSAGE);

		}
		try {

			client.createReceiver(EventHubClient.DEFAULT_CONSUMER_GROUP_NAME, partitionId, Instant.EPOCH)
					.thenAccept(new Consumer<PartitionReceiver>() {
						public void accept(PartitionReceiver receiver) {

							try {
								while (true) {
									Iterable<EventData> receivedEvents = receiver.receive(200).get();

									int batchSize = 0;
									if (receivedEvents == null)
										break;
									if (receivedEvents != null) {
										for (EventData receivedEvent : receivedEvents) {

											JSONObject obj = new JSONObject(
													new String(receivedEvent.getBody(), Charset.defaultCharset()));
											String d = obj.getString("device");
											// long a = obj.getLong("time");
											long a = receivedEvent.getSystemProperties().getEnqueuedTime()
													.toEpochMilli();
											String st = obj.getString("data");
											String deviceName = DeviceNameMap.get(d);
											if (deviceName == null) deviceName = d;
											testGUI.addToTable(deviceName, "" + a, st);
											batchSize++;
										}
									}

								}
							} catch (Exception e) {
								JOptionPane.showMessageDialog(null, "Failed to receive messages: " + e.getMessage(),
										"ERROR", JOptionPane.WARNING_MESSAGE);
							}
						}
					});
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Failed  to create receiver: " + e.getMessage(), "ERROR",
					JOptionPane.WARNING_MESSAGE);
		}

		return client;
	}

}
