-- phpMyAdmin SQL Dump
-- version 3.4.5
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Erstellungszeit: 18. Jun 2012 um 09:32
-- Server Version: 5.5.16
-- PHP-Version: 5.3.8

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Datenbank: `visualizer_themehandler`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `themecloud`
--

CREATE TABLE IF NOT EXISTS `themecloud` (
  `themeCloudId` int(11) NOT NULL AUTO_INCREMENT,
  `themeCloudName` varchar(255) COLLATE utf8_bin NOT NULL,
  `userId` int(11) NOT NULL,
  PRIMARY KEY (`themeCloudId`),
  UNIQUE KEY `themeCloudName` (`themeCloudName`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=5 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `themecloud_term`
--

CREATE TABLE IF NOT EXISTS `themecloud_term` (
  `themeCloudId` int(11) NOT NULL,
  `termId` int(11) NOT NULL,
  `rating` int(11) NOT NULL,
  `weighting` float NOT NULL,
  PRIMARY KEY (`themeCloudId`,`termId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
