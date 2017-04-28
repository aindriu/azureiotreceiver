package testreceiver;

import java.awt.GridBagLayout;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class DeviceAddPopup {

	public static void main(String[] args) {
		
		DeviceAddPopup dap = new DeviceAddPopup();
		dap.init();

	}

	public void init() {
		JTextField deviceID = new JTextField();
		JTextField deviceName = new JTextField();
		final JComponent[] inputs = new JComponent[] {
		        new JLabel("Device ID"),
		        deviceID,
		        new JLabel("Device Name"),
		        deviceName,
		};
		int result = JOptionPane.showConfirmDialog(null, inputs, "Add device", JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			if (deviceID.getText().length() > 0 && deviceName.getText().length() > 0)
		    	DeviceNameMap.addDevice(deviceID.getText(), deviceName.getText());
		} else {
		   
		}
		
	}

}
