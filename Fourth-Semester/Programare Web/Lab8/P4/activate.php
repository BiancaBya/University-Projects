<?php
require_once '../config.php';

$mesaj = '';

if (isset($_GET['token'])) {
    $token = $_GET['token'];

    $stmt = $pdo->prepare("SELECT id, activat FROM utilizatori WHERE token_activare = ?");
    $stmt->execute([$token]);
    $user = $stmt->fetch();

    if ($user) {
        if ($user['activat']) {
            $mesaj = 'Contul este deja activat.';
        } else {
            $update = $pdo->prepare("UPDATE utilizatori SET activat = 1, token_activare = NULL WHERE id = ?");
            $update->execute([$user['id']]);
            $mesaj = 'Cont activat cu succes! Te poti autentifica acum.';
        }
    } else {
        $mesaj = 'Token invalid sau cont deja activat.';
    }
} else {
    $mesaj = 'Tokenul de activare nu este specificat.';
}
?>

<!DOCTYPE html>
<html lang="ro">
<head>
    <meta charset="UTF-8">
    <title>Activare cont</title>
    <style>
        body { font-family: Arial, sans-serif; padding: 40px; background-color: #f8f8f8; }
        .container { max-width: 600px; margin: auto; padding: 30px; background: white; border-radius: 8px; box-shadow: 0 0 10px #ccc; text-align: center; }
        .success { color: green; font-weight: bold; }
        .error { color: red; font-weight: bold; }
    </style>
</head>
<body>

<div class="container">
    <h2>Activare cont</h2>
    <p class="<?= strpos($mesaj, 'succes') !== false ? 'success' : 'error' ?>">
        <?= htmlspecialchars($mesaj) ?>
    </p>
    <a href="index.php">Inapoi la autentificare</a>
</div>

</body>
</html>
