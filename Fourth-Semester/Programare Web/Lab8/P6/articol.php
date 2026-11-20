<?php
require_once '../config.php';

$mesaj = '';
$eroare = '';

if (isset($_GET['succes'])) {
    $mesaj = 'Comentariul a fost trimis și va apărea după aprobare.';
}

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $nume = trim($_POST['nume'] ?? '');
    $comentariu = trim($_POST['comentariu'] ?? '');

    if (strlen($nume) < 2 || strlen($comentariu) < 5) {
        $eroare = 'Numele trebuie sa aiba minim 2 caractere si comentariul minim 5.';
    } else {
        $stmt = $pdo->prepare("INSERT INTO comentarii (nume, continut, aprobat) VALUES (?, ?, 0)");
        $stmt->execute([$nume, $comentariu]);

        header("Location: articol.php?succes=1");
        exit;
    }
}


$stmt = $pdo->query("SELECT nume, continut, data_adaugare FROM comentarii WHERE aprobat = 1 ORDER BY data_adaugare DESC");
$comentarii = $stmt->fetchAll();
?>

<!DOCTYPE html>
<html lang="ro">
<head>
    <meta charset="UTF-8">
    <title>Articol</title>
    <style>
        body { font-family: Arial, background: #f8f8f8; padding: 20px; }
        .container { max-width: 800px; margin: auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px #ccc; }
        h1 { text-align: center; }
        .comentariu { border-bottom: 1px solid #ddd; padding: 10px 0; }
        .formular { margin-top: 30px; }
        input, textarea { width: 98%; padding: 10px; margin-top: 10px; }
        button { margin-top: 10px; padding: 10px 20px; }
        .msg { color: green; }
        .err { color: red; }
    </style>
</head>
<body>

<div class="container">
   <h1>The Art of Urban Gardening: Bringing Green to the City</h1>
   <p>Urban gardening has exploded in popularity over the past decade, as city dwellers seek ways to reconnect with nature despite limited space. From balcony herb gardens to rooftop vegetable plots, cultivating plants in urban environments not only enhances aesthetics but also provides tangible benefits like fresh produce, improved air quality, and mental well-being. Whether you live in a tiny studio or a sprawling high-rise, there are creative strategies for turning a concrete jungle into a thriving green oasis.</p>
   <p>One of the simplest ways to start an urban garden is by using containers. Window boxes, hanging planters, and stackable pots allow you to grow a variety of herbs—such as basil, mint, and thyme—without needing a traditional backyard. Herbs are particularly well-suited for small-scale gardening because they don’t require deep soil and can thrive on a sunny windowsill. Additionally, growing herbs at home saves money on groceries and ensures you always have fresh seasoning on hand.</p>
   <p>For those with a bit more outdoor space—like a balcony, patio, or fire escape—vertical gardening offers an efficient use of square footage. Vertical planters, wall-mounted pockets, and modular shelving systems let you stack leafy greens, strawberries, cherry tomatoes, or even dwarf fruit trees. By training vines upward or suspending planters at different heights, you create layers of foliage that maximize sunlight exposure and airflow, while also creating a lush, visually appealing barrier from neighboring buildings.</p>
   <p>Rooftop gardens represent another exciting frontier for urban growers. Converting a flat roof into a rooftop farm may require permission from building management and some structural considerations (such as weight capacity), but the payoff can be significant. Rooftop gardens not only provide a private green retreat but also help reduce the urban heat island effect by insulating the building and lowering surrounding temperatures. Many community organizations and non-profits now offer shared rooftop garden spaces, where residents can rent small plots and collaborate on composting, irrigation, and seasonal planting schedules.</p>
   <p>Beyond the practical benefits of fresh produce and improved air quality, urban gardening offers mental health advantages as well. Research has shown that spending time tending to plants reduces stress, boosts mood, and encourages a sense of accomplishment. Even five minutes spent watering a pot of flowers or checking on tomato seedlings can serve as a mindful break from the hectic pace of city life. Over time, urban gardeners often develop a heightened awareness of seasonal cycles, local wildlife, and the interconnectedness of ecosystems—insights that are surprisingly rare in a metropolitan setting.</p>
   <p>To get started, consider these basic tips:</p>
   <ul>
     <li><strong>Assess Sunlight:</strong> Pay attention to which areas of your home receive direct sunlight and for how long. Most vegetables and herbs need at least six hours of sunlight each day.</li>
     <li><strong>Choose Suitable Containers:</strong> Make sure pots have adequate drainage and are large enough for the plants you intend to grow. Lightweight materials like plastic or fabric grow bags can be easier to move.</li>
     <li><strong>Use Quality Soil:</strong> Invest in a high-quality potting mix specifically designed for container gardening. Good soil retains moisture and provides essential nutrients.</li>
     <li><strong>Water Wisely:</strong> Containers dry out more quickly than ground soil. Establish a regular watering schedule, but avoid overwatering by checking if the top inch of soil is dry before adding more water.</li>
     <li><strong>Rotate and Prune:</strong> Rotate containers periodically to ensure even growth, and prune plants to encourage bushier development and prevent overcrowding.</li>
   </ul>
   <p>In conclusion, urban gardening is not just a trend—it’s a practical and fulfilling way to bring life into otherwise grey, concrete spaces. By starting small with containers or scaling up to vertical planters and rooftop plots, you can enjoy fresh, homegrown produce and foster a deeper connection to nature right in the heart of the city. Whether you’re a seasoned gardener or a complete beginner, the urban gardening community is full of resources, from online forums to local workshops, ready to help you succeed.</p>

    <h2>Comentarii</h2>

    <?php foreach ($comentarii as $c): ?>
        <div class="comentariu">
            <strong><?= htmlspecialchars($c['nume']) ?></strong> (<?= $c['data_adaugare'] ?>)
            <p><?= nl2br(htmlspecialchars($c['continut'])) ?></p>
        </div>
    <?php endforeach; ?>

    <div class="formular">
        <h3>Adauga un comentariu</h3>
        <?php if ($mesaj): ?><p class="msg"><?= htmlspecialchars($mesaj) ?></p><?php endif; ?>
        <?php if ($eroare): ?><p class="err"><?= htmlspecialchars($eroare) ?></p><?php endif; ?>

        <form method="post">
            <input type="text" name="nume" placeholder="Numele tău" required>
            <textarea name="comentariu" placeholder="Comentariul tău" rows="5" required></textarea>
            <button type="submit">Trimite</button>
        </form>
    </div>
</div>

</body>
</html>
