<?php
session_start();
require_once '../config.php';

if (!isset($_SESSION['admin_id'])) {
    header("Location: admin_login.php");
    exit;
}

$id = filter_input(INPUT_POST, 'id', FILTER_VALIDATE_INT);

if ($id !== false && $id !== null) {
    $stmt = $pdo->prepare("DELETE FROM comentarii WHERE id = ?");
    $stmt->execute([$id]);
}

header("Location: admin_panel.php");
exit;
