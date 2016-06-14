-- phpMyAdmin SQL Dump
-- version 3.4.10.1deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jun 13, 2016 at 12:51 AM
-- Server version: 5.6.24
-- PHP Version: 5.3.10-1ubuntu3.19

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `GossipGirl`
--

-- --------------------------------------------------------

--
-- Table structure for table `Subscriber`
--

CREATE TABLE IF NOT EXISTS `Subscriber` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `subscriber_id` int(20) NOT NULL,
  `subscribed_id` int(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `subscribed_id` (`subscribed_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=33 ;

--
-- Dumping data for table `Subscriber`
--

INSERT INTO `Subscriber` (`id`, `subscriber_id`, `subscribed_id`) VALUES
(2, 1, 3),
(3, 5, 2),
(22, 1, 2),
(30, 2, 4),
(31, 3, 1),
(32, 3, 5);

-- --------------------------------------------------------

--
-- Table structure for table `User`
--

CREATE TABLE IF NOT EXISTS `User` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(20) DEFAULT NULL,
  `last_name` varchar(20) DEFAULT NULL,
  `address` varchar(20) DEFAULT NULL,
  `age` int(20) DEFAULT NULL,
  `email_id` varchar(30) NOT NULL,
  `password` varchar(15) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=6 ;

--
-- Dumping data for table `User`
--

INSERT INTO `User` (`id`, `first_name`, `last_name`, `address`, `age`, `email_id`, `password`) VALUES
(1, 'Sunny', 'Malhotra', 'hyderabad', 23, 'sunny@gmail.com', 'qwerty'),
(2, 'Vishal', 'singh', 'Ahmedabad', 29, 'vishal@gmail.com', 'qwerty'),
(3, 'Rahul', 'Arora', 'Mumbai', 16, 'rahul@gmail.com', 'asdf'),
(4, 'Nidhi', 'Singhania', 'Pune', 33, 'nidhi@gmail.com', 'asdf'),
(5, 'Saurabh', 'Srivastav', 'Guwahati', 23, 'saurabh@gmail.com', 'asdf');

--
-- Constraints for dumped tables
--

--
-- Constraints for table `Subscriber`
--
ALTER TABLE `Subscriber`
  ADD CONSTRAINT `Subscriber_ibfk_1` FOREIGN KEY (`subscribed_id`) REFERENCES `User` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
