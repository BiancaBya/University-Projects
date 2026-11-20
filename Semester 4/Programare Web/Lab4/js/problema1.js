$(function() {

    function mutaElement($sursa, $dest) {
        const $optiune = $sursa.find('option:selected').first();
        if ( $optiune.length ) {
            $optiune.appendTo($dest);
        }
    }

    $('#lista1').on('dblclick', function() {
        mutaElement( $('#lista1'), $('#lista2') );
    });

    $('#lista2').on('dblclick', function() {
        mutaElement( $('#lista2'), $('#lista1') );
    });
});


