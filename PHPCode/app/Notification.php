<?php

function sendNotification($token,$title,$body){


		$clickAction = "com.friendster.notification";

		$msg = array
		(
		'body' 	=> $body,
		'title'	=> $title,
		'icon'	=> 'default',
		'sound' => 'default',
		'click_action'=>$clickAction
		);


		$data = array(
				'isFromNotification' => 'true'
		);
		$fields = array
				(
					'to'		=>$token,
					'notification'	=> $msg,
					'data' => $data
				);


		$headers = array
				(
					'Authorization: key=' . API_ACCESS_KEY,
					'Content-Type: application/json'
				);

		#Send Reponse To FireBase Server
			$ch = curl_init();
			curl_setopt( $ch,CURLOPT_URL, 'https://fcm.googleapis.com/fcm/send' );
			curl_setopt( $ch,CURLOPT_POST, true );
			curl_setopt( $ch,CURLOPT_HTTPHEADER, $headers );
			curl_setopt( $ch,CURLOPT_RETURNTRANSFER, true );
			curl_setopt( $ch,CURLOPT_SSL_VERIFYPEER, false );
			curl_setopt( $ch,CURLOPT_POSTFIELDS, json_encode( $fields ) );
			$result = curl_exec($ch );
			curl_close( $ch );

		}

function getUserInfo($userId){

	include __DIR__ . '/../bootstrap/dbconnect.php';
		$stmt =  $pdo->prepare("SELECT users.name, users.userToken from `users` WHERE `uid` = :uid LIMIT 1");
		$stmt->bindParam(':uid', $userId, PDO::PARAM_STR);
			$stmt->execute();
		$userInfo =$stmt->fetch(PDO::FETCH_OBJ);
		return $userInfo;
	}

?>
