CREATE DATABASE autoscouts;

USE autoscouts;

CREATE TABLE Inventory
(
   id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
   ItemName varchar(60) NOT NULL,
   Price float NOT NULL,
   Discount float,
   Qty int NOT NULL,
   Threshold int NOT NULL
);

INSERT INTO Inventory (ItemName,Price,Discount,Qty,Threshold) 
       VALUES ('tomato juice', 3.25, .10, 4, 2);
INSERT INTO Inventory (ItemName,Price,Discount,Qty,Threshold) 
       VALUES ('vodka', 24.75, 0, 5, 3);



