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

The server (AutoScouts.jar) starts up on port 41114.
The clients will attempt to connect on localhost by default, but can take an alternative address as an argument.

### To clean up for proper push to GitHub:
$ *ant clean*


