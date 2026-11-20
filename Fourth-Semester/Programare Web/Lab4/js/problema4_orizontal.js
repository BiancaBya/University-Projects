$(function() {
    $('.sortable-table').each(function() {
        const $table = $(this);

        $table.on('click', 'tr th:first-child', function() {
            const $th        = $(this);
            const $rows      = $table.find('tr');
            const rowIndex   = $rows.index($th.closest('tr'));
            const $headerRow = $rows.eq(rowIndex);
            const $cells     = $headerRow.children();
            const colCount   = $cells.length;

            let values = [];
            $cells.each(function(i) {
                if (i === 0) return;
                values.push({
                    idx: i,
                    value: $(this).text().trim()
                });
            });

            const isNumeric = values.every(o => !isNaN(parseFloat(o.value)));

            values.sort((a, b) => {
                let va = isNumeric
                    ? parseFloat(a.value)
                    : a.value.toLowerCase();
                let vb = isNumeric
                    ? parseFloat(b.value)
                    : b.value.toLowerCase();
                return va < vb ? -1 : va > vb ? 1 : 0;
            });

            $rows.each(function() {
                const $r        = $(this);
                const cellsArr  = $r.children().toArray();
                const newOrder = [ cellsArr[0] ];
                values.forEach(v => {
                    newOrder.push(cellsArr[v.idx]);
                });
                $r.empty().append(newOrder);
            });

            $rows.removeClass('highlight');
            $headerRow.addClass('highlight');
        });
    });
});


