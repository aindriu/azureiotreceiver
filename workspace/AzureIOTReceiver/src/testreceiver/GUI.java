package testreceiver;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class GUI {
	private JTextField connString;
	private JTextField partString;
	private JTable table;

	public static void main(String args[]) {

		GUI t = new GUI();
		t.init();
	}

	public void init() {
		GridBagLayout gbl = new GridBagLayout();
		JFrame frame = new JFrame("Device Data");
		frame.setLayout(gbl);

		// Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		// frame.setPreferredSize(dim);
		frame.setVisible(true);

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = 1;
		gc.gridy = 1;
		gc.insets = new Insets(5, 5, 5, 5);

		JLabel conn = new JLabel("Connection String:");
		frame.add(conn, gc);

		gc = new GridBagConstraints();
		gc.gridx = 2;
		gc.gridy = 1;
		gc.insets = new Insets(5, 5, 5, 5);

		connString = new JTextField(
				"Endpoint=sb://iothub-ns-riv-144717-4d6a93f30c.servicebus.windows.net/;EntityPath=riv;SharedAccessKeyName=iothubowner;SharedAccessKey=lkqf7Ycx56OlZGPg0YzYhF8S0u+YBbnhCV/ohHXmBvM=",
				20);

		frame.add(connString, gc);

		gc = new GridBagConstraints();
		gc.gridx = 1;
		gc.gridy = 2;
		gc.insets = new Insets(5, 5, 5, 5);
		JLabel part = new JLabel("Partition ID:");
		frame.add(part, gc);

		gc = new GridBagConstraints();
		gc.gridx = 2;
		gc.gridy = 2;
		gc.insets = new Insets(5, 5, 5, 5);
		partString = new JTextField("0", 20);

		frame.add(partString, gc);

		gc = new GridBagConstraints();
		gc.gridx = 3;
		gc.gridy = 3;
		gc.insets = new Insets(5, 5, 5, 5);
		JButton connectButton = new JButton("Connect");
		frame.add(connectButton, gc);
		connectButton.addActionListener(new ConnectActionListener(this));

		gc = new GridBagConstraints();
		gc.gridx = 4;
		gc.gridy = 3;
		gc.insets = new Insets(5, 5, 5, 5);
		JButton clearButton = new JButton("Clear");
		frame.add(clearButton, gc);
		clearButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				DefaultTableModel dm = (DefaultTableModel) table.getModel();
				dm.getDataVector().removeAllElements();
				dm.fireTableDataChanged();

			}
		});

		gc = new GridBagConstraints();
		gc.gridx = 5;
		gc.gridy = 3;
		gc.insets = new Insets(5, 5, 5, 5);
		JButton saveButton = new JButton("Save");
		frame.add(saveButton, gc);
		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Specify file to save");
				File file;
				int userSelection = fileChooser.showSaveDialog(frame);

				if (userSelection == JFileChooser.APPROVE_OPTION) {
					file = fileChooser.getSelectedFile();
					if (!file.toString().contains(".csv"))
						file = new File(file.getPath() + ".csv");

				} else
					return;

				try {
					BufferedWriter writer = new BufferedWriter(new FileWriter(file));
					DefaultTableModel dtm = (DefaultTableModel) table.getModel();
					int nRow = dtm.getRowCount(), nCol = dtm.getColumnCount();

					for (int i = 0; i < nRow; i++) {
						for (int j = 0; j < nCol; j++) {
							writer.write("" + dtm.getValueAt(i, j) + ",");
						}
						writer.write("\n");

						file.delete();
					}
					writer.close();
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "IO Issue: " + e1.getMessage(), "ERROR",
							JOptionPane.WARNING_MESSAGE);

				}

			}
		});

		String[] columnNames = { "Device ID", "Time", "Payload" };
		/*
		 * Object[][] data = { {"23434234", "34543534", "teststring"},
		 * {"34432432", "43545435", "anotherstring"}, {"35543535", "34435454",
		 * "yetanother"}, };
		 */
		Object[][] data = {};

		gc = new GridBagConstraints();
		gc.gridx = 1;
		gc.gridy = 4;
		gc.insets = new Insets(5, 5, 5, 5);
		gc.gridwidth = GridBagConstraints.REMAINDER;
		gc.gridheight = GridBagConstraints.REMAINDER;
		gc.weighty = 1;
		DefaultTableModel dtm = new DefaultTableModel(data, columnNames);

		table = new JTable(data, columnNames);
		table.setModel(dtm);
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
		table.setRowSorter(sorter);
		JScrollPane scrollPane = new JScrollPane(table);

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		// frame.setPreferredSize(dim);
		double i1 = dim.getWidth() - 200.0;
		double i2 = dim.getHeight() - 200.0;
		Dimension dim2 = new Dimension();
		dim2.width = (int) i1;
		dim2.height = (int) i2;
		scrollPane.setPreferredSize(dim2);

		table.setFillsViewportHeight(true);

		frame.add(scrollPane, gc);
		frame.pack();
		frame.setVisible(true);
	}

	public String getConnectionString() {
		return connString.getText();
	}

	public String getPartitionID() {
		return partString.getText();
	}

	public void addToTable(String deviceName, String time, String str) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		Date d = new Date(Long.parseLong(time));

		StringBuilder output = new StringBuilder();
		for (int i = 0; i < str.length(); i += 2) {
			String s = str.substring(i, i + 2);
			output.append((char) Integer.parseInt(s, 16));
		}

		model.addRow(new Object[] { deviceName, d.toString(), output.toString() });
	}
}
