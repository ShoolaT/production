<#import "../layout.ftlh" as main>

<@main.layout>
    <h2>Ingredient List</h2>

    <form action="/ingredients/all" method="get">
        <div class="mt-3">
            <label for="productFilter">Filter by Product:</label>
            <select class="form-control" id="productFilter" name="productId" onchange="filterIngredients()">
                <option value="">All Products</option>
                <#list products as product>
                    <option value="${product.id}" <#if product.id == (selectedProductId!)?number>selected</#if>>${product.name}</option>
                </#list>
            </select>
        </div>
    </form>

    <p id="selectedProductName" class="mt-2"></p>

    <table class="table mt-3">
        <thead>
        <tr>
            <th scope="col">Raw Material</th>
            <th scope="col">Quantity</th>
            <th scope="col">Actions</th>
        </tr>
        </thead>
        <tbody>
        <#list ingredients as ingredient>
            <tr>
                <td>${ingredient.rawMaterial.name}</td>
                <td>${ingredient.quantity}</td>
                <td>
                    <a href="/ingredients/${ingredient.id}/edit" class="btn btn-outline-warning me-2">Edit</a>
                    <button class="btn btn-outline-danger" onclick="confirmDelete(${ingredient.id}, ${selectedProductId})">Delete</button>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>

    <div class="mt-3">
        <a href="/ingredients/create?productId=${selectedProductId}" class="btn btn-success">Add Ingredient</a>
    </div>

    <script>
        function filterIngredients() {
            var selectedProductId = document.getElementById("productFilter").value;
            var selectedProductName = document.getElementById("productFilter").options[document.getElementById("productFilter").selectedIndex].text;

            setTimeout(function() {
                window.location.href = "/ingredients/all?sort=id&productId=" + selectedProductId;
            }, 10);
        }
        function confirmDelete(ingredientId, productId) {
            var confirmation = confirm("Are you sure you want to delete this ingredient?");
            if (confirmation) {
                window.location.href = '/ingredients/' + ingredientId + '/delete/confirmed?productId=' + productId;
            }
        }
    </script>
</@main.layout>
