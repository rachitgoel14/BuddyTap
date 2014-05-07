-- phpMyAdmin SQL Dump
-- version 3.5.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: May 07, 2014 at 05:54 AM
-- Server version: 5.5.25a
-- PHP Version: 5.4.4

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `webservices`
--

-- --------------------------------------------------------

--
-- Table structure for table `groupid`
--

CREATE TABLE IF NOT EXISTS `groupid` (
  `gid` int(20) NOT NULL,
  `groupname` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `username` varchar(20) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `groupid`
--

INSERT INTO `groupid` (`gid`, `groupname`, `username`) VALUES
(111111, 'cool', 'a'),
(111111, 'cool', 'b'),
(111111, 'cool', 'c'),
(111111, 'c', 'd'),
(0, 'bad', 'a'),
(111111, 'cool', 'go'),
(111111, 'cool', 'xc'),
(111111, 'cool', 'ddd'),
(934658, 'airtel', 'sss'),
(934658, 'airtel', 'goel133'),
(496380, 'see', 'goel133'),
(269373, 'sasa', 'we'),
(939675, 'sss', 'ttt'),
(410153, 'z', 'ttt'),
(921786, 'ert', 'unm'),
(56789, 'wah', 'rac'),
(358028, 'tur', 'tu'),
(646359, 'tur', 'tu'),
(404358, 'eeeee', 'op'),
(478587, 'tcg', 'vgy'),
(648931, 'wsd', 'qwert'),
(368179, 'a-one', 'jeet'),
(814321, 'got', 'yu'),
(731265, 'zss', 'yu'),
(101061, 'fg', 'yu'),
(954078, 'lets', 'sql'),
(207343, 'hand', 'ter'),
(710119, 'thos', 'real'),
(171055, 'thos', 'real'),
(168853, 'bro', 'regi'),
(534683, 'ba', 'user'),
(558767, 'duff', 'bus'),
(689998, 'dew', 'bus'),
(813709, 'send', 'bus'),
(194551, 'mmm', 'bus'),
(292421, 'seed', 'bus'),
(632163, 'enter', 'bus'),
(993264, 'fad', 'bus'),
(577344, 'fadu', 'bus'),
(577344, 'fadu', 'stat'),
(508257, 'colo', 'next'),
(412701, 'dog', 'bus'),
(548133, 'aajTak', 'bus'),
(200292, 'java', 'admin'),
(200292, 'java', 'ainve');

-- --------------------------------------------------------

--
-- Table structure for table `locations`
--

CREATE TABLE IF NOT EXISTS `locations` (
  `app_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  `latitude` text COLLATE utf8_unicode_ci NOT NULL,
  `longitude` text COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`app_id`),
  UNIQUE KEY `app_id` (`app_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=32 ;

--
-- Dumping data for table `locations`
--

INSERT INTO `locations` (`app_id`, `username`, `latitude`, `longitude`) VALUES
(1, 'fg', '222', '333'),
(2, 'rac', '57.6788', '78.12'),
(5, 'xc', '28', '77'),
(6, 'xc', '28', '77'),
(7, 'rac', '28', '77'),
(8, 'rac', '28', '77'),
(9, 'rac', '33.789', '45.356'),
(10, 'rac', '28', '77'),
(17, 'a', '33.333', '27.45'),
(18, 'b', '57.89', '33.89'),
(19, 'c', '28', '77'),
(20, 'e', '12', '13'),
(21, 'rttt', '28', '77'),
(22, 'rachit', '53', '77'),
(23, 'rac', '28', '77'),
(24, 'rac', '28', '77'),
(25, 'go', '28', '77'),
(26, 'ry', '28', '77'),
(27, 'we', '28', '77'),
(28, 'ccc', '28', '77'),
(29, 'ccc', '28', '77'),
(30, 'ttt', '28', '77'),
(31, 'unm', '28', '77');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  `password` text COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`,`username`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=42 ;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `password`) VALUES
(1, 'fg', '23'),
(2, 'xc', '445'),
(3, 'rac', 'drt'),
(4, 'ac', 'abc'),
(5, 'rc', 'rca'),
(6, 'rachit', 'rac'),
(8, 'goel', 'goel'),
(9, 'gg', 'gg'),
(10, 'go', 'go'),
(11, 'ddd', 'sd'),
(12, 'sss', 'sss'),
(13, 'goel133', 'gg'),
(14, 'ry', 'aw'),
(15, 'we', 'as'),
(16, 'ccc', 'ccc'),
(17, 'cc', 'cc'),
(18, 'vv', 'vv'),
(19, 'bbbb', 'bbbb'),
(20, 'ttt', 'ttt'),
(21, 'unm', 'gg'),
(22, 'tu', 'tt'),
(23, 'op', 'op'),
(24, 'vgy', 'gui'),
(25, 'qwert', 'hhj'),
(26, 'vbbb', 'fgh'),
(27, 'vvh', 'rfy'),
(29, 'yu', 'yu'),
(30, 'sql', 'sql'),
(31, 'rew', 'rew'),
(32, 'ter', 'ter'),
(33, 'real', 'rr'),
(34, 'regi', 'red'),
(35, 'user', 'ser'),
(36, 'bus', 'bus'),
(37, 'stat', 'as'),
(38, 'next', 'next'),
(39, 'bud', 'bud'),
(40, 'teri', 'teri'),
(41, 'admin', 'admin');

-- --------------------------------------------------------

--
-- Table structure for table `user_locations`
--

CREATE TABLE IF NOT EXISTS `user_locations` (
  `app_id` int(15) NOT NULL AUTO_INCREMENT,
  `username` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `latitude` text COLLATE utf8_unicode_ci NOT NULL,
  `longitude` text COLLATE utf8_unicode_ci NOT NULL,
  `address` text COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`app_id`),
  UNIQUE KEY `app_id` (`app_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=28 ;

--
-- Dumping data for table `user_locations`
--

INSERT INTO `user_locations` (`app_id`, `username`, `latitude`, `longitude`, `address`) VALUES
(1, 'a', '12.9', '34.0', 'k-13 2 flldmnn nhdjjdnjd jdjdjd jdjm asmgkks afk'),
(2, 'rac', '28', '77', 'Gurudwara Rd, Model Town II, Model Town, New Delhi, India'),
(3, 'rac', '76.45', '45.89', 'Dwarka sector-2'),
(4, 'abc', '23.45', '34.5', 'punjabi bagh'),
(5, 'rac', '28', '77', 'Gurudwara Rd, Model Town II, Model Town, New Delhi, India'),
(6, 'tu', '28', '77', 'Gurudwara Rd, Model Town II, Model Town, New Delhi, India'),
(7, 'vgy', '28', '77', 'Gurudwara Rd, Model Town II, Model Town, New Delhi, India'),
(8, 'qwert', '28', '77', 'Gurudwara Rd, Model Town II, Model Town, New Delhi, India'),
(9, 'vbbb', '28', '77', 'Gurudwara Rd, Model Town II, Model Town, New Delhi, India'),
(10, 'vvh', '28', '77', 'Gurudwara Rd, Model Town II, Model Town, New Delhi, India'),
(11, 'jeet', '28', '77', 'Gurudwara Rd, Model Town II, Model Town, New Delhi, India'),
(12, 'yu', '28', '77', 'Gurudwara Rd, Model Town II, Model Town, New Delhi, India'),
(13, 'yu', '28', '77', 'Gurudwara Rd, Model Town II, Model Town, New Delhi, India'),
(14, 'sql', '28', '77', 'Gurudwara Rd, Model Town II, Model Town, New Delhi, India'),
(15, 'ter', '28', '77', 'Gurudwara Rd, Model Town II, Model Town, New Delhi, India'),
(16, 'real', '28', '77', 'Gurudwara Rd, Model Town II, Model Town, New Delhi, India'),
(17, 'regi', '28', '77', 'Gurudwara Rd, Model Town II, Model Town, New Delhi, India'),
(18, 'user', '28', '77', 'Gurudwara Rd, Model Town II, Model Town, New Delhi, India'),
(19, 'bus', '28', '77', 'Gurudwara Rd, Model Town II, Model Town, New Delhi, India'),
(20, 'bus', '28', '77', 'Gurudwara Rd, Model Town II, Model Town, New Delhi, India'),
(21, 'bus', '28', '77', 'Gurudwara Rd, Model Town II, Model Town, New Delhi, India'),
(22, 'stat', '28', '77', 'Gurudwara Rd, Model Town II, Model Town, New Delhi, India'),
(23, 'next', '28', '77', 'Gurudwara Rd, Model Town II, Model Town, New Delhi, India'),
(24, 'teri', '28', '77', 'Gurudwara Rd, Model Town II, Model Town, New Delhi, India'),
(25, 'bus', '28', '77', 'Gurudwara Rd, Model Town II, Model Town, New Delhi, India'),
(26, 'admin', '28', '77', 'Gurudwara Rd, Model Town II, Model Town, New Delhi, India'),
(27, 'ainve', '56', '33', 'XYZ');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
