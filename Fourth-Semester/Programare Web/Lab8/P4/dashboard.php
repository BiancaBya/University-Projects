<?php
session_start();
require_once '../config.php';

if (!isset($_SESSION['user_id'], $_SESSION['email'])) {
    header("Location: index.php");
    exit;
}

$id_user = $_SESSION['user_id'];
$email = $_SESSION['email'];

$stmt = $pdo->prepare("SELECT nume_fisier FROM poze WHERE id_user = ? ORDER BY data_upload DESC");
$stmt->execute([$id_user]);
$poze = $stmt->fetchAll();
?>

<!DOCTYPE html>
<html lang="ro">
<head>
    <meta charset="UTF-8">
    <title>Profilul tău</title>
    <style>
        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background-color: #f2f2f2;
        }

        .topbar {
            position: sticky;
            display: flex;
            top: 0;
            background-color: white;
            padding: 15px 20px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            z-index: 10;
        }

        .topbar form {
            display: flex;
            justify-content: center;
            max-width: 600px;
            margin: auto;
        }

        .topbar input[type="text"] {
            padding: 10px;
            width: 70%;
            border: 1px solid #ccc;
            border-radius: 5px 0 0 5px;
        }

        .topbar button {
            padding: 10px 15px;
            border: none;
            background-color: #007BFF;
            color: white;
            cursor: pointer;
        }

        .submitBttn {
            border-radius: 0 5px 5px 0;
        }

        .container {
            padding: 20px;
            max-width: 800px;
            margin: auto;
        }

        .poza {
            margin-bottom: 20px;
            background: white;
            padding: 10px;
            border-radius: 8px;
            box-shadow: 0 0 5px #ccc;
        }

        .poza img {
            max-width: 100%;
            border-radius: 5px;
            display: block;
            margin: auto;
        }

        h2 {
            text-align: center;
            color: #333;
        }
    </style>
</head>
<body>

<div class="topbar">
    <a href = 'logout.php'>
        <button type="button">Log Out</button>
    </a>

    <form method="get" action="user.php">
        <input type="text" name="email" placeholder="Cauta utilizator dupa email" required>
        <button class="submitBttn" type="submit">Caută</button>
    </form>
    <a href="upload.php" style="text-decoration: none;">
            <button type="button">Adaugă poză</button>
    </a>
</div>

<div class="container">
    <h2>Bine ai venit, <?= htmlspecialchars($email) ?></h2>

    <?php if (count($poze) === 0): ?>
        <p>Nu ai incarcat nicio poza.</p>
    <?php else: ?>
        <?php foreach ($poze as $poza): ?>
            <div class="poza">
                <img src="uploads/<?= htmlspecialchars($poza['nume_fisier']) ?>" alt="Poza ta">
            </div>
        <?php endforeach; ?>
    <?php endif; ?>
</div>

</body>
</html>
