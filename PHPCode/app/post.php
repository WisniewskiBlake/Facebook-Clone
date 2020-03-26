<?php


// Api for posting status
$app->post('/app/poststatus',function($request){
	include __DIR__ .'/../bootstrap/dbconnect.php';
		$post = $request->getParsedBody()['post'];
		$postUserId = $request->getParsedBody()['postUserId'];
		$privacy = $request->getParsedBody()['privacy'];
		$isImageSelected = $request->getParsedBody()['isImageSelected'];
		$statusImage = "";

		if($isImageSelected=='1'){
			try {
					if (move_uploaded_file( $_FILES ['file'] ["tmp_name"], "../uploads/" . $_FILES ["file"] ["name"] )) {
							$statusImage = "../uploads/" . $_FILES ["file"] ["name"];
					}
				} catch (Exception $e) {
				echo false;
				die();
			 }
			}
		 $stmt = $pdo->prepare("INSERT INTO `posts` ( `post`, `postUserId`, `statusImage`, `statusTime`, `likeCount`, `hasComment`, `privacy`)
			VALUES ( :post, :postUserId, :statusImage, current_timestamp, 0,  0, :privacy); ");

		$stmt->bindParam(':post', $post, PDO::PARAM_STR);
		$stmt->bindParam(':postUserId', $postUserId, PDO::PARAM_STR);
		$stmt->bindParam(':statusImage', $statusImage, PDO::PARAM_STR);
		$stmt->bindParam(':privacy',   $privacy, PDO::PARAM_STR);
		$stmt= $stmt->execute();

		if($stmt){
			echo true;
		}else{
			echo false;
		}
});


//Api for uploading profile image and cover image
$app->post('/app/uploadImage',function($request){
	include __DIR__ .'/../bootstrap/dbconnect.php';

		$postUserId = $request->getParsedBody()['postUserId'];
		$imageUploadType = $request->getParsedBody()['imageUploadType'];
		$statusImage = "";
			try {
					if (move_uploaded_file( $_FILES ['file'] ["tmp_name"], "../uploads/" . $_FILES ["file"] ["name"] )) {
						 if($imageUploadType==1){
                                 $stmt = $pdo->prepare("UPDATE  `users` SET `coverUrl` = :uploadUrl WHERE `uid` = :uid; ");
                             }else{
                                $stmt = $pdo->prepare("UPDATE  `users` SET `profileurl` = :uploadUrl WHERE `uid` = :uid; ");
                             }
                              $statusImage = "http://192.168.1.16/FacebookApp/uploads/" . $_FILES ["file"] ["name"];
					}
				} catch (Exception $e) {
				echo false;
				die();
			 }

		$stmt->bindParam(':uploadUrl', $statusImage, PDO::PARAM_STR);
    $stmt->bindParam(':uid', $postUserId, PDO::PARAM_STR);
		$stmt= $stmt->execute();

		if($stmt){
			echo true;
		}else{
			echo false;
		}

});

//Api for like and unlike feature
$app->post('/app/likeunlike',function($request){


		include __DIR__ .'/../bootstrap/dbconnect.php';

		$userId = $request->getParsedBody()['userId'];
		$contentId =  $request->getParsedBody()['postId'];
		$contentOwnerId =  $request->getParsedBody()['contentOwnerId'];
		$operationType = $request->getParsedBody()['operationType'];

		if($operationType==1){

						// code for like

					$stmt = $pdo->prepare("UPDATE `posts` SET `likeCount` = `likeCount`+1  WHERE `postId` = :postId");
					$stmt->bindParam(":postId", $contentId, PDO::PARAM_INT);
					$stmt->execute();
					$count = $stmt->rowCount();

					if($count =='1'){



					$stmt = $pdo->prepare("INSERT INTO `userpostlikes` (`likeBy`, `postOn`) VALUES (:likeBy, :postOn); ");
					$stmt->bindParam(':likeBy', $userId, PDO::PARAM_STR);
					$stmt->bindParam(':postOn', $contentId, PDO::PARAM_STR);
					$stmt= $stmt->execute();


					if($stmt){

						$stmt = $pdo->prepare("INSERT INTO `notifications` (`notificationTo`, `notificationFrom`, `type`,`notificationTime`,`postId`) VALUES (:notificationTo, :notificationFrom,:type, current_timestamp,:postId); ");

							// type  = 1  means notification is  for post like
							$type = 1;
							$stmt->bindParam(':notificationTo', $contentOwnerId, PDO::PARAM_STR);
							$stmt->bindParam(':notificationFrom', $userId, PDO::PARAM_STR);
							$stmt->bindParam(':postId', $contentId, PDO::PARAM_STR);
							$stmt->bindParam(':type', $type, PDO::PARAM_INT);
							$stmt= $stmt->execute();

							if($stmt){
								$likeCount = getLikeCount($contentId);
								//var_dump($likeCount);
								if($likeCount->likeCount ==="0"){
								echo 0;
								}else{

									if($userId != $contentOwnerId){
										$senderInfo = getUserInfo($userId);
										$receiverInfo = getUserInfo($contentOwnerId);

										$title = "New Like";
										$body  = $senderInfo->name . " Liked your post";
										$token = $receiverInfo->userToken;

										sendNotification($token,$title,$body);
									}
									echo (int) $likeCount->likeCount;
								}

							}else{
								echo 0;
							}
							}else{

								echo 0;
							}

							}else{
								echo 0;
							}


		}else{
				// code for unlike

					$stmt = $pdo->prepare("UPDATE `posts` SET `likeCount` = `likeCount`-1  WHERE `postId` = :postId");
					$stmt->bindParam(":postId", $contentId, PDO::PARAM_INT);
					$stmt->execute();
					$count = $stmt->rowCount();


					$stmt = $pdo->prepare("DELETE FROM  `userpostlikes` WHERE `likeBy`=:likeBy AND `postOn` = :postOn ");
					$stmt->bindParam(':likeBy', $userId, PDO::PARAM_STR);
					$stmt->bindParam(':postOn', $contentId, PDO::PARAM_STR);
					$stmt= $stmt->execute();


					if($stmt){
					$stmt = $pdo->prepare("DELETE FROM  `notifications` WHERE `notificationTo`=:notificationTo AND `notificationFrom` =:notificationFrom ");

					$stmt->bindParam(':notificationTo', $contentOwnerId, PDO::PARAM_STR);
					$stmt->bindParam(':notificationFrom', $userId, PDO::PARAM_STR);
					$stmt= $stmt->execute();


						$likeCount = getLikeCount($contentId);

						if( ( (int) $likeCount->likeCount ) != '0' ){
								echo $likeCount->likeCount;
						}else{
								echo 1;
						}

					}else{
						echo "null";
					}

		}


});

function getLikeCount($postId){

		include __DIR__ . '/../bootstrap/dbconnect.php';
		$stmt =  $pdo->prepare("SELECT likeCount from `posts` WHERE `postId` = :postId LIMIT 1");
		$stmt->bindParam(':postId', $postId, PDO::PARAM_STR);
		$stmt->execute();
		$userInfo =$stmt->fetch(PDO::FETCH_OBJ);
		return $userInfo;

	}


?>
