<?php

// Api For Login
$app->post('/app/login', function ($request) {
		include __DIR__ .'/../bootstrap/dbconnect.php';
		$uid = $request->getParsedBody()['uid'];
		$name = $request->getParsedBody()['name'];
		$email = $request->getParsedBody()['email'];
		$profileUrl = $request->getParsedBody()['profileUrl'];
		$coverUrl = $request->getParsedBody()['coverUrl'];
		$userToken = $request->getParsedBody()['userToken'];
		$checkStmt =  $pdo->prepare("SELECT `uid` from `users` WHERE `uid` = :uid LIMIT 1");
		$checkStmt->bindParam(':uid', $uid, PDO::PARAM_STR);
					$checkStmt->execute();
		$count = $checkStmt->rowCount();
		if($count==1){

				// user has already signed up just update the token
			$stmt = $pdo->prepare("UPDATE  `users` SET `userToken` = :userToken WHERE `uid` = :uid; ");
			$stmt->bindParam(':userToken', $userToken, PDO::PARAM_STR);
			$stmt->bindParam(':uid', $uid, PDO::PARAM_STR);
			$stmt= $stmt->execute();


		}else{
					// User has just signed up... insert all the user data from here
		$stmt = $pdo->prepare("INSERT INTO `users` (`uid`, `name`, `email`, `profileUrl`, `coverUrl`, `userToken`) VALUES (:uid, :name, :email, :profileUrl, :coverUrl, :userToken); ");
		$stmt->bindParam(':uid', $uid, PDO::PARAM_STR);
		$stmt->bindParam(':name', $name, PDO::PARAM_STR);
		$stmt->bindParam(':email', $email, PDO::PARAM_STR);
		$stmt->bindParam(':profileUrl', $profileUrl, PDO::PARAM_STR);
		$stmt->bindParam(':coverUrl', $coverUrl, PDO::PARAM_STR);
		$stmt->bindParam(':userToken', $userToken, PDO::PARAM_STR);
		$stmt= $stmt->execute();

		}

		if($stmt){
				echo true;
		}else{
			echo false;
		}
});



// Api for loading own profile
$app->get('/app/loadownprofile',function($request){
	include __DIR__ .'/../bootstrap/dbconnect.php';
	$user_id = $request->getQueryParam('userId');
	$stmt = $pdo->prepare('SELECT * FROM `users` WHERE `uid` = :userId');
	$stmt->bindParam(':userId', $user_id, PDO::PARAM_STR);
	$stmt->execute();
	$row = $stmt->fetch(PDO::FETCH_ASSOC);
	$row['state']="5";
	echo json_encode($row);

});

// Test Api for sending Notification
$app->get('/app/test',function($request){
	echo json_encode( getUserInfo("l8bgwzji5nYmmFgGtyHgI1nrfRH2"));
	//sendNotification("c5peMkbOd10:APA91bG9Ob37yImQiuqbxi53GklfjEKg1C19hiabER8ko2gquCrWRtunbr4r0b8z55jszoQJLasQo6WCeQLr92OS_yoBXHh9KDEVGWK58wgtLQsOOEjWTo99ES5mqqyRCPvzIhJlOFfA","From Php","This is a notification From Php ");

});

// Api for loading others profile
$app->get('/app/loadotherprofile',function($request){
	include __DIR__ .'/../bootstrap/dbconnect.php';
	$user_id = $request->getQueryParam('userId');
	$profileId = $request->getQueryParam('profileId');
	$stmt = $pdo->prepare('SELECT * FROM `users` WHERE `uid` = :userId');
	$stmt->bindParam(':userId', $profileId, PDO::PARAM_STR);
	$stmt->execute();
	$row = $stmt->fetch(PDO::FETCH_ASSOC);
	$current_state = 0;

	$request = checkRequest($user_id,$profileId);
		if($request){
			if($request['sender']==$user_id){
				// we have send the request
				$current_state = "2";
			}else{
				$current_state="3";
				//we have received the request
			}
		}else{
			if(checkFriend($user_id,$profileId)){
				$current_state = "1";
				//we are friends
			}else{
				$current_state="4";
				//we are unknown to one another
			}
		}
	$row['state']= $current_state;

	echo json_encode($row);

});

