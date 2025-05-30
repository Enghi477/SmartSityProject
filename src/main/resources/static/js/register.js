
    function validateAge(input) {
    const ageError = document.getElementById('ageError');
    if (input.value < 18 || input.value > 100) {
    ageError.style.display = 'block';
    input.setCustomValidity('Возраст должен быть от 18 до 100 лет');
} else {
    ageError.style.display = 'none';
    input.setCustomValidity('');
}
}

    document.getElementById('registerForm').addEventListener('submit', function(event) {
    const ageInput = document.getElementById('age');
    if (ageInput.value < 18 || ageInput.value > 100) {
    event.preventDefault();
    document.getElementById('ageError').style.display = 'block';
    ageInput.focus();
}
});