<?php
require_once '../config.php';

$username = 'bibi';
$parola = 'parola';  

$hash = password_hash($parola, PASSWORD_DEFAULT);

$stmt = $pdo->prepare("INSERT INTO admin (username, parola_hash) VALUES (?, ?)");
$stmt->execute([$username, $hash]);

echo "Administratorul a fost adăugat cu succes.";
