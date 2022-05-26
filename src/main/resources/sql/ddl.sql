 -- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema happyhouse
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema happyhouse
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `happyhouse` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci ;
USE `happyhouse` ;

DROP TABLE IF EXISTS `sido`;
DROP TABLE IF EXISTS `sigugun`;
DROP TABLE IF EXISTS `upmyundong`;
DROP TABLE IF EXISTS `house`;
DROP TABLE IF EXISTS `house_deal`;


-- -----------------------------------------------------
-- Table `happyhouse`.`sido`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `happyhouse`.`sido` (
  `sido_id` BIGINT NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(45) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`sido_id`),
  UNIQUE INDEX `code_UNIQUE` (`code` ASC) VISIBLE,
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 64
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `happyhouse`.`sigugun`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `happyhouse`.`sigugun` (
  `sigugun_id` BIGINT NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(45) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `sido_id` BIGINT NOT NULL,
  PRIMARY KEY (`sigugun_id`),
  UNIQUE INDEX `code_UNIQUE` (`code` ASC) VISIBLE,
  INDEX `fk_sigugun_sido1_idx` (`sido_id` ASC) VISIBLE,
  CONSTRAINT `fk_sigugun_sido1`
    FOREIGN KEY (`sido_id`)
    REFERENCES `happyhouse`.`sido` (`sido_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 331
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `happyhouse`.`upmyundong`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `happyhouse`.`upmyundong` (
  `upmyundong_id` BIGINT NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(45) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `lat` FLOAT NOT NULL,
  `lng` FLOAT NOT NULL,
  `sigugun_id` BIGINT NOT NULL,
  PRIMARY KEY (`upmyundong_id`),
  UNIQUE INDEX `code_UNIQUE` (`code` ASC) VISIBLE,
  INDEX `fk_upmyundong_sigugun1_idx` (`sigugun_id` ASC) VISIBLE,
  CONSTRAINT `fk_upmyundong_sigugun1`
    FOREIGN KEY (`sigugun_id`)
    REFERENCES `happyhouse`.`sigugun` (`sigugun_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 18738
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `happyhouse`.`house`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `happyhouse`.`house` (
  `house_id` BIGINT NOT NULL AUTO_INCREMENT,
  `apt_name` VARCHAR(45) NOT NULL,
  `build_year` INT NOT NULL,
  `jibun` VARCHAR(45) NOT NULL,
  `upmyundong_id` BIGINT NOT NULL,
  PRIMARY KEY (`house_id`),
  INDEX `fk_house_info_upmyundong1_idx` (`upmyundong_id` ASC) VISIBLE,
  CONSTRAINT `fk_house_info_upmyundong1`
    FOREIGN KEY (`upmyundong_id`)
    REFERENCES `happyhouse`.`upmyundong` (`upmyundong_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 733
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `happyhouse`.`house_deal`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `happyhouse`.`house_deal` (
  `house_deal_id` BIGINT NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(45) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_bin' NOT NULL,
  `floor` INT NOT NULL,
  `price` INT NOT NULL,
  `exclusive_private_area` FLOAT NOT NULL,
  `deal_date` DATE NOT NULL,
  `house_id` BIGINT NOT NULL,
  PRIMARY KEY (`house_deal_id`),
  INDEX `fk_DealInfo_HouseInfo1_idx` (`house_id` ASC) VISIBLE,
  CONSTRAINT `fk_DealInfo_HouseInfo1`
    FOREIGN KEY (`house_id`)
    REFERENCES `happyhouse`.`house` (`house_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 8587
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

----------------------------------------

select * from INFORMATION_SCHEMA.TABLE_CONSTRAINTS where CONSTRAINT_TYPE = 'FOREIGN KEY';
LOAD DATA INFILE "C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/sido.txt"
INTO TABLE sido CHARACTER SET utf8
FIELDS TERMINATED BY '\t'
LINES TERMINATED BY '\n';


----------------------------------------

LOAD DATA INFILE "C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/sigugun.txt"
INTO TABLE sigugun CHARACTER SET utf8
FIELDS TERMINATED BY '\t'
LINES TERMINATED BY '\n';


-----------------------------------

LOAD DATA INFILE "C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/upmyundong.txt"
INTO TABLE upmyundong CHARACTER SET utf8
FIELDS TERMINATED BY '\t'
LINES TERMINATED BY '\n';

LOAD DATA INFILE "C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/House_11110_200001_202204.csv"
INTO TABLE upmyundong CHARACTER SET utf8
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\n';

-- 외래키 삭제
alter table house_info drop foreign key fk_house_info_upmyundong1;