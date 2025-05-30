
    function sortTable(field) {
    const url = new URL(window.location.href);
    const params = new URLSearchParams(url.search);

    if (params.get('sortField') === field) {
    params.set('sortDirection', params.get('sortDirection') === 'asc' ? 'desc' : 'asc');
} else {
    params.set('sortField', field);
    params.set('sortDirection', 'asc');
}

    window.location.href = url.pathname + '?' + params.toString();
}

    function resetFilters() {
    window.location.href = '/admin/dashboard';
}

    function deleteUser(button) {
    const userId = button.getAttribute('data-id');
    if (confirm('Are you sure you want to delete this user?')) {
    fetch('/admin/users/' + userId, {
    method: 'DELETE',
    headers: {
    'Content-Type': 'application/json',
    'Authorization': 'Bearer ' + getJwtToken()
}
}).then(response => {
    if (response.ok) {
    location.reload();
} else {
    alert('Error deleting user');
}
});
}
}

    function getJwtToken() {
    const cookies = document.cookie.split(';');
    for (let cookie of cookies) {
    const [name, value] = cookie.trim().split('=');
    if (name === 'jwt') {
    return decodeURIComponent(value);
}
}
    return '';
}
