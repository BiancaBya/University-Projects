$(function() {
    let revealed = [];
    let locked   = false;

    $('td').each(function() {
        const $td    = $(this);
        const imgUrl = $td.data('img');
        $td.html(`
      <div class="flip-card-inner">
        <div class="flip-card-front"></div>
        <div class="flip-card-back">
          <img src="${imgUrl}" alt="card image"/>
        </div>
      </div>
    `);
    });

    function show($td) { $td.addClass('revealed'); }
    function hide($td) { $td.removeClass('revealed'); }
    function isWin() { return $('td.revealed').length === $('td').length; }

    $('td').on('click', function() {
        const $this = $(this);
        if (locked || $this.hasClass('revealed')) return;

        show($this);
        revealed.push($this);

        if (revealed.length === 2) {
            const [a, b] = revealed;
            if (a.data('value') === b.data('value')) {
                revealed = [];
                if (isWin()) {
                    $('#label')
                        .html('🎉 <strong>Ai câștigat 😎!</strong>')
                        .css({
                            fontSize:   '24px',
                            color:      '#013748',
                            fontWeight: 'bold',
                            textAlign:  'center'
                        });
                }
            } else {
                locked = true;
                setTimeout(() => {
                    hide(a);
                    hide(b);
                    revealed = [];
                    locked   = false;
                }, 2000);
            }
        }
    });
});


