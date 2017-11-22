# AutoScouts
Automated Supermarket Checkout System (Software Engineering Project)


### To compile:
$ *ant*

### To compile, generate JavaDocs, and create JAR file in dist folder:
$ *ant dist*

### To run server, after dist:
$ *java -jar dist/AutoScouts.jar*

The server (AutoScouts.jar) starts up on port 41114 by default, or increments until it finds a free port.

The server can be provided an optional argument to seek out a database credentials file.

Example: $ *java -jar AutoScouts.jar /path/to/dblogin.txt*

### To run clients, after dist:
$ *java -jar dist/RestockerUI.jar* 

$ *java -jar dist/ManagerUI.jar*

$ *java -jar dist/CustomerUI.jar*

The clients will attempt to connect to localhost on port 41114 by default, but can take an alternative address and port number as arguments.

Example: $ *java -jar dist/CustomerUI.jar myserver.example.net 45678*

### To clean up for proper push to GitHub:
$ *ant clean*


