<?php
require_once '../config.php';
require_once '../vendor/autoload.php';

use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

$mesaj = '';
$eroare = '';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $email = trim($_POST['email'] ?? '');
    $parola = $_POST['parola'] ?? '';

    if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
        $eroare = 'Adresa de email nu este validă.';
    } elseif (strlen($parola) < 6) {
        $eroare = 'Parola trebuie să aibă cel puțin 6 caractere.';
    } else {
        $stmt = $pdo->prepare("SELECT id FROM utilizatori WHERE email = ?");
        $stmt->execute([$email]);

        if ($stmt->fetch()) {
            $eroare = 'Emailul este deja înregistrat.';
        } else {
            $hash = password_hash($parola, PASSWORD_DEFAULT);
            $token = bin2hex(random_bytes(32));

            $insert = $pdo->prepare("INSERT INTO utilizatori (email, parola, activat, token_activare) VALUES (?, ?, 0, ?)");
            $insert->execute([$email, $hash, $token]);

            $mail = new PHPMailer(true);
            try {
                $mail->isSMTP();
                $mail->Host = 'smtp.mailtrap.io';
                $mail->SMTPAuth = true;
                $mail->Username = '5d2fae3c5fe2ff';
                $mail->Password = 'cd0a9d54924d30';
                $mail->Port = 2525;

                $mail->setFrom('noreply@siteul-tau.com', 'Cont Test');
                $mail->addAddress($email);
                $mail->Subject = 'Activare cont';

                $link = "http://localhost/LabPHP/P4/activate.php?token=$token";
                $mail->Body = "Salut! Pentru a-ti activa contul, acceseaza acest link:\n\n$link";

                $mail->send();
                $mesaj = 'Contul a fost creat. Verifică adresa de email pentru activare.';
            } catch (Exception $e) {
                $eroare = 'Eroare la trimiterea emailului: ' . $mail->ErrorInfo;
            }
        }
    }
}
?>

<!DOCTYPE html>
<html lang="ro">
<head>
    <meta charset="UTF-8">
    <title>Înregistrare</title>
    <style>
        body { font-family: Arial; padding: 30px; background: #f4f4f4; }
        form { max-width: 400px; margin: auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px #ccc; }
        input { width: 93%; padding: 10px; margin-top: 10px; margin-bottom: 10px;}
        button { padding: 10px 20px; margin-top: 15px; }
        .msg { color: green; margin-top: 10px; }
        .error { color: red; margin-top: 10px; }
    </style>
</head>
<body>

<h2 style="text-align:center;">Creare cont</h2>

<form method="post">
    <label for="email">Email:</label>
    <input type="email" name="email" required>

    <label for="parola">Parolă:</label>
    <input type="password" name="parola" required>

    <button type="submit">Înregistrează-te</button>

    <?php if ($mesaj): ?>
        <p class="msg"><?= htmlspecialchars($mesaj) ?></p>
    <?php endif; ?>

    <?php if ($eroare): ?>
        <p class="error"><?= htmlspecialchars($eroare) ?></p>
    <?php endif; ?>
</form>

</body>
</html>
