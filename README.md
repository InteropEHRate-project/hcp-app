# hcp-app

InteropEHRate HCP Application
The HCP App is software demonstrating the usage of the InteropEHRate protocols D2D and R2D.

Installation guide

Tools: laptop with access on internet and operating system -> Windows, bluetooth Download the apps here: HCP App: Steps to follow for the installation of the HCP App:

activate the bluetooth on the device
right click on the Run-HCP.app .bat file and click edit;
image

-modify the path to the IPSValidatorPack folder files after the "=" sign: "-Dips.validator.pack=C:\HCP-app\IPSValidatorPack" -> it is your path (on your laptop) where the archive has been unzipped.

image

Make sure that the path is correct. The path should contain DOUBLE BACK-SLASHES "\".

image

Save the modifications that were made in the Run-HCP.app file and then double click on the file. Please, note that this path is required for using the D2D library. The HCP application will still run even if the path is wrong but without the possibility of using the D2D library. HCP app: hcp-web-app.jar can be reached at http://localhost:8080/hcp-web-ui Backend hospital microservice hcp-app-hospital-services.jar that can be reached at http://localhost:8075/ (this one doesn't have an user interface).

For the Login credentials are:

- Username: doctor
- Password: doctor
