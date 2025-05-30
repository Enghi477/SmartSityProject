
    document.addEventListener('DOMContentLoaded', function() {
    const elements = document.querySelectorAll('.fade-in');
    elements.forEach(el => {
    el.style.opacity = '0';
});

    setTimeout(() => {
    elements.forEach(el => {
    el.style.opacity = '1';
});
}, 100);
});
