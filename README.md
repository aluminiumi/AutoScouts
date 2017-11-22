# AutoScouts
Automated Supermarket Checkout System (Software Engineering Project)


### To compile:
$ *ant*

### To compile, generate JavaDocs, and create JAR file in dist folder:
$ *ant dist*

### To run, after dist:
$ *java -jar dist/AutoScouts.jar*

$ *java -jar dist/RestockerUI.jar* 

$ *java -jar dist/ManagerUI.jar*

$ *java -jar dist/CustomerUI.jar*

The server (AutoScouts.jar) starts up on port 41114 by default, or increments until it finds a free port.

The clients will attempt to connect on localhost by default, but can take an alternative address as an argument.

Example: $ *java -jar dist/CustomerUI.jar localhost 41115*

### To clean up for proper push to GitHub:
$ *ant clean*


