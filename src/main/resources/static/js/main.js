
    function updateTime() {
    const now = new Date();
    const timeStr = now.toLocaleTimeString('ru-RU');
    const dateOptions = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
    const dateStr = now.toLocaleDateString('ru-RU', dateOptions);

    document.getElementById('current-time').textContent = timeStr;
    document.getElementById('current-date').textContent = dateStr;
}
    updateTime();
    setInterval(updateTime, 1000);

    // Погода
    document.addEventListener('DOMContentLoaded', function() {
    const cityId = 482283; // Тольятти
    const apiKey = '68c388d372ff55617bde8763d111c784';

    const weatherIconMap = {
    '01d': 'bi-sun', '01n': 'bi-moon',
    '02d': 'bi-cloud-sun', '02n': 'bi-cloud-moon',
    '03d': 'bi-cloud', '03n': 'bi-cloud',
    '04d': 'bi-clouds', '04n': 'bi-clouds',
    '09d': 'bi-cloud-rain', '09n': 'bi-cloud-rain',
    '10d': 'bi-cloud-rain-heavy', '10n': 'bi-cloud-rain-heavy',
    '11d': 'bi-lightning', '11n': 'bi-lightning',
    '13d': 'bi-snow', '13n': 'bi-snow',
    '50d': 'bi-cloud-fog', '50n': 'bi-cloud-fog'
};

    fetch(`https://api.openweathermap.org/data/2.5/weather?id=${cityId}&appid=${apiKey}&units=metric&lang=ru`)
    .then(response => response.json())
    .then(data => {
    document.getElementById('temperature').textContent = `${Math.round(data.main.temp)}°C`;
    document.getElementById('weather-description').textContent =
    data.weather[0].description.charAt(0).toUpperCase() + data.weather[0].description.slice(1);
    document.getElementById('humidity').textContent = `${data.main.humidity}%`;
    document.getElementById('wind-speed').textContent = `${Math.round(data.wind.speed)} м/с`;
    document.getElementById('pressure').textContent = `${Math.round(data.main.pressure/1.333)} мм рт.ст.`;

    const iconCode = data.weather[0].icon;
    document.getElementById('weather-icon').innerHTML =
    `<i class="bi ${weatherIconMap[iconCode] || 'bi-question-circle'}"></i>`;
})
    .catch(() => {
    document.getElementById('weather-description').textContent = 'Не удалось загрузить данные';
});
});