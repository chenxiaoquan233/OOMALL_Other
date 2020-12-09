--
-- Test data for table `favourite_good`
--

LOCK TABLES `favourite_goods` WRITE;
/*!40000 ALTER TABLE `favourite_goods` DISABLE KEYS */;
DELETE FROM favourite_goods WHERE id='9999997';
DELETE FROM favourite_goods WHERE id='9999998';
DELETE FROM favourite_goods WHERE id='9999999';
INSERT INTO `favourite_goods` VALUES
    (9999998, 1, 1, '2020-12-05 13:22:04', '2020-12-05 13:22:04'),
    (9999999, 2, 2, '2020-12-05 13:22:04', '2020-12-05 13:22:04');
/*!40000 ALTER TABLE `favourite_goods` ENABLE KEYS */;
UNLOCK TABLES;