-- phpMyAdmin SQL Dump
-- version 3.3.7deb6
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Erstellungszeit: 31. Mai 2012 um 11:15
-- Server Version: 5.1.49
-- PHP-Version: 5.3.3-7+squeeze8

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Datenbank: `taghandler_termLexicon`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `term`
--

CREATE TABLE IF NOT EXISTS `term` (
  `termId` int(11) NOT NULL AUTO_INCREMENT,
  `termValue` varchar(255) COLLATE utf8_bin NOT NULL,
  `termFrequency` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`termId`),
  UNIQUE KEY `termValue` (`termValue`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=360270 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `termBlacklist`
--

CREATE TABLE IF NOT EXISTS `termBlacklist` (
  `termId` int(11) NOT NULL,
  PRIMARY KEY (`termId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
