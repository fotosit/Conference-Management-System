-- MySQL dump 10.13  Distrib 8.0.20, for macos10.15 (x86_64)
--
-- Host: 127.0.0.1    Database: ConferenceManagementSystemDB
-- ------------------------------------------------------
-- Server version	8.0.20

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Papers`
--

DROP TABLE IF EXISTS `Papers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Papers` (
  `paperId` int NOT NULL AUTO_INCREMENT,
  `authorName` varchar(60) COLLATE utf8_turkish_ci NOT NULL,
  `authorMail` varchar(60) COLLATE utf8_turkish_ci NOT NULL,
  `authorUniversityName` varchar(128) COLLATE utf8_turkish_ci NOT NULL,
  `paperName` varchar(128) COLLATE utf8_turkish_ci NOT NULL,
  `paperAbstract` varchar(600) COLLATE utf8_turkish_ci NOT NULL,
  `keywords` varchar(100) COLLATE utf8_turkish_ci NOT NULL,
  `paperTopics` varchar(450) COLLATE utf8_turkish_ci NOT NULL,
  `paper` longtext COLLATE utf8_turkish_ci NOT NULL,
  `submitPerson` int NOT NULL,
  `submitDate` varchar(45) COLLATE utf8_turkish_ci NOT NULL,
  `referees` varchar(45) COLLATE utf8_turkish_ci DEFAULT NULL,
  `meanScore` double DEFAULT NULL,
  `isEvaluationComplete` tinyint NOT NULL,
  `deadline` varchar(45) COLLATE utf8_turkish_ci DEFAULT NULL,
  `publishDate` varchar(45) COLLATE utf8_turkish_ci DEFAULT NULL,
  `paperStatus` tinyint DEFAULT NULL,
  PRIMARY KEY (`paperId`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Papers`
--

LOCK TABLES `Papers` WRITE;
/*!40000 ALTER TABLE `Papers` DISABLE KEYS */;
/*!40000 ALTER TABLE `Papers` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-08-10 22:45:11
