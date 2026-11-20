$(function() {
    let revealedCells = [];
    let locked = false;

    function seeNumber($cell) {
        $cell
            .text($cell.data('value'))
            .addClass('revealed');
    }

    function hideNumber($cell) {
        $cell
            .text('\u00A0')
            .removeClass('revealed');
    }

    function checkWin() {
        return $('td.revealed').length === $('td').length;
    }

    $('td').on('click', function() {
        const $this = $(this);

        if (locked || $this.hasClass('revealed')) {
            return;
        }

        seeNumber($this);
        revealedCells.push($this);

        if (revealedCells.length === 2) {
            const [first, second] = revealedCells;

            if (first.data('value') === second.data('value')) {
                revealedCells = [];
                if (checkWin()) {
                    $('#label').text('Ai câștigat');
                }
            } else {
                locked = true;
                setTimeout(() => {
                    hideNumber(first);
                    hideNumber(second);
                    revealedCells = [];
                    locked = false;
                }, 1000);
            }
        }
    });
});



