import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class GUI {

	private JLabel imageLabel;
	private LocalFTPClient localFTPClient;
	private File fl;
	
	public GUI(LocalFTPClient f) {
		this.localFTPClient = f;
	}

	public static void main(String[] args) {
		LocalFTPClient f = new LocalFTPClient();
		
		GUI t = new GUI(f);
		t.init();

		
		//f.createNewestFolder("192.168.1.201");
		
	}

	private void init() {
		Properties prop = new Properties();
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");
		String hostname = "127.0.0.1";
		String port = "21";
		String username = "andrew";
		String password = "test";
		
		if (inputStream != null)
		{
			try {
				prop.load(inputStream);
				hostname = prop.getProperty("hostname");
				port = prop.getProperty("port");
				username = prop.getProperty("username");
				password = prop.getProperty("password");
				
			} catch (IOException e1) {
				hostname = "127.0.0.1";
				port = "21";
				username = "andrew";
				password = "test";
			}
		}
		
		localFTPClient.setHostname(hostname);
		localFTPClient.setPort(port);
		localFTPClient.setUsername(username);
		localFTPClient.setPassword(password);
		
		GridBagLayout gbl = new GridBagLayout();
		JFrame frame = new JFrame("Images");
		frame.setLayout(gbl);

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

		JLabel conn = new JLabel("Host Address:");
		frame.add(conn, gc);

		gc = new GridBagConstraints();
		gc.gridx = 2;
		gc.gridy = 1;
		gc.insets = new Insets(5, 5, 5, 5);

		JTextField hostString = new JTextField(
				hostname, 20);

		frame.add(hostString, gc);
		
		gc = new GridBagConstraints();
		gc.gridx = 3;
		gc.gridy = 1;
		gc.insets = new Insets(5, 5, 5, 5);
		JLabel portL = new JLabel("Port:");
		frame.add(portL, gc);
		
		gc = new GridBagConstraints();
		gc.gridx = 4;
		gc.gridy = 1;
		gc.insets = new Insets(5, 5, 5, 5);
		JTextField portT = new JTextField(port,7);
		frame.add(portT, gc);
		
		gc = new GridBagConstraints();
		gc.gridx = 1;
		gc.gridy = 2;
		gc.insets = new Insets(5, 5, 5, 5);
		JLabel usernameL = new JLabel("Username:");
		frame.add(usernameL, gc);
		
		gc = new GridBagConstraints();
		gc.gridx = 2;
		gc.gridy = 2;
		gc.insets = new Insets(5, 5, 5, 5);
		JTextField usernameT = new JTextField(username, 20);
		frame.add(usernameT, gc);
		
		gc = new GridBagConstraints();
		gc.gridx = 3;
		gc.gridy = 2;
		gc.insets = new Insets(5, 5, 5, 5);
		JLabel passL = new JLabel("Password:");
		frame.add(passL, gc);
		
		gc = new GridBagConstraints();
		gc.gridx = 4;
		gc.gridy = 2;
		gc.insets = new Insets(5, 5, 5, 5);
		JPasswordField passT = new JPasswordField(password,15);
		frame.add(passT, gc);
		
		gc = new GridBagConstraints();
		gc.gridx = 5;
		gc.gridy = 3;
		gc.insets = new Insets(5, 5, 5, 5);
		JButton connectB = new JButton("Connect");
		frame.add(connectB, gc);
		
		
		connectB.addActionListener(new ActionListener() {

			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				LocalFTPClient f = new LocalFTPClient();
				/*
				File fl = f.downloadNewestFile();
				if (fl != null)
				{
					ImageIcon image = new ImageIcon(fl.getAbsolutePath());
					image.getImage().flush();
					imageLabel.setIcon(image);
					frame.pack();
					frame.repaint();
				}
				*/
				
				Thread t = new Thread(new Runnable() {

					@Override
					public void run() {
						for (;;)
						{
							fl = f.downloadNewestFile();
							if (fl != null)
							{
								ImageIcon image = new ImageIcon(fl.getAbsolutePath());
								image.getImage().flush();
								imageLabel.setIcon(image);
								frame.pack();
								frame.repaint();
								//fl.delete();
							}
							
							
							File downloadedFiles = new File(System.getProperty("user.dir"));
						
							File[] listOfFiles = downloadedFiles.listFiles();

							    for (int i = 0; i < listOfFiles.length; i++) {
							    	
							      if (fl != null && listOfFiles[i].getName().equals(fl.getName()))
							    	  continue;
							      if (listOfFiles[i].isFile() && (listOfFiles[i].getName().contains(".gif") || listOfFiles[i].getName().contains(".jpg") || listOfFiles[i].getName().contains(".jpeg"))) {
							        listOfFiles[i].delete();
							      }
							    }
							    
							    
							
							f.moveAllFiles();
							try {
								Thread.sleep(10000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							
						}
					} });
				t.start();
				connectB.setEnabled(false);
			} });
		
		
		gc = new GridBagConstraints();
		gc.gridx = 1;
		gc.gridy = 4;
		gc.insets = new Insets(5, 5, 5, 5);
		gc.gridwidth = GridBagConstraints.REMAINDER;
		gc.gridheight = GridBagConstraints.REMAINDER;
		
		ImageIcon image = new ImageIcon("");
        imageLabel = new JLabel(image); 
        frame.add(imageLabel,gc);
        
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        frame.pack();
		frame.setVisible(true);
        	
	}
}
