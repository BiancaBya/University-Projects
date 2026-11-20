<?php
session_start();
$host = 'localhost';

$user = 'bibi';
$pass = 'parola';
$db   = 'trenuri_db';

$charset = 'utf8mb4';
$dsn = "mysql:host=$host;dbname=$db;charset=$charset";

$options = [
    PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
    PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
];

try {
    $pdo = new PDO($dsn, $user, $pass, $options);
} catch (\PDOException $e) {
    die("Conexiunea a eșuat: " . htmlspecialchars($e->getMessage()));
}

$n_per_page = 5;
$page = 1;

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    if (isset($_POST['n_per_page'])) {
        $n = filter_input(INPUT_POST, 'n_per_page', FILTER_VALIDATE_INT, [
            'options' => ['min_range' => 1, 'max_range' => 50]
        ]);
        if ($n !== false) {
            $_SESSION['n_per_page'] = $n;
        }
    }
    header("Location: index.php");
    exit;
}

if (isset($_SESSION['n_per_page'])) {
    $n_per_page = $_SESSION['n_per_page'];
}

if (isset($_GET['page'])) {
    $p = filter_input(INPUT_GET, 'page', FILTER_VALIDATE_INT, ['options' => ['min_range' => 1]]);
    if ($p !== false) {
        $page = $p;
    }
}

$offset = ($page - 1) * $n_per_page;

$total_stmt = $pdo->query("SELECT COUNT(*) FROM produse");
$total_products = $total_stmt->fetchColumn();
$total_pages = ceil($total_products / $n_per_page);

$stmt = $pdo->prepare("SELECT * FROM produse ORDER BY id ASC LIMIT :limit OFFSET :offset");
$stmt->bindValue(':limit', $n_per_page, PDO::PARAM_INT);
$stmt->bindValue(':offset', $offset, PDO::PARAM_INT);
$stmt->execute();
$produse = $stmt->fetchAll();
?>

<!DOCTYPE html>
<html lang="ro">
<head>
    <meta charset="UTF-8">
    <title>Catalog Produse</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f0f4f8;
            color: #333;
            padding: 20px;
        }

        h1 {
            background-color: #00497a;
            color: #ffffff;
            padding: 15px;
            border-radius: 5px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.2);
        }

        form {
            margin-bottom: 20px;
            background-color: #ffffff;
            padding: 10px 15px;
            border-left: 4px solid #007acc;
            border-radius: 4px;
        }
        label {
            font-weight: bold;
            color: #007acc;
        }
        select, button {
            padding: 5px 10px;
            margin-left: 5px;
            border: 1px solid #007acc;
            border-radius: 3px;
            background-color: #e6f2ff;
            color: #00497a;
            cursor: pointer;
        }
        select:focus, button:focus {
            outline: none;
            box-shadow: 0 0 3px rgba(0,122,204,0.7);
        }
        button:hover {
            background-color: #cce6ff;
        }

        .produs {
            background-color: #ffffff;
            border: 1px solid #d1e7f0;
            padding: 15px;
            margin-bottom: 15px;
            border-radius: 5px;
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
            transition: transform 0.2s, box-shadow 0.2s;
        }
        .produs:hover {
            transform: translateY(-2px);
            box-shadow: 0 3px 8px rgba(0,0,0,0.15);
        }
        .produs h3 {
            color: #007acc;
            margin-top: 0;
        }
        .produs p {
            color: #555;
        }
        .produs strong {
            color: #00497a;
        }

        .paginare {
            text-align: center;
            margin-top: 20px;
        }
        .paginare a {
            display: inline-block;
            margin: 0 10px;
            text-decoration: none;
            padding: 8px 12px;
            background-color: #007acc;
            color: #ffffff;
            border-radius: 4px;
            transition: background-color 0.2s;
        }
        .paginare a:hover {
            background-color: #005f99;
        }
        .paginare a.disabled {
            background-color: #cccccc;
            cursor: not-allowed;
        }
    </style>
</head>
<body>

<h1>Catalog Produse</h1>

<form method="post">
    <label for="n_per_page">Produse pe pagină:</label>
    <select name="n_per_page" id="n_per_page">
        <?php foreach ([3, 5, 7, 10, 15, 20] as $opt): ?>
            <option value="<?= $opt ?>" <?= $opt == $n_per_page ? 'selected' : '' ?>><?= $opt ?></option>
        <?php endforeach; ?>
    </select>
    <button type="submit">Setează</button>
</form>

<?php if (empty($produse)): ?>
    <p>Nu există produse de afișat.</p>
<?php else: ?>
    <?php foreach ($produse as $produs): ?>
        <div class="produs">
            <h3><?= htmlspecialchars($produs['denumire']) ?></h3>
            <p><?= nl2br(htmlspecialchars($produs['descriere'])) ?></p>
            <strong>Pret:</strong> <?= number_format($produs['pret'], 2) ?> RON
        </div>
    <?php endforeach; ?>

    <div class="paginare">
        <?php if ($page > 1): ?>
            <a href="?page=<?= $page - 1 ?>">&lt;&lt; Prev</a>
        <?php else: ?>
            <a class="disabled">&lt;&lt; Prev</a>
        <?php endif; ?>

        <?php if ($page < $total_pages): ?>
            <a href="?page=<?= $page + 1 ?>">Next &gt;&gt;</a>
        <?php else: ?>
            <a class="disabled">Next &gt;&gt;</a>
        <?php endif; ?>
    </div>
<?php endif; ?>

</body>
</html>