// Api for search
$app->get('/app/search',function($request){
	include __DIR__ .'/../bootstrap/dbconnect.php';
	$keyword = $request->getQueryParam('keyword');

	$stmt = $pdo->prepare("
						SELECT * from users
						where name like '$keyword%'
						limit 10

					");


	$stmt->execute();
	$row = $stmt->fetchAll(PDO::FETCH_ASSOC);

	echo json_encode($row);

});

// Api for laoding Friends and Requests
$app->get('/app/loadfriends',function($request){

	include __DIR__ .'/../bootstrap/dbconnect.php';
	$userId = $request->getQueryParam('userId');

	$stmt = $pdo->prepare('
								SELECT users.* FROM `users`
								Inner JOIN `requests`
								ON users.uid = requests.sender
								 WHERE `receiver` = :userId'
							);

		 $stmt->bindParam(':userId', $userId, PDO::PARAM_STR);

		$stmt->execute();

		$row['requests']= $stmt->fetchAll(PDO::FETCH_ASSOC);



		$stmt = $pdo->prepare('
								SELECT users.* FROM `users`
								Inner JOIN `friends`
								ON users.uid = friends.profileId
								 WHERE friends.userId = :userId'
							);

		 $stmt->bindParam(':userId', $userId, PDO::PARAM_STR);

		$stmt->execute();
		$row['friends'] = $stmt->fetchAll(PDO::FETCH_ASSOC);


		echo json_encode($row);

});

//Api for showing user's profile timeline
$app->get('/app/profiletimeline',function($request){

     	 include __DIR__ . '/../bootstrap/dbconnect.php';


		 $uid = $request->getQueryParam('uid');
		 $limit = $request->getQueryParam('limit');
		 $offset = $request->getQueryParam('offset');
		 $current_state = $request->getQueryParam('current_state');

	 	 $stmt =  $pdo->prepare("SELECT `name`,`profileUrl`,`userToken` from `users` WHERE `uid` = :uid LIMIT 1");
		 $stmt->bindParam(':uid', $uid, PDO::PARAM_STR);
		 $stmt->execute();

		 $userInfo =$stmt->fetch(PDO::FETCH_OBJ);



				/*

				privacy flags representation

			        0 - > Friends privacy level
			        1 - > Only Me privacy level
			        2 - > Public privacy level

			     */


			   /*
					Relations between two accounts

				    1=  two people are friends
				    4 = people are unkown
				    5 = own profile


			     */

		if($current_state==5){


			$stmt = " SELECT * FROM `posts` WHERE `postUserId` = :uid ORDER By statusTime DESC";

			/*

				-> our own profile,
				-> can view only me, friends and public  privacy level post

			*/

		}else if($current_state==4){

			$stmt = " SELECT * FROM `posts` WHERE `postUserId` = :uid AND `privacy` = 2 ORDER By statusTime DESC";

			/*

				-> not friend account ( unknown profile ),
				-> can view public privacy level post

			*/
		}else if($current_state==1){

			$stmt = " SELECT * FROM `posts` WHERE `postUserId` = :uid AND ( `privacy` = 2 OR `privacy` = 0 ) ORDER By statusTime DESC";

			/*

				-> friends accoun
				-> can view fiends and public privacy level post

			*/
		}else{
			$stmt = " SELECT * FROM `posts` WHERE `postUserId` = :uid AND `privacy` = 2 ORDER By statusTime DESC";
			/*
				-> relation not known
				-> can view nothing
			*/
		}

		$stmt .=  '  LIMIT '.$limit. ' OFFSET '.$offset;


	 	$stmt = $pdo->prepare($stmt);

		$stmt->bindParam(':uid', $uid, PDO::PARAM_STR);
	   	$stmt->execute();

		$reviews= $stmt->fetchAll(PDO::FETCH_OBJ);



		foreach ($reviews as $key => $value) {

			$value->name        =  $userInfo->name;
			$value->profileUrl =   $userInfo->profileUrl;
			$value->userToken   = $userInfo->userToken;

			if(checkLike($uid,$value->postId)){
		 		$value->isLiked=true;
		 	}else{
		 		$value->isLiked=false;
			}

		}
			echo json_encode($reviews);


});


//Api for personalized timeline
$app->get('/app/gettimelinepost',function($request){

     	 include __DIR__ . '/../bootstrap/dbconnect.php';



		 $uid = $request->getQueryParam('uid');
		 $limit = $request->getQueryParam('limit');
		 $offset = $request->getQueryParam('offset');

	$stmt = $pdo->prepare("
	 							SELECT 	 posts.*, users.name, users.profileUrl,users.userToken
						 		from 	`timeline`
						 		INNER JOIN `posts`
						 			on timeline.postId = posts.postId
						 		INNER JOIN `users`
						 			on  posts.postUserId = users.uid
						 		WHERE 	timeline.whoseTimeLine= :uid
						 		ORDER By timeline.statusTime DESC
						 		LIMIT $limit OFFSET $offset
						 		"
						 	);

		$stmt->bindParam(':uid', $uid, PDO::PARAM_STR);
	   	$stmt->execute();



	  $reviews= $stmt->fetchAll(PDO::FETCH_OBJ);

	foreach ($reviews as $key => $value) {

			if(checkLike($uid,$value->postId)){
		 		$value->isLiked=true;
		 	}else{
		 		$value->isLiked=false;
			}

		}

			echo json_encode($reviews);


});

$app->get('/app/getnotification',function($request){

		include __DIR__ . '/../bootstrap/dbconnect.php';
		 $userId = $request->getQueryParam('uid');

		$stmt = $pdo->prepare('

					SELECT notifications.*,users.name,users.profileUrl,posts.post FROM `notifications`
					LEFT join users
					ON
					notifications.notificationFrom = users.uid
					LEFT join posts
					ON
					notifications.postId = posts.postId
					WHERE `notificationTo` = :userId
					ORDER BY
					notifications.notificationTime
					DESC

					');


		 $stmt->bindParam(':userId', $userId, PDO::PARAM_STR);
		$stmt->execute();
		$row = $stmt->fetchAll(PDO::FETCH_ASSOC);

		echo json_encode($row);
});

$app->get('/app/details',function($request){
 	include __DIR__ . '/../bootstrap/dbconnect.php';


		$postId = $request->getQueryParam('postId');
		$uid = $request->getQueryParam('uid');

	 	$stmt = $pdo->prepare("
	 							SELECT posts.*, users.name, users.profileUrl,users.userToken
	 							 FROM `posts`
	 							 LEFT join users
								  ON
								 posts.postUserId = users.uid
	 							 WHERE `postId` = :postId

	 							 ");
		$stmt->bindParam(':postId', $postId, PDO::PARAM_INT);
	   	$stmt->execute();

		$result =$stmt->fetch(PDO::FETCH_OBJ);

		if(checkLike($uid,$postId)){
		 		$result->isLiked=true;
		 	}else{
		 		$result->isLiked=false;
			}
		echo json_encode($result);

});

 function checkLike($userId,$postId){
	 include __DIR__ . '/../bootstrap/dbconnect.php';
		$stmt = $pdo->prepare("SELECT * FROM `userpostlikes` WHERE `likeBy` = :userId AND `postOn` = :postId");
		$stmt->bindParam(":userId", $userId, PDO::PARAM_INT);
		$stmt->bindParam(":postId", $postId, PDO::PARAM_INT);
		$stmt->execute();
		return $stmt->fetch(PDO::FETCH_OBJ);

	}
?>
