$(function() {
    const n = 4;
    let emptyRow, emptyCol;

    function shuffle(arr) {
        for (let i = arr.length - 1; i > 0; i--) {
            const j = Math.floor(Math.random() * (i + 1));
            [arr[i], arr[j]] = [arr[j], arr[i]];
        }
    }

    function generateMatrix() {
        const vals = [];
        for (let i = 1; i < n * n; i++) vals.push(i);
        vals.push(null);
        shuffle(vals);
        const mat = [];
        for (let r = 0; r < n; r++) {
            mat.push(vals.slice(r * n, r * n + n));
        }
        return mat;
    }

    const matrix = generateMatrix();

    $('td').each(function() {
        const $td = $(this);
        const [ , r, c ] = $td.attr('id').split('-').map((v,i) => i>0 ? parseInt(v) : v);
        const v = matrix[r][c];
        if (v === null) {
            $td.text('');
            emptyRow = r; emptyCol = c;
        } else {
            $td.text(v);
        }
    });

    function moveTo(r, c) {
        if (r < 0 || r >= n || c < 0 || c >= n) return;

        matrix[emptyRow][emptyCol] = matrix[r][c];
        matrix[r][c] = null;

        const $from = $(`#cell-${emptyRow}-${emptyCol}`);
        const $to   = $(`#cell-${r}-${c}`);
        $from.text( $to.text() );
        $to.text('');
        emptyRow = r; emptyCol = c;
    }

    $(document).on('keydown', function(e) {
        switch (e.key) {
            case 'ArrowUp':    moveTo(emptyRow - 1, emptyCol); break;
            case 'ArrowDown':  moveTo(emptyRow + 1, emptyCol); break;
            case 'ArrowLeft':  moveTo(emptyRow, emptyCol - 1); break;
            case 'ArrowRight': moveTo(emptyRow, emptyCol + 1); break;
        }
    });
});


