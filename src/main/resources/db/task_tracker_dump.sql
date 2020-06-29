-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: task_tracker
-- ------------------------------------------------------
-- Server version	5.5.23

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `hibernate_sequence`
--

DROP TABLE IF EXISTS `hibernate_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequence`
--

LOCK TABLES `hibernate_sequence` WRITE;
/*!40000 ALTER TABLE `hibernate_sequence` DISABLE KEYS */;
INSERT INTO `hibernate_sequence` VALUES (67),(67);
/*!40000 ALTER TABLE `hibernate_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tasks`
--

DROP TABLE IF EXISTS `tasks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tasks` (
  `id` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `status` varchar(255) NOT NULL,
  `title` varchar(255) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6s1ob9k4ihi75xbxe2w0ylsdh` (`user_id`),
  CONSTRAINT `FK6s1ob9k4ihi75xbxe2w0ylsdh` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tasks`
--

LOCK TABLES `tasks` WRITE;
/*!40000 ALTER TABLE `tasks` DISABLE KEYS */;
INSERT INTO `tasks` VALUES (32,'Create entity','VIEW','CE',10),(33,'Create repos','IN_PROGRESS','CR',29),(34,'Delete user','DONE','DU',29),(35,'Edit something','VIEW','ES',33),(36,'Do more','IN_PROGRESS','DM',32),(37,'Make create innovate','DONE','MCI',33),(38,'Create pagination','VIEW','CRP',33),(39,'Call product owner','IN_PROGRESS','CPO',34),(40,'Call babulia','DONE','CB',34),(41,'Read docs','VIEW','RD',35),(42,'Localization','VIEW','LN',35),(43,'Update button','IN_PROGRESS','UB',36),(44,'Listen to the music','DONE','LTTM',36),(45,'Write documentation','VIEW','WD',37),(46,'Do nothing','DONE','DN',37),(47,'Make new func','VIEW','MNF',38),(48,'Do all things','IN_PROGRESS','DAT',38),(49,'Change the country','IN_PROGRESS','CTC',39),(50,'-------------','VIEW','T35',39),(51,'Leave this city','DONE','LTC',40),(52,'Help the town','IN_PROGRESS','HTT',41),(53,'Do math','VIEW','DoM',42),(54,'Learn Java','IN_PROGRESS','LJ',43),(55,'Just do it','DONE','JDI',44),(56,'no descr','VIEW','T36',45),(57,'Create new tasks','DONE','CNT',46),(58,'Do job','IN_PROGRESS','DJ',47),(59,NULL,'IN_PROGRESS','Create mapper',48),(60,NULL,'DONE','Run tests',49),(61,NULL,'IN_PROGRESS','Some task2',50);
/*!40000 ALTER TABLE `tasks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `email` varchar(255) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(255) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (10,'art4315@gmail.com','Artem','Polishchuk','$2a$10$7WbVpgq6887JgE7p8YOeA.CUJCVhlVhAHESyMcYWYT0U7PwubbeMy','ROLE_ADMIN'),(29,'ab@gmail.com','Nelson','Mandela','$2a$10$zBfoE19UCpV2CIfcvFWbHuUNdUOVHfoJANXrXqgewHnc5a9MvR/0i','ROLE_USER'),(32,'1@mail.com','Panas','Myrnyi','$2a$10$iHRegytfgDWAJA1ar1SYvuS6OMyFAw2k4Ta9VUN53AQFW5AyC1daK','ROLE_USER'),(33,'taras@mail.com','Taras','Tarasenko','$2a$10$vR6jlvM9NL3lnXOxswJmueG66KhmzbctsWre7y8FlD1WZ9/KkdoSi','ROLE_USER'),(34,'step@mail.com','Bogdan','Stepanov','$2a$10$WAGCZaPpMy90rYAkegYQLezZ/c.GNTNZ9fASdm1mehhukhb1kWyCa','ROLE_USER'),(35,'capital@mail.com','Jack','London','$2a$10$U98hyXe6MHeSVbEGIrkozeUqVyG/77Dqvv7fHoy1fKrSZBG5QRP86','ROLE_USER'),(36,'golden_ball@mail.com','Oleg','Blohin','$2a$10$DIhCKwAlgFuiQH9YOuox9.AHx36c1oM0Lk6GvZQJZAzE2CAxTU1vS','ROLE_USER'),(37,'maryana@mail.com','Maryana','Bondar','$2a$10$9.cmJCCZ2md4PSZbpWAF/O1hi/Oe5l.t8IQfGrG/DFz4B3B5B/sHa','ROLE_USER'),(38,'tark@mail.com','Andrey','Tarkovskiy','$2a$10$eywu2uISzalEkpM8dN0DPOAbKb9bi6ELx42mWG97Fb.cMfoAKi5Q6','ROLE_USER'),(39,'misto@gmail.com','Stepan','Radchenko','$2a$10$3saJh7fQ4sULijXVR11LlO.mgA1JQo6J2Ftzlh7JAOSmGEkUvngvW','ROLE_USER'),(40,'ali@gmail.com','Ali','Bubakr','$2a$10$E590z.pqWLVk2q/chy3V3uq.XnGYZxTZac/c9KcnYh6aJ82bEkwZi','ROLE_USER'),(41,'cossack@gmail.com','Bogdan','Hmelnytskyi','$2a$10$1AzRBGaJo1sNCVq.kRsYieGDNsKUtLJN2mx8fyr7lpQlVz3cFOHwC','ROLE_USER'),(42,'rock@gmail.com','Jhonn','Bon Jovi','$2a$10$my1kQ71FHTjMtKWF86DWz.ntHWH4Hv9R5ZQpsBAJNnqh3PmLP801C','ROLE_USER'),(43,'dire_straits@gmail.com','Mark','Knopfler','$2a$10$B7R4usuZuoHA386cmi9BYO3/5wL6ug8F3VVx0OIWlbfg62zW6DWN2','ROLE_USER'),(44,'el_loko@gmail.com','Marselo','Bielsa','$2a$10$x0Hzz8/e2RdarYpnwQyOqO6xKmA4pdS0qxqVZOgMUYWDTaYZsuLRK','ROLE_USER'),(45,'bratiwka@gmail.com','Artem','Milevskiy','$2a$10$4oIiwl0U2RqashzK8EedWe.OGdq6WAC.yqKQ9GwcjuPtJs2VJT93S','ROLE_USER'),(46,'syr_na@gmail.com','Dario','Srna','$2a$10$U8ek38FpZd8lF7XtKiFGEeXEDZTIbshtybGhdREHFb082XrfifL6K','ROLE_USER'),(47,'marko@gmail.com','Marko','Devic','$2a$10$/uFh16efiyigrIDSdV72septIqz.hRJ4iZeYvc4Pz34PDrdovTEOa','ROLE_USER'),(48,'leo@gmail.com','Lionel','Messi','$2a$10$X3i88UhwjlEx01Jkp2lideCkI9x7MTLjSkbFJctdmF/5k5v4mrcVm','ROLE_USER'),(49,'sii@gmail.com','Kristiano','Ronaldo','$2a$10$1lENK7Gi5aberYjgnyER.O.Z.j0NLdznt7KVUjFq7B2PIypeP96ku','ROLE_USER'),(50,'legend@gmail.com','Valeriy','Lobanovskiy','$2a$10$RZFy505E2wCTjsVjVSw3heTYRuc4PPxupDMoFLc1HwUaoOr/CSnBG','ROLE_USER');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-06-29 13:24:29
