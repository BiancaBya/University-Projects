<?php
session_start();
require_once '../config.php';

$eroare = '';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $username = trim($_POST['username'] ?? '');
    $parola = $_POST['parola'] ?? '';

    $stmt = $pdo->prepare("SELECT id, parola_hash FROM admin WHERE username = ?");
    $stmt->execute([$username]);
    $admin = $stmt->fetch();

    if ($admin && password_verify($parola, $admin['parola_hash'])) {
        $_SESSION['admin_id'] = $admin['id'];
        $_SESSION['admin_username'] = $username;
        header('Location: admin_panel.php');
        exit;
    } else {
        $eroare = 'Date de autentificare incorecte.';
    }
}
?>

<!DOCTYPE html>
<html lang="ro">
<head>
    <meta charset="UTF-8">
    <title>Login Admin</title>
    <style>
        body { font-family: Arial; padding: 50px; background: #eee; }
        form { background: white; padding: 30px; max-width: 400px; margin: auto; border-radius: 10px; box-shadow: 0 0 10px #aaa; }
        input { width: 95%; padding: 10px; margin-top: 10px; }
        button { margin-top: 15px; padding: 10px 20px; }
        .err { color: red; }
    </style>
</head>
<body>

<form method="post">
    <h2>Autentificare administrator</h2>
    <?php if ($eroare): ?><p class="err"><?= htmlspecialchars($eroare) ?></p><?php endif; ?>
    <input type="text" name="username" placeholder="Utilizator" required>
    <input type="password" name="parola" placeholder="Parolă" required>
    <button type="submit">Autentifică-te</button>
</form>

</body>
</html>
