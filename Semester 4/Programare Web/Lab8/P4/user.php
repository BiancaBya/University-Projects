<?php
session_start();
require_once '../config.php';

if (!isset($_SESSION['user_id'])) {
    header("Location: index.php");
    exit;
}

$poze = [];
$email_cautat = $_GET['email'] ?? '';
$email_cautat = trim($email_cautat);
$mesaj = '';

if ($email_cautat) {
    $stmt = $pdo->prepare("SELECT id FROM utilizatori WHERE email = ?");
    $stmt->execute([$email_cautat]);
    $utilizator = $stmt->fetch();

    if ($utilizator) {
        $id_alt_user = $utilizator['id'];

        $stmt = $pdo->prepare("SELECT nume_fisier FROM poze WHERE id_user = ? ORDER BY data_upload DESC");
        $stmt->execute([$id_alt_user]);
        $poze = $stmt->fetchAll();
    } else {
        $mesaj = 'Utilizatorul nu a fost găsit.';
    }
} else {
    $mesaj = 'Nu a fost specificat niciun email.';
}
?>

<!DOCTYPE html>
<html lang="ro">
<head>
    <meta charset="UTF-8">
    <title>Profil utilizator</title>
    <style>
        body { 
            font-family: Arial, 
            background: #f0f0f0; 
            margin: 0; 
            padding: 30px; 
        }
        .container { 
            max-width: 800px; 
            margin: auto; 
            background: white; 
            padding: 20px; 
            border-radius: 10px; 
            box-shadow: 0 0 10px #ccc; 
        }
        .poza { 
            margin-bottom: 20px; 
            padding: 10px; 
            background: #fff; 
            border-radius: 8px; 
            box-shadow: 0 0 5px #ccc; 
        }
        img { 
            max-width: 100%; 
            display: block; 
            margin: auto; 
        }
        h2 { text-align: center; }
        .msg { text-align: center; color: red; }
        a { 
            position: sticky;
            top: 20px;
            text-align: center;
            text-decoration: none; 
            color: #007BFF; 
        }
    </style>
</head>
<body>
<a href="dashboard.php">Înapoi la profilul tău</a>
<div class="container">
    <h2>Profil: <?= htmlspecialchars($email_cautat) ?></h2>

    <?php if ($mesaj): ?>
        <p class="msg"><?= htmlspecialchars($mesaj) ?></p>
    <?php elseif (count($poze) === 0): ?>
        <p class="msg">Acest utilizator nu a încărcat nicio poză.</p>
    <?php else: ?>
        <?php foreach ($poze as $poza): ?>
            <div class="poza">
                <img src="uploads/<?= htmlspecialchars($poza['nume_fisier']) ?>" alt="poza utilizator">
            </div>
        <?php endforeach; ?>
    <?php endif; ?>
</div>

</body>
</html>
