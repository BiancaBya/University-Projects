$(function() {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    function validareDate() {
        const $name  = $('#name');
        const $date  = $('#date');
        const $email = $('#email');
        const $age   = $('#age');

        let missingValues = [];
        let missingIds    = [];
        let allIds        = ['name', 'date', 'email', 'age'];

        if ($name.val().trim() === '' || $name.val().length <= 3) {
            missingValues.push('Name');
            missingIds.push('name');
        }
        if ($date.val().trim() === '') {
            missingValues.push('Date');
            missingIds.push('date');
        }
        if ($email.val().trim() === '' || !emailRegex.test($email.val())) {
            missingValues.push('Email');
            missingIds.push('email');
        }
        if ($age.val().trim() === '') {
            missingValues.push('Age');
            missingIds.push('age');
        }

        const completedIds = allIds.filter(id => !missingIds.includes(id));

        if (missingValues.length > 0) {
            $('#label').text('Urmatoarele date nu sunt complete: ' + missingValues.join(' '));
            $.each(missingIds, (_, id) => {
                $('#' + id).css('border', '2px solid red');
            });
            $.each(completedIds, (_, id) => {
                $('#' + id).css('border', '2px solid black');
            });
        } else {
            $('#label').text('Datele sunt completate corect');
            $('#name, #date, #email, #age').css('border', '2px solid black');
        }
    }

    $('#submit').on('click', validareDate);
});


