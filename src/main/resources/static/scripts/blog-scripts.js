/**
 * Created by ivano on 18.12.2016 г..
 */
$(function() {
    $('#messages li').click(function() {
        $(this).fadeOut();
    });
    setTimeout(function() {
        $('#messages li.info').fadeOut();
    }, 3000);
});
