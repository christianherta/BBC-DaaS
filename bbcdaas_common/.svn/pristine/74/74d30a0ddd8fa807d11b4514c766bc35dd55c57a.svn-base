-- phpMyAdmin SQL Dump
-- version 3.4.5
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Erstellungszeit: 30. Jul 2012 um 14:00
-- Server Version: 5.5.16
-- PHP-Version: 5.3.8

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Datenbank: `taghandler_xyz`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `entity`
--

CREATE TABLE IF NOT EXISTS `entity` (
  `entityId` int(11) NOT NULL,
  `entityName` varchar(255) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`entityId`),
  UNIQUE KEY `name` (`entityName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `entity_field`
--

CREATE TABLE IF NOT EXISTS `entity_field` (
  `fieldId` int(11) NOT NULL,
  `fieldType` int(11) NOT NULL,
  `entityId` int(11) NOT NULL,
  PRIMARY KEY (`fieldId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `field_term`
--

CREATE TABLE IF NOT EXISTS `field_term` (
  `fieldId` int(11) NOT NULL,
  `termId` int(11) NOT NULL,
  PRIMARY KEY (`fieldId`,`termId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `field_top_syntag_term`
--

CREATE TABLE IF NOT EXISTS `field_top_syntag_term` (
  `fieldId` int(11) NOT NULL,
  `termId` int(11) NOT NULL,
  `score` float NOT NULL,
  PRIMARY KEY (`fieldId`,`termId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `process`
--

CREATE TABLE IF NOT EXISTS `process` (
  `processId` int(11) NOT NULL AUTO_INCREMENT,
  `startDate` varchar(255) COLLATE utf8_bin NOT NULL,
  `endDate` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `errorCode` int(11) NOT NULL DEFAULT '0',
  `readEntities` int(11) NOT NULL DEFAULT '0',
  `computeSyntagmaticRelations` int(11) NOT NULL DEFAULT '0',
  `computeTopRelatedTerms` int(11) NOT NULL DEFAULT '0',
  `computeTopSyntagmaticTerms` int(11) NOT NULL DEFAULT '0',
  `maxTopRelatedTerms` int(11) NOT NULL,
  `maxPercentageTopTerms` float NOT NULL,
  `minNbCorrelatedTerms` int(11) NOT NULL,
  `minTermFrequency` int(11) NOT NULL,
  `minSyntagmaticValue` float NOT NULL,
  `syntagmaticEntityTermFactor` float NOT NULL,
  `a` float NOT NULL,
  `b` float NOT NULL,
  PRIMARY KEY (`processId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=52 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `term`
--

CREATE TABLE IF NOT EXISTS `term` (
  `termId` int(11) NOT NULL,
  `termValue` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `termFrequency` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`termId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `term_matrix`
--

CREATE TABLE IF NOT EXISTS `term_matrix` (
  `termId1` int(11) NOT NULL,
  `termId2` int(11) NOT NULL,
  `cooc` int(11) NOT NULL DEFAULT '1',
  `syntag` float DEFAULT NULL,
  PRIMARY KEY (`termId1`,`termId2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `top_related_term`
--

CREATE TABLE IF NOT EXISTS `top_related_term` (
  `termId` int(11) NOT NULL,
  `topTermId` int(11) NOT NULL,
  `syntag` float DEFAULT NULL,
  PRIMARY KEY (`termId`,`topTermId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
