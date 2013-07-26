-- phpMyAdmin SQL Dump
-- version 3.4.5
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Erstellungszeit: 25. Jun 2012 um 11:10
-- Server Version: 5.5.16
-- PHP-Version: 5.3.8

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Datenbank: `themehandler`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `term`
--

CREATE TABLE IF NOT EXISTS `term` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TERMVALUE` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=4 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `themecloud`
--

CREATE TABLE IF NOT EXISTS `themecloud` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `USERID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `themecloudterm`
--

CREATE TABLE IF NOT EXISTS `themecloudterm` (
  `ID` int(11) NOT NULL,
  `TYPE` int(11) DEFAULT NULL,
  `WEIGHTING` float DEFAULT NULL,
  `THEMECLOUD_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_THEMECLOUDTERM_THEMECLOUD_ID` (`THEMECLOUD_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `userdata`
--

CREATE TABLE IF NOT EXISTS `userdata` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USERNAME` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=2 ;

--
-- Constraints der exportierten Tabellen
--

--
-- Constraints der Tabelle `themecloudterm`
--
ALTER TABLE `themecloudterm`
  ADD CONSTRAINT `FK_THEMECLOUDTERM_THEMECLOUD_ID` FOREIGN KEY (`THEMECLOUD_ID`) REFERENCES `themecloud` (`ID`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
