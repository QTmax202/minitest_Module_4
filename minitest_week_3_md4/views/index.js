let index = 0;

$('#image').on('submit', uploadFile);

function addNewProduct(event) {
    event.stopPropagation();
    event.preventDefault();
    let image = event.target.files;
    let data = new FormData();
    let name = $('#name').val();
    let price = $('#price').val();
    let description = $('#description').val();
    let category = $('#category').val();
    $.each(image, function(key, value)
    {
        data.append(key, value);
    });
    postFilesData(data);
    let newProduct = {
        name: name,
        price: price,
        description: description,
        imageFile: image,
        category: {
            id: category,
        }
    };
    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: "POST",
        data: JSON.stringify(newProduct),
        cache: false,
        dataType: 'json',
        processData: false,
        contentType: false,
        url: "http://localhost:8080/api/product/save",
        success: function () {
            getProductByPage(0);
        }
    });
    event.preventDefault();
}

function editProduct(id) {
    $.ajax({
        type: "GET",
        url: `http://localhost:8080/api/products/${id}`,
        success: function (data) {
            $('#name').val(data.name);
            $('#price').val(data.price);
            $('#quantity').val(data.quantity);
            $('#description').val(data.description);
            index = data.id;
            document.getElementById("form").hidden = false;
            document.getElementById("form-button").onclick = function () {
                editProduct1()
            };
            getCategory();
        }
    });
}

function editProduct1(event) {
    event.stopPropagation();
    event.preventDefault();
    let image = event.target.files;
    let data = new FormData();
    let name = $('#name').val();
    let price = $('#price').val();
    let description = $('#description').val();
    let category = $('#category').val();
    $.each(image, function(key, value)
    {
        data.append(key, value);
    });
    postFilesData(data);
    let newProduct = {
        name: name,
        price: price,
        description: description,
        imageFile: image,
        category: {
            id: category,
        }
    };
    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: "PUT",
        data: JSON.stringify(newProduct),
        cache: false,
        dataType: 'json',
        processData: false,
        contentType: false,
        url: `http://localhost:8080/api/products/${index}`,
        success: function () {
            getProductByPage(0)
        }
    });
    event.preventDefault();
}

function getProductByPage(page) {
    $.ajax({
        type: "GET",
        url: `http://localhost:8080/api/products?page=${page}`,
        success: function (data) {
            let array = data.content
            let content = '<tr>\n' +
                '<th>#</th>\n' +
                '<th>Image</th>\n' +
                '<th>Name</th>\n' +
                '<th>Price</th>\n' +
                '<th>Desciption</th>\n' +
                '<th>Category</th>\n' +
                '<th>Action</th>\n' +
                '</tr>';
            for (let i = 0; i < array.length; i++) {
                content += displayProduct(array[i]);
            }
            document.getElementById("productList").innerHTML = content;
            document.getElementById("displayPage").innerHTML = displayPage(data)
            document.getElementById("form").hidden = true;
            if (data.pageable.pageNumber === 0) {
                document.getElementById("backup").hidden = true
            }
            if (data.pageable.pageNumber + 1 === data.totalPages) {
                document.getElementById("next").hidden = true
            }
        }
    });
}

function displayPage(data){
    return `<button class="btn btn-primary" id="backup" onclick="isPrevious(${data.pageable.pageNumber})">Previous</button>
    <span>${data.pageable.pageNumber+1} | ${data.totalPages}</span>
    <button class="btn btn-primary" id="next" onclick="isNext(${data.pageable.pageNumber})">Next</button>`
}

function isPrevious(pageNumber) {
    getProductByPage(pageNumber-1)
}

function isNext(pageNumber) {
    getProductByPage(pageNumber+1)
}

function deleteProduct(id) {
    $.ajax({
        type: "DELETE",
        url: `http://localhost:8080/api//product/delete/${id}`,
        success: function () {
            getProductByPage(0)
        }
    });
}

function searchProduct() {
    let search = document.getElementById("search").value;
    $.ajax({
        type: "GET",
        url: `http://localhost:8080/api/products/search?search=${search}`,
        success: function (data) {
            let content = '<tr>\n' +
                '<th>#</th>\n' +
                '<th>Image</th>\n' +
                '<th>Name</th>\n' +
                '<th>Price</th>\n' +
                '<th>Desciption</th>\n' +
                '<th>Category</th>\n' +
                '<th>Action</th>\n' +
                '</tr>';
            for (let i = 0; i < data.length; i++) {
                content += displayProduct(data[i]);
            }
            document.getElementById('productList').innerHTML = content;
            document.getElementById("searchForm").reset()
        }
    });
    event.preventDefault();
}

function displayProduct(product) {
    return `<tr>
            <td>${product.id}</td>
            <td><img src="${product.imageUrl}" alt=""></td>
            <td>${product.name}</td>
            <td>${product.price}</td>
            <td>${product.description}</td>
            <td>${product.category.name}</td>
            <td><button class="btn btn-danger" onclick="deleteProduct(${product.id})">Delete</button> | 
            <button class="btn btn-warning" onclick="editProduct(${product.id})">Edit</button></td></tr>`;
}

function displayFormCreate() {
    document.getElementById("form").reset()
    document.getElementById("form").hidden = false;
    document.getElementById("form-button").onclick = function () {
        addNewProduct();
    }
    getCategory();
}

function getCategory() {
    $.ajax({
        type: "GET",
        url: `http://localhost:8080/api/cate`,
        success: function (data) {
            let content = ''
            for (let i = 0; i < data.length; i++) {
                content += displayCategory(data[i]);
            }
            document.getElementById('div-category').innerHTML = content;
            document.getElementById('div-list-category').innerHTML = content;
        }
    });
}

function displayCategory(category) {
    return `<option id="${category.id}" value="${category.id}">${category.name}</option>`;
}

getProductByPage(0);
getCategory();