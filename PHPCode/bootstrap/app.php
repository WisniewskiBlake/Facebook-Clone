<?php
require  __DIR__ .  '/../vendor/autoload.php';
$app = new \Slim\App();

include __DIR__ .'/../app/user.php';
include __DIR__ .'/../app/post.php';
include __DIR__ .'/../app/friend.php';
include __DIR__ .'/../app/comment.php';
include __DIR__ .'/../app/notification.php';
include __DIR__ .'/../app/serverkey.php';
?>
