<?php
	$dbh = 'mysql:host=localhost; dbname=FacebookApp';
	$username = 'root';
	$password = '';

	try {
		$pdo = new PDO($dbh,$username,$password);
	} catch (Exception $e) {
		echo "Connection Error";
		die();
	}
?>
