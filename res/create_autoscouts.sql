CREATE DATABASE autoscouts;

USE autoscouts;

--CREATE TABLE Accounts
--(
--   AccountID varchar(30) NOT NULL PRIMARY KEY,
--   Password varchar(60) NOT NULL,
--   CardNo int NOT NULL,
--   CustomerEmail varchar(30) NOT NULL
--);

CREATE TABLE Inventory
(
   id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
   ItemName varchar(60) NOT NULL,
   Price float NOT NULL,
   Discount float,
   Qty int NOT NULL,
   Threshold int NOT NULL
);

--CREATE TABLE Orders
--(
--   id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
--   AccountID varchar(30),
--   FOREIGN KEY (AccountID) REFERENCES Accounts(AccountID),
--   Item varchar(60),
--   FOREIGN KEY (Item) REFERENCES Items(ItemName),
--   Quantity int NOT NULL,
--   TotalPrice float NOT NULL,
--   AuthorizationNo int NOT NULL
--);


--INSERT INTO Accounts (AccountID,Password,CardNo,CustomerEmail) VALUES ('steve',MD5(CONCAT('secretsalt','2345')),12345678,'steve@ttu.edu');
--INSERT INTO Accounts (AccountID,Password,CardNo,CustomerEmail) VALUES ('alex',MD5(CONCAT('secretsalt','4567')),23456789,'alex@ttu.edu');
--INSERT INTO Accounts (AccountID,Password,CardNo,CustomerEmail) VALUES ('Jane',MD5(CONCAT('secretsalt','6789')),45678901,'jane@ttu.edu');
--INSERT INTO Accounts (AccountID,Password,CardNo,CustomerEmail) VALUES ('john',MD5(CONCAT('secretsalt','5678')),56789012,'john@ttu.edu');
--INSERT INTO Accounts (AccountID,Password,CardNo,CustomerEmail) VALUES ('sam',MD5(CONCAT('secretsalt','7890')),89012345,'sam@ttu.edu');

INSERT INTO Inventory (ItemName,Price,Discount,Qty,Threshold) 
       VALUES ('tomato juice', 3.25, .10, 4, 2);
INSERT INTO Inventory (ItemName,Price,Discount,Qty,Threshold) 
       VALUES ('vodka', 24.75, 0, 5, 3);

--INSERT INTO Orders (AccountID,Item,Quantity,TotalPrice,AuthorizationNo) VALUES ('alex','note',3,20.00,3333);
--INSERT INTO Orders (AccountID,Item,Quantity,TotalPrice,AuthorizationNo) VALUES ('john','book',1,50.00,5555);
--INSERT INTO Orders (AccountID,Item,Quantity,TotalPrice,AuthorizationNo) VALUES ('sam','pencil',20,10.00,7777);


