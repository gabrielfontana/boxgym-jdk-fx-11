CREATE DATABASE IF NOT EXISTS `boxgym`;

USE `boxgym`;

CREATE TABLE `supplier` (
  `supplierId` INT(11) NOT NULL AUTO_INCREMENT,
  `companyRegistry` CHAR(14) NOT NULL,
  `corporateName` VARCHAR(255) NOT NULL,
  `tradeName` VARCHAR(255) NULL,
  `email` VARCHAR(255) NULL, 
  `phone` VARCHAR(11) NULL, 
  `zipCode` CHAR(8) NULL,
  `address` VARCHAR(255) NULL, 
  `addressComplement` VARCHAR(255) NULL, 
  `district` VARCHAR(255) NULL, 
  `city` VARCHAR(255) NULL, 
  `federativeUnit` CHAR(2) NULL,
  `createdAt` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`supplierId`),
  UNIQUE KEY `supplierUnique` (`companyRegistry`) USING BTREE
);

CREATE TABLE `product` (
  `productId` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `category` VARCHAR(255) NULL,
  `description` TEXT NULL,
  `amount` INT(10) NOT NULL,
  `minimumStock` INT(10) NOT NULL,
  `costPrice` DECIMAL(10, 2) NOT NULL,
  `sellingPrice` DECIMAL(10, 2) NOT NULL,
  `image` MEDIUMBLOB NULL,
  `createdAt` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`productId`)
);

CREATE TABLE `userRegistration` (
  `userId` INT(11) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(32) NOT NULL,
  `password` VARCHAR(64) NOT NULL,
  `confirmPassword` VARCHAR(64) NOT NULL,
  `createdAt` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`userId`)
);

CREATE TABLE `stockentry` (
  `stockEntryId` INT(11) NOT NULL AUTO_INCREMENT,
  `fkSupplier` INT(11) NOT NULL,
  `invoiceIssueDate` DATE NOT NULL,
  `invoiceNumber` VARCHAR(10) NULL,
  `createdAt` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`stockEntryId`),
  FOREIGN KEY (`fkSupplier`) REFERENCES `supplier`(`supplierId`) ON UPDATE CASCADE
);

CREATE TABLE `stockentry_product` (
  `stockEntryProductId` INT(11) NOT NULL AUTO_INCREMENT,
  `fkStockEntry` INT(11) NOT NULL,
  `fkProduct` INT(11) NOT NULL,
  `amount` INT(10) NOT NULL,
  `costPrice` DECIMAL(10, 2) NOT NULL,
  `createdAt` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`stockEntryProductId`),
  FOREIGN KEY (`fkStockEntry`) REFERENCES `stockentry`(`stockEntryId`) ON UPDATE CASCADE,
  FOREIGN KEY (`fkProduct`) REFERENCES `product`(`productId`) ON UPDATE CASCADE
);

CREATE TABLE `customer` (
	`customerId` INT(11) NOT NULL AUTO_INCREMENT,
	`personRegistry` CHAR(11) NOT NULL,
	`name` VARCHAR(255) NOT NULL,
	`sex` VARCHAR(9) NOT NULL,
	`email` VARCHAR(255) NULL, 
	`phone` VARCHAR(11) NULL, 
	`zipCode` CHAR(8) NULL,
	`address` VARCHAR(255) NULL, 
	`addressComplement` VARCHAR(255) NULL, 
	`district` VARCHAR(255) NULL, 
	`city` VARCHAR(255) NULL, 
	`federativeUnit` CHAR(2) NULL,
	`createdAt` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`updatedAt` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`customerId`),
	UNIQUE KEY `customerUnique` (`personRegistry`) USING BTREE
);

DELIMITER $$
CREATE TRIGGER `triggerAddProductAmountAfterStockEntry` AFTER INSERT ON `stockentry_product`
FOR EACH ROW BEGIN
	UPDATE product SET amount = amount + NEW.amount WHERE productId = NEW.fkProduct;
END
$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER `triggerRemoveProductAmountAfterStockEntryExclusion` AFTER DELETE ON `stockentry_product`
FOR EACH ROW BEGIN
	UPDATE product SET amount = amount - OLD.amount WHERE productId = OLD.fkProduct;
END
$$
DELIMITER ;

CREATE TABLE `sale` (
  `saleId` INT(11) NOT NULL AUTO_INCREMENT,
  `fkCustomer` INT(11) NOT NULL,
  `saleDate` DATE NOT NULL,
  `createdAt` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`saleId`),
  FOREIGN KEY (`fkCustomer`) REFERENCES `customer`(`customerId`) ON UPDATE CASCADE
);

CREATE TABLE `sale_product` (
  `saleProductId` INT(11) NOT NULL AUTO_INCREMENT,
  `fkSale` INT(11) NOT NULL,
  `fkProduct` INT(11) NOT NULL,
  `amount` INT(10) NOT NULL,
  `unitPrice` DECIMAL(10, 2) NOT NULL,
  `createdAt` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`saleProductId`),
  FOREIGN KEY (`fkSale`) REFERENCES `sale`(`saleId`) ON UPDATE CASCADE,
  FOREIGN KEY (`fkProduct`) REFERENCES `product`(`productId`) ON UPDATE CASCADE
);

DELIMITER $$
CREATE TRIGGER `triggerRemoveProductAmountAfterSale` AFTER INSERT ON `sale_product`
FOR EACH ROW BEGIN
	UPDATE product SET amount = amount - NEW.amount WHERE productId = NEW.fkProduct;
END
$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER `triggerAddProductAmountAfterSaleExclusion` AFTER DELETE ON `sale_product`
FOR EACH ROW BEGIN
	UPDATE product SET amount = amount + OLD.amount WHERE productId = OLD.fkProduct;
END
$$
DELIMITER ;

CREATE TABLE `exercise` (
  `exerciseId` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `abbreviation` VARCHAR(255) NOT NULL,
  `exerciseType` VARCHAR(255) NOT NULL,
  `exerciseGroup` VARCHAR(255) NOT NULL,
  `description` TEXT NULL,
  `instruction` TEXT NULL,
  `createdAt` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`exerciseId`)
);

CREATE TABLE `workout` (
  `workoutId` INT(11) NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(255) NOT NULL,
  `goal` VARCHAR(255) NOT NULL,
  `sessions` INT(11) NOT NULL,
  `createdAt` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`workoutId`)
);

CREATE TABLE `workout_exercise` (
  `workoutExerciseId` INT(11) NOT NULL AUTO_INCREMENT,
  `fkWorkout` INT(11) NOT NULL,
  `fkExercise` INT(11) NOT NULL,
  `sets` INT(10) NOT NULL,
  `reps` INT(10) NOT NULL,
  `rest` INT(10) NOT NULL,
  `createdAt` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`workoutExerciseId`),
  FOREIGN KEY (`fkWorkout`) REFERENCES `workout`(`workoutId`) ON UPDATE CASCADE,
  FOREIGN KEY (`fkExercise`) REFERENCES `exercise`(`exerciseId`) ON UPDATE CASCADE
);
