-- phpMyAdmin SQL Dump
-- version 3.3.7deb6
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Erstellungszeit: 31. Mai 2012 um 11:21
-- Server Version: 5.1.49
-- PHP-Version: 5.3.3-7+squeeze8

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Datenbank: `evaluation`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `randomTermGroups`
--

CREATE TABLE IF NOT EXISTS `randomTermGroups` (
  `rtgID` int(11) NOT NULL,
  `groupLabel` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '',
  `minSyntag` float NOT NULL,
  PRIMARY KEY (`rtgID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `randomTerms`
--

CREATE TABLE IF NOT EXISTS `randomTerms` (
  `rtgID` int(11) NOT NULL,
  `randomTermID` int(11) NOT NULL,
  PRIMARY KEY (`rtgID`,`randomTermID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `sample`
--

CREATE TABLE IF NOT EXISTS `sample` (
  `rtgID` int(11) NOT NULL,
  `userID` int(11) NOT NULL,
  `randomTermID` int(11) NOT NULL,
  `ratedTermID` int(11) NOT NULL,
  `rating` int(11) NOT NULL,
  `added` int(11) NOT NULL,
  PRIMARY KEY (`rtgID`,`userID`,`randomTermID`,`ratedTermID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
