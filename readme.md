Prerequisites:

1) Java 7 or 8.
2) MySQL v5 or greater (while installing username and password should be “root” and “masterig” respectively or change the “user” and “password” inside “/app/conf/application.conf” file in the project).
3) Activator/Play Framework.

Installation instructions for Play Framework and MySQL setup - 

1) Download and extract activator from following link -
https://downloads.typesafe.com/typesafe-activator/1.3.10/typesafe-activator-1.3.10-minimal.zip

2) Edit the ~/.bashrc file and give the path name of the bin folder inside the activator
directory (after extracting activator. Use following commands to do so -
$ sudo nano ~/.bashrc
At the end of file add this line -
export PATH = pathToBinFolderInsideActivator-1.3.10-minimal: $PATH

eg: export PATH = user/home/Desktop/activator-1.3.10-minimal/bin: $PATH
save the file and exit

3) Clone the project from Git 
link: https://github.com/surajanthwal/NotificationUsingWebSockets.git

4) Create the MySQL database “UserData” from MySQL dump inside “pathToProjectDirectory/public/UserData.sql”
Steps: Inside MySQL console
mysql> create database UserData;
mysql> use UserData;
mysql> source /pathToUserData.sql;

5) Open the terminal, go to the project directory and use this command to start the server on the machine as localhost.
NOTE: During first run, activator will download required plugins and dependancies, so it may take a while.
$ activator run
use ctrl + c to stop the server

Usage: 

1) Some sample login credential are there in “Users” table of “UserData” Database. You can use them or you can also signup and create new users.

2) Now you can launch the web portal from the local host. Use the following address in your browser - 
“localhost:9000/”

3) Multiple users can log in from different browsers.

4) To access the server from different machines, replace “localhost” in “/app/assets/app.js” file with the IP Address of the machine on which server is running. Save and restart the server.
