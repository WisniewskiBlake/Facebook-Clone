

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;



-- --------------------------------------------------------

--
-- Table structure for table `friends`
--

CREATE TABLE `friends` (
  `friendId` int(11) NOT NULL,
  `userId` varchar(255) NOT NULL,
  `profileId` varchar(255) NOT NULL,
  `friendOn` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `friends`
--

INSERT INTO `friends` (`friendId`, `userId`, `profileId`, `friendOn`) VALUES
(13, 'l8bgwzji5nYmmFgGtyHgI1nrfRH2 ', 'BmXThFksCtXuc62vBW9d3DOQ3KE3', '2018-12-26 11:21:06'),
(14, 'BmXThFksCtXuc62vBW9d3DOQ3KE3', 'l8bgwzji5nYmmFgGtyHgI1nrfRH2 ', '2018-12-26 11:21:06');

-- --------------------------------------------------------

--
-- Table structure for table `notifications`
--

CREATE TABLE `notifications` (
  `nid` int(11) NOT NULL,
  `notificationTo` varchar(255) NOT NULL,
  `notificationFrom` varchar(255) NOT NULL,
  `type` int(11) NOT NULL,
  `notificationTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `postId` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `notifications`
--

INSERT INTO `notifications` (`nid`, `notificationTo`, `notificationFrom`, `type`, `notificationTime`, `postId`) VALUES
(1, '2DcXs4SMlMODlQYbdg1od1qnHg63_', 'l8bgwzji5nYmmFgGtyHgI1nrfRH2', 4, '2018-12-22 05:05:16', 0),
(6, 'l8bgwzji5nYmmFgGtyHgI1nrfRH2', 'sUMLmN60xKg2YKt0E40q5Vacxx32', 4, '2018-12-22 10:40:48', 0),
(7, 'sUMLmN60xKg2YKt0E40q5Vacxx32', 'l8bgwzji5nYmmFgGtyHgI1nrfRH2', 5, '2018-12-22 11:07:54', 0),
(9, 'l8bgwzji5nYmmFgGtyHgI1nrfRH2', 'BmXThFksCtXuc62vBW9d3DOQ3KE3', 4, '2018-12-22 12:33:45', 0),
(10, 'l8bgwzji5nYmmFgGtyHgI1nrfRH2', 'sUMLmN60xKg2YKt0E40q5Vacxx32', 4, '2018-12-22 12:35:35', 0),
(11, 'l8bgwzji5nYmmFgGtyHgI1nrfRH2', 'sUMLmN60xKg2YKt0E40q5Vacxx32', 4, '2018-12-22 12:36:12', 0),
(12, 'l8bgwzji5nYmmFgGtyHgI1nrfRH2', 'BmXThFksCtXuc62vBW9d3DOQ3KE3', 4, '2018-12-22 12:37:16', 0),
(13, 'BmXThFksCtXuc62vBW9d3DOQ3KE3', 'l8bgwzji5nYmmFgGtyHgI1nrfRH2', 5, '2018-12-22 12:37:37', 0),
(14, 'l8bgwzji5nYmmFgGtyHgI1nrfRH2', '2DcXs4SMlMODlQYbdg1od1qnHg63_', 4, '2018-12-22 12:38:09', 0),
(15, '2DcXs4SMlMODlQYbdg1od1qnHg63_', 'l8bgwzji5nYmmFgGtyHgI1nrfRH2', 5, '2018-12-24 03:00:19', 0),
(16, 'l8bgwzji5nYmmFgGtyHgI1nrfRH2', 'sUMLmN60xKg2YKt0E40q5Vacxx32', 4, '2018-12-24 03:59:56', 0),
(17, 'sUMLmN60xKg2YKt0E40q5Vacxx32', 'l8bgwzji5nYmmFgGtyHgI1nrfRH2', 5, '2018-12-24 04:00:10', 0),
(18, 'l8bgwzji5nYmmFgGtyHgI1nrfRH2', 'sUMLmN60xKg2YKt0E40q5Vacxx32', 4, '2018-12-24 04:01:32', 0),
(19, 'sUMLmN60xKg2YKt0E40q5Vacxx32', 'l8bgwzji5nYmmFgGtyHgI1nrfRH2', 5, '2018-12-24 05:02:21', 0),
(20, 'l8bgwzji5nYmmFgGtyHgI1nrfRH2', 'sUMLmN60xKg2YKt0E40q5Vacxx32', 4, '2018-12-24 05:05:45', 0),
(21, 'sUMLmN60xKg2YKt0E40q5Vacxx32', 'l8bgwzji5nYmmFgGtyHgI1nrfRH2', 5, '2018-12-24 05:05:55', 0),
(22, 'l8bgwzji5nYmmFgGtyHgI1nrfRH2', 'BmXThFksCtXuc62vBW9d3DOQ3KE3', 4, '2018-12-26 11:20:47', 0),
(23, 'BmXThFksCtXuc62vBW9d3DOQ3KE3', 'l8bgwzji5nYmmFgGtyHgI1nrfRH2 ', 5, '2018-12-26 11:21:06', 0);

-- --------------------------------------------------------

--
-- Table structure for table `posts`
--

CREATE TABLE `posts` (
  `postId` int(11) NOT NULL,
  `postUserId` varchar(255) NOT NULL,
  `post` text NOT NULL,
  `statusImage` text NOT NULL,
  `statusTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `likeCount` int(11) NOT NULL,
  `commentCount` int(11) NOT NULL,
  `hasComment` tinyint(1) NOT NULL DEFAULT '0',
  `privacy` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `posts`
--

INSERT INTO `posts` (`postId`, `postUserId`, `post`, `statusImage`, `statusTime`, `likeCount`, `commentCount`, `hasComment`, `privacy`) VALUES
(8, 'l8bgwzji5nYmmFgGtyHgI1nrfRH2', 'Lets Celebrate Pale Blue Dot', '../uploads/pale.jpg', '2018-12-26 10:22:29', 0, 0, 0, 1),
(9, 'BmXThFksCtXuc62vBW9d3DOQ3KE3', 'We can do it together !', '', '2018-12-26 17:51:31', 0, 0, 0, 0),
(11, 'l8bgwzji5nYmmFgGtyHgI1nrfRH2', 'Feeling happy while teaching on udemy', '../uploads/android.jpg', '2018-12-26 10:19:55', 0, 0, 0, 2),
(12, 'BmXThFksCtXuc62vBW9d3DOQ3KE3', 'Keep Shining :)', '../uploads/FB_IMG_1536373252966.jpG', '2018-12-26 14:27:27', 0, 0, 0, 2),
(14, 'l8bgwzji5nYmmFgGtyHgI1nrfRH2', 'Second Post', '../uploads/FB_IMG_1535990131982.jpg', '2018-12-26 08:35:38', 0, 0, 0, 0),
(15, 'l8bgwzji5nYmmFgGtyHgI1nrfRH2', 'Hello from Space', '../uploads/FB_IMG_1536373292927.jpg', '2018-12-26 08:35:57', 0, 0, 0, 0),
(17, 'l8bgwzji5nYmmFgGtyHgI1nrfRH2', 'test', '', '2018-12-26 16:08:38', 0, 0, 0, 0),
(18, 'l8bgwzji5nYmmFgGtyHgI1nrfRH2', 'hi', '', '2018-12-26 16:18:51', 0, 0, 0, 0),
(19, 'l8bgwzji5nYmmFgGtyHgI1nrfRH2', 'public privacy level', '', '2018-12-26 16:19:40', 0, 0, 0, 2),
(20, 'l8bgwzji5nYmmFgGtyHgI1nrfRH2', 'only me', '', '2018-12-26 16:20:11', 1, 1, 0, 1);

--
-- Triggers `posts`
--
DELIMITER $$
CREATE TRIGGER `alert_trigger` AFTER INSERT ON `posts` FOR EACH ROW BEGIN
DECLARE done INT DEFAULT FALSE;
DECLARE ids VARCHAR(255);

DECLARE privacyLevel INT;

DECLARE cur CURSOR FOR SELECT profileId FROM friends WHERE userId = new.postUserId;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

OPEN cur;
ins_loop:LOOP
FETCH cur INTO ids;
IF done THEN
LEAVE ins_loop;
END IF;


SELECT privacy INTO privacyLevel FROM posts WHERE postId=new.postId AND privacy = new.privacy;
IF ( privacyLevel=0 OR privacylevel = 2) THEN


INSERT INTO timeline VALUES (null,ids,new.postId,CURRENT_TIMESTAMP);

END IF;



END LOOP;
CLOSE cur;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `requests`
--

CREATE TABLE `requests` (
  `requestId` int(11) NOT NULL,
  `sender` varchar(255) NOT NULL,
  `receiver` varchar(255) NOT NULL,
  `requestDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `timeline`
--

CREATE TABLE `timeline` (
  `tid` int(11) NOT NULL,
  `whoseTimeLine` varchar(255) NOT NULL,
  `postId` int(11) NOT NULL,
  `statusTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `timeline`
--

INSERT INTO `timeline` (`tid`, `whoseTimeLine`, `postId`, `statusTime`) VALUES
(1, 'BmXThFksCtXuc62vBW9d3DOQ3KE3', 17, '2018-12-26 16:08:38'),
(2, 'BmXThFksCtXuc62vBW9d3DOQ3KE3', 18, '2018-12-26 16:18:51'),
(3, 'l8bgwzji5nYmmFgGtyHgI1nrfRH2', 9, '2018-12-26 16:19:40');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `uid` varchar(255) NOT NULL,
  `name` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `profileUrl` text NOT NULL,
  `coverUrl` text NOT NULL,
  `userToken` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`uid`, `name`, `email`, `profileUrl`, `coverUrl`, `userToken`) VALUES
('2DcXs4SMlMODlQYbdg1od1qnHg63_', 'ABC 4', 'test11@gmail.com', 'https://lh4.googleusercontent.com/-vAjZhgo-v1M/AAAAAAAAAAI/AAAAAAAAACQ/6vA8iHphJy0/s96-c/photo.jpg', 'https://static.independent.co.uk/s3fs-public/thumbnails/image/2017/07/07/13/546856858456.jpg?w968h681', 'token 1'),
('5d78IWxH81evFiJgJX0pvooxAQ53', 'Astronomy', 'test0@gmail.com', 'https://smedia2.intoday.in/indiatoday/images/stories/2016November/carl1_110816043159.jpg', 'https://thechive.files.wordpress.com/2017/05/carl-sagan-219.jpg?quality=85&strip=info&w=600', 'token 2'),
('BmXThFksCtXuc62vBW9d3DOQ3KE3', 'ABC 3', 'test1@gmail.com', 'https://images.bewakoof.com/t540/karma-boyfriend-t-shirt-women-s-printed-boyfriend-t-shirts-170191-1521444915.jpg', 'https://petapixel.com/assets/uploads/2016/09/karmadronefeat-800x420.jpg', 'token 3'),
('bVcIsLPAfEbPSglf5mAhZtbpJLE2', 'ABC', 'test@gmail.com', 'https://garinkilpatrick.com/wp-content/uploads/2015/04/hard-work-beats-talent.jpg', 'https://d30o31ylp1hvg6.cloudfront.net/uploads/blog/do-you-know-the-real-value-of-hard-work/_thumb/848x450/do-you-know-the-real-value-of-hard-work.png', 'token 4'),
('l8bgwzji5nYmmFgGtyHgI1nrfRH2', 'test 1', 'test11l@gmail.com', 'https://images.unsplash.com/photo-1444703686981-a3abbc4d4fe3?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80', 'https://static.highexistence.com/wp-content/uploads/2018/09/carl.jpg', 'token '),
('MtklaNwpybQFCMoWMiEV9YaaKZD3', 'Abc 5', 'kishan@gmail.com', 'https://static1.squarespace.com/static/5464f93de4b0efe17c6565de/t/55a92d12e4b0a4c3c8b476b1/1437150483148/', 'https://scontent-sea1-1.cdninstagram.com/vp/c378f7cbcc39772e5d1a99286e263106/5C202B3E/t51.2885-15/e35/41105242_461098851087952_1742074784072825773_n.jpg?ig_cache_key=MTg3MTY1NTcwNDYxNjc0MDI0OQ%3D%3D.2', 'token 5'),
('O3AEOLTrQyQC2zq70JCYzU2zgyv2', 'Abc 8', 'sudip@gmail.com', 'http://www.quotemaster.org/images/03/032463ccab941e840f70a245c09371ae.jpg', 'https://static.independent.co.uk/s3fs-public/thumbnails/image/2017/07/07/13/546856858456.jpg?w968h681', 'token 6'),
('sUMLmN60xKg2YKt0E40q5Vacxx32', 'Joy', 'joy@gmail.com', 'https://www.wallpaperup.com/uploads/wallpapers/2013/09/07/144257/f188c7782ffacb5ca0b7246d7ab898f6-700.jpg', 'https://safbaby.com/wp-content/uploads/2018/04/Smiling-children-on-the-grass-for-blog-post-on-Supplements-for-Healthy-Children.jpg', 'token 7');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `friends`
--
ALTER TABLE `friends`
  ADD PRIMARY KEY (`friendId`),
  ADD UNIQUE KEY `userId` (`userId`,`profileId`);

--
-- Indexes for table `notifications`
--
ALTER TABLE `notifications`
  ADD PRIMARY KEY (`nid`);

--
-- Indexes for table `posts`
--
ALTER TABLE `posts`
  ADD PRIMARY KEY (`postId`);

--
-- Indexes for table `requests`
--
ALTER TABLE `requests`
  ADD PRIMARY KEY (`requestId`);

--
-- Indexes for table `timeline`
--
ALTER TABLE `timeline`
  ADD PRIMARY KEY (`tid`),
  ADD UNIQUE KEY `whoseTimeLine` (`whoseTimeLine`,`postId`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`uid`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `friends`
--
ALTER TABLE `friends`
  MODIFY `friendId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `notifications`
--
ALTER TABLE `notifications`
  MODIFY `nid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT for table `posts`
--
ALTER TABLE `posts`
  MODIFY `postId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT for table `requests`
--
ALTER TABLE `requests`
  MODIFY `requestId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `timeline`
--
ALTER TABLE `timeline`
  MODIFY `tid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
