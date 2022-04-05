CREATE DATABASE IF NOT EXISTS `boxgym`;

USE `boxgym`;

CREATE TABLE `supplier` (
  `supplierId` INT(11) NOT NULL AUTO_INCREMENT,
  `companyRegistry` CHAR(14) NOT NULL,
  `corporateName` VARCHAR(255) NOT NULL,
  `tradeName` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NULL, 
  `phone` VARCHAR(11) NULL, 
  `zipCode` CHAR(8) NULL,
  `address` VARCHAR(255) NULL, 
  `addressComplement` VARCHAR(255) NULL, 
  `district` VARCHAR(255) NULL, 
  `city` VARCHAR(255) NULL, 
  `federativeUnit` CHAR(2) NULL,
  `createdAt` timestamp NOT NULL DEFAULT current_timestamp(),
  `updatedAt` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`supplierId`),
  UNIQUE KEY `supplierUnique` (`companyRegistry`) USING BTREE
);

CREATE TABLE `product` (
  `productId` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `category` VARCHAR(255),
  `description` TEXT,
  `amount` INT(10) NOT NULL,
  `minimumStock` INT(10) NOT NULL,
  `costPrice` DECIMAL(10, 2) NOT NULL,
  `sellingPrice` DECIMAL(10, 2) NOT NULL,
  `image` MEDIUMBLOB NULL,
  `fkSupplier` INT(11) NOT NULL,
  `createdAt` timestamp NOT NULL DEFAULT current_timestamp(),
  `updatedAt` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`productId`),
  FOREIGN KEY (`fkSupplier`) REFERENCES `supplier`(`supplierId`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `user` (
  `userId` INT(11) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(32) NOT NULL,
  `password` VARCHAR(64) NOT NULL,
  `confirmPassword` VARCHAR(64) NOT NULL,
  `createdAt` timestamp NOT NULL DEFAULT current_timestamp(),
  `updatedAt` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`userId`)
);

CREATE TABLE `stockentry` (
  `stockEntryId` INT(11) NOT NULL AUTO_INCREMENT,
  `fkSupplier` INT(11) NOT NULL,
  `invoiceIssueDate` DATE,
  `invoiceNumber` VARCHAR(255),
  `createdAt` timestamp NOT NULL DEFAULT current_timestamp(),
  `updatedAt` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`stockEntryId`),
  FOREIGN KEY (`fkSupplier`) REFERENCES `supplier`(`supplierId`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `stockentry_product` (
  `stockEntryProductId` INT(11) NOT NULL AUTO_INCREMENT,
  `fkStockEntry` INT(11) NOT NULL,
  `fkProduct` INT(11) NOT NULL,
  `amount` INT(10) NOT NULL,
  `costPrice` DECIMAL(10, 2) NOT NULL,
  `createdAt` timestamp NOT NULL DEFAULT current_timestamp(),
  `updatedAt` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`stockEntryProductId`),
  FOREIGN KEY (`fkStockEntry`) REFERENCES `stockentry`(`stockEntryId`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`fkProduct`) REFERENCES `product`(`productId`) ON DELETE CASCADE ON UPDATE CASCADE
);