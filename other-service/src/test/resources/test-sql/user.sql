--
-- Test data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES
    (20000, 'testuser', '123456', 'test', 0, '2020-01-01', 0, 4, 'test@test.com',             '12300010001', 0, '2020-12-05 13:22:04', '2020-12-05 13:22:04'),
    (20001, 'banuser',  '123456', 'ban',  0, '2020-01-01', 0, 6, 'ban@ban.com',               '12300010002', 0, '2020-12-05 13:22:04', '2020-12-05 13:22:04'),
    (20002, 'XQChen',   '123456', 'cxq',  0, '2020-01-01', 0, 6, 'chenxiaoquan233@gmail.com', '12300010003', 0, '2020-12-05 13:22:04', '2020-12-05 13:22:04');
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;