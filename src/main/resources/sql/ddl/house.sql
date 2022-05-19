use happyhouse;

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

-- -----------------------------------------------------
-- Table `happyhouse`.`Sido`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `happyhouse`.`Sido` (
  `id` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `happyhouse`.`Sigugun`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `happyhouse`.`Sigugun` (
  `id` VARCHAR(45) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `sido_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_sigugun_sido1_idx` (`sido_id` ASC) VISIBLE,
  CONSTRAINT `fk_sigugun_sido1`
    FOREIGN KEY (`sido_id`)
    REFERENCES `happyhouse`.`Sido` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `happyhouse`.`Upmyundong`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `happyhouse`.`Upmyundong` (
  `name` VARCHAR(45) NOT NULL,
  `code` VARCHAR(45) NOT NULL,
  `lat` VARCHAR(45) NULL DEFAULT NULL,
  `lng` VARCHAR(45) NULL DEFAULT NULL,
  `sigugun_id` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`name`, `code`),
  INDEX `fk_upmyundong_sigugun1_idx` (`sigugun_id` ASC) VISIBLE,
  CONSTRAINT `fk_upmyundong_sigugun1`
    FOREIGN KEY (`sigugun_id`)
    REFERENCES `happyhouse`.`Sigugun` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
