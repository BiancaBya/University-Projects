<?php
session_start();
require_once '../config.php';

if (!isset($_SESSION['admin_id'])) {
    header("Location: admin_login.php");
    exit;
}

$stmt = $pdo->query("SELECT * FROM comentarii WHERE aprobat = 0 ORDER BY data_adaugare ASC");
$comentarii = $stmt->fetchAll();
?>

<!DOCTYPE html>
<html lang="ro">
<head>
    <meta charset="UTF-8">
    <title>Moderare comentarii</title>
    <style>
        body { font-family: Arial; background: #f5f5f5; padding: 30px; }
        .container { max-width: 800px; margin: auto; background: white; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px #ccc; }
        .comentariu { border-bottom: 1px solid #ddd; padding: 10px 0; }
        .actions form { display: inline-block; margin-right: 10px; }
        button { padding: 5px 10px; }
    </style>
</head>
<body>

<div class="container">
    <h2>Comentarii în așteptare</h2>

    <?php if (count($comentarii) === 0): ?>
        <p>Nu există comentarii noi.</p>
    <?php endif; ?>

    <?php foreach ($comentarii as $c): ?>
        <div class="comentariu">
            <strong><?= htmlspecialchars($c['nume']) ?></strong> (<?= $c['data_adaugare'] ?>)
            <p><?= nl2br(htmlspecialchars($c['continut'])) ?></p>
            <div class="actions">
                <form method="post" action="aproba.php">
                    <input type="hidden" name="id" value="<?= $c['id'] ?>">
                    <button type="submit">Aprobă</button>
                </form>
                <form method="post" action="sterge.php">
                    <input type="hidden" name="id" value="<?= $c['id'] ?>">
                    <button type="submit">Șterge</button>
                </form>
            </div>
        </div>
    <?php endforeach; ?>
</div>

</body>
</html>
