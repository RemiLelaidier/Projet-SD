-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le :  mar. 09 jan. 2018 à 13:11
-- Version du serveur :  5.7.19
-- Version de PHP :  5.6.31

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `projet_sd`
--

-- --------------------------------------------------------

--
-- Structure de la table `encheres`
--

DROP TABLE IF EXISTS `encheres`;
CREATE TABLE IF NOT EXISTS `encheres` (
  `id_ec` int(11) NOT NULL AUTO_INCREMENT,
  `client` varchar(25) DEFAULT NULL,
  `prix` double DEFAULT NULL,
  `objet_vente` varchar(25) DEFAULT NULL,
  `date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id_ec`)
) ENGINE=MyISAM AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `encheres`
--

INSERT INTO `encheres` (`id_ec`, `client`, `prix`, `objet_vente`, `date`) VALUES
(4, 'cl1', 16, 'marteau', '2018-01-05 15:22:47'),
(3, 'cl1', 15, 'marteau', '2018-01-05 15:22:47'),
(5, 'cl1', 15, 'marteau', '2018-01-05 15:22:51'),
(6, 'cl1', 16, 'marteau', '2018-01-05 15:22:51'),
(7, 'cl1', 15, 'clous', '2018-01-05 15:24:20'),
(8, 'cl1', 15, 'clous', '2018-01-05 15:25:20');

-- --------------------------------------------------------

--
-- Structure de la table `historique_vente`
--

DROP TABLE IF EXISTS `historique_vente`;
CREATE TABLE IF NOT EXISTS `historique_vente` (
  `id_ve` int(11) NOT NULL AUTO_INCREMENT,
  `client` varchar(25) DEFAULT NULL,
  `prix` double DEFAULT NULL,
  `objet_achat` varchar(25) DEFAULT NULL,
  `date_mise_en_vente` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id_ve`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `historique_vente`
--

INSERT INTO `historique_vente` (`id_ve`, `client`, `prix`, `objet_achat`, `date_mise_en_vente`) VALUES
(2, 'cl1', 24, 'marteau', '2018-01-04 14:31:56');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
