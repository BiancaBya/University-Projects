<?php
session_start();
require_once '../config.php';

if (!isset($_SESSION['user_id'])) {
    header("Location: index.php");
    exit;
}

$id_user = $_SESSION['user_id'];
$mesaj = '';
$eroare = '';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    if (isset($_FILES['poza']) && $_FILES['poza']['error'] === UPLOAD_ERR_OK) {
        $nume_original = $_FILES['poza']['name'];
        $tmp_path = $_FILES['poza']['tmp_name'];
        $size = $_FILES['poza']['size'];
        $mime = mime_content_type($tmp_path);

        $ext = strtolower(pathinfo($nume_original, PATHINFO_EXTENSION));
        $ext_permise = ['jpg', 'jpeg', 'png', 'gif'];
        $mime_permise = ['image/jpeg', 'image/png', 'image/gif'];

        if (!in_array($ext, $ext_permise) || !in_array($mime, $mime_permise)) {
            $eroare = 'Fișier invalid. Doar imagini JPG, PNG sau GIF.';
        } elseif ($size > 2 * 1024 * 1024) {
            $eroare = 'Fișierul este prea mare. Maxim 2MB.';
        } else {
            $nume_nou = uniqid('img_', true) . '.' . $ext;
            $destinatie = __DIR__ . '/uploads/' . $nume_nou;

            if (move_uploaded_file($tmp_path, $destinatie)) {
                $stmt = $pdo->prepare("INSERT INTO poze (id_user, nume_fisier) VALUES (?, ?)");
                $stmt->execute([$id_user, $nume_nou]);
                $mesaj = 'Imaginea a fost încărcată cu succes.';
            } else {
                $eroare = 'Eroare la mutarea fișierului.';
            }
        }
    } else {
        $eroare = 'Selectează un fișier valid.';
    }
}
?>

<!DOCTYPE html>
<html lang="ro">
<head>
    <meta charset="UTF-8">
    <title>Adaugă poză</title>
    <style>
        body { font-family: Arial; padding: 30px; background: #f9f9f9; }
        form { max-width: 400px; margin: auto; background: white; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px #ccc; }
        input[type="file"] { margin-top: 15px; }
        button { margin-top: 20px; padding: 10px 20px; }
        .msg { color: green; }
        .error { color: red; }
        a { display: block; text-align: center; margin-top: 20px; }
    </style>
</head>
<body>

<form method="post" enctype="multipart/form-data">
    <h2>Încarcă o poză</h2>
    <input type="file" name="poza" accept=".jpg,.jpeg,.png,.gif" required>
    <button type="submit">Încarcă</button>

    <?php if ($mesaj): ?>
        <p class="msg"><?= htmlspecialchars($mesaj) ?></p>
    <?php endif; ?>

    <?php if ($eroare): ?>
        <p class="error"><?= htmlspecialchars($eroare) ?></p>
    <?php endif; ?>

    <a href="dashboard.php">Înapoi la profil</a>
</form>

</body>
</html>
