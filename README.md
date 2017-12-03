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

----------------------------------INSTALLATION INSTRUCTIONS FOR WINDOWS----------------------------------

Follow these instructions to install the AutoScouts system developed by Reed's Group to a Windows computer. If you find
that you require more detailed instructions on how to complete a given step, Google and Stack Overflow are your friends.

Step 1: Ensure you have Apache Ant (bundled with many Java IDEs) and either MySQL or MariaDB installed.

Step 2: From the Windows command line, change directory into the AutoScouts directory and run the command "ant dist" (without
the quotes). Ignore the many warning messages this generates.

Step 3: Run your database's command prompt and enter "source [path to file]\create_autoscouts.sql" (without ANY quotes; for
example, source C:\My Stuff\Autoscouts\dist\sql\create_autoscouts.sql).

Step 4: The database login credentials are in the dblogin.txt file. Make sure you create a user with username "cs3365" and
password "reedsgroup" that will connect to localhost on port 3306 to database autoscouts. Copy and paste the following line
into the SQL command line once you are logged in: GRANT ALL PRIVILEGES ON autoscouts.* TO 'cs3365'@'localhost' IDENTIFIED BY 'reedsgroup';

Step 5: Run the server by either: 
	a) Navigating to the dist folder in Windows Explorer and double-clicking AutoScouts.jar (if you want it to run silently)
	b) Navigating to the dist folder in your command prompt window and running the command "java -jar dist/AutoScouts.jar"
	(without the quotes) (if you want to see the command-line output)
	 
Step 6: You are now running the AutoScouts system. You may run any of the three client interfaces by double-clicking on their
icons in their folder or by running them from the command line the same way you run AutoScouts.jar.

