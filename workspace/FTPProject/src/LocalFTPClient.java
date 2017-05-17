import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class LocalFTPClient {

	private String hostname;
	private String port;
	private String username;
	private String password;
	
	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public static void main(String[] args) {
		
		LocalFTPClient f = new LocalFTPClient();
		f.moveAllFiles();
	}
	
	public void moveAllFiles()
	{
		FTPClient ftp = new FTPClient();
	    FTPClientConfig config = new FTPClientConfig();
	    ArrayList<FTPFile> filesArray = new ArrayList<FTPFile>(); 
	    
	    ftp.configure(config);
	    boolean error = false;
	    try {
	      int reply;
	      String server = "localhost";
	      ftp.connect(server);
	      

	      
	      reply = ftp.getReplyCode();

	      if(!FTPReply.isPositiveCompletion(reply)) {
	        ftp.disconnect();
	        System.err.println("FTP server refused connection.");
	        System.exit(1);
	      }
	      ftp.login("andrew", "test");
	      String dir = ftp.printWorkingDirectory();
	      //System.out.println(dir);
	      FTPFile[] files = ftp.listFiles(dir);
	      
	      for (int i = 0; i < files.length; i++)
	      {
	    	  Calendar c = files[i].getTimestamp();
	    	  //System.out.println(files[i] + " " + c.getTimeInMillis());
	    	  filesArray.add(files[i]);
	      }
	      
	      
	      LinkedList<FTPFile> filesToDelete = new LinkedList<FTPFile>();
	      for (FTPFile f : filesArray)
	      {
	    	  if (f.isDirectory())
	    		  filesToDelete.add(f);
	      }
	      
	      Collections.sort(filesArray, new Comparator<FTPFile>() {

			@Override
			public int compare(FTPFile arg0, FTPFile arg1) {
				return arg1.getTimestamp().compareTo(arg0.getTimestamp());
			} } );
	      
	      
	      for (FTPFile f : filesToDelete)
	    	  filesArray.remove(f);
	      
	      
	     
	      
	      if (filesArray.size() > 0)
	      {
	    	  for (FTPFile f : filesArray)
	    	  {
		    	  String month = "" + (f.getTimestamp().getTime().getMonth() + 1);
		    	  
		    	  SimpleDateFormat format = new SimpleDateFormat("yyyy");
		  		  String year = format.format(f.getTimestamp().getTime());
		  		  
		  		  //System.out.println("Month " + month + " Year " + year);
		  		  
		  		  FTPFile folder = createFolder("127.0.0.1", year, month);
		  		  System.out.println(folder.getName());
		  		  
		  		 
		  		  ftp.rename(f.getName(), "/" + folder.getName() + "/" + f.getName());
		  				
	    	  }
	          
	         
	      }
	      
	      
	      ftp.logout();
	    } catch(IOException e) {
	      error = true;
	      e.printStackTrace();
	    } finally {
	      if(ftp.isConnected()) {
	        try {
	          ftp.disconnect();
	        } catch(IOException ioe) {
	          // do nothing
	        }
	      }
	    }
	    
	}
	public FTPFile createFolder(String ipAddress, String year, String month)
	{
		/*
		Date date = new Date();
		String folderName;
		SimpleDateFormat format = new SimpleDateFormat("yyyy_MM");
		String dateS = format.format(date);
		*/
		
		
		String folderName = "" + year + "_" + month + "_" + ipAddress;
		
		FTPClient ftp = new FTPClient();
	    FTPClientConfig config = new FTPClientConfig();
	    ArrayList<FTPFile> filesArray = new ArrayList<FTPFile>(); 
	    
	    ftp.configure(config);
	    boolean error = false;
	    try {
	      int reply;
	      String server = "localhost";
	      ftp.connect(server);
	      
	      reply = ftp.getReplyCode();

	      if(!FTPReply.isPositiveCompletion(reply)) {
	        ftp.disconnect();
	        System.err.println("FTP server refused connection.");
	        System.exit(1);
	      }
	      ftp.login("andrew", "test");
	      String dir = ftp.printWorkingDirectory();
	      
	      FTPFile[] files = ftp.listFiles(dir);
	      
	      
	      for (int i = 0; i < files.length; i++)
	      {
	    	  if (files[i].getName().equals(folderName))
	    		  return files[i];
	    	  
	      }

	      boolean s = ftp.makeDirectory(dir + folderName);
	      
	      files = ftp.listFiles(dir);
	      
	      for (int i = 0; i < files.length; i++)
	      {
	    	  if (files[i].getName().equals(folderName))
	    		  return files[i];
	    	  
	      }
	      
	      
	      ftp.logout();
	    } catch(IOException e) {
	      error = true;
	      e.printStackTrace();
	    } finally {
	      if(ftp.isConnected()) {
	        try {
	          ftp.disconnect();
	        } catch(IOException ioe) {
	          // do nothing
	        }
	      }
	    }
	    return null;
	}
	public File downloadNewestFile() {
		FTPClient ftp = new FTPClient();
	    FTPClientConfig config = new FTPClientConfig();
	    ArrayList<FTPFile> filesArray = new ArrayList<FTPFile>(); 
	    
	    ftp.configure(config);
	    boolean error = false;
	    try {
	      int reply;
	      String server = "localhost";
	      ftp.connect(server);
	      

	      
	      reply = ftp.getReplyCode();

	      if(!FTPReply.isPositiveCompletion(reply)) {
	        ftp.disconnect();
	        System.err.println("FTP server refused connection.");
	        System.exit(1);
	      }
	      ftp.login("andrew", "test");
	      String dir = ftp.printWorkingDirectory();
	      //System.out.println(dir);
	      FTPFile[] files = ftp.listFiles(dir);
	      
	      for (int i = 0; i < files.length; i++)
	      {
	    	  Calendar c = files[i].getTimestamp();
	    	  //System.out.println(files[i] + " " + c.getTimeInMillis());
	    	  filesArray.add(files[i]);
	      }
	      
	      
	      LinkedList<FTPFile> filesToDelete = new LinkedList<FTPFile>();
	      for (FTPFile f : filesArray)
	      {
	    	  if (f.isDirectory())
	    		  filesToDelete.add(f);
	      }
	      
	      Collections.sort(filesArray, new Comparator<FTPFile>() {

			@Override
			public int compare(FTPFile arg0, FTPFile arg1) {
				return arg1.getTimestamp().compareTo(arg0.getTimestamp());
			} } );
	      
	      
	      for (FTPFile f : filesToDelete)
	    	  filesArray.remove(f);
	      
	      
	     
	      
	      if (filesArray.size() > 0)
	      {
	    	  FTPFile lastEntry = filesArray.get(0);
	    	  FTPFile file = lastEntry;
	    	  ftp.enterLocalPassiveMode();
	          ftp.setFileType(ftp.BINARY_FILE_TYPE);
	          
	          String remoteFile1 = "/" + file.getName();
	          File downloadFile1 = new File(System.getProperty("user.dir") + "\\" + file.getTimestamp().getTimeInMillis() + "-" + file.getName() );
	          OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
	            boolean success = ftp.retrieveFile(remoteFile1, outputStream1);
	            outputStream1.close();
	 
	            if (success) {
	                //System.out.println("File #1 has been downloaded successfully.");
	                ftp.logout();
	                return downloadFile1;
	            }
	            
	            return null;
	            
	      }
	      
	      
	      ftp.logout();
	    } catch(IOException e) {
	      error = true;
	      e.printStackTrace();
	    } finally {
	      if(ftp.isConnected()) {
	        try {
	          ftp.disconnect();
	        } catch(IOException ioe) {
	          // do nothing
	        }
	      }
	    }
	    return null;
	}

}
