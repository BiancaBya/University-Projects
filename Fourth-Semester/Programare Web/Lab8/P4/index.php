<?php
session_start();
require_once '../config.php';

if (isset($_SESSION['user_id'])) {
    header("Location: dashboard.php");
    exit;
}

$eroare = '';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $email = trim($_POST['email'] ?? '');
    $parola = $_POST['parola'] ?? '';

    if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
        $eroare = 'Email invalid.';
    } else {
        $stmt = $pdo->prepare("SELECT * FROM utilizatori WHERE email = ?");
        $stmt->execute([$email]);
        $user = $stmt->fetch();

        if ($user && password_verify($parola, $user['parola'])) {
            if (!$user['activat']) {
                $eroare = 'Contul nu este activat. Verifică emailul.';
            } else {
                $_SESSION['user_id'] = $user['id'];
                $_SESSION['email'] = $user['email'];
                header("Location: dashboard.php");
                exit;
            }
        } else {
            $eroare = 'Email sau parolă greșită.';
        }
    }
}
?>

<!DOCTYPE html>
<html lang="ro">
<head>
    <meta charset="UTF-8">
    <title>Autentificare</title>
    <style>
        body { font-family: Arial, sans-serif; padding: 40px; background: #f2f2f2; }
        form { background: white; padding: 20px; max-width: 400px; margin: auto; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
        input[type="email"], input[type="password"] { width: 94%; padding: 10px; margin-top: 10px; margin-bottom:10px;}
        button { padding: 10px 20px; margin-top: 15px; }
        .signup-link { display: block; margin-top: 15px; text-align: center; }
        .error { color: red; margin-bottom: 10px; }
    </style>
</head>
<body>

<h2 style="text-align:center;">Autentificare</h2>

<form method="post" action="">
    <?php if ($eroare): ?>
        <div class="error"><?= htmlspecialchars($eroare) ?></div>
    <?php endif; ?>

    <label for="email">Email:</label>
    <input type="email" name="email" required>

    <label for="parola">Parola:</label>
    <input type="password" name="parola" required>

    <button type="submit">Autentificare</button>

    <a class="signup-link" href="register.php">Nu ai cont? Creeaza unul!</a>
</form>

</body>
</html>
