README.TXT

I created two classes. The GUI class is just a class that prompts for username / password and host / port and a connect button. There is also a properties config file called config.properties that loads the configuration information to the GUI before you connect.

The LocalFTPClient class has some methods that interact with the FTP server. 

createNewestFolder() creates a folder in the ftp server with the current date.

downloadNewestFile() downloads the newest image on the ftp server.

