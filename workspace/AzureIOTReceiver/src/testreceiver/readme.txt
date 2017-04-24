Setting up Microsoft Azure IoT:

Log into Microsoft Azure IoT after creating an account - https://portal.azure.com
To create a new Azure IOT HUB click New -> Internet Of Things -> IOT Hub.
Enter in Name, and set Device-to-cloud partitions to 2. Create a new resource group and the location to west europe.

To get the connection string for sigfox backend go to the azure iot hub you created, and click on 'Shared access policies' under Settings. Click on iothubowner under policy and copy the 'Connection String'.  This string is used in both the application and for the sigfox backend.

Setting up a device in sigfox backend:
Go to http://snoc.fr.sigfoxactivate and create the account as specified.
Log into the site - https://backend.sigfox.com
Click on the device and then the link on device type. This opens a new window and a menu to the left. Click on callbacks.
Click New to the top right.
Add the connection string from the microsoft azure IOT site as the connection string.
Add the json string as this:
    {
        "device" : "{device}",
        "time" : {time},
        "data" : "{data}"
    }


