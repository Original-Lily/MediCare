-- MySQL dump 10.13  Distrib 8.3.0, for Linux (x86_64)
--
-- Host: localhost    Database: hospital_management
-- ------------------------------------------------------
-- Server version	8.3.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Booking`
--

DROP TABLE IF EXISTS `Booking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Booking` (
  `BookingID` int NOT NULL AUTO_INCREMENT,
  `NHSno` int DEFAULT NULL,
  `DoctorID` int DEFAULT NULL,
  `BookingReason` varchar(255) DEFAULT NULL,
  `BookingTime` datetime DEFAULT NULL,
  PRIMARY KEY (`BookingID`),
  KEY `NHSno` (`NHSno`),
  KEY `DoctorID` (`DoctorID`),
  CONSTRAINT `Booking_ibfk_1` FOREIGN KEY (`NHSno`) REFERENCES `Patient` (`NHSno`),
  CONSTRAINT `Booking_ibfk_2` FOREIGN KEY (`DoctorID`) REFERENCES `Doctor` (`DoctorID`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Booking`
--

LOCK TABLES `Booking` WRITE;
/*!40000 ALTER TABLE `Booking` DISABLE KEYS */;
INSERT INTO `Booking` VALUES (5,123456789,5,'Annual check-up','2024-03-06 09:00:00'),(6,987654321,6,'Skin rash consultation','2024-03-08 11:30:00'),(13,123456789,6,'Cancer screening','2024-03-14 11:00:00'),(14,987654321,9,'Headache consultation','2024-03-16 09:30:00'),(15,111111111,10,'Knee pain evaluation','2024-03-18 10:30:00'),(16,222222222,11,'Annual check-up','2024-03-20 09:00:00'),(17,333333333,12,'Cardiac check-up','2024-03-22 14:00:00'),(18,444444444,5,'Skin rash consultation','2024-03-24 11:00:00'),(19,555555555,6,'Cancer screening','2024-03-26 15:30:00'),(20,666666666,9,'Headache consultation','2024-03-28 12:30:00');
/*!40000 ALTER TABLE `Booking` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Doctor`
--

DROP TABLE IF EXISTS `Doctor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Doctor` (
  `DoctorID` int NOT NULL AUTO_INCREMENT,
  `DoctorFirstName` varchar(50) DEFAULT NULL,
  `DoctorSurname` varchar(50) DEFAULT NULL,
  `DoctorEmail` varchar(100) DEFAULT NULL,
  `SurgeryID` int DEFAULT NULL,
  `DoctorSpecialties` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`DoctorID`),
  KEY `SurgeryID` (`SurgeryID`),
  CONSTRAINT `Doctor_ibfk_1` FOREIGN KEY (`SurgeryID`) REFERENCES `Surgery` (`SurgeryID`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Doctor`
--

LOCK TABLES `Doctor` WRITE;
/*!40000 ALTER TABLE `Doctor` DISABLE KEYS */;
INSERT INTO `Doctor` VALUES (5,'Dr. Sarah','Johnson','sarah.johnson@example.com',1,'General Medicine'),(6,'Dr. Michael','Williams','michael.williams@example.com',2,'Pediatrics, Dermatology'),(9,'Dr. Emily','Anderson','emily.anderson@example.com',1,'Cardiology'),(10,'Dr. David','Miller','david.miller@example.com',2,'Orthopedics'),(11,'Dr. Jessica','Wilson','jessica.wilson@example.com',1,'Oncology'),(12,'Dr. Mark','Taylor','mark.taylor@example.com',2,'Neurology');
/*!40000 ALTER TABLE `Doctor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Logs`
--

DROP TABLE IF EXISTS `Logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Logs` (
  `NHSno` int NOT NULL,
  `SignInTime` datetime DEFAULT NULL,
  `SignOutTime` datetime DEFAULT NULL,
  `Edits` text,
  PRIMARY KEY (`NHSno`),
  CONSTRAINT `Logs_ibfk_1` FOREIGN KEY (`NHSno`) REFERENCES `Patient` (`NHSno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Logs`
--

LOCK TABLES `Logs` WRITE;
/*!40000 ALTER TABLE `Logs` DISABLE KEYS */;
INSERT INTO `Logs` VALUES (123456789,'2024-03-06 08:00:00','2024-03-06 17:00:00','Updated patient information'),(987654321,'2024-03-08 10:00:00','2024-03-08 13:00:00','Performed consultation');
/*!40000 ALTER TABLE `Logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Patient`
--

DROP TABLE IF EXISTS `Patient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Patient` (
  `NHSno` int NOT NULL,
  `PatientFirstName` varchar(50) DEFAULT NULL,
  `PatientSurname` varchar(50) DEFAULT NULL,
  `PatientEmail` varchar(100) DEFAULT NULL,
  `PatientPhoneNo` varchar(15) DEFAULT NULL,
  `Address` varchar(255) DEFAULT NULL,
  `Password` varchar(255) DEFAULT NULL,
  `History` text,
  PRIMARY KEY (`NHSno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Patient`
--

LOCK TABLES `Patient` WRITE;
/*!40000 ALTER TABLE `Patient` DISABLE KEYS */;
INSERT INTO `Patient` VALUES (111111111,'Alice','Johnson','alice.johnson@example.com','111-111-1111','789 Elm St, City, Country','password123','No significant medical history'),(123456789,'John','Doe','john.doe@example.com','123-456-7890','123 Main St, City, Country','password123','No significant medical history'),(222222222,'Bob','Smith','bob.smith@example.com','222-222-2222','456 Maple Ave, Town, Country','pass123','Allergic to peanuts'),(333333333,'Charlie','Williams','charlie.williams@example.com','333-333-3333','123 Oak St, Village, Country','password123','Asthma'),(444444444,'David','Brown','david.brown@example.com','444-444-4444','789 Elm St, City, Country','pass123','Diabetes'),(555555555,'Ella','Miller','ella.miller@example.com','555-555-5555','456 Maple Ave, Town, Country','password123','High blood pressure'),(666666666,'Fiona','Wilson','fiona.wilson@example.com','666-666-6666','123 Oak St, Village, Country','pass123','Migraines'),(777777777,'George','Taylor','george.taylor@example.com','777-777-7777','789 Elm St, City, Country','password123','Allergic to shellfish'),(888888888,'Hannah','Clark','hannah.clark@example.com','888-888-8888','456 Maple Ave, Town, Country','pass123','None'),(987654321,'Jane','Smith','jane.smith@example.com','987-654-3210','456 Oak St, Town, Country','pass123','Allergic to penicillin'),(999999999,'Ian','White','ian.white@example.com','999-999-9999','123 Oak St, Village, Country','password123','No significant medical history'),(1010101010,'Jessica','Davis','jessica.davis@example.com','101-010-1010','789 Elm St, City, Country','pass123','Allergic to pollen');
/*!40000 ALTER TABLE `Patient` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Surgery`
--

DROP TABLE IF EXISTS `Surgery`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Surgery` (
  `SurgeryID` int NOT NULL AUTO_INCREMENT,
  `SurgeryName` varchar(100) DEFAULT NULL,
  `SurgeryAddress` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`SurgeryID`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Surgery`
--

LOCK TABLES `Surgery` WRITE;
/*!40000 ALTER TABLE `Surgery` DISABLE KEYS */;
INSERT INTO `Surgery` VALUES (1,'City Medical Center','789 Elm St, City, Country'),(2,'Town Health Clinic','456 Maple Ave, Town, Country'),(11,'Central Hospital','123 Oak St, Village, Country');
/*!40000 ALTER TABLE `Surgery` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-03-10 10:10:03
