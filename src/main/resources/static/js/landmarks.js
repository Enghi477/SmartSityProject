
    let map;
    let searchResults = [];
    let route;
    let placemarks = [];
    let primaryColor;

    ymaps.ready(initMap);


    function initMap() {
    const root = document.documentElement;
    primaryColor = getComputedStyle(root).getPropertyValue('--primary-color').trim();

    // Создаем карту
    map = new ymaps.Map('map', {
    center: [53.507852, 49.420411],
    zoom: 11,
    controls: ['zoomControl', 'typeSelector', 'fullscreenControl']
});

    setTimeout(() => {
    map.container.fitToViewport();
}, 100);

    addLandmarksToMap();
}

    window.addEventListener('resize', () => {
    if (map) {
    setTimeout(() => {
    map.container.fitToViewport();
}, 100);
}
});

    function addLandmarksToMap() {
    placemarks.forEach(pm => map.geoObjects.remove(pm));
    placemarks = [];

    document.querySelectorAll('.landmark-item').forEach(item => {
    if (!item.classList.contains('hidden')) {
    const coords = [parseFloat(item.dataset.lat), parseFloat(item.dataset.lon)];
    const placemark = new ymaps.Placemark(coords, {
    hintContent: item.querySelector('h4').textContent,
    balloonContent: `
                    <div style="max-width: 300px">
                        ${item.querySelector('img') ?
    `<img src="${item.querySelector('img').src}"
                                   style="max-width:100%; border-radius:4px; margin-bottom:8px;">`
    : ''}
                        <h5 style="margin: 0 0 8px">${item.querySelector('h4').textContent}</h5>
                        <p style="margin: 0 0 8px; color: #666">${item.querySelector('p').textContent}</p>
                        <span style="color: ${primaryColor}">
                            ${item.querySelector('span').textContent}
                        </span>
                    </div>
                `
}, {
    preset: 'islands#blueIcon',
    balloonCloseButton: true
});

    placemarks.push(placemark);
    map.geoObjects.add(placemark);
}
});
}


    function searchPlace() {
    const query = document.getElementById('searchInput').value.toLowerCase().trim();

    if (!query) {
    alert('Введите поисковый запрос');
    return;
}

    clearSearchResults();

    const foundItems = [];
    document.querySelectorAll('.landmark-item').forEach(item => {
    const name = item.querySelector('h4').textContent.toLowerCase();
    const description = item.querySelector('p').textContent.toLowerCase();

    if (name.includes(query) || description.includes(query)) {
    foundItems.push(item);
    item.classList.remove('hidden');

    highlightText(item.querySelector('h4'), query);
    highlightText(item.querySelector('p'), query);
} else {
    item.classList.add('hidden');
    removeHighlight(item);
}
});

    if (foundItems.length === 0) {
    alert('Ничего не найдено');
    clearSearchResults();
    return;
}

    const bounds = [];
    foundItems.forEach(item => {
    bounds.push([parseFloat(item.dataset.lat), parseFloat(item.dataset.lon)]);
});

    addLandmarksToMap();

    if (bounds.length > 0) {
    map.setBounds(bounds, {
    checkZoomRange: true,
    zoomMargin: 50
});
}

    searchResults = foundItems;
}

    function highlightText(element, query) {
    const text = element.textContent;
    const regex = new RegExp(query, 'gi');
    const highlighted = text.replace(regex, match => `<span class="search-highlight">${match}</span>`);
    element.innerHTML = highlighted;
}

    function removeHighlight(item) {
    const h4 = item.querySelector('h4');
    const p = item.querySelector('p');

    if (h4) h4.innerHTML = h4.textContent;
    if (p) p.innerHTML = p.textContent;
}

    function clearSearch() {
    document.getElementById('searchInput').value = '';
    clearSearchResults();
}

    function clearSearchResults() {
    document.querySelectorAll('.landmark-item').forEach(item => {
        item.classList.remove('hidden');
        removeHighlight(item);
    });
    searchResults = [];
    addLandmarksToMap();
}
    function buildRoute(routingMode) {
    const fromSelect = document.getElementById('routeFrom');
    const toSelect = document.getElementById('routeTo');
    const routeInfo = document.getElementById('routeInfo');
    const routeType = document.getElementById('routeType');
    const routeDistance = document.getElementById('routeDistance');
    const routeDuration = document.getElementById('routeDuration');

    if (!fromSelect.value || !toSelect.value) {
    routeInfo.style.display = 'none';
    alert('Выберите начальную и конечную точки');
    return;
}

    if (route) {
    map.geoObjects.remove(route);
}

    const fromCoords = fromSelect.value.split(',');
    const toCoords = toSelect.value.split(',');

    let typeText = '';
    switch(routingMode) {
    case 'auto': typeText = 'На машине'; break;
    case 'masstransit': typeText = 'Общественный транспорт'; break;
    case 'pedestrian': typeText = 'Пешком'; break;
    default: typeText = 'На машине';
}
    routeType.textContent = typeText;

    route = new ymaps.multiRouter.MultiRoute({
    referencePoints: [fromCoords, toCoords],
    params: { routingMode: routingMode || 'auto' }
}, {
    boundsAutoApply: true,
    wayPointVisible: true,
    routeActiveStrokeWidth: 5,
    routeActiveStrokeStyle: 'solid',
    routeActiveStrokeColor: '#4361ee'
});

    map.geoObjects.add(route);

    route.model.events.add('requestsuccess', function() {
    map.setBounds(route.getBounds(), {
    checkZoomRange: true,
    zoomMargin: 50
});

    const activeRoute = route.getActiveRoute();
    if (activeRoute) {
    routeDistance.textContent = activeRoute.properties.get('distance').text;
    routeDuration.textContent = activeRoute.properties.get('duration').text;
    routeInfo.style.display = 'block';
}
});

    route.model.events.add('requestfail', function() {
    routeInfo.style.display = 'none';
    alert('Не удалось построить маршрут между выбранными точками');
});
}

    function clearRoute() {
    if (route) {
    map.geoObjects.remove(route);
    route = null;
}
    document.getElementById('routeFrom').value = '';
    document.getElementById('routeTo').value = '';
    document.getElementById('routeInfo').style.display = 'none';
}

    function showOnMap(element) {
    document.querySelectorAll('.landmark-item').forEach(el => el.classList.remove('active'));
    element.classList.add('active');

    const coords = [
    parseFloat(element.dataset.lat),
    parseFloat(element.dataset.lon)
    ];

    map.panTo(coords, {
    flying: true,
    duration: 500
}).then(() => {
    map.balloon.open(coords, {
    content: element.querySelector('h4').textContent
});
});
}

    function deleteLandmark(event) {
    event.stopPropagation();
    const id = event.currentTarget.getAttribute('data-id');

    if (confirm('Вы уверены, что хотите удалить эту достопримечательность?')) {
    fetch('/admin/landmarks/delete/' + id, {
    method: 'DELETE',
})
    .then(response => {
    if (response.ok) {
    window.location.reload();
} else {
    alert('Ошибка при удалении: ' + response.statusText);
}
})
    .catch(error => {
    console.error('Ошибка:', error);
    alert('Произошла ошибка при удалении');
});
}
}