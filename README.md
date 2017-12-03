# AutoScouts
Automated Supermarket Checkout System (Software Engineering Project)

## Prerequisites
Ensure that you have Apache Ant (bundled with many Java IDEs), and either MySQL or MariaDB installed.

## Installation

### To setup the database:

#### From database shell:
Within your database query shell (run 'mysql' to get this shell, or use your favorite graphical interface), run the sequence of queries contained in dist/sql/create_autoscouts.sql. You may do this from the command line shell with:

	source C:\Path to File\AutoScouts\dist\sql\create_autoscouts.sql
or

	source /path/to/AutoScouts/dist/sql/create_autoscouts.sql
	
The database login credentials are in the dist/dblogin.txt file. Either update this file to reflect your credentials to your database, or create a user with username "cs3365" and password "reedsgroup" that will connect to localhost on port 3306 to database autoscouts. The following SQL query will create the appropriate user:

	GRANT ALL PRIVILEGES ON autoscouts.* TO 'cs3365'@'localhost' IDENTIFIED BY 'reedsgroup';

### To compile:
From the command line, in either Linux or Windows, navigate to the directory where AutoScouts is saved and run:

	ant

### To compile, generate JavaDocs, and create JAR file in dist folder:
From the command line, in either Linux or Windows, navigate to the directory where AutoScouts is saved and run:

	ant dist
	
Warning messages may be safely ignored.

### To run server, after dist:

#### To run server:
From the command line, run:

	java -jar dist/AutoScouts.jar

Otherwise, to run silently in the background, from a graphical interface, double-click AutoScouts.jar

The server (AutoScouts.jar) starts up on port 41114 by default, or increments until it finds a free port.

The server can be provided an optional argument to seek out a database credentials file.

Example: 

	java -jar AutoScouts.jar /path/to/dblogin.txt

### To run clients, after dist:

#### To run the Restocker client
From the command line, run:

	java -jar dist/RestockerUI.jar

#### To run the Manager client
From the command line, run:

	java -jar dist/ManagerUI.jar

#### To run the Customer client
From the command line, run:

	java -jar dist/CustomerUI.jar

The clients will attempt to connect to localhost on port 41114 by default, but can take an alternative address and port number as arguments.

Example: 

	java -jar dist/CustomerUI.jar myserver.example.net 45678

### To clean up for proper push to GitHub (developers only):
From the command line, run:
	
	ant clean
