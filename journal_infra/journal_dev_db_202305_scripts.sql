
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';


CREATE SCHEMA IF NOT EXISTS `journal_dev_db_202305` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `journal_dev_db_202305` ;

-- -----------------------------------------------------
-- Table `journal_dev_db_202305`.`tb_label`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `journal_dev_db_202305`.`tb_label` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL DEFAULT NULL,
  `color` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `journal_dev_db_202305`.`tb_user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `journal_dev_db_202305`.`tb_user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `email` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
  UNIQUE INDEX `UNI` (`username` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 106
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `journal_dev_db_202305`.`tb_post`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `journal_dev_db_202305`.`tb_post` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `content` VARCHAR(500) NULL DEFAULT NULL,
  `latest_date` DATETIME NOT NULL,
  `fk_user` INT NOT NULL,
  `fk_label` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `id_idx` (`fk_user` ASC) VISIBLE,
  INDEX `fk_label_idx` (`fk_label` ASC) VISIBLE,
  CONSTRAINT `fk_label`
    FOREIGN KEY (`fk_label`)
    REFERENCES `journal_dev_db_202305`.`tb_label` (`id`),
  CONSTRAINT `fk_user`
    FOREIGN KEY (`fk_user`)
    REFERENCES `journal_dev_db_202305`.`tb_user` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

INSERT INTO `journal_dev_db_202305`.`tb_user` (`username`, `password`, `email`) VALUES ('test01', '03AC674216F3E15C761EE1A5E255F067953623C8B388B4459E13F978D7C846F4', 'test@test.com')
