function addCategoryField(type) {
    const container = document.getElementById(type + 'CategoriesContainer');
    const div = document.createElement('div');
    div.className = 'mb-2 input-group';
    div.innerHTML = `
            <input type="text" name="${type}Categories" class="form-control">
            <button type="button" class="btn btn-outline-danger"
                    onclick="removeCategory(this, '${type}')">Удалить</button>
        `;
    container.appendChild(div);
}

function removeCategory(button, type) {
    const container = document.getElementById(type + 'CategoriesContainer');
    if (container.children.length > 1) {
        button.parentElement.remove();
    } else {
        button.previousElementSibling.value = '';
    }
}