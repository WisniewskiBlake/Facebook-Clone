<?php

// Api for sendFriend, cancel Friend, Un Friend, accept Friend
$app->post('/app/performaction',function($request){



     	 include __DIR__ . '/../bootstrap/dbconnect.php';

		 $userId = $request->getParsedBody()['userId'];
		 $profileId = $request->getParsedBody()['profileid'];
		 $operationType = $request->getParsedBody()['operationType'];


		 if($operationType==1){
		 		unFriend($userId,$profileId);
		 }else if($operationType==2){
		 		cancelRequest($userId,$profileId);
		 }else if($operationType==3){
		 		acceptRequest($userId,$profileId);
		 }else if($operationType==4){
		 		sentRequest($userId,$profileId);
		 }


});



  function checkRequest($userId,$profileId){
		include __DIR__ . '/../bootstrap/dbconnect.php';
		$stmt = $pdo->prepare("SELECT * FROM `requests` WHERE `sender` = :userId AND `receiver` = :profileId OR `sender` = :profileId AND `receiver` = :userId");
		$stmt->bindParam(':userId', $userId, PDO::PARAM_INT);
		$stmt->bindParam(':profileId', $profileId, PDO::PARAM_INT);
		$stmt->execute();
		return $stmt->fetch(PDO::FETCH_ASSOC);

	}

    function checkFriend($userId,$profileId){

	 	 include __DIR__ . '/../bootstrap/dbconnect.php';

			$stmt = $pdo->prepare("SELECT * FROM `friends` WHERE `userId` = :userId AND `profileId` = :profileId");
			$stmt->bindParam(':userId', $userId, PDO::PARAM_STR);
			$stmt->bindParam(':profileId', $profileId, PDO::PARAM_STR);
			$stmt->execute();
			return $stmt->fetch(PDO::FETCH_ASSOC);

		}

	 function sentRequest($userId,$profileId){

		$insertRequest = insert('requests', array('sender' => $userId, 'receiver' => $profileId, 'requestDate' => date('Y-m-d H:i:s')));
		$insertNotification = insert('notifications', array('notificationTo' => $profileId, 'notificationFrom' => $userId,'type'=>'4', 'notificationTime' => date('Y-m-d H:i:s'),'postId'=>'0'));

				$senderInfo = getUserInfo($userId);
				$receiverInfo = getUserInfo($profileId);

				$title = "New Friend Request";
				$body  = $senderInfo->name . " Send you friend Request";
				$token = $receiverInfo->userToken;

				sendNotification($token,$title,$body);

			if($insertRequest && $insertNotification){
				echo true;
			}else{
				echo false;
			}

	}

	 function cancelRequest($userId,$profileId){
		$deleteRequest = delete('requests', array('sender' => $userId, 'receiver' => $profileId));
		$deleteRequestNotification = delete('notifications', array('notificationTo' => $profileId, 'notificationFrom' => $userId,'type' => '4'));
		if($deleteRequest && $deleteRequestNotification){
			echo true;
		}else{
			echo false;
		}
	}

	 function acceptRequest($userId,$profileId){

	 	include __DIR__ . '/../bootstrap/dbconnect.php';

		$addToFriendTable1= insert('friends', array('userId' => $userId, 'profileId' => $profileId, 'friendOn' => date('Y-m-d H:i:s')));
		$addToFriendTable2= insert('friends', array('userId' => $profileId, 'profileId' => $userId, 'friendOn' => date('Y-m-d H:i:s')));

		$insertNotificationAccepted = insert('notifications', array('notificationTo' => $profileId, 'notificationFrom' => $userId,'type'=>'5', 'notificationTime' => date('Y-m-d H:i:s'),'postId'=>'0'));

		if($addToFriendTable1 && $addToFriendTable2 && $insertNotificationAccepted){


				$deleteRequest1 = delete('requests', array('sender' => $userId, 'receiver' => $profileId));
				$deleteRequest2 = delete('requests', array('sender' => $profileId, 'receiver' => $userId));

					$senderInfo = getUserInfo($userId);
					$receiveInfo = getUserInfo($profileId);

					$title = "Friend Request Accepeted";
					$body = $senderInfo->name . " accepted your Friend Request";
					$token = $receiveInfo->userToken;

					sendNotification($token,$title,$body);

					if($deleteRequest1 || $deleteRequest2){
							echo true;
					}else{
						echo false;
					}

		}else{
			echo false;
		}
	}

	 function unFriend($userId,$profileId){
	 	include __DIR__ . '/../bootstrap/dbconnect.php';

			$unFriend1 = delete('friends', array('userId' => $userId, 'profileId' => $profileId));
			$unFriend2 = delete('friends', array('userId' => $profileId, 'profileId' => $userId));

			if($unFriend1 || $unFriend2 ){
				echo true;
			}else{
				echo false;
			}
	}

	 function insert($table, $fields = array()){
	 	include __DIR__ . '/../bootstrap/dbconnect.php';

		$columns = implode(',', array_keys($fields));

		$values  = ':'.implode(', :', array_keys($fields));

		$sql     = "INSERT INTO {$table} ({$columns}) VALUES ({$values})";

		if($stmt = $pdo->prepare($sql)){

			foreach ($fields as $key => $data) {
				$stmt->bindValue(':'.$key, $data);
			}

			$stmt->execute();
			return $pdo->lastInsertId();
		}
	}

	 function delete($table, $array){
	 	include __DIR__ . '/../bootstrap/dbconnect.php';

		$sql   = "DELETE FROM " . $table;
		$where = " WHERE ";

		foreach($array as $key => $value){
			$sql .= $where . $key . " = '" . $value . "'";
			$where = " AND ";
		}

		$sql .= ";";
		$stmt = $pdo->prepare($sql);
		$stmt->execute();

		if($stmt){
			return true;
		}else{
			return false;
		}
	}


?>
