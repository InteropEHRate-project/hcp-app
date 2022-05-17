# InteropEHRate HCP Application

The HCP App is software demonstrating the usage of the InteropEHRate protocols D2D and R2D.

Installation guide

Tools: laptop with access on internet and operating system -> Windows, bluetooth
Download the apps here: 
HCP App:
 Steps to follow for the installation of the HCP App:
- activate the bluetooth on the device
- right click on the Run-HCP.app .bat file and click edit; 

![image](https://user-images.githubusercontent.com/104497337/168797837-536a953d-e6ac-4177-a0e1-159dc3df5366.png)


-modify the path to the IPSValidatorPack folder files after the "=" sign: "-Dips.validator.pack=C:\\HCP-app\\IPSValidatorPack" -> it is your path (on your laptop) where the archive has been unzipped.

![image](https://user-images.githubusercontent.com/104497337/168796194-1f3c09fb-0ad6-4b2f-b02b-ac98a59f5a3e.png)


Make sure that the path is correct. The path should contain DOUBLE BACK-SLASHES "\\".

![image](https://user-images.githubusercontent.com/104497337/168796107-87ab610b-f4d2-44bf-b34d-0f5fabafb475.png)

Save the modifications that were made in the Run-HCP.app file and then double click on the file.
Please, note that this path is required for using the D2D library. The HCP application will still run even if the path is wrong but without the possibility of using the D2D library.
 HCP app: hcp-web-app.jar can be reached at http://localhost:8080/hcp-web-ui
Backend hospital microservice hcp-app-hospital-services.jar that can be reached at http://localhost:8075/  (this one doesn't have an user interface).

 For the Login credentials are:
 
	- Username: doctor
	- Password: doctor

