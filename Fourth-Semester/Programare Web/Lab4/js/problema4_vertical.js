$(function() {
    $('.sortable-table').each(function() {
        const $table   = $(this);
        const $headers = $table.find('thead th');

        $headers.each(function(colIndex) {
            const $th = $(this);
            $th.on('click', function() {
                const $rows    = $table.find('tbody tr').get();
                let isNumeric  = true;

                $.each($rows, function(_, row) {
                    const text = $(row).children('td').eq(colIndex).text().trim();
                    if (isNaN(text)) isNumeric = false;
                });

                $rows.sort(function(a, b) {
                    let valA = $(a).children('td').eq(colIndex).text().trim();
                    let valB = $(b).children('td').eq(colIndex).text().trim();
                    if (isNumeric) {
                        valA = parseFloat(valA);
                        valB = parseFloat(valB);
                    } else {
                        valA = valA.toLowerCase();
                        valB = valB.toLowerCase();
                    }
                    return valA > valB ? 1 : (valA < valB ? -1 : 0);
                });

                const $tbody = $table.find('tbody').empty();
                $.each($rows, function(_, row) {
                    $tbody.append(row);
                });

                $headers.removeClass('highlight');
                $th.addClass('highlight');
            });
        });
    });
});


