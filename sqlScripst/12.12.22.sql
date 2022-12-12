-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema MC_products
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema MC_products
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `MC_products` DEFAULT CHARACTER SET utf8 ;
USE `MC_products` ;

-- -----------------------------------------------------
-- Table `MC_products`.`Vylepseni`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `MC_products`.`Vylepseni` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nazev` VARCHAR(45) NOT NULL,
  `cena` DOUBLE NOT NULL,
  `dostupne` INT NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `MC_products`.`Kategorie`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `MC_products`.`Kategorie` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nazev` VARCHAR(45) NOT NULL,
  `dostupne` INT NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `MC_products`.`Produkty`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `MC_products`.`Produkty` (
  `íd` INT NOT NULL AUTO_INCREMENT,
  `nazev` VARCHAR(45) NOT NULL,
  `cena` DOUBLE NOT NULL,
  `dostupne` INT NOT NULL,
  `kategorie` INT NOT NULL,
  PRIMARY KEY (`íd`),
  INDEX `fk_Produkty_Ketegorie_idx` (`kategorie` ASC) VISIBLE,
  CONSTRAINT `fk_Produkty_Ketegorie`
    FOREIGN KEY (`kategorie`)
    REFERENCES `MC_products`.`Kategorie` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `MC_products`.`VylepseniProduktu`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `MC_products`.`VylepseniProduktu` (
  `vylepseni` INT NOT NULL,
  `produkt` INT NOT NULL,
  PRIMARY KEY (`vylepseni`, `produkt`),
  INDEX `fk_Vylepseni_has_Produkty_Produkty1_idx` (`produkt` ASC) VISIBLE,
  INDEX `fk_Vylepseni_has_Produkty_Vylepseni1_idx` (`vylepseni` ASC) VISIBLE,
  CONSTRAINT `fk_Vylepseni_has_Produkty_Vylepseni1`
    FOREIGN KEY (`vylepseni`)
    REFERENCES `MC_products`.`Vylepseni` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Vylepseni_has_Produkty_Produkty1`
    FOREIGN KEY (`produkt`)
    REFERENCES `MC_products`.`Produkty` (`íd`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `MC_products`.`VylepseniProKetegorie`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `MC_products`.`VylepseniProKetegorie` (
  `vylepseni` INT NOT NULL,
  `kategorie` INT NOT NULL,
  PRIMARY KEY (`vylepseni`, `kategorie`),
  INDEX `fk_Vylepseni_has_Ketegorie_Ketegorie1_idx` (`kategorie` ASC) VISIBLE,
  INDEX `fk_Vylepseni_has_Ketegorie_Vylepseni1_idx` (`vylepseni` ASC) VISIBLE,
  CONSTRAINT `fk_Vylepseni_has_Ketegorie_Vylepseni1`
    FOREIGN KEY (`vylepseni`)
    REFERENCES `MC_products`.`Vylepseni` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Vylepseni_has_Ketegorie_Ketegorie1`
    FOREIGN KEY (`kategorie`)
    REFERENCES `MC_products`.`Kategorie` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '		';


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
